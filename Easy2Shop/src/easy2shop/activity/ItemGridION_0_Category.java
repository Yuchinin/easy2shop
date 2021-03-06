package easy2shop.activity;

import org.json.JSONArray;
import org.json.JSONException;

import easy2shop.function.Base;
import easy2shop.function.ItemFunctions;
import easy2shop.function.MarqueeText;
import easy2shop.ycn.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup.LayoutParams;

public class ItemGridION_0_Category extends Base {
	private GridView photoGrid;
	private int mPhotoSize, mPhotoSpacing;
	private ImageAdapter imageAdapter;

	ImageAdapter adapter;
	JSONArray J;
	Context context = this;
	ItemFunctions itemf;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InitializeUI();
	}
	
	private void InitializeUI() {
		setContentView(R.layout.best_grid);
		//intent = new Intent();
		itemf = new ItemFunctions(context);
		setTitle("Category");
		String sql = "select category,gender,imgurl from(select * from product where enable = 1 order by rand()) product where category is not null group by category";
		new DataLoaderTask().execute(sql);
		//ShowImage();
	}
	
	private void ShowImage() {
		// get the photo size and spacing
		mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
		mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);

		// initialize image adapter
		imageAdapter = new ImageAdapter();

		photoGrid = (GridView) findViewById(R.id.albumGrid);

		// set image adapter to the GridView
		photoGrid.setAdapter(imageAdapter);

		// get the view tree observer of the grid and set the height and numcols dynamically
		photoGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (imageAdapter.getNumColumns() == 0) {
					final int numColumns = (int) Math.floor(photoGrid.getWidth() / (mPhotoSize + mPhotoSpacing));
					if (numColumns > 0) {
						final int columnWidth = (photoGrid.getWidth() / numColumns) - mPhotoSpacing;
						imageAdapter.setNumColumns(numColumns);
						imageAdapter.setItemHeight(columnWidth);

					}
				}
			}
		});
		photoGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				try {
					String gender = processJson(J.getJSONObject(position),"gender");
					String category = processJson(J.getJSONObject(position),"category");
					Log.d("category",category);
					Log.d("gender",gender);
					if(itemf.getGender(gender).equals("")){
						Log.d("gender","null");
						intent.setClass(context, ItemGridION_2_Type.class);
					}else{
						Log.d("gender","notnull");
						intent.setClass(context, ItemGridION_1_Gender.class);
					}
					intent.putExtra("category", category);
					intent.putExtra("gender", gender);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});	
	}
	
	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private int mItemHeight = 0;
		private int mNumColumns = 0;
		private RelativeLayout.LayoutParams mImageViewLayoutParams;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mImageViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		}

		public int getCount() {
			return J.length();
		}

		// set numcols
		public void setNumColumns(int numColumns) {
			mNumColumns = numColumns;
		}

		public int getNumColumns() {
			return mNumColumns;
		}

		// set photo item height
		public void setItemHeight(int height) {
			if (height == mItemHeight) {
				return;
			}
			mItemHeight = height;
			mImageViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
			notifyDataSetChanged();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View view, ViewGroup parent) {

			if (view == null) view = mInflater.inflate(R.layout.photo_item2, null);

			ImageView cover = (ImageView) view.findViewById(R.id.cover);
			//TextView title = (TextView) view.findViewById(R.id.textgrid);
			MarqueeText price = (MarqueeText) view.findViewById(R.id.MarqueePrice);
			MarqueeText title = (MarqueeText) view.findViewById(R.id.marqueetext);

			cover.setLayoutParams(mImageViewLayoutParams);

			// Check the height matches our calculated column width
			if (cover.getLayoutParams().height != mItemHeight) {
				cover.setLayoutParams(mImageViewLayoutParams);
			}
			
			try {
				title.setText(J.getJSONObject(position).getString("category"));
				IONImage(J.getJSONObject(position).getString("imgurl"),cover,false);
					price.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return view;
		}
	}
	
	/** AsyncTask to parse json data and load ListView */
    private class DataLoaderTask extends AsyncTask<String, String, String>{
    	private ProgressDialog dialog = new ProgressDialog(context);
    	
    	protected void onPreExecute() {
    		this.dialog.setCancelable(false);
    		this.dialog.setIndeterminate(false);
    		this.dialog.setIcon(R.drawable.loading);
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
				publishProgress(getString(R.string.downloading));
				J = query_sql(str[0]);
				if(J!=null) return "1";
			}catch(Exception e){
				e.printStackTrace();
				return getString(R.string.error_server);
			}
			return getString(R.string.error);
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
			}
			else{
					Dialog(getString(R.string.error),str,true);
				}
		}		
    }
}