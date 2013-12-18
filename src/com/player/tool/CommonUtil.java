package com.player.tool;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.player.adapter.PlayHistoryAdapter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;

public class CommonUtil {
	private Context context;
	public CommonUtil(Context context) {
		this.context = context;
	}
	
	/**
	 * ��ȡ��ǰ����
	 * @return
	 */
	public int getBrightness(){
		int brightness = -1;
		try{
			brightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
		}catch(SettingNotFoundException ex){
			ex.printStackTrace();
		}
		
		return brightness;
	}
	
	/**
	 * ��ȡ��ǰ����
	 * @return
	 */
	public int getVolume(){
		int volume = -1;
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		return volume;
	}
	
	/**
	 * ����������
	 * @return
	 */
	public int getMaxVolume() {
		int maxVolume = -1;
		
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		return maxVolume;
	}
	
	/**
	 * ��ȡraw�ļ����µ�Json�ļ�
	 * @param jsonId Json������
	 * @return byte[]
	 */
	public byte[] readJson(int jsonId) {
		InputStream is = context.getResources().openRawResource(jsonId);
		byte[] buffer = null;
		try {
			buffer = new byte[is.available()];
			is.read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
		return buffer;
	}
	
	

	/**
	 * ͨ��������ƻ�ȡ�����е�T������
	 * @param objName Ҫ�жϵĶ�������
	 * @return 
	 */
	public static String getGenericType(String objName){
		Class<PlayHistoryAdapter> class1 = PlayHistoryAdapter.class;
		Field mapField = null;
		try {
			mapField = class1.getDeclaredField(objName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return "";
		}
		
		Type mapMainType = mapField.getGenericType();
		
		if (mapMainType instanceof ParameterizedType) {
			 // ִ��ǿ������ת��   
            ParameterizedType parameterizedType = (ParameterizedType)mapMainType;   
            // ��ȡ����������Ϣ����Map   
            Type basicType = parameterizedType.getRawType();   
            System.out.println("��������Ϊ��"+basicType);   
            // ��ȡ�������͵ķ��Ͳ���   
            Type[] types = parameterizedType.getActualTypeArguments();   
            for (int i = 0; i < types.length;i++) {   
                System.out.println("��"+(i+1)+"�����������ǣ�"+types[i].toString());
                return types[i].toString();
            }   
        } 
        
		return "";
		
	}
	
	/**
	 * �����Ļ�ķֱ��ʵĿ�
	 * @param activity
	 * @return ��Ļ�Ŀ�ȣ���λ����
	 */
	public int getScreenPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		
		return width;
		
	}
	
}
