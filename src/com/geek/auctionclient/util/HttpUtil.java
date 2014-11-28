package com.geek.auctionclient.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	// ����httpclient����
	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String BASE_URL = "";

	/**
	 * @param url
	 *            ���������URL
	 * @return ��������Ӧ�ַ���
	 * @throws Exception
	 */
	public static String getRequest(final String url) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// ����httpget����
						HttpGet get = new HttpGet(url);
						// ����get����
						HttpResponse response = httpClient.execute(get);
						// ����������ɹ��ķ�����Ӧ
						if (response.getStatusLine().getStatusCode() == 200) {
							String result = EntityUtils.toString(response
									.getEntity());
							return result;
						}
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}

	/**
	 * 
	 */
	public static String postRequest(final String url,
			final Map<String, String> rawParams) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// ����httppost����
						HttpPost post = new HttpPost(url);
						// ������ݵĲ������࣬���ԶԴ��ݵĲ������з�װ
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						for (String key : rawParams.keySet()) {
							// ��װ�������
							params.add(new BasicNameValuePair(key, rawParams
									.get(key)));
						}
						// �����������
						post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
						// ����post����
						HttpResponse httpResponse = httpClient.execute(post);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							// ��ȡ��������Ӧ�ַ���
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							return result;
						}
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}
}
