package com.iruen.www.http.apache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.GsonBuilder;
import com.iruen.www.helper.Config;
import com.iruen.www.kakao.domain.KakaoMessage;

public class HttpClientKakaoSendPushMessage {
	// HttpClient 생성
			private static URIBuilder builder = new URIBuilder();
			private static String uuid = Config.getInstance().getProperties("uuid");
			private static String adminKey = Config.getInstance().getProperties("adminKey");
			private static HttpClientKakaoSearchPushToken tokenSearch = new HttpClientKakaoSearchPushToken();

			@SuppressWarnings("unchecked")
			public void sendMessage(String message) {
				HttpClient httpClient = HttpClientBuilder.create().build();
				tokenSearch.searchToken();
				
				builder.setScheme("https");
				builder.setHost("kapi.kakao.com");
				builder.setPath("/v1/push/send");
				
				try {
					// HttpGet생성
					HttpPost httpPost = new HttpPost(builder.build());
					
					//Setup the request header
					httpPost.addHeader("Authorization", adminKey);

					System.out.println("executing request : " + httpPost.getURI());

					// Setup the request parameters
					ArrayList<NameValuePair> postParameters;
					JSONArray uuids = new JSONArray();
					uuids.add(uuid);
//					KakaoMessage message = KakaoMessage.getDummyMessage();
					JSONObject customMessageObject = new JSONObject();
//					customMessageObject.put("message", "iruen dummy message!");
					customMessageObject.put("message",message);
					JSONObject messageObject = new JSONObject();
					messageObject.put("custom_field", customMessageObject);
					messageObject.put("return_url","skyfly33.dothome.co.kr/kakao/kakaologindemo.html");
					JSONObject gcmMessage = new JSONObject();
					gcmMessage.put("for_gcm", messageObject);
					
					postParameters = new ArrayList<NameValuePair>(4);
				    postParameters.add(new BasicNameValuePair("uuids", uuids.toJSONString()));
				    System.out.println("message : " + messageObject.toJSONString());
				    postParameters.add(new BasicNameValuePair("push_message", gcmMessage.toJSONString()));
				    
				    httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

					HttpResponse response = httpClient.execute(httpPost);
					HttpEntity entity = response.getEntity();

					System.out.println("---------------------------------------- push message send start ----------------------------------------");
					// 응답 결과
					System.out.println(response.getStatusLine());
					if (entity != null) {
						System.out.println("Response content length : " + entity.getContentLength());
						System.out.println("Reponse Content-type : " + entity.getContentType());
						BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

						String line = "";
						while ((line = rd.readLine()) != null) {
							System.out.println(line);
						}
					}
					httpPost.abort();
					System.out.println("---------------------------------------- push message send end ----------------------------------------\n");

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch(URISyntaxException e){
					e.printStackTrace();
				}finally {
					httpClient.getConnectionManager().shutdown();
				}
			}
}
