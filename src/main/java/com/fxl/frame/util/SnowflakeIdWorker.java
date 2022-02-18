package com.fxl.frame.util;

import com.fxl.frame.util.data.TimeUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.Enumeration;

/**
 *
 * 来源：https://www.cnblogs.com/relucent/p/4955340.html
 * 作者：永夜微光
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 *
 * 网上的教程一般存在两个问题：
 * 1. 机器ID（5位）和数据中心ID（5位）配置没有解决，分布式部署的时候会使用相同的配置，任然有ID重复的风险。
 * 2. 使用的时候需要实例化对象，没有形成开箱即用的工具类。
 *
 * 本文针对上面两个问题进行解决，笔者的解决方案是，workId使用服务器hostName生成，
 * dataCenterId使用IP生成，这样可以最大限度防止10位机器码重复，但是由于两个ID都不能超过32，
 * 只能取余数，还是难免产生重复，但是实际使用中，hostName和IP的配置一般连续或相近，
 * 只要不是刚好相隔32位，就不会有问题，况且，hostName和IP同时相隔32的情况更加是几乎不可能
 * 的事，平时做的分布式部署，一般也不会超过10台容器。使用上面的方法可以零配置使用雪花算法，
 * 雪花算法10位机器码的设定理论上可以有1024个节点，生产上使用docker配置一般是一次编译，
 * 然后分布式部署到不同容器，不会有不同的配置，这里不知道其他公司是如何解决的，即使有方法
 * 使用一套配置，然后运行时根据不同容器读取不同的配置，但是给每个容器编配ID，1024个
 * （大部分情况下没有这么多），似乎也不太可能，此问题留待日后解决后再行补充。
 *
 *
 * id重复的原因：
 * 1、时钟回拨
 * 大家应该都知道雪花算法存在的缺点是：
 * 依赖机器时钟，如果机器时钟回拨，会导致重复ID生成
 * 在单机上是递增的，但是由于设计到分布式环境，每台机器上的时钟不可能完全同步，有时候会出现不是全局递增的情况（此缺点可以认为无所谓，一般分布式ID只要求趋势递增，并不会严格要求递增～90%的需求都只要求趋势递增）
 *
 * 2、只要workerid相同，且各个服务器如果大体做了时间同步，那么生成的Id在分布式环境可以认为是总体有序，
 * 同时在两台机器上出现请求，就会产生重复，或者说只要线上IP末尾相同，就有可能会产生重复！！！
 */
public class SnowflakeIdWorker {
	// ==============================Fields===========================================
	/** 开始时间截 (2021-01-29 00:00:00) */
	private final static long twepoch = 1611849600000L;

	/** 机器id所占的位数 */
	private final static long workerIdBits = 5L;

	/** 数据标识id所占的位数 */
	private final static long datacenterIdBits = 5L;

	/** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
	private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);

	/** 支持的最大数据标识id，结果是31 */
	private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

	/** 序列在id中占的位数 */
	private final static long sequenceBits = 12L;

	/** 机器ID向左移12位 */
	private final static long workerIdShift = sequenceBits;

	/** 数据标识id向左移17位(12+5) */
	private final static long datacenterIdShift = sequenceBits + workerIdBits;

	/** 时间截向左移22位(5+5+12) */
	private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

	/** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
	private final static long sequenceMask = -1L ^ (-1L << sequenceBits);

	/** 工作机器ID(0~31) */
	private long workerId;

	/** 数据中心ID/集群id(0~31) */
	private long datacenterId;

	/** 毫秒内序列(0~4095) */
	private long sequence = 0L;

	/** 上次生成ID的时间截 */
	private long lastTimestamp = -1L;

	private static SnowflakeIdWorker idWorker;

	static {
		idWorker = new SnowflakeIdWorker(getWorkId(), getDataCenterId());
	}



