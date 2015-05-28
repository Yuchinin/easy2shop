package easy2shop.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;

public class JSONParser {
	 
    static InputStream is = null;
    static JSONObject jObj = null;
 
    // constructor
    public JSONParser() {
 
    }
 
    public JSONObject makeHttpRequest(
    		String url, String method, List<NameValuePair> params) {
    	Log.d(new Exception().getStackTrace()[0].getMethodName(),url);
 
        // Making HTTP request
        try {
        	HttpParams httpParameters = new BasicHttpParams();
        	// Set the timeout in milliseconds until a connection is established.
        	// The default value is zero, that means the timeout is not used. 
        	int timeoutConnection = 10000;
        	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        	// Set the default socket timeout (SO_TIMEOUT) 
        	// in milliseconds which is the timeout for waiting for data.
        	int timeoutSocket = 10000;
        	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        	
        	// defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            //---<
            httpClient.setParams(httpParameters);
            //--->
        	
            HttpResponse httpResponse = null;
            
            long startTime = System.currentTimeMillis();
        	if(method == "POST"){
        		//UrlEncodedFormEntity form;
        		//form = new UrlEncodedFormEntity(params);
                //form.setContentEncoding(HTTP.UTF_8);
                Log.e("http", method);
                //Log.d("form",form.toString());
        		HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
        		//httpPost.setEntity(form);
                httpResponse = httpClient.execute(httpPost);
        	}else if(method == "GET"){
                Log.e("httpGet", method);
        		String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
        	}
            
            //---<
            Log.e("httpResponse", httpResponse.toString());
            //--->
            
            HttpEntity httpEntity = httpResponse.getEntity();
            
            long endTime = System.currentTimeMillis();
/*            long contentLength = httpEntity.getContentLength();
            Log.d("contentLength", Long.toString(contentLength));
            float bandwidth = contentLength / ((endTime-startTime) / 1000); 
            Log.d("Bandwidth", Float.toString(bandwidth));*/
            Log.d("Time Use:", Float.toString(endTime-startTime)+"ms");
            
            
            is = httpEntity.getContent();
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }
 
        try {
        	if(is != null){
        		BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "n");
                }
                is.close();
                jObj = new JSONObject(sb.toString());
        	}
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return jObj;
 
    }
}
