package com.iruen.www.http.apache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.iruen.www.helper.Config;

public class HttpClientKakaoPushTokenRegister {
	
	// HttpClient 생성
		private static String adminKey = Config.getInstance().getProperties("adminKey");
		private static String uuid = Config.getInstance().getProperties("uuid");
		private static String device_id = Config.getInstance().getProperties("device_id");
		private static String push_type = Config.getInstance().getProperties("push_type");
		private static String push_token = Config.getInstance().getProperties("push_token");

		public void tokenRegister() {
			HttpClient httpClient = HttpClientBuilder.create().build();
			try {
				// HttpGet생성
				HttpPost httpPost = new HttpPost("https://kapi.kakao.com/v1/push/register");

				System.out.println("executing request : " + httpPost.getURI());
				
				//Setup the request header
				httpPost.addHeader("Authorization", adminKey);

				// Setup the request parameters
				ArrayList<NameValuePair> postParameters;
				
				postParameters = new ArrayList<NameValuePair>(4);
			    postParameters.add(new BasicNameValuePair("uuid", uuid));
			    postParameters.add(new BasicNameValuePair("device_id", device_id));
			    postParameters.add(new BasicNameValuePair("push_type", push_type));
			    postParameters.add(new BasicNameValuePair("push_token", push_token));
			    
			    httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();

				System.out.println("---------------------------------------- token register start ----------------------------------------");
				// 응답 결과
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: " + entity.getContentLength());
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));

					String line = "";
					while ((line = rd.readLine()) != null) {
						System.out.println(line);
					}
				}
				
				httpPost.abort();
				System.out.println("---------------------------------------- token register end ----------------------------------------\n");

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException ee) {
				ee.printStackTrace();
			} finally {
				httpClient.getConnectionManager().shutdown();
			}
		}
}
