package com.example.Utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;

/*��װhttpCilent get��post����ʽ
httpCilentά��session
һ��HTTPclient�������һ�������*/
public class HttpUtils {
	
	public static synchronized HttpResponse send(HttpClient client,String url,List<NameValuePair> param) throws ClientProtocolException, IOException{
		HttpResponse res=null;
		if(param==null){
			//����get��ʽ����
			HttpGet get=new HttpGet(url);
			res=client.execute(get);
		}
		else{
			//����post��ʽ����
			HttpPost post=new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(param,HTTP.UTF_8));
			res=client.execute(post);
		}
		return res;
	}
}
