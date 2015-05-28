package easy2shop.activity;

import easy2shop.function.Base;
import easy2shop.function.UserFunctions;
import easy2shop.ycn.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Main extends Base {
	Context ct = this;
	UserFunctions uf;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new LoadViewTask().execute();
		
		
		Log.d("uniqueId", getUniqueId());
		Log.d("getCountryCode()",getCountryCode());
		
		//Log.d("getCurrencyCode()",getCurrencyCode());
		//----------debug use
		//intent.setClass(context,ItemGridION_0_Category.class);
		//String sql = "select * from(select * from product order by rand()) product group by category";
		//new DataLoaderTask().execute(sql);
		//----------debug use
		//Intent intent = new Intent();
		//intent.setClass(getApplicationContext(), ItemList.class);
		//startActivity(intent);
	}
	
	private void InitializeView(){
		setContentView(R.layout.main);
		uf = new UserFunctions(ct);
		Button btn1 = (Button)findViewById(R.id.btn1);
		Button btn2 = (Button)findViewById(R.id.btn2);
		btn1.setOnClickListener(btnlistener);
		btn2.setOnClickListener(btnlistener);
		btn1.setText("List Item");
		btn2.setText("Grid Item");
	}
	
	private Button.OnClickListener btnlistener = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			if(v.getId()==R.id.btn1){
				intent.setClass(context,ItemListION.class);
			}else if(v.getId()==R.id.btn2){
				intent.setClass(context,ItemGridION_0_Category.class);
			}
			startActivity(intent);
		}
	};
	
	//To use the AsyncTask, it must be subclassed  
    private class LoadViewTask extends AsyncTask<Void, Integer, String> {
        //Before running code in separate thread  
        @Override  
        protected void onPreExecute()  
        {  
            //Create a new progress dialog  
            progressDialog = new ProgressDialog(context);  
            //Set the progress dialog to display a horizontal progress bar  
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
            //Set the dialog title to 'Loading...'  
            progressDialog.setTitle(getString(R.string.app_name));  
            //Set the dialog message to 'Loading application View, please wait...'  
            progressDialog.setMessage(getString(R.string.loadingmain));  
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
  
        //The code to be executed in a background thread.  
        @Override  
        protected String doInBackground(Void... params)  
        {  
            /* This is just a code that delays the thread execution 4 times, 
             * during 850 milliseconds and updates the current progress. This 
             * is where the code that is going to be executed on a background 
             * thread must be placed. 
             */  
            try{
                //Get the current thread's token  
                synchronized (this){
                	//Initialize an integer (that will act as a counter) to zero  
                    int counter = 0;
                    
                    if(!isNetworkConnected()) return "No Connected to Network";
                    
                    if(!isInternetAvailable()) return "No Internet Access";
                    
                	
                    //While the counter is smaller than four  
                    while(counter <= 100){
                    	if(counter==0){
                    		String code = "";
                    		code = getServerData();
                            if(!code.equals("1")) return code;
                    	}
                    	
                    	if(counter==10){
                    		if(!getAllCurrencyValue()) return "Error fetching currency";
                    	}
                        //Wait 850 milliseconds  
                        this.wait(10);
                        //Increment the counter  
                        counter++;
                        //Set the current progress.  
                        //This value is going to be passed to the onProgressUpdate() method.  
                        publishProgress(counter);  
                    }
                }
            }catch (Exception e){
            	return getString(R.string.error_server);
            }  
            return "1";
        }  
  
        //Update the progress  
        @Override  
        protected void onProgressUpdate(Integer... values)  
        {  
            //set the current progress of the progress dialog  
            progressDialog.setProgress(values[0]);  
        }  
  
        //after executing the code in the thread  
        @Override  
        protected void onPostExecute(String result)  
        {  
        	try{
            //close the progress dialog  
            progressDialog.dismiss();  
            
            //initialize the View  
            Log.d("dismiss", "pass");
            if(result.equals("1")){
            	int lcode = (int)st.getLatestVersionCode();
            	int code = (int)GetVersionCode();
            	Log.d("lcode", Integer.toString(lcode));
            	if(lcode>code){
                	DialogUpdateYesNo();
                }else{
            	InitializeView();
            	//setContentView(R.layout.main);
                Log.d("setContentView", "pass");
                //SetViewAndListener();
                }
            }
            else
            	Dialog(getString(R.string.error),result,true);
            
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    }
	
	@Override
	public void onResume()
	    {  // After a pause OR at startup
	    super.onResume();
	    //Refresh your stuff here
	   
	}
}
