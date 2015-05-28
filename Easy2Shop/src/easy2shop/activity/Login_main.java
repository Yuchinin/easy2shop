package easy2shop.activity;

import org.apache.http.NoHttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;
 



import easy2shop.function.Base;
import easy2shop.function.UserFunctions;
import easy2shop.ycn.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class Login_main extends Base {
	final Context context = this;
	String email,password;
    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    UserFunctions uf;
    String KEY_SUCCESS = "success";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        
        SetViewAndListener();
        uf = new UserFunctions(context);
        Log.d("Login", "onCreate");
    }
    
    private Button.OnClickListener btnlistener = new Button.OnClickListener(){
		//@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//switch (v.getId()){
			if(v.getId()==R.id.btnLogin){
				//--------------------------------------------------
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				
				View promptView = layoutInflater.inflate(R.layout.prompts2, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

				// set prompts.xml to be the layout file of the alertdialog builder
				alertDialogBuilder.setView(promptView);
				
				final EditText name = (EditText) promptView.findViewById(R.id.email_input);

				final EditText pass = (EditText) promptView.findViewById(R.id.pass_input);

				// setup a dialog window
				alertDialogBuilder
						//.setCancelable(false)
						.setTitle(getString(R.string.userlogin))
						.setIcon(R.drawable.login_icon)
						.setPositiveButton(getString(R.string.login), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// get user input and set it to result
										email = name.getText().toString();
										password = pass.getText().toString();
										if((email.length()==0)){
											//dialog.dismiss();
											Dialog(getString(R.string.error),getString(R.string.empty_email),false);
										}else if((password.length()==0)) {
											Dialog(getString(R.string.error),getString(R.string.empty_pass),false);
											//dialog.dismiss();
										}else{
											new Login().execute(email,password);
										}
									}
								});

				// create an alert dialog
				AlertDialog alertD = alertDialogBuilder.create();

				alertD.show();
				//--------------------------------------------------
                
			}else if(v.getId()==R.id.btnLinkToRegisterScreen){
				Intent i = new Intent();
				i.setClass(context, Register_main.class);
				startActivity(i);
                finish();
			}
		}
	};
    
    public void SetViewAndListener(){
    	// Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        
        btnLogin.setOnClickListener(btnlistener);
        btnLinkToRegister.setOnClickListener(btnlistener);
    }
    
    private class Login extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(Login_main.this);

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
        	this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            //this.dialog.setTitle(getString(R.string.app_name));
            this.dialog.setTitle(getString(R.string.login));
            this.dialog.setIcon(R.drawable.login_icon);
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }
        
        @Override
        protected String doInBackground(String... params) {
        	JSONObject jObj;
        	Log.d("doInBackground", "Start");
        	String email = params[0];
        	Log.d("email", email);
        	String password = params[1];
        	Log.d("password", password);
        	
        	try{
        		// return basic data and token
        		jObj = uf.loginUser(email, password);
        		if(Integer.parseInt(jObj.getString("success")) == 1){
		        	// user successfully logged in
		            // Store user details in SQLite Database
		            JSONObject jObj_user = jObj.getJSONObject("user");

		            // Clear all previous data in database
		            //uf.logoutUser();
		            // Store user data
		            st.saveToken(jObj_user.getString("token"));
		        }else{
		            if(Integer.parseInt(jObj.getString("error"))==1){
		            	return getString(R.string.error_email);
		            }else if(Integer.parseInt(jObj.getString("error"))==2){
		            	return getString(R.string.error_emailorpass);
		            }
		        }
        	}catch(Exception e){
        		return getString(R.string.error_server);
        	}
            return KEY_SUCCESS;
        }

        @Override
        protected void onPostExecute(String msg) {
            
            if(msg==KEY_SUCCESS){
            	finish();
            }else{
            	//loginErrorMsg.setText(msg);
            	Dialog(getString(R.string.error)+" "+getString(R.string.login),msg,false);
            }
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
         
        }

    }

}