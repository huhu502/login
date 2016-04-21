package com.example.checkmobilecommect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

public class DownloadService extends Service {
	public String TAG="DownloadService";
	public  static final int Download_Notification_id=1;
	
	private NotificationManager mNotificationManager;
	private Notification   mNotification;
	private RemoteViews mRemoteView,mFinish_RemoteView;
	int progress=0,fileSize=100;
	private String apk_url="http://192.168.199.129:8080/MyWebServer/My2048.apk";
	private String	apkname="My2048.apk";
	private String filePath;
	private boolean downloading=true;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v(TAG, "Service����");
		//��ʼ������
		filePath=Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/";
		
		creatNotification();
		//downlode_sample.start();//ģ�������߳�
		download.start();
		
		
	}

	private void creatNotification() {
		mNotificationManager = (NotificationManager) getSystemService(  
		            android.content.Context.NOTIFICATION_SERVICE);  
		//notification�����ã����Բ���ʡȥ
		 mNotification = new Notification();  
		mNotification.icon=R.drawable.ic_launcher;
		mNotification.when=System.currentTimeMillis();
		//�Զ����
		mRemoteView = new RemoteViews(this.getPackageName(), R.layout.remote_view_layout);
		mRemoteView.setImageViewResource(R.id.image	, R.drawable.ic_launcher);
		mRemoteView.setTextViewText(R.id.text, "��������");
		mRemoteView.setProgressBar(R.id.progress_horizontal, fileSize, progress, false);
		
		mNotification.contentView=mRemoteView;
		mNotification.flags=Notification.FLAG_NO_CLEAR;
		mNotificationManager.notify(Download_Notification_id, mNotification);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG, "service����");
		mNotificationManager.cancel(1);
	}
	
	Thread download=new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {  
                //���������°�apk��ַ  
                URL url = new URL(apk_url);  
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
                conn.connect();  
                int length = conn.getContentLength();  
                InputStream is = conn.getInputStream();  
                File file = new File(filePath);  
                if(!file.exists()){  
                    //����ļ��в�����,�򴴽�  
                    file.mkdir();  
                }  
                //���ط��������°汾�����д�ļ���  
                String apkFile = filePath + apkname;  
                File ApkFile = new File(apkFile);  
                FileOutputStream fos = new FileOutputStream(ApkFile);  
                int count = 0;  
                byte buf[] = new byte[1024];  
                do{  
                    int numRead = is.read(buf);  
                    count += numRead;  
                    //���½�����  
                    progress = (int) (((float) count / length) * 100);  
                    handler.sendEmptyMessage(1);
                    try {
						download.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    if(numRead <= 0){  
                        //�������֪ͨ��װ  
                      
                       handler.sendEmptyMessage(0);
                       downloading=false;
                        break;  
                    }  
                    fos.write(buf,0,numRead);  
                    //�����ȡ��ʱ����ֹͣ����  
                }while(downloading);  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
		}
	});
	
	 private Handler handler = new Handler() {  
	        public void handleMessage(Message msg) {  
	            switch (msg.what) {  
	            case 1:  
	            	Log.v(TAG, "���½���:"+progress);
	                // ���½������  
	            	mRemoteView.setProgressBar(R.id.progress_horizontal, fileSize, progress, false);
					mNotificationManager.notify(Download_Notification_id, mNotification);
	                break;  
	            case 0:  
	                // ��װapk�ļ�  
	                Log.v(TAG, "�������");
	            	mRemoteView.setTextViewText(R.id.text, "�������");
	    			mNotification.flags=Notification.FLAG_AUTO_CANCEL;
	    			mNotificationManager.notify(Download_Notification_id, mNotification);
	    			installApk();
	    			onDestroy();
	                break;  
	            default:  
	                break;  
	            }  
	        };  
	    };  
	
	    private void installApk() {  
	        // ��ȡ��ǰsdcard�洢·��  
	        File apkfile = new File(filePath + apkname);  
	        if (!apkfile.exists()) {  
	            return;  
	        }  
	        Intent i = new Intent(Intent.ACTION_VIEW);  
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        // ��װ�����ǩ����һ�£����ܳ��ֳ���δ��װ��ʾ  
	        i.setDataAndType(Uri.fromFile(new File(apkfile.getAbsolutePath())), "application/vnd.android.package-archive");   
	        this.startActivity(i);  
	    }  
	Thread downlode_sample=new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (progress!=fileSize) {
				progress=progress+1;
				mRemoteView.setProgressBar(R.id.progress_horizontal, fileSize, progress, false);
				
				mNotificationManager.notify(1, mNotification);
				try {
					downlode_sample.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mRemoteView.setTextViewText(R.id.text, "�������");
			mNotification.flags=Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(1, mNotification);
			//��װ������а�װ
			onDestroy();
			
		}
	});
}
