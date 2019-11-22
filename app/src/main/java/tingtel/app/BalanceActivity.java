package tingtel.app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import tingtel.app.Adapters.BalanceAdapter;
import tingtel.app.Methods.AppDatabase;
import tingtel.app.Methods.Methods;
import tingtel.app.Models.Balance;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_SMS;


public class BalanceActivity extends AppCompatActivity {

    Methods methodsClass = new Methods();
    AppDatabase appdatabase;
    List<Balance> items = new ArrayList<>();
    RecyclerView Rv_Balance;
    public static RecyclerView.Adapter adapter;
    FancyButton btnClear;
    Spinner spNetworks;
    int REQUEST_PHONE_STATE = 101;
    int i = 0;

    String SimIccid, SimName, BalanceType;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("TingTelPref", 0);
        editor = sharedPreferences.edit();

        appdatabase = AppDatabase.getDatabaseInstance(BalanceActivity.this);
        Rv_Balance = (RecyclerView) findViewById(R.id.rv_history);
        btnClear = (FancyButton)findViewById(R.id.btnClear);
        spNetworks = (Spinner) findViewById(R.id.sp_networks);




        appdatabase = AppDatabase.getDatabaseInstance(this);

        Bundle bundle = getIntent().getExtras();

        Intent intent = getIntent();
        if (intent.hasExtra("SimIccid")) {
            SimIccid = bundle.getString("SimIccid");
            SimName = bundle.getString("SimName");
            BalanceType = bundle.getString("Type");
            LoadDatabaseData(SimIccid, BalanceType, SimName);

        } else {

            LoadDatabase();
            populateSpinner();
        }
        spNetworks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spNetworks.getSelectedItem().toString().equalsIgnoreCase("All")){
                    LoadDatabase();
                } else {

                    LoadSelectedNetworkDatabase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBalance();
            }
        });

        


    }

    private void LoadDatabaseData(final String  SimIccid, final String BalanceType, final String SimName) {

        // Toast.makeText(this, "i am here", Toast.LENGTH_SHORT).show();
        //load saved room data to recyclerview
        Runnable r = new Runnable(){
            @Override
            public void run() {
                //  final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production")
                //            .build();
                items.clear();
                items = appdatabase.balanceDao().getAirtimeOrDataList( SimIccid, BalanceType);


                BalanceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Rv_Balance.setLayoutManager(new LinearLayoutManager(BalanceActivity.this));

                        //show latest items first
                        Collections.reverse(items);

                        adapter= new BalanceAdapter(BalanceActivity.this, items);

                        try {
                            Rv_Balance.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            //Toast.makeText(BalanceActivity.this, "notifydone", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(BalanceActivity.this, "something amiss", Toast.LENGTH_SHORT).show();
                        }
                    }});
            }
        };

        Thread newThread= new Thread(r);
        newThread.start();


    }

    private void populateSpinner() {
        if (ActivityCompat.checkSelfPermission(BalanceActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //ask permission
            ActivityCompat.requestPermissions(BalanceActivity.this, new String[]{READ_PHONE_STATE, RECEIVE_SMS, CALL_PHONE}, REQUEST_PHONE_STATE);


        } else {
            String[] NetworkArray = methodsClass.DetectSimCards(BalanceActivity.this);
            ArrayAdapter<String> networkArray = new ArrayAdapter<String>(BalanceActivity.this,R.layout.spinner_item, NetworkArray);
            spNetworks.setAdapter(networkArray);
            spNetworks.setSelection(networkArray.getCount()-1);
            Log.e("logmessage", "spinnerpopulated");
        }
    }


    private void deleteBalance() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                appdatabase.balanceDao().delete();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(BalanceActivity.this, "All Balance Deleted", Toast.LENGTH_LONG).show();
                LoadDatabase();

            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }






    private void LoadSelectedNetworkDatabase() {

        // Toast.makeText(this, "i am here", Toast.LENGTH_SHORT).show();
        //load saved room data to recyclerview
        Runnable r = new Runnable(){
            @Override
            public void run() {
                //  final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production")
                //            .build();
                items.clear();

                String SimIccid;

                if (spNetworks.getSelectedItemPosition() == 0) {
                    SimIccid = sharedPreferences.getString("SIM1ICCID", "");

                } else if (spNetworks.getSelectedItemPosition() == 1) {
                    SimIccid = sharedPreferences.getString("SIM2ICCID", "");
                } else {
                    SimIccid = "";
                }

                Log.e("logmessage", "selected iccid " + SimIccid);
                items = appdatabase.balanceDao().getItemsByUuid(SimIccid);


                BalanceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Rv_Balance.setLayoutManager(new LinearLayoutManager(BalanceActivity.this));

                        //show latest items first
                        Collections.reverse(items);

                        adapter= new BalanceAdapter(BalanceActivity.this, items);

                        try {
                            Rv_Balance.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            //Toast.makeText(BalanceActivity.this, "notifydone", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(BalanceActivity.this, "something amiss", Toast.LENGTH_SHORT).show();
                        }
                    }});
            }
        };

        Thread newThread= new Thread(r);
        newThread.start();

    }
    private void LoadDatabase() {

        // Toast.makeText(this, "i am here", Toast.LENGTH_SHORT).show();
        //load saved room data to recyclerview
        Runnable r = new Runnable(){
            @Override
            public void run() {
                //  final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production")
                //            .build();
                items.clear();
                items = appdatabase.balanceDao().getAllItems();


                BalanceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Rv_Balance.setLayoutManager(new LinearLayoutManager(BalanceActivity.this));

                        //show latest items first
                        Collections.reverse(items);

                        adapter= new BalanceAdapter(BalanceActivity.this, items);

                        try {
                            Rv_Balance.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            //Toast.makeText(BalanceActivity.this, "notifydone", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(BalanceActivity.this, "something amiss", Toast.LENGTH_SHORT).show();
                        }
                    }});
            }
        };

        Thread newThread= new Thread(r);
        newThread.start();

    }
    

}
