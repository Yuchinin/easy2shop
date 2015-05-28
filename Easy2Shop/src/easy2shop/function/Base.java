package easy2shop.function;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.builder.AnimateGifMode;

import easy2shop.activity.Main;
import easy2shop.activity.Setting;
import easy2shop.ycn.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

public abstract class Base extends Activity {
	public Context context = this;

    private static String TABLE_tag = "DATA";
    //private static String GET_tag = "GET";
    private static String POST_tag = "POST";
    
    private static int MENU_ABOUT = 0;

	public SettingFunctions st;

	@Override
    public boolean onPrepareOptionsMenu(Menu menu){
		if(menu.hasVisibleItems())
			menu.clear();
		menu.add(0,0,MENU_ABOUT,"Settings");
    	return super.onCreateOptionsMenu(menu); 	
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		
		if(id==MENU_ABOUT){
			Intent intent = new Intent();
			intent.setClass(context, Setting.class);
			startActivity(intent);
		}
		
    	return super.onOptionsItemSelected(item);   			
	}
	
public String getUniqueId(){
	return Secure.getString(this.getContentResolver(),
            Secure.ANDROID_ID);
}
	
	public String GetVersionName(){
    	try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public int GetVersionCode(){
    	try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
    }
    
    /**
	 * return system language in string
	 */
	public String getLanguage(){
    	return Locale.getDefault().toString();
    }
	
	/**
	 * 
	 * @return us in emulator
	 * should be my
	 */
	public String getCountryCode(){
		TelephonyManager manager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
	    String countrycode=manager.getNetworkCountryIso();
	    System.out.println("---->"+countrycode+"<----");
	    return countrycode;
	}
	
	public boolean getAllCurrencyValue(){
		try{
			JSONArray J = query_sql("SELECT currency_name FROM easy2shop.product where currency_name != '' group by currency_name;");
			int arrsize = J.length();
	    	for(int x=0;x<arrsize;x++){
	    		String cname = J.getJSONObject(x).getString("currency_name");
	    		String ccode = st.getCurrencyCode();
	    		String convertedC = convertCurrency(cname,ccode);
	    		st.saveCurrencyValue(cname,convertedC);
	    		Log.d("convertCurrency(cname, ccode)", convertedC);
	    	}
	    	Log.d("getAllCurrencyValue()", "Success");
	    	return true;
		}catch(Exception e){
			e.printStackTrace();
			Log.d("getAllCurrencyValue()", "Failed");
			return false;
		}
    }
	
	/**
	 * 
	 * @return MYR
	 * will be USB in Emulator
	 *//*
	public String getCurrencyCode(){
		return Currency.getInstance(new Locale("",getCountryCode())).getCurrencyCode();
	}*/
	
	public String convertCurrency(String from,String to){
		String tmp = null;
    	String URL = "http://devel.farebookings.com/api/curconversor/"+from+"/"+to+"/"+"1/json";
    	JSONObject json = JSonParser2(URL);
    	Log.d("URL",URL);
    	
    	try {
    		if(json!=null){
    			Log.d("JSON",json.toString());
        		tmp = json.getString(to);
    		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	return tmp;
    }
	
	public Boolean isStringNullEmpty(String string){
		if(string!=null&&!string.isEmpty()){
			// not null
			return false;
		}else
			return true;
	}
	
	public String processJson(JSONObject json,String key){
			try {
				if(json.isNull(key)) return "";
				else return json.getString(key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
	}
	
    /**
     * return value:
     * true : Internet Available
     * false: No Internet Connection
     */
	public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
            	Log.d("ipAddr", ipAddr.toString());
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }
	
	/**
	 * return value:
	 * true : Network Connected
	 * false: No Network Connection
	 */
	public boolean isNetworkConnected() {
        ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) {
            return true;
        }
        return false;
    }
	
	public String getServerData(){
		Log.d("run", "getServerData");
			// server data found
			try {
				JSONArray J = null;
				SettingFunctions st = new SettingFunctions(context);
				J = query_sql("select * from easy2shop.setting");
				st.saveLatestVersionName(J.getJSONObject(0).getString("version_name"));
				st.saveLatestVersionCode(J.getJSONObject(0).getInt("version_code"));
				Log.d("getServerData", "saving latest version");
				Log.d("saveLatestVersionName", st.getLatestVersionName());
				Log.d("saveLatestVersionCode", Integer.toString(st.getLatestVersionCode()));
				return "1";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Fail to get server data.";
			}
	}
	
	/**
	 * IONImage(imgurl,imageview)
	 * Custom Image Displayer
	 **/
	public void IONImage(String url,ImageView image,Boolean nocache){
		if(nocache){
			Ion.with(context)
			.load(url).noCache()
			.withBitmap()
			.placeholder(R.drawable.loading)
			.error(R.drawable.ic_error)
			.intoImageView(image);
		}else{
			Ion.with(context)
			.load(url)
			.withBitmap()
			.placeholder(R.drawable.loading)
			.error(R.drawable.ic_error)
			.intoImageView(image);
		}
	}
	
	/**
	 * 
	 * @param notitle
	 * @param fullscreen
	 */
	public void setFullScreen(Boolean notitle,Boolean fullscreen){
		if(notitle) requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(fullscreen) getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	/**
	 * return value:
	 * JSONArray : if successful
	 * null : if fail to get query
	 */
	public JSONArray query_sql(String query){
		JSONParser jsonParser = new JSONParser();
		st = new SettingFunctions(context);
    	String URL = "http://"+st.getIp()+st.getRootFolder()+"get_query.php";
    	Log.d("URL",URL);
    	Log.d("query_sql",query);
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", TABLE_tag));
    	params.add(new BasicNameValuePair("query_string",query));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
    	try {
    		Log.d("JSON",json.toString());
			return json.getJSONArray(TABLE_tag);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	return null;
    }
	
	/**
	 * return 1 on success or error message
	 */
	public JSONObject query_sql_action(String query){
		JSONParser jsonParser = new JSONParser();
		st = new SettingFunctions(context);
    	String URL = "http://"+st.getIp()+st.getRootFolder()+"action_query.php";
    	Log.d("URL",URL);
    	Log.d("query_sql",query);
    	// Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", TABLE_tag));
    	params.add(new BasicNameValuePair("query_string",query));
    	// getting JSON string from URL
    	JSONObject json = jsonParser.makeHttpRequest(URL, POST_tag, params);
    	try {
    		Log.d("JSON",json.toString());
			return json;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	return null;
    }
	
	/**
	 * 
	 * @param uri
	 * @return JSONObject or null
	 */
	private JSONObject JSonParser2(String uri){
		String responseBody = null;
		HttpResponse response = null;
		JSONObject obj = null;
		DefaultHttpClient http = new DefaultHttpClient();
		HttpGet httpMethod = new HttpGet();
		
		try {
			httpMethod.setURI(new URI(uri));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// time cal start
		long startTime = System.currentTimeMillis();
		
		try {
			response = http.execute(httpMethod);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int responseCode = response.getStatusLine().getStatusCode();
		switch(responseCode)
		{
		    case 200:
		        HttpEntity entity = response.getEntity();
		        if(entity != null){
		        	try {
		        		responseBody = EntityUtils.toString(entity);
					}catch (Exception e) {
						e.printStackTrace();
					}
		        }
		        long endTime = System.currentTimeMillis();
		        Log.d("Time Use:", Float.toString(endTime-startTime)+"ms");
				break;
		}
		
		try {
			obj = new JSONObject(responseBody);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * Dialog(title,msg,isfinish)
	 */
	public void Dialog(String title,String msg,final Boolean finish){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		if(title.equals(getString(R.string.error)))
			alertDialogBuilder.setIcon(R.drawable.icon_error);
		alertDialogBuilder.setMessage(msg).setCancelable(false)
			.setPositiveButton(getString(R.string.dismiss),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
					if(finish==true){
						finish();
					}
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public void DialogYesNo(String title,String msg){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(msg).setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
					finish();
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.dismiss();
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public void browseWeb(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://"+url));
		startActivity(i);
	}
    
    public void DialogUpdateYesNo(){
    	st = new SettingFunctions(context);
    	String local_ver_name = GetVersionName();
    	int local_ver_code = GetVersionCode();
    	String server_ver_name = st.getLatestVersionName();
    	int server_ver_code = st.getLatestVersionCode();
    	String tmp = "\n"+context.getString(R.string.verName)+local_ver_name+"\n"+
    			context.getString(R.string.verCode)+local_ver_code+"\n"+
    			context.getString(R.string.verNameLatest)+server_ver_name+"\n"+
    			context.getString(R.string.verCodeLatest)+server_ver_code;
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(getString(R.string.update));
		
		alertDialogBuilder.setMessage(getString(R.string.update_msg)+tmp).setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					UpdateApp atualizaApp = new UpdateApp();
		            //atualizaApp.setContext(getApplicationContext());
		            atualizaApp.execute("http://"+st.getAPKUrl());
					//browseWeb(st.getAPKUrl());
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public class UpdateApp extends AsyncTask<String,Void,Void>{
    	ProgressDialog progressDialog;
    	
    	@Override  
        protected void onPreExecute()  
        {  
            //Create a new progress dialog  
            progressDialog = new ProgressDialog(context);  
            //Set the progress dialog to display a horizontal progress bar  
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
            //Set the dialog title to 'Loading...'  
            progressDialog.setTitle(getString(R.string.update));  
            //Set the dialog message to 'Loading application View, please wait...'  
            progressDialog.setMessage("Updating file...");  
            //This dialog can't be canceled by pressing the back key  
            progressDialog.setCancelable(false);  
            //This dialog isn't indeterminate  
            progressDialog.setIndeterminate(false);  
            //The maximum number of items is 100  
            progressDialog.setMax(100);  
            //Set the current progress to zero  
            progressDialog.setProgress(0);  
            //Display the progress dialog
            progressDialog.setIcon(R.drawable.ic_launcher);
            progressDialog.show();  
        }  

    	@Override
    	protected Void doInBackground(String... arg0) {
    	      try {
    	    	  Ion.with(context)
                  .load(arg0[0])
                      // attach the percentage report to a progress bar.
                      // can also attach to a ProgressDialog with progressDialog.
                      .progressDialog(progressDialog)
                      // callbacks on progress can happen on the UI thread
                      // via progressHandler. This is useful if you need to update a TextView.
                      // Updates to TextViews MUST happen on the UI thread.
                      .progressHandler(new ProgressCallback() {
                          @Override
                          public void onProgress(long downloaded, long total) {
                        	  progressDialog.setProgress((int) (total*100/downloaded));
                              //downloadCount.setText("" + downloaded + " / " + total);
                          }
                      })
                      // write to a file
                      .write(new File("/mnt/sdcard/Download/update.apk"))
                      // run a callback on completion
                      .setCallback(new FutureCallback<File>() {
                          @Override
                          public void onCompleted(Exception e, File result) {
                        	  progressDialog.dismiss();
                              if (e != null) {
                                  Dialog(getString(R.string.error),"Error Updating File",true);
                              }
                              Intent intent = new Intent(Intent.ACTION_VIEW);
                              intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/update.apk")), "application/vnd.android.package-archive");
                              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                              context.startActivity(intent);
                              finish();
                          }
                      });
    	        } catch (Exception e) {
    	            Log.e("UpdateAPP", "Update error! " + e.getMessage());
    	        }
    	    return null;
    	}
    	
    	//Update the progress  
        protected void onProgressUpdate(Integer... values)  
        {  
            //set the current progress of the progress dialog  
            progressDialog.setProgress(values[0]);  
        }  
  
        
    }
    
    public void DialogLogoutYesNo(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(getString(R.string.logout));
		alertDialogBuilder.setMessage(getString(R.string.r_u_sure)).setCancelable(false).
				setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					Log.d("current class", context.getClass().getSimpleName());
					//---------------------
					Intent intent = new Intent(context, Main.class);

	                // Close all views before launching Dashboard
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                startActivity(intent);
	                //db.logoutUser();
	                finish();
				}
			  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
	
}
