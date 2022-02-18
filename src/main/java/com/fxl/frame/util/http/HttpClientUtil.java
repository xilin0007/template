package com.fxl.frame.util.http;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * apache httpClient，支持https的调用
 * @author fangxilin
 * @date 2018年2月1日
 */
public class HttpClientUtil {

    // utf-8字符编码
    public static final String CHARSET_UTF_8 = "utf-8";

    // HTTP内容类型。
    public static final String CONTENT_TYPE_TEXT_HTML = "text/xml";

    // HTTP内容类型。相当于form表单的形式，提交数据
    public static final String CONTENT_TYPE_FORM_URL = "application/x-www-form-urlencoded";

    // HTTP内容类型。相当于form表单的形式，提交数据
    public static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";

    // 连接管理器
    private static PoolingHttpClientConnectionManager pool;

    // 请求配置
    private static RequestConfig requestConfig;

    private static CloseableHttpClient httpClient;

    static {

        try {
            //System.out.println("初始化HttpClientTest~~~开始");
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            //解决调用第三方API TLSv1.2版本接口时报错，解决jdk1.7https 请求默认是TLS1不支持TLS1.2的问题，解决报错 SSLException: Received fatal alert: internal_error
            SSLContext sslContext = SSLContexts.custom().useProtocol("TLSv1.2").loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            /*SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());*/
            // 配置同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register(
                    "http", PlainConnectionSocketFactory.getSocketFactory()).register(
                    "https", sslsf).build();
            // 初始化连接管理器
            pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值，表示一次请求时间内能接收的最大请求数,整个池子的大小
            pool.setMaxTotal(200);
            // 设置最大路由，服务每次能并行接收的请求数
            pool.setDefaultMaxPerRoute(50);
            // 根据默认超时限制初始化requestConfig
            int socketTimeout = 5000;
            int connectTimeout = 5000;
            int connectionRequestTimeout = 5000;
            // 设置请求超时时间
            requestConfig = RequestConfig.custom().setConnectionRequestTimeout(
                    connectionRequestTimeout).setSocketTimeout(socketTimeout).setConnectTimeout(
                    connectTimeout).build();
            //设置重试机制
//            ServiceUnavailableRetryStrategy retryStrategy = new ServiceUnavailableRetryStrategy() {
//                @Override
//                public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
//                    System.out.println("----------------当前重试次数：" + executionCount + "，上一次请求响应结果：" + response); //response获取不到接口出参，只能获取到响应状态等
//                    if (executionCount <= 1)
//                        return true;
//                    else
//                        return false;
//                }
//
//                @Override
//                public long getRetryInterval() {
//                    return 2000;
//                }
//            };
            //默认的重试机制
            DefaultHttpRequestRetryHandler defaultRetryStrategy = new DefaultHttpRequestRetryHandler(0, false);

            //http client初始化
            httpClient = HttpClients.custom()
                    // 设置连接池管理
                    .setConnectionManager(pool)
                    //设置SSL，pool中设置了，可省略
//                    .setSSLSocketFactory(sslsf)
                    // 设置请求配置
                    .setDefaultRequestConfig(requestConfig)
                    // 设置重试次数
                    .setRetryHandler(defaultRetryStrategy)
//                    .setServiceUnavailableRetryStrategy(retryStrategy)
                    .build();
            //System.out.println("初始化HttpClientTest~~~结束");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        // 设置请求超时时间
        /*requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000)
                .setConnectionRequestTimeout(50000).build();*/
    }


