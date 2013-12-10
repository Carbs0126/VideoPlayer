package com.player.tool;

import java.util.List;

import net.tsz.afinal.FinalDb;

import android.content.Context;

/**
 * ���ݿ⹤����
 * @author Administrator
 *
 */
public class DBUtil{
	public final static String DBNAME = "tv_list";//���ݿ�����
	
	private Context context;
	private List<?> list;
	public DBUtil(Context context) {
		this.context = context;
	}
	
	/**
	 * ��ȡ����
	 * @param clazz
	 * @return
	 */
	public List<?> getAllTvType(Class<?> clazz) {
		FinalDb db = FinalDb.create(context);
		list = db.findAll(clazz);
		
		return list;
	}
	
}
