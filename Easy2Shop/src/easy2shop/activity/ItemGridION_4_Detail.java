package easy2shop.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import easy2shop.function.Base;
import easy2shop.function.ItemFunctions;
import easy2shop.ycn.R;
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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ItemGridION_4_Detail extends Base {
	JSONArray IMAGEURL;
	Context context = this;
	//Intent intent;
	ItemFunctions itemf;
	Gallery ecoGallery;
	ImageAdapter adapter;
	String title,currency_name,price,description,CODE;
	TextView titlev,descv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InitializeUI();
	}
	
	@SuppressWarnings("deprecation")
	private void InitializeUI() {
		setFullScreen(true,true);
		setContentView(R.layout.product_details);
		titlev = (TextView)findViewById(R.id.txtName);
		descv = (TextView)findViewById(R.id.txtDesc);
		Bundle bundle = getIntent().getExtras();
		CODE = bundle.getString("code");
		//intent = new Intent();
		itemf = new ItemFunctions(context);
		ecoGallery = (Gallery) findViewById(R.id.gallery);
		adapter = new ImageAdapter();
		new DataLoaderTask().execute(CODE);
		//ShowImage();
	}
	
	private void ShowImage(){
		titlev.setText(title);
		descv.setText(itemf.getPrice(currency_name,price));
		ecoGallery.setAdapter(adapter);
		ecoGallery.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ViewPagerActivity.class);
				intent.putExtra("jArr", IMAGEURL.toString());
				intent.putExtra("position", position);
				startActivity(intent);
			}
			
		});
		ecoGallery.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				ActionDialog(position);
				return false;
			}
			
		});
	}
	
	public void ActionDialog(final int pos){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder
				.setPositiveButton(getString(R.string.modify),new DialogInterface.OnClickListener() {
					//long click cart list item
				public void onClick(DialogInterface dialog,int id) {
					//if modify clicked
					//--------------------------------------------------
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					View promptView = layoutInflater.inflate(R.menu.stock_item_menu, null);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setView(promptView);
					alertDialogBuilder
							.setPositiveButton(getString(R.string.modify), new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											
										}
									});
					AlertDialog alertD = alertDialogBuilder.create();
					alertD.show();
					//--------------------------------------------------
				}
			  }).setNegativeButton(getString(R.string.delete),new DialogInterface.OnClickListener() {
				  // if delete clicked
				public void onClick(DialogInterface dialog,int id) {
					//-------------------------------------
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setTitle(getString(R.string.delete));
					alertDialogBuilder.setMessage(getString(R.string.r_u_sure))
					.setCancelable(false)
						.setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								try {
									new DataActionTask().execute("delete from product_data where imgurl = '"+
											IMAGEURL.getJSONObject(pos).getString("imgurl")+"'");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						  }).setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
								}
							  });
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					//-------------------------------------
					
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
	
	private class ImageAdapter extends BaseAdapter {

        public int getCount() {
        	if(IMAGEURL!=null) return IMAGEURL.length();
        	else return 0;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	ImageView imageView = (ImageView)convertView;
        	if (imageView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_gallery_image, parent, false);
			}
        	try {
				IONImage(IMAGEURL.getJSONObject(position).getString("imgurl"),imageView,false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return imageView;
        }
    }
	
	/** AsyncTask to parse json data and load ListView */
    private class DataActionTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		this.dialog.setCancelable(false);
    		this.dialog.setIcon(R.drawable.loading);
    		this.dialog.setIndeterminate(false);
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }

        @Override  
        protected void onProgressUpdate(String... values)
        {  
            dialog.setMessage(values[0]);
        }  

		@Override
		protected String doInBackground(String... str) {
			try{
				JSONObject json = query_sql_action(str[0]);
				if(json.getString("success").equals("1")){
					return "1";
				}else{
					return json.getString("message");
				}
			}catch(Exception e){
				e.printStackTrace();
				return getString(R.string.error_server);
			}
		}
		
        /** Invoked by the Android on "doInBackground" is executed */
		@Override
		protected void onPostExecute(String str) {
			
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			
			if(str.equals("1")){
				new DataLoaderTask().execute(CODE);
			}else{
				Dialog(getString(R.string.error),str,true);
			}
		}		
    }
	
	/** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		this.dialog.setCancelable(false);
    		this.dialog.setIcon(R.drawable.loading);
    		this.dialog.setIndeterminate(false);
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }

        @Override  
        protected void onProgressUpdate(String... values)
        {  
            dialog.setMessage(values[0]);
        }  

		@Override
		protected String doInBackground(String... str) {
			try{
				String CODE = str[0];
				String sql1 = "select * from product where code = '"+CODE+"'";
				String sql2 = "select imgurl from product_data where code = '"+CODE+"'";
				// get product data
				publishProgress("Downloading Data "+CODE);
				JSONArray T = query_sql(sql1);
				title = T.getJSONObject(0).getString("name");
				currency_name = T.getJSONObject(0).getString("currency_name");
				price = T.getJSONObject(0).getString("price");
				// get product imgurl
				publishProgress("Downloading Image "+CODE);
				IMAGEURL = query_sql(sql2);
				getAllCurrencyValue();
				return "1";
			}catch(Exception e){
				e.printStackTrace();
				return getString(R.string.error_server);
			}
		}
		
        /** Invoked by the Android on "doInBackground" is executed */
		@Override
		protected void onPostExecute(String str) {
			
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			
			if(str.equals("1")){
				//startActivity(intent);
				ShowImage();
			}else{
				Dialog(getString(R.string.error),str,true);
			}
		}		
    }
}