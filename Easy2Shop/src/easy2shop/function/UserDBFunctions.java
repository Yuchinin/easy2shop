package easy2shop.function;

import java.io.IOException;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class UserDBFunctions extends SQLiteOpenHelper {
	Context context;
	SQLiteDatabase db;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "main_db";
 
    // Login table name
    private static final String TABLE_LOGIN = "login";
    
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/easy2shop.ycn/databases/";
 
    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_C_AT = "created_at";
    private static final String KEY_U_AT = "updated_at";
    private static final String KEY_LVL = "level";
    private static final String KEY_TS = "timestamp";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_BIRTHDAY = "birthday";
    
    String TABLE_TEMPLATE = "("
			//+ KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT UNIQUE,"
            + KEY_UID + " TEXT,"
            + KEY_C_AT + " TEXT,"
            + KEY_U_AT + " TEXT,"
            + KEY_LVL + " INTEGER,"
            + KEY_TS + " TIMESTAMP,"
            + KEY_PHONE + " TEXT,"
            + KEY_GENDER + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_BIRTHDAY + " DATE" +
            		")";
	
    String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN + TABLE_TEMPLATE;
    
    
    String COLUMN[] = {KEY_ID,KEY_NAME,KEY_EMAIL,KEY_UID,KEY_C_AT,KEY_U_AT,KEY_LVL,KEY_TS,KEY_PHONE,KEY_GENDER,KEY_ADDRESS,KEY_BIRTHDAY};
 
    public UserDBFunctions(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //this.onCreate(getWritableDatabase());
        checkDataBase();
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    		Log.d(DATABASE_NAME,"Create Table!");
    		this.db = db;
            db.execSQL(CREATE_LOGIN_TABLE);
            db.setVersion(DATABASE_VERSION);
    }
    
    private void checkDataBase(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	try{
    		String myPath = DB_PATH + DATABASE_NAME;
    		Log.d("myPath",myPath);
    		//checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		//db = SQLiteDatabase.openOrCreateDatabase(myPath, null);
    		db  = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		
    		//dbg = this.getReadableDatabase();
    		if(db.getVersion()<DATABASE_VERSION){
				Log.d(DATABASE_NAME, "New Version Found!");
				Log.d(DATABASE_NAME, "Begin Upgrading Database...");
				onUpgrade(db,db.getVersion(),DATABASE_VERSION);
    		}
    		Log.d(DATABASE_NAME, "Checked ver = "+db.getVersion());
    		Log.d(DATABASE_NAME, "Current ver = "+DATABASE_VERSION);
    	}catch(Exception e){
    		e.printStackTrace();
    		//database does't exist yet.
    	}
    	if(db != null){
    		db.close();
    	}
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
			Log.d(DATABASE_NAME,"New Version: "+newVersion+" > Old Version: "+oldVersion);
			Log.d(DATABASE_NAME,"Updating Database: "+DATABASE_NAME);
    }
 
    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String created_at, String updated_at
    		, String level, String timestamp, String phone,String g,String addr,String birth) {
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_C_AT, created_at); // Created At
        values.put(KEY_U_AT, updated_at);
        values.put(KEY_LVL, level);
        values.put(KEY_TS, timestamp);
        values.put(KEY_PHONE, phone);
        values.put(KEY_GENDER, g);
        values.put(KEY_ADDRESS, addr);
        values.put(KEY_BIRTHDAY, birth);
 
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }
 
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
 
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
        	for(int x=1;x<COLUMN.length;x++){
        		user.put(COLUMN[x], cursor.getString(cursor.getColumnIndex(COLUMN[x])));
        	}
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
 
    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        String countQuery = "SELECT * FROM " + TABLE_LOGIN;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
 
        // return row count
        return rowCount;
    }
    
    public String getUid(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	String countQuery = "SELECT * FROM " + TABLE_LOGIN;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        String temp = cursor.getString(cursor.getColumnIndex(KEY_UID));
        db.close();
        return temp;
    }
    
    public String getName(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        Cursor c = this.get(1);
        String temp = c.getString(c.getColumnIndex(KEY_NAME));
        c.close();
        return temp;
    }
    
    public String getEmail(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        Cursor c = this.get(1);
        String temp = c.getString(c.getColumnIndex(KEY_EMAIL));
        c.close();
        return temp;
    }
    
    public int getLevel(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
        int temp = c.getInt(c.getColumnIndex(KEY_LVL));
        c.close();
        return temp;
    }
    
    public String getGender(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
    	String temp = c.getString(c.getColumnIndex(KEY_GENDER));
    	c.close();
        return temp;
    }
    
    public String getPhone(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
    	String temp = c.getString(c.getColumnIndex(KEY_PHONE));
    	c.close();
        return temp;
    }
    
    public String getAddress(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
    	String temp = c.getString(c.getColumnIndex(KEY_ADDRESS));
    	c.close();
        return temp;
    }
    
    public String getBirthday(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
    	Cursor c = this.get(1);
    	String temp = c.getString(c.getColumnIndex(KEY_BIRTHDAY));
    	c.close();
        return temp;
    }
 
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
    	Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
        db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
    
	public Cursor get(long rowId) throws SQLException{
		Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
		db = this.getReadableDatabase();
		Cursor mCursor = db.query(TABLE_LOGIN, COLUMN,KEY_ID + "=" + rowId,null,null,null,null,null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor getAll(){
		Log.d("CurrentMethod", new Exception().getStackTrace()[0].getMethodName());
		db = this.getReadableDatabase();
		Cursor mCursor = db.query(TABLE_LOGIN, COLUMN, null, null, null, null, null);
		return mCursor;
	}
	
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(){
        if(this.getRowCount() > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(){
    	if(this.getRowCount() > 0) resetTables();
        return true;
    }
 
}
