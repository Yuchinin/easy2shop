package easy2shop.activity;

import easy2shop.function.BaseFunctions;
import easy2shop.ycn.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class about extends Activity {
	TextView txtTodo,txtWip,txtDone,txtVer;
	ScrollView sVParent,sV1,sV2,sV3;
	BaseFunctions bf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		bf = new BaseFunctions(this);
		
		SetViewAndListener();
		
		//sVParent.setOnTouchListener(ScrollViewOnTouchListener);
		
		txtVer.setText(getString(R.string.verName)+bf.GetVersionName()+" \n"+ getString(R.string.verCode) +bf.GetVersionCode());
        
       /// private ScrollView.OnTouchListener ScrollViewOnTouchListener = new ScrollView.OnTouchListener
	}
	
	public void SetViewAndListener(){
		txtVer = (TextView)findViewById(R.id.txtVer);
		sVParent = (ScrollView)findViewById(R.id.sVParent);
	}

}
