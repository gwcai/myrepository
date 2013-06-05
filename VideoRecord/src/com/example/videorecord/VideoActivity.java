package com.example.videorecord;

import getFilePath.FileName;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Locale;
import java.util.Timer;

import cc.localsocket.WriteToLocalSocket;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class VideoActivity extends Activity implements OnInfoListener, OnErrorListener{
	private Button btStart;
	private Camera mCamera;
	private SurfaceView mPreview;
	private MediaRecorder mVideo;
	private TextView tv1,tv2,tv3,tv4,tv5;
	private boolean bool ;
	private int hour,second,minute;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private WriteToLocalSocket ls;
	private static final int mVideoEncoder = MediaRecorder.VideoEncoder.H264; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//屏幕旋转
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐去标题
		setContentView(R.layout.main);
		hour = 0;
		minute = 0;
		second = 0;
		ls = new WriteToLocalSocket();
		try{
			mCamera = Camera.open();
		}catch (Exception e){
        	e.printStackTrace();
        }
		mPreview = new CameraPreview(this,mCamera);  
		FrameLayout preview = (FrameLayout) findViewById(R.id.surface);
		preview.addView(mPreview);
		
		
		btStart = (Button) findViewById(R.id.start);
		tv1 = (TextView) findViewById(R.id.hour);
		tv2 = (TextView) findViewById(R.id.minute);
		tv3 = (TextView) findViewById(R.id.second);
		tv4 = (TextView) findViewById(R.id.dot1);
		tv5 = (TextView) findViewById(R.id.dot2);
		tv1.setVisibility(View.GONE);
		tv2.setVisibility(View.GONE);
		tv3.setVisibility(View.GONE);
		tv4.setVisibility(View.GONE);
		tv5.setVisibility(View.GONE);
		btStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	mCamera.lock();
				releaseCamera();
				if(mVideo != null)    //停止录制
				{
					bool = false;
					tv1.setText(format(hour));
					tv2.setText(format(minute));
					tv3.setText(format(second));
					mVideo.stop();
					mVideo.release();
					mVideo = null;
				}else{
					bool = true;
					
					if(!prepareVideoRecorder())   //开始录制视频
							releaseMediaRecorder();
						tv1.setVisibility(View.VISIBLE);
						tv2.setVisibility(View.VISIBLE);
						tv4.setVisibility(View.VISIBLE);
						tv5.setVisibility(View.VISIBLE);
						tv3.setVisibility(View.VISIBLE);
						handler.postDelayed(task, 1000);  //1 s之后将handler加入消息队列开始执行							
				}
			}			
		});
	}
	private boolean prepareVideoRecorder()
	{		
		mVideo = new MediaRecorder();
		mVideo.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mVideo.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		
	//	mVideo.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW)); //视频质量720p
				
		mVideo.setPreviewDisplay(mPreview.getHolder().getSurface());  //设置预览
		mVideo.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mVideo.setVideoSize(640, 480);
		mVideo.setVideoEncoder(mVideoEncoder);  //编码格式
		mVideo.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
//		mVideo.setOutputFile(FileName.getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
		FileDescriptor fd = ls.sender.getFileDescriptor();
		mVideo.setOutputFile(fd);         //写入LocalSocket
		
		try {
			mVideo.setOnInfoListener(this);                
			mVideo.setOnErrorListener(this);
			mVideo.prepare();
			mVideo.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			releaseMediaRecorder();
			return false;
		}
		return true;
	}
	
	private void releaseMediaRecorder(){
        if (mVideo != null) {
        	mVideo.reset();   // clear recorder configuration
        	mVideo.release(); // release the recorder object
        	mVideo = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//releaseMediaRecorder();
		//releaseCamera();
	}	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mVideo != null)
		   releaseMediaRecorder();
		if(mCamera != null)
			releaseCamera();
	}
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//	}


	/*实现计时功能*/
	private Handler handler = new Handler();
	public Runnable task = new Runnable(){

		@Override
		public void run()
         {
             if (bool)
             {
                 handler.postDelayed(this, 1000);
                 second++;
                 if (second < 60)
                 {
                     tv3.setText(format(second));
                 } else if (second < 3600)
                 {
                     minute += second / 60;
                     second = second % 60;
                     tv2.setText(format(minute));
                     tv3.setText(format(second));
                 } else
                 {
                     hour += second / 3600;
                     minute += (second % 3600) / 60;
                     second = (second % 3600) % 60;
                     tv1.setText(format(hour));
                     tv2.setText(format(minute));
                     tv3.setText(format(second));
                 }
             }
         }
	};
	private String format(int num) {
		// TODO Auto-generated method stub
		String s =  String.valueOf(num);
		if(s.length() == 1)
			s = "0" + s;
		return s;
	}
	/*录制出错事件*/

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		switch (what) {
		case MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN:
			Log.d("TAG", "MEDIA_RECORDER_INFO_UNKNOWN");
			break;
		case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
			Log.d("TAG", "MEDIA_RECORDER_INFO_MAX_DURATION_REACHED");
			break;
		case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
			Log.d("TAG", "MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED");
			break;
		}
	}
	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		if (what == MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN) {
			Log.d("TAG", "MEDIA_RECORDER_ERROR_UNKNOWN");
			finish();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

}
