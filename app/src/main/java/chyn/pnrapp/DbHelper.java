package chyn.pnrapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by chyn on 26-Dec-15.
 */



public class DbHelper extends SQLiteOpenHelper{

    private String TAG = "DB helper : ";

    public static final String DATABASE_NAME = "pnrDB";
    public static final int DATABASE_VERSION = 1;

    private String SQL_CREATE_ENTITY = "CREATE TABLE "+DATABASE_NAME+
            "(PNR_NUMBER TEXT PRIMARY KEY, " +
            "PNR_STATUS TEXT, " +
            "FROM_TO TEXT," +
            "FROM_TO OTHER_DETAILS TEXT)";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTITY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public boolean insert(pnrClass pnrObject){
        Log.v(TAG, "inserting pnr object");
        Cursor c = null;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PNR_NUMBER", pnrObject.getPnrNumber());
        values.put("PNR_STATUS", pnrObject.getPnrStatus());
        values.put("FROM_TO", pnrObject.getFromTo());
        Log.v(TAG, "created the query string");
        long newRowId;
        try{
            newRowId = db.insert("pnrDB", null, values);
        }catch (Exception e){
            Log.v(TAG, e.toString());
            return false;
        }
        if(newRowId > 0){
            Log.v(TAG, "inserted at id " + newRowId);
            return true;
        }
        else if(newRowId == -1){
            Log.v(TAG, "record already exists.");
            return true;
        }
        else return false;
    }

    public pnrClass query(String pnrNumber){
        pnrClass pnrObjet = new pnrClass();


        return pnrObjet;
    }

}