    /**
     * 发送Post请求
     * @param httpPost
     * @return
     */
    private static String sendHttpPost(HttpPost httpPost) {
        //CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        // 响应内容
        String responseContent = null;
        try {
            // 配置请求信息
            //httpPost.setConfig(requestConfig);
            //添加请求头信息，getHeader()可以获得请求头Request Header
            //httpPost.setHeader("Cookie", "JSESSIONID=A8E1B5509812A6B7C32F9AA9EE31FBF0");
            // 执行请求
            response = httpClient.execute(httpPost);
            // 得到响应实例
            HttpEntity entity = response.getEntity();
	        //获取inputStream，可实现文件资源的下载功能
            //InputStream inputStream = entity.getContent();


            // 可以获得响应头Response Header
            // Header[] headers = response.getHeaders(HttpHeaders.CONTENT_TYPE);
            // for (Header header : headers) {
            // System.out.println(header.getName());
            // }

            // 得到响应类型
            // System.out.println(ContentType.getOrDefault(response.getEntity()).getMimeType());

            // 判断响应状态
            if (response.getStatusLine().getStatusCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + response.getStatusLine().getStatusCode());
            }

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
                responseContent = StringEscapeUtils.unescapeXml(responseContent);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求
     * @param httpGet
     * @return
     */
    private static String sendHttpGet(HttpGet httpGet) {
        //CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        // 响应内容
        String responseContent = null;
        try {
            // 配置请求信息
            //httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            // 得到响应实例
            HttpEntity entity = response.getEntity();

            // 可以获得响应头
            // Header[] headers = response.getHeaders(HttpHeaders.CONTENT_TYPE);
            // for (Header header : headers) {
            // System.out.println(header.getName());
            // }

            // 得到响应类型
            // System.out.println(ContentType.getOrDefault(response.getEntity()).getMimeType());

            // 判断响应状态
            if (response.getStatusLine().getStatusCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + response.getStatusLine().getStatusCode());
            }

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                responseContent = EntityUtils.toString(entity, CHARSET_UTF_8);
                responseContent = StringEscapeUtils.unescapeXml(responseContent);
                EntityUtils.consume(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }



    /**
     * 发送 post请求
     * @param httpUrl 地址
     */
    public static String sendHttpPost(String httpUrl) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        //添加头部信息
        //httpPost.setHeader("jsessionid", "A8E1B5509812A6B7C32F9AA9EE31FBF0");
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 get请求
     * @param httpUrl 地址
     */
    public static String sendHttpGet(String httpUrl) {
        // 创建get请求
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpGet(httpGet);
    }

    /**
     * 发送 get请求（代理方式）
     *
     * org.apache.commons.httpclient.HttpClient设置代理的方式；
     *      org.apache.commons.httpclient.HttpClient httpClient = new HttpClient();
     * 		//设置代理访问
     * 		org.apache.commons.httpclient.HostConfiguration hostConf = new HostConfiguration();
     * 		hostConf.setProxy("242.91160.cn", 60057);
     * 		httpClient.setHostConfiguration(hostConf);
     *
     * @param httpUrl 地址
     */
    public static String sendHttpGet(String httpUrl, String proxyHost, int proxyPort) {
        // 创建get请求
        HttpGet httpGet = new HttpGet(httpUrl);
//        HttpHost httpHost = new HttpHost("242.91160.cn", 60057);
        HttpHost httpHost = new HttpHost(proxyHost, proxyPort);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setProxy(httpHost)
                .build();
        httpGet.setConfig(requestConfig);
        //设置Http报文头信息
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        return sendHttpGet(httpGet);
    }



    /**
     * 发送 post请求（带文件）
     * @param httpUrl 地址
     * @param maps 参数
     * @param fileLists 附件
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        if (maps != null) {
            for (String key : maps.keySet()) {
                meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
            }
        }
        if (fileLists != null) {
            for (File file : fileLists) {
                FileBody fileBody = new FileBody(file);
                meBuilder.addPart("files", fileBody);
            }
        }
        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param params 参数(格式:key1=value1&key2=value2)
     *
     */
    public static String sendHttpPost(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            if (params != null && params.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(params, "UTF-8");
                stringEntity.setContentType(CONTENT_TYPE_FORM_URL);
                httpPost.setEntity(stringEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     * @param maps 参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, Object> maps) {
        String parem = convertStringParamter(maps);
        return sendHttpPost(httpUrl, parem);
    }




    /**
     * 发送 post请求 发送json数据
     * @param httpUrl 地址
     * @param paramsJson 参数(格式 json)
     *
     */
    public static String sendHttpPostJson(String httpUrl, String paramsJson) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            if (paramsJson != null && paramsJson.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(paramsJson, "UTF-8");
                stringEntity.setContentType(CONTENT_TYPE_JSON_URL);
                httpPost.setEntity(stringEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求 发送xml数据
     * @param httpUrl   地址
     * @param paramsXml  参数(格式 Xml)
     *
     */
    public static String sendHttpPostXml(String httpUrl, String paramsXml) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            if (paramsXml != null && paramsXml.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(paramsXml, "UTF-8");
                stringEntity.setContentType(CONTENT_TYPE_TEXT_HTML);
                httpPost.setEntity(stringEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求 发送xml数据（代理方式）
     * @param httpUrl   地址
     * @param paramsXml  参数(格式 Xml)
     *
     */
    public static String sendHttpPostXml(String httpUrl, String paramsXml, String proxyHost, int proxyPort) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        HttpHost httpHost = new HttpHost(proxyHost, proxyPort);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setProxy(httpHost)
                .build();
        httpPost.setConfig(requestConfig);
        //设置Http报文头信息
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        try {
            // 设置参数
            if (paramsXml != null && paramsXml.trim().length() > 0) {
                StringEntity stringEntity = new StringEntity(paramsXml, "UTF-8");
                stringEntity.setContentType(CONTENT_TYPE_TEXT_HTML);
                httpPost.setEntity(stringEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }


    /**
     * 将map集合的键值对转化成：key1=value1&key2=value2 的形式
     * @param parameterMap 需要转化的键值对集合
     * @return 字符串
     */
    public static String convertStringParamter(Map<String, Object> parameterMap) {
    	StringBuilder parameterBuilder = new StringBuilder();
        if (parameterMap != null) {
            Iterator<Entry<String, Object>> iterator = parameterMap.entrySet().iterator();
            while (iterator.hasNext()) {
            	Map.Entry<String, Object> entry = iterator.next();
            	parameterBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                if (iterator.hasNext()) {
                	parameterBuilder.append("&");
                }
            }
        }
        return parameterBuilder.toString();
    }


    public static void main(String[] args) throws Exception {

        //System.out.println(sendHttpGet("http://www.baidu.com"));
    	//String httpUrl = "http://192.168.0.2:8080/nutritionV2/user/findUserByUserId;jsessionid=A8E1B5509812A6B7C32F9AA9EE31FBF0";
    	/*String httpUrl = "http://192.168.0.2:8080/nutritionV2/user/findUserByUserId";
    	Map<String, Object> maps = new HashMap<>();
    	maps.put("userId", 11908);
    	maps.put("hospitalId", 42);
    	System.out.println(sendHttpPost(httpUrl, maps));*/

        //String url = "http://report.91160.com/report";
//        String url = "https://wxis.91160.com/hdeps/servlet";
//        Map<String, Object> maps = new HashMap<>();
//        maps.put("param", "serviceId{=}pay_checkAccount{,}dataPackType{=}4{,}dataDesZip{=}00{,}userId{=}111{,}password{=}AE13384C53777290C135546992B092CE");
//        maps.put("data", "<request><head><key>check_account</key><hospcode>22</hospcode><token></token><time></time></head><body><unit_id>22</unit_id><pay_method>weixin</pay_method><bill_dateBegin>2021-05-07</bill_dateBegin><bill_dateEnd>2021-05-07</bill_dateEnd><unitBusinessId>inHospital</unitBusinessId></body></request>");
//        String str = sendHttpPost(url, maps);
//        System.out.println(str);

        String url = "http://121.12.165.76:9182/webservices/hisApiWebService?wsdl";
//        String resp = sendHttpGet(url, "242.91160.cn", 60057);
        String params = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.ky.com\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "  <ser:hisESB_submitExamApptOrder>\n" +
                "         <xmlString>\n" +
                "         <![CDATA[\n" +
                "\n" +
                "     <request>\n" +
                "    <head>\n" +
                "        <key>hisESB_submitExamApptOrder</key>\n" +
                "        <hospcode>263</hospcode>\n" +
                "        <token></token>\n" +
                "        <time></time>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <orderNo>14p5aa989</orderNo>\n" +
                "        <cardNo>A000001321</cardNo>\n" +
                "        <orgName>康华医院</orgName>\n" +
                "        <orgCode>263</orgCode>\n" +
                "        <examineDate>2021-09-09</examineDate>\n" +
                "        <bodyTemperature>36</bodyTemperature>\n" +
                "        <mzType>1</mzType>\n" +
                "    </body>\n" +
                "</request>\n" +
                " \n" +
                "         ]]>\n" +
                "         </xmlString>\n" +
                "      </ser:hisESB_submitExamApptOrder>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        String resp = sendHttpPostXml(url, params, "242.91160.cn", 60057);
        System.out.println("resp = " + resp);
    }
}