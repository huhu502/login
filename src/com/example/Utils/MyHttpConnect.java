package com.example.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class MyHttpConnect {
	String TAG="MyPost";
	HttpClient http ;
	HttpPost post ;
	String urlString;
	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	public MyHttpConnect(String url) {
		// TODO Auto-generated constructor stub
		 this.urlString=url;
		 http = new DefaultHttpClient();
		 post = new HttpPost(url);
	}
	
	public void addPostValue(String name,String value){
		parameters.add(new BasicNameValuePair(name,value));
		Log.v(TAG, name+"="+value);
	}

	public String StartGet() {
		try {// ��ȡHttpURLConnection���Ӷ���
			 URL url = new URL(urlString); 
			HttpURLConnection httpConn = (HttpURLConnection)url
					.openConnection();
			// ������������
			httpConn.setConnectTimeout(3000);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("GET");
			// ��ȡ��Ӧ��
			int respCode = httpConn.getResponseCode();
			if (respCode == 200) {
				 InputStream is = httpConn.getInputStream();
                 String result = getStringFromInputStream(is);
                 Log.v(TAG, "result:"+result);
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String getStringFromInputStream(InputStream is) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		// һ��Ҫдlen=is.read(buffer)
		// ���while((is.read(buffer))!=-1)���޷�������д��buffer��
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		is.close();
		String state = os.toString();// �����е�����ת�����ַ���,���õı�����utf-8(ģ����Ĭ�ϱ���)
		os.close();
		return state;
	}

	public String Startpost()  {
			try {
				post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));//���ô�������������д���
				HttpResponse httpResponse = http.execute(post);//��ȡ����ķ��ؽ��
				if (httpResponse.getStatusLine().getStatusCode() == 200) {// �������Ӧ
					Log.e(TAG, "success!");
					String result = EntityUtils.toString(httpResponse
							.getEntity());// ��ȡ���е��ַ���
					 Log.v(TAG, "result:"+result);
					return result;
				} else {
					Log.e(TAG,"fail");
					return null;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(ClientProtocolException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			return null; 
	}
}
