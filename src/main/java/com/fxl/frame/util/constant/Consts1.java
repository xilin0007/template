package com.fxl.frame.util.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * 项目常量初始化赋值
 * @author fangxilin
 * @date 2018年3月7日
 */
public class Consts1 {
	
	private static Properties config = new Properties();
	
	private static final String CONFIG_FILEPATH = "/conf/config.properties";
	
	static {
		//获取环境变量的路径
//		String resource = System.getenv("WEBAPP_TEMPLATE_CONF");
//		resource = resource.replace("\\", "/");
		try {
			URI uri = Thread.currentThread().getContextClassLoader().getResource(CONFIG_FILEPATH).toURI();
//			config.load(new FileInputStream(new File(resource + CONFIG_FILEPATH)));
			config.load(new FileInputStream(new File(uri)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**项目URL*/
	public static final String BASE_URL = config.getProperty("BASE_URL");
	
	/**本文件上传路径*/
	public static final String BASE_UPLOAD_PATH = "/fastdfs/uploadFile";
	
	/**图片服务器URL*/
	public static final String BASE_FILE_URL = config.getProperty("BASE_FILE_URL");
	
	/**common项目*/
	public static final String COMMON_URL = config.getProperty("COMMON_URL");
	
	/**common项目--文件上传路径*/
	public static final String COMMON_UPLOAD_PATH = "/file/upload_file";
	
	public static void main(String[] args) {
		System.out.println(Consts1.BASE_URL);
		System.out.println(Consts1.BASE_FILE_URL);
		System.out.println(Consts1.COMMON_URL);
	}
	
}
