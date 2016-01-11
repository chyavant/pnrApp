package chyn.pnrapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<pnrClass> pnrList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get all PNRs into the pnr list variable
        getPNR();
        //creating adapter for the list view
        ArrayAdapter adapter = new myListAdapter();
        //creating the list view.
        ListView listView = (ListView) findViewById(R.id.pnrListView);
        listView.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Done creating UI", Toast.LENGTH_LONG).show();



        ImageButton fab = (ImageButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), getPNR.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    public void registerClickCallback(){
        ListView list = (ListView) findViewById(R.id.pnrListView);
        list.setOnClickListener(new AdapterView<>.OnClickListener() {
            public void onItemClick(AdapterView<?> parent, ListView ViewClicked, int position, long id) {
                pnrClass selectedPnr = pnrList.get(position);
                String message = "pnr " + selectedPnr + " is clicked";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }*/

    public void onActivityResult(Intent intent){

    }

    private class myListAdapter extends ArrayAdapter<pnrClass>{
        public myListAdapter(){
            super(MainActivity.this, R.layout.pnr_item, pnrList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //to make sure the view is not null
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.pnr_item, parent, false);
            }
            pnrClass localPnrObject = pnrList.get(position);
            TextView pnrNumber = (TextView) itemView.findViewById(R.id.fromTO);
            pnrNumber.setText(""+localPnrObject.getPnrNumber());

            return itemView;
        }

    }

    private void getPNR(){
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
            pnrList.clear();
            do {
                String pnrNumber = c.getString(0);
                pnrClass tempPnrClass = new pnrClass();
                tempPnrClass.setPnrNumber(pnrNumber);
                pnrList.add(tempPnrClass);
            }while(c.moveToNext());
            Toast.makeText(getApplicationContext(), "length of pnrList is " + pnrList.size(), Toast.LENGTH_LONG).show();
        }
    }
}