	//==============================Constructors=====================================
	/**
	 * 构造函数
	 * @param workerId 工作ID (0~31)
	 * @param datacenterId 数据中心ID/集群id (0~31)
	 */
	public SnowflakeIdWorker(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	// ==============================Methods==========================================
	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * @return SnowflakeId
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();

		//如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(
					String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		//如果是同一时间生成的，则进行毫秒内序列
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			//毫秒内序列溢出
			if (sequence == 0) {
				//阻塞到下一个毫秒,获得新的时间戳
				timestamp = tilNextMillis(lastTimestamp);
			}
		}
		//时间戳改变，毫秒内序列重置
		else {
			sequence = 0L;
		}

		//上次生成ID的时间截
		lastTimestamp = timestamp;

		//移位并通过或运算拼到一起组成64位的ID
		return ((timestamp - twepoch) << timestampLeftShift) //
				| (datacenterId << datacenterIdShift) //
				| (workerId << workerIdShift) //
				| sequence;
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 返回以毫秒为单位的当前时间
	 * @return 当前时间(毫秒)
	 */
	protected long timeGen() {
		return System.currentTimeMillis();
	}


	/**
	 * 获取本机ip
	 * @return
	 */
	private static String getHostIp() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ip = (InetAddress) addresses.nextElement();
					if (ip != null
							&& ip instanceof Inet4Address
							&& !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
							&& ip.getHostAddress().indexOf(":") == -1) {
						System.out.println("本机的IP = " + ip.getHostAddress());
						return ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Long getWorkId() {
		try {
			String hostAddress = Inet4Address.getLocalHost().getHostAddress();
//			System.out.println("hostAddress = " + hostAddress); //10.1.95.92
//			String hostName = Inet4Address.getLocalHost().getHostName();
//			System.out.println("hostName = " + hostName); //fxl-Mac.local
			int[] ints = StringUtils.toCodePoints(hostAddress);
			int sums = 0;
			for(int b : ints){
				sums += b;
			}
			return (long)(sums % 32);
		} catch (UnknownHostException e) {
			// 如果获取失败，则使用随机数备用
			return RandomUtils.nextLong(0,31);
		}
	}

	private static Long getDataCenterId() {
		try {
			String hostName = Inet4Address.getLocalHost().getHostName();
//			String hostName = SystemUtils.getHostName();
			int[] ints = StringUtils.toCodePoints(hostName);
			int sums = 0;
			for (int i: ints) {
				sums += i;
			}
			return (long)(sums % 32);
		} catch (UnknownHostException e) {
			// 如果获取失败，则使用随机数备用
			return RandomUtils.nextLong(0,31);
		}

	}



	/**
	 * 静态工具方法
	 * @return
	 */
	public static synchronized Long generateId() {
		long id = idWorker.nextId();
		return id;
	}

	//==============================Test=============================================
	/** 测试 */
	public static void main(String[] args) {
//		String hostIp = getHostIp();
//		System.out.println("hostIp = " + hostIp);

//		Long workId = getWorkId();
//		System.out.println("workId = " + workId);
//
//		Long dataCenterId = getDataCenterId();
//		System.out.println("dataCenterId = " + dataCenterId);
		SnowflakeIdWorker sn1 = new SnowflakeIdWorker(0, 29);
		SnowflakeIdWorker sn2 = new SnowflakeIdWorker(21, 3);
		SnowflakeIdWorker sn3 = new SnowflakeIdWorker(12, 1);

		for (int i = 0; i < 20; i++) {
//			Long id = generateId();
//			System.out.println(id);
			long l1 = sn1.nextId();
			System.out.println("l1 = " + l1);
//
//			long l2 = sn2.nextId();
//			System.out.println("l2 = " + l2);
//
//			long l3 = sn3.nextId();
//			System.out.println("l3 = " + l3);

		}


//		Date date = TimeUtils.stringFormatToDate("2021-01-29 00:00:00", "yyyy-MM-dd HH:mm:ss");
//		System.out.println(date.getTime());
//
//		System.out.println(System.currentTimeMillis());


	}

}
