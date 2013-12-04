package com.player.main;

import java.io.IOException;

import com.player.tool.CommonUtil;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainPlayerActivity extends Activity implements OnCompletionListener,OnInfoListener, OnBufferingUpdateListener,OnTouchListener {

	/**
	 * TODO: Set the path variable to a streaming video URL or a local media file
	 * path.
	 */
	private String path = "http://live.gslb.letv.com/gslb?stream_id=cctv5_800&tag=live&ext=m3u8&sign=live_tv";
	//private String path = "http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/0300020503528470BB522307FE5B85C2966162-C259-CF18-957F-3858C1FA97BD?K=de0f1115f5b64e2a261d5755";
	//private String path = "rtmp://lm02.everyontv.net/ptv/phd197";
	private Uri uri;
	private VideoView mVideoView;
	private boolean isStart;
	private ProgressBar pb;
	private TextView downloadRateView, loadRateView , endText;
	private ImageButton replay_btn;
	private MediaController mMediaController;
	private float startX ;
	private float startY ;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.main_player);
		mVideoView = (VideoView) findViewById(R.id.buffer);
		pb = (ProgressBar) findViewById(R.id.probar);
		replay_btn = (ImageButton)findViewById(R.id.replayBtn);
		//replay_btn.setVisibility(View.GONE);
		
		downloadRateView = (TextView) findViewById(R.id.download_rate);
		loadRateView = (TextView) findViewById(R.id.load_rate);
		endText = (TextView)findViewById(R.id.endText);
		
		Intent intent = getIntent();
		path = intent.getStringExtra("tv_url");
		
		if (path == "") {
			// Tell the user to provide a media file URL/path.
			Toast.makeText(MainPlayerActivity.this, "Please edit VideoBuffer Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
			return;
		} else {
			/*
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 */
			uri = Uri.parse(path);
			mVideoView.setVideoURI(uri);
			mMediaController = new MediaController(this);
			mVideoView.setMediaController(mMediaController);
			mVideoView.requestFocus();
			mVideoView.setOnInfoListener(this);
			mVideoView.setOnBufferingUpdateListener(this);
			mVideoView.setOnCompletionListener(this);
			mVideoView.setOnTouchListener(this);
			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					// optional need Vitamio 4.0
					mediaPlayer.setPlaybackSpeed(1.0f);
				}
			});
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				isStart = true;
				pb.setVisibility(View.VISIBLE);
				downloadRateView.setVisibility(View.VISIBLE);
				loadRateView.setVisibility(View.VISIBLE);
				
			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			if (isStart) {
				mVideoView.start();
				pb.setVisibility(View.GONE);
				downloadRateView.setVisibility(View.GONE);
				loadRateView.setVisibility(View.GONE);
			}
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			downloadRateView.setText(extra + "kb/s");
			break;
		}
		return true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		loadRateView.setText("���ڼ���    " + percent + "%");
	}

	@Override
	public void onCompletion(final MediaPlayer mp) {
		replay_btn.setVisibility(View.VISIBLE);
		endText.setVisibility(View.VISIBLE);
		mVideoView.setBackgroundColor(getResources().getColor(android.R.color.black));
		replay_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mp.reset();
				mVideoView.setVideoURI(Uri.parse(path));
				mVideoView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				try {
					mp.prepare();
					replay_btn.setVisibility(View.GONE);
					endText.setVisibility(View.GONE);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		CommonUtil util  = new CommonUtil(getApplicationContext());
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//��¼��ʼ����
			startX = event.getX();
			startY = event.getY();
			//Toast.makeText(this, "��ʼ����Ϊ��"+"��ʼ���꣺"+startX+"::"+startY, Toast.LENGTH_SHORT).show();
			
			break;
		case MotionEvent.ACTION_UP:
			if (mMediaController.isShowing()) {
			     mMediaController.hide();
			} else {
			     mMediaController.show();
			}
			
			if (startX - event.getX() > 100) {
				//mVideoView.setVolume(leftVolume, rightVolume)
				System.out.println("�󻬣�");
			}else if (event.getX()-startX > 100) {
				System.out.println("�һ���");
			}else if (startY - event.getY() >100 ) {
				System.out.println(util.getVolume()+":���������" + util.getMaxVolume() +"�ϻ����ľ��룺 "+ (startY - event.getY()/100));
			}else if (event.getY()- startY > 100 ) {
				//System.out.println("�»���");
				System.out.println(util.getVolume()+":���������" + util.getMaxVolume() +"�ϻ����ľ��룺 "+ (event.getY()- startY /100));
			}
			break;
		case MotionEvent.ACTION_POINTER_UP://�������������㣬�ڶ��������뿪
			System.out.println("�ڶ��������뿪");
			break;
			
		default:
			break;
		}
		return true;
	}
}