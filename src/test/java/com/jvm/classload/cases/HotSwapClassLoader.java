package com.jvm.classload.cases;

/**
 * 为了多次载入执行类而加入的加载器
 * 吧defineClass方法开放出来，只用外部显示调用的时候才会使用到loadByte方法
 * 由虚拟机调用时，任然按照原有的双亲委派规则使用loadClass方法进行类加载
 */
public class HotSwapClassLoader extends ClassLoader {

	public HotSwapClassLoader() {
		super(HotSwapClassLoader.class.getClassLoader());
	}
	
	public Class<?> loadByte(byte[] classByte) {
		return defineClass(null, classByte, 0, classByte.length);
	}
	
}
