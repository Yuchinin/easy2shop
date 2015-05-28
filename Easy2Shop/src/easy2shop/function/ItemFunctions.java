package easy2shop.function;

import java.text.DecimalFormat;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
 




import easy2shop.ycn.R;
import android.content.Context;
import android.util.Log;
 
public class ItemFunctions extends BaseFunctions {
	
	private SettingFunctions st;
    static String imgfolder = "/easy2shop/android/product_img/";
    Context ct;
	
    public ItemFunctions(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.ct = context;
        st = new SettingFunctions(ct);
	}
    
    public String getGender(String gender){
    	if(gender.isEmpty()||gender.length()<=0||gender.equals("0")){
    		return "";
    	}else{
    		return gender;
    	}
    }

    public String getPrice(String currency_name,String price){
    	DecimalFormat DF2 = new DecimalFormat("#.##");
    	Double p = Double.valueOf(price);
    	p = p*Double.parseDouble(st.getCurrencyValue(currency_name));
    	Log.d("Current Price",p.toString());
    	p*=3;	// triple price
    	Log.d("Triple Price",p.toString());
    	
    	p = p-p*5/100;
    	Log.d("Price 5%", Double.toString(p*5/100));
    	Log.d("Price -5%", p.toString());
    	if(p<=0||p.isNaN())
    		return "No Price Found";
    	else
    		return "RM"+DF2.format(p);
    }
    
    public String getImageUrl(JSONArray J,int position,String type){
    	String url = null;
		try {
			if(J.getJSONObject(position).getString("imgtype").equals("url")){
				url = J.getJSONObject(position).getString("imgurl");
			}else{
				url = "http://"+st.getIp()+imgfolder+
						J.getJSONObject(position).getString(type)+"."+J.getJSONObject(position).getString("imgtype");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return url;
    }
    
    private String getDesc(String value){
    	if(value.length()>0){
    		return value;
    	}else{
    		return ct.getString(R.string.unknown);
    	}
    }
    
/*    public String getDescription(int position, JSONArray J) {
    	String tmp = "";
		try {
			tmp += 
					ct.getString(R.string.price)+":"+getPrice(J.getJSONObject(position).getString(desc[1]))+"\n"+
					ct.getString(R.string.location)+":"+getLocation(J.getJSONObject(position).getString(desc[2]))+"\n"+
					ct.getString(R.string.season)+":"+getSeasonString(J.getJSONObject(position).getString(desc[3]))+"\n"+
					ct.getString(R.string.plantable)+":"+getPlantableString(J.getJSONObject(position).getString(desc[4]))+"\n"+
					ct.getString(R.string.dayuse)+":"+J.getJSONObject(position).getString(desc[5])+"\n"+
					ct.getString(R.string.desc)+":"+getDesc(J.getJSONObject(position).getString(desc[6]));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
    }
*/    
    public String getDescriptionLight(String description) {
    	
		return description;
    }
//*/
}
