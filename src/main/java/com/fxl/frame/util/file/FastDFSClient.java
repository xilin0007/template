package com.fxl.frame.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class FastDFSClient {
	
	private static final Logger logger = Logger.getLogger(FastDFSClient.class);
	
	private static final String CONFIG_FILENAME = "/conf/fdfs_client.conf";
	private static StorageClient1 storageClient1 = null;

	// 初始化FastDFS Client
	static {
		/*InetSocketAddress[] trackerServers = new InetSocketAddress[1];
		trackerServers[0] = new InetSocketAddress("192.168.2.67", 22122);
		ClientGlobal.setG_tracker_group(new TrackerGroup(trackerServers));
		ClientGlobal.setG_connect_timeout(2000);
		ClientGlobal.setG_network_timeout(30000);
		ClientGlobal.setG_anti_steal_token(false);
		ClientGlobal.setG_charset("UTF-8");
		ClientGlobal.setG_secret_key(null);*/
		try {
//			ClientGlobal.init(System.getenv("WEBAPP_TEMPLATE_CONF") + CONFIG_FILENAME);
			ClientGlobal.init(Thread.currentThread().getContextClassLoader().getResource(CONFIG_FILENAME).toString());
			TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
			TrackerServer trackerServer = trackerClient.getConnection();
			if (trackerServer == null) {
				throw new IllegalStateException("getConnection return null");
			}
			StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
			if (storageServer == null) {
				throw new IllegalStateException("getStoreStorage return null");
			}
			storageClient1 = new StorageClient1(trackerServer, storageServer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件追加操作
	 * @param fileId
	 * @param inputStream
	 * @return
	 */
	public static int appenderFile(String fileId, InputStream inputStream) {
		int result = -1;
		try {
			byte[] buff = new byte[1024];
			int readcount;
			while ((readcount = inputStream.read(buff)) != -1) {
				byte[] newbuffer;
				if (readcount < buff.length) {
					newbuffer = new byte[readcount];
					System.arraycopy(buff, 0, newbuffer, 0, readcount);
					result = storageClient1.append_file1(fileId, newbuffer);
				} else {
					result = storageClient1.append_file1(fileId, buff);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
     * 上传文件
     * @param file 文件对象
     * @param fileName 文件名
     * @return
     */
    public static String uploadFile(File file, String fileName, Map<String,String> metaList) {
    	InputStream inStream = null;
    	try {
			inStream = new FileInputStream(file);
			return uploadFile(inStream, fileName, metaList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
        	IOUtils.closeQuietly(inStream);
        }
    	return null;
    }
    /**
     * 上传文件
     * @param file 文件对象
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return
     */
    public static String uploadFile(InputStream inStream, String fileName, Map<String,String> metaList) {
        try {
            byte[] buff = IOUtils.toByteArray(inStream);
            NameValuePair[] nameValuePairs = null;
            if (metaList != null) {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry<String, String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++] = new NameValuePair(name, value);
                }
            }
            String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
            return storageClient1.upload_file1(buff, fileExtName, nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	IOUtils.closeQuietly(inStream);
        }
        return null;
    }
    
    /**
     * 获取文件元数据
     * @param fileId 文件ID
     * @return
     */
    public static Map<String, String> getFileMetadata(String fileId) {
        try {
            NameValuePair[] metaList = storageClient1.get_metadata1(fileId);
            if (metaList != null) {
                HashMap<String, String> map = new HashMap<String, String>();
                for (NameValuePair metaItem : metaList) {
                    map.put(metaItem.getName(),metaItem.getValue());
                }
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 删除失败返回-1，否则返回0
     */
    public static int deleteFile1(String fileId) {
        try {
            return storageClient1.delete_file1(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * 下载文件
     * @param fileId 文件ID（上传文件成功后返回的ID）
     * @param outFile 文件下载保存位置
     * @return
     */
    public static int downloadFile(String fileId, File outFile) {
        FileOutputStream fos = null;
        try {
            byte[] content = storageClient1.download_file1(fileId);
            fos = new FileOutputStream(outFile);
            //IOUtils.copy(content,fos);
            IOUtils.write(content, fos);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
    
    public static String getConfigPath() {
		try {
			String classPath = new File(FastDFSClient.class.getResource("/").getFile()).getCanonicalPath();
			String configFilePath = classPath + File.separator + "fdfs_client.conf";
			return configFilePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		
		//上传文件测试
		/*File file = new File("G:\\media\\Pictures\\1.jpg");
		String fileName = "1.jpg";
		Map<String,String> metaList = new HashMap<String, String>();
        metaList.put("width","1024");
        metaList.put("height","768");
        metaList.put("author","fxl");
        metaList.put("date","20170720");
		String uploadFile = uploadFile(file, fileName, metaList);
		System.out.println(uploadFile);*/
		
		//下载文件测试
		//String fileId = "group1/M00/05/90/wKgCQ1lwFrSAGUkEAADjuAoTie0647.jpg";
		String fileId = "group1/M00/05/91/wKgCQ1lwJwyACp6zAAAxwEOSM7g190.jpg";
		String path = "F:\\temp\\xxx.jpg";
		File outFile = new File(path);
		downloadFile(fileId, outFile);
		System.out.println("下载成功！");
		
		//获取文件元数据测试
		/*String fileId = "group1/M00/05/91/wKgCQ1lwJwyACp6zAAAxwEOSM7g190.jpg";
		Map<String,String> metaList = getFileMetadata(fileId);
        for (Iterator<Map.Entry<String,String>>  iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String,String> entry = iterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            System.out.println(name + " = " + value );
        }*/
	}

}