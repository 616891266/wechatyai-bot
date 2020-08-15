package com.smwsk.bot.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 网络请求工具类型
 * Author: Wang Shao Kui
 * Create date: 2020/7/16 - 9:32
 * Description:
 */
public class HttpUtil {

	static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	private final static int CONNECT_TIMEOUT = 3000;
	private final static int SOCKET_TIMEOUT = 3000;
	private static String defaultDecode = "UTF-8";
	private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
	/**
	 * post请求
	 *
	 * @param requestUrl    请求url
	 * @param requestParams 请求参数
	 * @param proxy         请求代理信息
	 * @return
	 */
	public static String requestPost(String requestUrl, Map<String, Object> requestParams, HttpHost... proxy) {
		String result = StringUtils.EMPTY;
		CloseableHttpClient httpClient = getHttpClient(proxy.length > 0 ? proxy[0] : null);
		HttpPost httpPost = new HttpPost(requestUrl);
		setDefaultRequestHeader(httpPost, httpClient);
		Set<Map.Entry<String, Object>> entries = requestParams.entrySet();
		List<BasicNameValuePair> basicNameValuePairList = new ArrayList<>();
		entries.forEach(item -> {
			basicNameValuePairList.add(new BasicNameValuePair(item.getKey(), item.getValue().toString()));
		});
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(basicNameValuePairList, defaultDecode));
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			logger.info("request response code: {}", statusCode);
			if (entity != null) {
				result = EntityUtils.toString(entity, defaultDecode);
				logger.info("request success response data: {}", result);
			} else {
				logger.info("request success response data: {}", result);
			}
		} catch (Exception e) {
			logger.error("request exception message: {}", e.getMessage());
			e.printStackTrace();
		} finally {
			poolingHttpClientConnectionManager.closeExpiredConnections();
		}
		return result;
	}

	/**
	 * get请求
	 *
	 * @param requestUrl    请求地址
	 * @param requestParams 请求参数
	 * @param proxy         请求代理信息
	 * @return
	 */
	public static String requestGet(String requestUrl, Map<String, Object> requestParams, HttpHost... proxy) {
		String result = StringUtils.EMPTY;
		CloseableHttpClient httpClient = getHttpClient(proxy.length > 0 ? proxy[0] : null);
		try {
			URIBuilder uriBuilder = new URIBuilder(requestUrl);
			Set<Map.Entry<String, Object>> entries = requestParams.entrySet();
			entries.forEach(item -> {
				uriBuilder.addParameter(item.getKey(), item.getValue().toString());
			});
			HttpGet httpGet = new HttpGet(uriBuilder.build());
			setDefaultRequestHeader(httpGet, httpClient);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (logger.isDebugEnabled()) {
				logger.debug("request code response: {}", statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, defaultDecode);
				if (logger.isDebugEnabled()) {
					logger.info("request success response data: {}", result);
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("request exception message: {}", e.getMessage());
		}finally {
			poolingHttpClientConnectionManager.closeExpiredConnections();
		}

		return result;
	}

	public static String requestGetIgnoreSSL(String requestUrl, Map<String, Object> requestParams, HttpHost... proxy) {
		String result = StringUtils.EMPTY;
		CloseableHttpClient httpClient = getIgnoreHttpClient(proxy.length > 0 ? proxy[0] : null);
		try {
			URIBuilder uriBuilder = new URIBuilder(requestUrl);
			Set<Map.Entry<String, Object>> entries = requestParams.entrySet();
			entries.forEach(item -> {
				uriBuilder.addParameter(item.getKey(), item.getValue().toString());
			});
			HttpGet httpGet = new HttpGet(uriBuilder.build());
			setDefaultRequestHeader(httpGet, httpClient);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (logger.isDebugEnabled()) {
				logger.debug("request code response: {}", statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, defaultDecode);
				if (logger.isDebugEnabled()) {
					logger.info("request success response data: {}", result);
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("request exception message: {}", e.getMessage());
		}finally {
			poolingHttpClientConnectionManager.closeExpiredConnections();
		}

		return result;
	}

	private static CloseableHttpClient getIgnoreHttpClient(HttpHost proxy) {
		SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		// 连接池最大的连接数
		poolingHttpClientConnectionManager.setMaxTotal(6000);
		// 单个路由的最大连接数,例如：www.baidu.com,图片最大高峰并发量520
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(500);
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
				new String[]{"TLSv1.2"}, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		RequestConfig.Builder custom = RequestConfig.custom();
		// 连接超时时间 建立连接时间 此前设置3000
		custom.setConnectTimeout(CONNECT_TIMEOUT);
		// 从connect Manager获取Connection 超时时间（连接池获取连接超时时间）
		custom.setConnectionRequestTimeout(CONNECT_TIMEOUT);
		// （获取response的返回时间）读取返回时间 此前设置40000，官方建议40s
		custom.setSocketTimeout(SOCKET_TIMEOUT);
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
				.setProxy(proxy).setDefaultRequestConfig(custom.build()).setSSLSocketFactory(sslConnectionSocketFactory).build();
		return httpClient;
	}

	/**
	 * 获取HttpClient请求对象
	 *
	 * @param proxy 设置代理对象
	 * @return
	 */
	private static CloseableHttpClient getHttpClient(HttpHost... proxy) {
		poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		// 连接池最大的连接数
		poolingHttpClientConnectionManager.setMaxTotal(6000);
		// 单个路由的最大连接数,例如：www.baidu.com,图片最大高峰并发量520
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(500);
		RequestConfig.Builder custom = RequestConfig.custom();
		// 连接超时时间 建立连接时间 此前设置3000
		custom.setConnectTimeout(CONNECT_TIMEOUT);
		// 从connect Manager获取Connection 超时时间（连接池获取连接超时时间）
		custom.setConnectionRequestTimeout(CONNECT_TIMEOUT);
		// （获取response的返回时间）读取返回时间 此前设置40000，官方建议40s
		custom.setSocketTimeout(SOCKET_TIMEOUT);
		if (proxy.length > 0) {
			custom.setProxy(proxy[0]);
		}
		RequestConfig config = custom.build();
		// DefaultHttpRequestRetryHandler 重试策略（可自定义）
		CloseableHttpClient httpClient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)).setConnectionManager(poolingHttpClientConnectionManager)
				.setDefaultRequestConfig(config).build();
		return httpClient;
	}

	/**
	 * 设置默认请求头
	 *
	 * @param httpGet
	 * @param httpClient
	 */
	private static void setDefaultRequestHeader(HttpGet httpGet, CloseableHttpClient httpClient) {
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
		httpGet.setHeader("Cache-Control", "max-age=0");
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("Upgrade-Insecure-Requests", "1");
	}

	/**
	 * 设置默认请求头
	 *
	 * @param httpPost
	 * @param httpClient
	 */
	private static void setDefaultRequestHeader(HttpPost httpPost, CloseableHttpClient httpClient) {
		httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
		httpPost.setHeader("Cache-Control", "max-age=0");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Upgrade-Insecure-Requests", "1");
	}

}
