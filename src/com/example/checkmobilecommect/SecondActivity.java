package com.example.checkmobilecommect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.Utils.MyHttpConnect;
import com.example.been.User;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends Activity {

	private String TAG="SecondActivity";
	private PackageInfo pinfo ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		Intent intent=getIntent();
		String name=intent.getStringExtra("name");
		
		TextView txt1=(TextView) findViewById(R.id.txt_name);
		txt1.setText("welcome "+name);
		
		 try {
				pinfo = getPackageManager().getPackageInfo(
							"com.example.checkmobilecommect", 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		TextView txt=(TextView) findViewById(R.id.txt_version);
		txt.setText("versionCode="+pinfo.versionCode+" versionName="+pinfo.versionName);
		
    	checkVersion.start();
	}
	
	Thread checkVersion = new Thread(new Runnable() {
		int n_versionCode = -1, versionCode = -1;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.v(TAG, "检测是否有更新");
			MyHttpConnect get_link = new MyHttpConnect(MyURL.Get_version);
			try {
				JSONObject json = JSON.parseObject(get_link.StartGet());
				int success = json.getIntValue("success");
				if (success == 1) {
					n_versionCode = json.getIntValue("versionCode");
				}
				
				versionCode = pinfo.versionCode;
				Log.v(TAG, "new:old=" + n_versionCode + ":" + versionCode);
				if (n_versionCode > versionCode) {
					// 发送消息弹窗
					version_handler.sendEmptyMessage(HAS_NEW_VERSION);
				}
			} catch (NullPointerException e) {
				// TODO: handle exception
				Log.v(TAG, "获取Json失败，请检查网络连接");
				e.printStackTrace();
			}
		}
	});
	
	private final int HAS_NEW_VERSION = 103;
	Handler version_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HAS_NEW_VERSION:
				// 显示dialog
				Builder builder = new Builder(SecondActivity.this);
				builder.setTitle("更新提示");
				builder.setMessage("检测到新版本更新，是否立即下载？");
				builder.setPositiveButton("快给我下", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//开启后台下载服务
						Log.v(TAG, "确定按钮点击了，下载服务开启");
						Intent intent=new Intent(SecondActivity.this, DownloadService.class);
						startService(intent);
					}
				});
				builder.setNegativeButton("稍后再说", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.v(TAG, "取消按钮被点击");
					}
				});
				builder.create().show();
				break;

			default:
				break;
			}
		};
	};
	
}
