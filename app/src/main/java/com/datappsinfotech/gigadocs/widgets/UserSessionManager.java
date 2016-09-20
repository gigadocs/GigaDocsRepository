package com.datappsinfotech.gigadocs.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.datappsinfotech.gigadocs.LoginActivity;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsSharedPreferenceManager;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigadocsConstants;

import java.util.HashMap;

public class UserSessionManager {

	public static String storedEmail;
	public static String storedpassword;
	Context _context;

	private static final String IS_USER_LOGIN = "IsUserLoggedIn";

	public UserSessionManager(Context context){
		this._context = context;
	}
	public void createUserLoginSession(){
		try {
			GigaDocsSharedPreferenceManager.setKey(_context,IS_USER_LOGIN,true);
			GigaDocsSharedPreferenceManager.setKey(_context, GigadocsConstants.EMAIL,LoginActivity.loginActivityDTO.getEmail_LoginActivity());
			GigaDocsSharedPreferenceManager.setKey(_context, GigadocsConstants.PASSWORD,LoginActivity.loginActivityDTO.getPassword_LoginActivity());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public boolean checkLogin(){
		try {
			if(!this.isUserLoggedIn()){
                Intent i = new Intent(_context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(i);
                return true;
            }
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}
	public void logoutUser(){
		try {
			GigaDocsSharedPreferenceManager.clearKeys(_context);
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public boolean isUserLoggedIn(){
		return GigaDocsSharedPreferenceManager.getKey(_context,IS_USER_LOGIN,false);
	}
}
