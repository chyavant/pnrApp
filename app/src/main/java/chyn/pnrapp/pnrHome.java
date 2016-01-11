package chyn.pnrapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 * Created by chyn on 10-Jan-16.
 */
public class pnrHome extends Activity{

    private TextView mTextView;
    String TAG = "Async Task : json : ";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnr_home);
        mTextView = (TextView) findViewById(R.id.pnrHomeView);

        Bundle b = getIntent().getExtras();
        String pnrNumber = b.getString("pnrNumber");

        //check if this pnr is present on DB
        //String result = getPNR(pnrNumber);
        //getPnrTask getpnrTask = new
        new getPnrTask().execute(pnrNumber);
    }

/*    private String getPNR(String pnrNumber){
        Context context = getApplicationContext();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.v("pnrHome getPnr : ", "hitting DB for " + pnrNumber);
        Cursor c = null;
        String[] requiredFields = {"PNR_NUMBER", "PNR_STATUS"};
        try {
            String queryString = "SELECT * FROM pnrDB WHERE PNR_NUMBER IS " + pnrNumber;
            c = db.rawQuery(queryString, null);
        }catch (Exception e){
            Log.v("Thrown exception", e.toString());
        }
        String toastMessage = "";
        if(c.moveToFirst()){
            pnrList.clear();
            do {
                String pnrNumber = c.getString(0);
                pnrClass tempPnrClass = new pnrClass();
                tempPnrClass.setPnrNumber(pnrNumber);
                pnrList.add(tempPnrClass);
            }while(c.moveToNext());
            return pnrList.toString();
        }
    }
*/
    public class getPnrTask extends AsyncTask<String, Void, String> implements chyn.pnrapp.getPnrTask {
        protected String doInBackground(String... pnrNumber){
            String APIKEY="kxgwg5488";  //Your API key
            Log.v("Async Task : ", "hitting the url");
            String endpoint = "http://api.railwayapi.com/pnr_status/pnr/"+pnrNumber[0]+"/apikey/"+APIKEY;

            HttpURLConnection request = null;
            BufferedReader rd;
            StringBuilder response = null;

            try{
                URL endpointUrl = new URL(endpoint);
                request = (HttpURLConnection)endpointUrl.openConnection();
                request.setRequestMethod("GET");
                request.connect();

                rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
                response = new StringBuilder();
                String line;
                while((line = rd.readLine()) != null){
                    response.append(line);

                    //get response into a json object.
                    JSONObject respObject = new JSONObject(response.toString());

                    //temp pnr class, and setting it with response values.
                    pnrClass tempPnrClass= new pnrClass();
                    tempPnrClass.setFromTo(respObject.getJSONObject("from_station").getString("code") + "-" + respObject.getJSONObject("to_station").getString("code"));
                    tempPnrClass.setPnrStatus(respObject.getJSONObject("passengers").getString("current_status"));

                    //get Db helper, inserting the object to DB.
                    DbHelper dbHelper = new DbHelper(getApplicationContext());
                    if(dbHelper.insert(tempPnrClass)) Log.v(TAG,"Insertion into DB succesful" );
                    else Log.v(TAG, "Insertion failed, check logs.");

                    //just logging the results.
                    Log.v("Async Task : ", line + "this is the response.");
                    Log.v(TAG , respObject.toString());
                    Log.v(TAG, respObject.getString("total_passengers"));
                    Log.v(TAG, respObject.getString("chart_prepared"));
                    Log.v(TAG, respObject.getJSONObject("boarding_point").getString("code"));
                }

            }catch (Exception e){
                return e.toString();
            }finally {
                try{
                    request.disconnect();
                }catch (Exception e){
                    return e.toString();
                }
                rd = null;
            }
            Log.v("from pnrHome", response.toString() + "almost end.");
            return response!=null?response.toString() : "Something Went wrong.";
        }

        protected void onPostExecute(String pnrNumber){
            Log.v("Async Task : postExec", pnrNumber.toString());
            mTextView.setText(pnrNumber.toString());

        }

    }

}
