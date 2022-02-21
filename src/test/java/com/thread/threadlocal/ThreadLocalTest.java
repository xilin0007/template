package com.thread.threadlocal;
/**
 * @Description ThreadLocalTest
 * @author fangxilin
 * @date 2021/5/17
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2021
 */
public class ThreadLocalTest {

	// 测试代码
	public static void main(String[] args){
		// 新开2个线程用于设置 & 获取 ThreadLoacl的值
		MyRunnable runnable = new MyRunnable();
		for (int i = 0; i < 10; i++) {
			new Thread(runnable, "线程" + i).start();
		}

	}

	// 线程类
	public static class MyRunnable implements Runnable {

		// 创建ThreadLocal & 初始化
		public static ThreadLocal<String> threadLocal = new ThreadLocal<String>(){
			@Override
			protected String initialValue() {
				return "初始化值";
			}
		};

		public static String threadName;

		@Override
		public void run() {

			// 运行线程时，分别设置 & 获取 ThreadLoacl的值
			String name = Thread.currentThread().getName();
			threadName = name;
			threadLocal.set(name + "的threadLocal"); // 设置值 = 线程名
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(name + "：" + threadLocal.get() + "-------执行任务的当前线程名 = " + threadName);
		}
	}
}
