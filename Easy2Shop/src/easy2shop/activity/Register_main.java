package easy2shop.activity;

import org.json.JSONObject;
import easy2shop.function.Base;
import easy2shop.function.UserFunctions;
import easy2shop.ycn.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
 
public class Register_main extends Base {
	final Context context = this;
	String strname;
    Button btnRegister;
    Button btnLinkToLogin;
    EditText inputFullName,inputEmail,inputPassword,inputPhone,edtAddress,edtBirthday;
    RadioGroup rg;
	RadioButton rbtnMale,rbtnFemale;
    TextView registerErrorMsg;
    UserFunctions userFunctions;
    
    String name,email,password,phone,gender,address,birthday;
 
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_C_AT = "created_at";
    private static String KEY_U_AT = "updated_at";
    private static String KEY_LVL = "level";
    private static String KEY_TS = "timestamp";
    private static String KEY_PHONE = "phone";
    private static String KEY_GENDER = "gender";
    private static String KEY_ADDRESS = "address";
    private static String KEY_BIRTHDAY = "birthday";
    private String MSG_EMPTY_NAME;// = getString(R.string.empty_name);
    private String MSG_EMPTY_EMAIL;// = getString(R.string.empty_email);
    private String MSG_EMPTY_PASS;// = getString(R.string.empty_pass);
    private String MSG_EMPTY_PHONE;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
 
        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.edtName);
        inputEmail = (EditText) findViewById(R.id.edtEmail);
        inputPassword = (EditText) findViewById(R.id.edtPassword);
        inputPhone = (EditText) findViewById(R.id.edtPhone);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtBirthday = (EditText) findViewById(R.id.edtDate);
        rg = (RadioGroup) findViewById(R.id.rGroupReg);
        
        rbtnMale = (RadioButton)findViewById(R.id.rbtnMale);
        rbtnFemale = (RadioButton)findViewById(R.id.rbtnFemale);
        
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
        
        MSG_EMPTY_NAME = getString(R.string.empty_name);
        MSG_EMPTY_EMAIL = getString(R.string.empty_email);
        MSG_EMPTY_PASS = getString(R.string.empty_pass);
        MSG_EMPTY_PHONE = getString(R.string.empty_phone);
        
        userFunctions = new UserFunctions(context);
 
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	String Errmsg = null;
            	name = inputFullName.getText().toString();
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                phone = inputPhone.getText().toString();
                gender = "M";
                address = edtAddress.getText().toString();
                birthday = edtBirthday.getText().toString();
                if(rg.getCheckedRadioButtonId()==R.id.rbtnFemale) gender = "F";
                
                if(name.length()==0) Errmsg = MSG_EMPTY_NAME;
            	if((email.length()==0)) Errmsg = MSG_EMPTY_EMAIL;
                if(password.length()==0) Errmsg = MSG_EMPTY_PASS;
                if(phone.length()==0) Errmsg = MSG_EMPTY_PHONE;
                
                if(Errmsg==null) {
                	new Register().execute(name,email,password,phone,gender,address,birthday);
                } else {
                	Dialog(getString(R.string.error)+" "+getString(R.string.register),Errmsg,false);
                }
            }
        });
 
        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
            	String re_from = null;
            	Intent i = new Intent(getApplicationContext(),
                        Login_main.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
    }
    
    private class Register extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(context);

        /** progress dialog to show user that the backup is processing. */
        /** application context. */

        protected void onPreExecute() {
        	this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            //this.dialog.setTitle(getString(R.string.app_name));
            this.dialog.setTitle(getString(R.string.register));
            this.dialog.setIcon(R.drawable.login_icon);
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }
        
        @Override
        protected String doInBackground(String... params) {
        	JSONObject jObj;
        	String name = params[0];
        	String email = params[1];
        	String password = params[2];
        	String phone = params[3];
        	String gender = params[4];
        	String address = params[5];
        	String birthday = params[6];
            
        	try{
        		jObj = userFunctions.registerUser(name,email, password,phone,gender,address,birthday);
        		if(Integer.parseInt(jObj.getString("success"))==1){
		            // Clear all previous data in database
		            // logout user
            		// get user data
            		
		        }else{
		        	if(Integer.parseInt(jObj.getString("error"))==1){
		        		return getString(R.string.error_email_registered);
		        	}else if(Integer.parseInt(jObj.getString("error"))==2){
		        		return getString(R.string.action_failed);
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
            	// show welcome dialog and goto dashboard
            	finish();
            }else{
            	//registerErrorMsg.setText(msg);
            	Dialog(getString(R.string.error)+" "+getString(R.string.register),msg,false);
            }
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
         
        }

    }
    
}