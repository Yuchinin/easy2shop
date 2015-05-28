package easy2shop.activity;

import org.json.JSONArray;
import org.json.JSONException;

import com.koushikdutta.ion.Ion;

import easy2shop.function.Base;
import easy2shop.function.BaseFunctions;
import easy2shop.function.ItemFunctions;
import easy2shop.function.SettingFunctions;
import easy2shop.function.UserDBFunctions;
import easy2shop.function.UserFunctions;
import easy2shop.ycn.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ItemListION extends Base {

	AbsListView listView;
	
	ItemAdapter adapter;
	//String[] imageUrls;
	JSONArray J;
	SettingFunctions settings;
	Context context = this;
	UserDBFunctions db;
	BaseFunctions baseFunctions;
	UserFunctions uf;
	ItemFunctions itemf;
	SettingFunctions st;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new SettingFunctions(context);
		baseFunctions = new BaseFunctions(context);
		uf = new UserFunctions(context);
		itemf = new ItemFunctions(context);
		st = new SettingFunctions(context);
		
		setContentView(R.layout.ac_image_list);
		setTitle("Category");
		//setContentView(R.layout.ac_image_grid);
		listView = (ListView) findViewById(android.R.id.list);
		//listView = (GridView) findViewById(R.id.gridview);
		
		//start----------------------------------------------
	    // URL to the JSON data         
	    // The parsing of the xml data is done in a non-ui thread 
	    DataLoaderTask listViewLoaderTask = new DataLoaderTask();
	    // Start parsing xml data
	    //listViewLoaderTask.execute("select category,gender,imgurl from(select * from product order by rand()) product where category is not null group by category");           
	    listViewLoaderTask.execute("select id,name_japanese from ragnarok.item_db_re");
	    //----------------------------------------------
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	/*private void detailActivity(int id,int position) {
		Intent intent = new Intent(this, Plant_details.class);
		intent.putExtra("J",J.toString());
		intent.putExtra("position", position);
		intent.putExtra("id", id);
		Log.d("position put in:", Integer.toString(position));
		startActivity(intent);
	}*/

	class ItemAdapter extends BaseAdapter {
		
		private ItemAdapter(JSONArray JSONARR){
			J = JSONARR;
		}

		private class ViewHolder {
			public TextView txt_desc,txtName,txtId;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return J.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.txtName = (TextView) view.findViewById(R.id.marqueetext);
				holder.txt_desc = (TextView) view.findViewById(R.id.marqueeDesc);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			String name,uid,imgurl;
			Double price = null;
			name=uid=imgurl=null;
	        try {
				name = J.getJSONObject(position).getString("name_japanese");
				uid = J.getJSONObject(position).getString("id");
		        imgurl = "http://"+st.getIp()+st.getRootFolder()+"images/"+uid+".png";
		        //price = J.getJSONObject(position).getDouble("price");
		        //J.getJSONObject(position).getString("imgurl");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        // Special Case
	        
	        //holder.txtId.setText(uid);
	        
	        holder.txtName.setText(name);

			//holder.txt_desc.setText(itemf.getPrice(price));
			
			IONImage(imgurl,holder.image,false);
			return view;
		}
	}
	
	//start----------------------------------------------------------------------------------------
    /** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		this.dialog.setCancelable(false);
    		this.dialog.setIndeterminate(false);
        			
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }
    	
    	//Update the progress  
        @Override  
        protected void onProgressUpdate(String... values)  
        {  
            //set the current progress of the progress dialog
            dialog.setMessage(values[0]);
            
        }  
    	
    	// Doing the parsing of xml data in a non-ui thread 
		@Override
		protected String doInBackground(String... str) {
				publishProgress(getString(R.string.downloading));
				try{
					publishProgress(getString(R.string.downloading));
					J = query_sql(str[0]);
					if(J!=null) return "success";
				}catch(Exception e){
					e.printStackTrace();
					return getString(R.string.error_server);
				}
				return "fail";
		}
		
        /** Invoked by the Android on "doInBackground" is executed */
		@Override
		protected void onPostExecute(String str) {
			
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			
			if(str.equals("success")){
				((ListView) listView).setAdapter(new ItemAdapter(J));
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					    Log.d("position:",Integer.toString(position));
					    Log.d("long:",Long.toString(id));
					}
				});
			}
			else{
					Dialog(getString(R.string.error),str,true);
				}
		}		
    }
    //-----------------------------------------------------------------------------------
	
}