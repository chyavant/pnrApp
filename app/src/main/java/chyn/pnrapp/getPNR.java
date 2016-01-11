package chyn.pnrapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v4.widget.ListViewAutoScrollHelper;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by chyn on 28-Nov-15.
 */
public class getPNR extends Activity{

    private Button mCancel;
    private Button mSubmit;
    private EditText mPnr;

    //private connectionHelper connection = new connectionHelper();

    protected void onCreate(final Bundle savedInstanceState){
        Log.v("onCreatePNR", "reached this line 1");
        super.onCreate(savedInstanceState);

        Log.v("onCreatePNR", "reached this line 2");

        setContentView(R.layout.add_pnr);

        mCancel = (Button) findViewById(R.id.cancel);
        mSubmit = (Button) findViewById(R.id.submit);
        mPnr = (EditText) findViewById(R.id.pnr_number);

        Log.v("pnr activity", "reached this line");

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnrClass pnr = new pnrClass();
                String pnrNumber = mPnr.getText().toString();
                pnr.setPnrNumber((mPnr.getText().toString()));
                if (pnrNumber.length() != 10) {
                    Toast.makeText(getApplicationContext(), "That's an invalid PNR number.", Toast.LENGTH_LONG).show();
                } else {
                    String status = "";
                    Toast.makeText(getApplicationContext(), "Query under progress.", Toast.LENGTH_LONG).show();
                    //status = connectionHelper.getPnrResponse(pnrNumber);
                    //create a thread with call back that updates the main activity UI
                    Context context = getApplicationContext();

                    Intent intent = new Intent(getBaseContext(), pnrHome.class);
                    Bundle b = new Bundle();
                    b.putString("pnrNumber", pnrNumber);
                    intent.putExtras(b);
                    startActivity(intent);

                    //push the new pnr value to database
                    DbHelper dbHelper = new DbHelper(context);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    //creating new map of values to construct the query
                    ContentValues values = new ContentValues();
                    values.put("PNR_NUMBER", pnrNumber);
                    values.put("PNR_STATUS", "not sure about the data");
                    try {
                        long newRowId = db.insert("pnrDB", null, values);
                        Toast.makeText(getApplicationContext(), "That was a successful insertion " + newRowId, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {

                    }

                    //MainActivity mainActivity = new MainActivity();
                    //mainActivity.onCreate(savedInstanceState);
                    finish();


                    Log.v("response from  ", status);
                    if (status == "No") {
                        //// TODO: 28-Nov-15
                        //navigate to a page throwing error
                    } else {
                        //// TODO: 28-Nov-15
                        //parse the json string and update this value in the local file
                        //for further reference
                        //navigate to the main page...
                    }
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                DbHelper dbHelper = new DbHelper(context);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Log.v("log on the cancel r", "cancel button handler");
                Cursor c = null;
                String[] requiredFields = {"PNR_NUMBER", "PNR_STATUS"};
                try {
                    String queryString = "SELECT * FROM pnrDB";
                    c = db.rawQuery(queryString, null);
                }catch (Exception e){
                    Log.v("Thrown exception", e.toString());
                }
                String toastMessage = "";
                if(c.moveToFirst()){
                    do {
                        toastMessage += " "+c.getString(0);
                    }while(c.moveToNext());
                }
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }
}
