
package easy2shop.activity;

import easy2shop.function.SettingFunctions;
import easy2shop.ycn.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Setting extends Activity {
	Context context = this;
	SettingFunctions settings;;
	RadioGroup radioGUrl;
	RadioButton rbtnCustom,rbtnEmulator,rbtnGenymotion;
	Button btnRun;
	EditText edtIp,edtCmd;
	String KEY_SELECTEDIP = "btn_ip";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		SetViewAndListener();
		
		radioGUrl.check(settings.getSelectedIp());
		//edtIp.setEnabled(false);
		Log.d("After Startup",settings.getIp());
	}

	public void SetViewAndListener() {
		settings = new SettingFunctions(context);
		btnRun = (Button)findViewById(R.id.btnCmd);
		radioGUrl = (RadioGroup)findViewById(R.id.rGroupUrl);
		rbtnCustom = (RadioButton)findViewById(R.id.rbtnCustom);
		rbtnEmulator = (RadioButton)findViewById(R.id.rbtnEmu);
		rbtnGenymotion = (RadioButton)findViewById(R.id.rbtnGeny);
		edtCmd = (EditText)findViewById(R.id.edtCmd);
		// default value
		edtCmd.setEnabled(false);
		btnRun.setText("Type Sql Command");
		//----------------------------
		btnRun.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(!edtCmd.isEnabled()){
					edtCmd.setEnabled(true);
					edtCmd.requestFocus();
					btnRun.setText("Query Command");
				}else{
					if(edtCmd.length()>0){
						btnRun.setEnabled(false);
						btnRun.setText("Executing command...");
						// query command through asycntask
						// after that reenable btnRun
					}else{
						btnRun.setText("Type Sql Command");
					}
					edtCmd.setEnabled(false);
				}
			}
			
		});
		edtIp = (EditText)findViewById(R.id.edtIp);
		edtIp.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				settings.saveIp(edtIp.getText().toString());
				settings.saveCustomIp(edtIp.getText().toString());
				Log.d("Changing Ip",settings.getIp());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
		});
		radioGUrl.setOnCheckedChangeListener(radioGlistener);
	}
	
	private RadioGroup.OnCheckedChangeListener radioGlistener = new RadioGroup.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId){
			case R.id.rbtnCustom:
				settings.saveSelectedIp(checkedId);
				settings.saveIp(settings.getCustomIp());
				edtIp.setEnabled(true);
				edtIp.setFocusableInTouchMode(true);
				edtIp.setText(settings.getCustomIp());
				break;
			case R.id.rbtnEmu:
				settings.saveSelectedIp(checkedId);
				settings.saveIp("10.0.2.2");
				edtIp.setEnabled(false);
				edtIp.setFocusableInTouchMode(false);
				edtIp.setText(settings.getIp());
				break;
			case R.id.rbtnGeny:
				settings.saveSelectedIp(checkedId);
				settings.saveIp("10.0.3.2");
				edtIp.setEnabled(false);
				edtIp.setFocusableInTouchMode(false);
				edtIp.setText(settings.getIp());
				break;
			}
			Log.d("Change Ip Setting",settings.getIp());
		}
	};
	
	public void Toast(String string){
		Toast.makeText(context,string, Toast.LENGTH_SHORT).show();
	}


}
