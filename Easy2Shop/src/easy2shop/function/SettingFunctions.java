package easy2shop.function;

import easy2shop.ycn.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SettingFunctions {
	protected Context ct;
	private SharedPreferences settings;
	private final String SETTING_NAME = "settings";
	
	private final String IP_SELECTED_KEY = "Ip Selected";
	private final String IP_SAV_KEY = "Ip Address";
	private final String VERSION_NAME = "Version Name";
	private final String VERSION_CODE = "Version Code";
	private final String CURRENCY_CODE = "RM";
	private final String PORT_SAV_KEY = "Port";
	private final String TOKEN_KEY = "Token";
	private final String CON_METHOD = "http://";
	private final String ROOT_FOLDER = "/easy2shop/android/";
	private final String APK_FOLDER = "/easy2shop/android/apk/";
	private final String APK_NAME = "easy2shop.apk";
	
	public SettingFunctions (Context context){
		ct = context;
		settings = ct.getSharedPreferences(SETTING_NAME,0);
	}
	//--------------------------Static Setting------------------------------
	public String getConnectionMethod(){
		return CON_METHOD;
	}
	
	public String getRootFolder(){
		return ROOT_FOLDER;
    }
	
	public String getAPKUrl(){
		//return "adf.ly/aCVuX";
		return getIp()+APK_FOLDER+APK_NAME;
	}
	
	public void saveToken(String value){
		settings.edit().putString(TOKEN_KEY,value).commit();
    }
	
	public String getToken(){
		return settings.getString(TOKEN_KEY,"");
    }
	
	public void saveIp(String value){
		settings.edit().putString(IP_SAV_KEY,value).commit();
    }
	
	public String getIp(){
		//return settings.getString(IP_SAV_KEY,"120.125.84.100");
		return settings.getString(IP_SAV_KEY,"10.0.3.2");
    }
	
	public void saveCustomIp(String value){
		settings.edit().putString(IP_SAV_KEY,value).commit();
    }
	
	public String getCustomIp(){
		//return settings.getString(IP_SAV_KEY,"120.125.84.100");
		return settings.getString(IP_SAV_KEY,"10.0.3.2");
    }
	
	public void savePort(String value){
		settings.edit().putString(PORT_SAV_KEY,value).commit();
    }
	
	public String getPort(){
		return settings.getString(PORT_SAV_KEY,"8081");
    }
	
	public void saveSelectedIp(int value){
		settings.edit().putInt(IP_SELECTED_KEY,value).commit();
    }
	
	public int getSelectedIp(){
		return settings.getInt(IP_SELECTED_KEY, R.id.rbtnGeny);
	}
	
	public String getLatestVersionName(){
		return settings.getString(VERSION_NAME,null);
	}
	
	public void saveLatestVersionName(String value){
		settings.edit().putString(VERSION_NAME,value).commit();
	}
	
	public int getLatestVersionCode(){
		return settings.getInt(VERSION_CODE,0);
	}
	
	public void saveLatestVersionCode(int value){
		settings.edit().putInt(VERSION_CODE,value).commit();
		Log.d("saveLatestVersionCode", Integer.toString(getLatestVersionCode()));
	}
	
	public String getCurrencyCode(){
		return settings.getString(CURRENCY_CODE,"MYR");
	}
	
	public void saveCurrencyCode(String value){
		settings.edit().putString(CURRENCY_CODE,value).commit();
	}
	
	public String getCurrencyValue(String currency_name){
		return settings.getString(currency_name+getCurrencyCode(),"");
	}
	
	public void saveCurrencyValue(String currency_name,String value){
		settings.edit().putString(currency_name+getCurrencyCode(),value).commit();
	}
	
	//public int getSelectedIp(){
		//return settings.getInt(IP_SELECTED_KEY,R.id.rbtnEmulator);
    //}
//------------------------------Static Setting End-----------------------------
	public void putString(String key, String _default){
		settings.edit().putString(key,_default).commit();
    }
	
	public String getString(String key, String _default){
		return settings.getString(key,_default);
    }
	
	public void putInt(String key, int _default){
		settings.edit().putInt(key,_default).commit();
    }
	
	public int getInt(String key, int _default){
		return settings.getInt(key, _default);
    }

}
