package tingtel.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tingtel.app.Adapters.NetworkCodeAdapter;
import tingtel.app.Methods.AppDatabase;
import tingtel.app.Methods.Methods;
import tingtel.app.Models.NetworksCode;

public class ListUssdActivity extends AppCompatActivity {

    AppDatabase appdatabase;
    String NetworkName;
    int SimNo;
    private RecyclerView recyclerView;
    Methods TingtelClass = new Methods();
    List<NetworksCode> items = new ArrayList<>();
    public static RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ussd);
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

        appdatabase = AppDatabase.getDatabaseInstance(this);

        Bundle bundle = getIntent().getExtras();

        Intent intent = getIntent();
        if (intent.hasExtra("NetworkName")) {
            NetworkName = bundle.getString("NetworkName");
            getNetworkName();
            SimNo = bundle.getInt("SimNo");
            getSupportActionBar().setTitle(TingtelClass.capitalizer(NetworkName) + " Ussd Codes");
            changeToolbarColor();
        }




        recyclerView = (RecyclerView) findViewById(R.id.rv_network_codes);

        GetDbb();


    }


    public void getNetworkName() {

        if (NetworkName.toLowerCase().contains("9mobile") || NetworkName.toLowerCase().contains("etisalat")) {
            NetworkName = "9mobile";
        } else if (NetworkName.toLowerCase().contains("glo")) {
            NetworkName = "glo";
        } else if (NetworkName.toLowerCase().contains("mtn")) {
            NetworkName = "mtn";
        } else if (NetworkName.toLowerCase().contains("airtel")) {
            NetworkName = "airtel";
        } else {

        }
    }

    private void changeToolbarColor() {
        String color = "";
        if (NetworkName.equalsIgnoreCase("mtn")) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cca300")));

        } else if (NetworkName.equalsIgnoreCase("9mobile")) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0D1D10")));

        } else if (NetworkName.equalsIgnoreCase("glo")) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#174A12")));

        } else if (NetworkName.equalsIgnoreCase("airtel")) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c2141c")));

        }

    }

    private void GetDbb() {

        //load saved room data to recyclerview
        Runnable r = new Runnable(){
            @Override
            public void run() {
                //  final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production")
                //            .build();
                items.clear();
                items = appdatabase.networksCodesDao().getItemsByName(NetworkName);



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   Toast.makeText(NetorksTransactionsActivity.this, "size is" + items.size(), Toast.LENGTH_SHORT).show();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                        adapter= new NetworkCodeAdapter(ListUssdActivity.this, items, NetworkName, SimNo);

                        try {
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            Toast.makeText(ListUssdActivity.this, "something amiss", Toast.LENGTH_SHORT).show();
                        }
                    }});
            }
        };

        Thread newThread= new Thread(r);
        newThread.start();

    }

}
