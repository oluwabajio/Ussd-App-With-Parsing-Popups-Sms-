package tingtel.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tingtel.app.Adapters.BalanceAdapter;
import tingtel.app.Adapters.TransferAdapter;
import tingtel.app.Methods.AppDatabase;
import tingtel.app.Methods.Methods;
import tingtel.app.Models.Balance;
import tingtel.app.Models.Transfer;

public class TransferHistoryActivity extends AppCompatActivity {
    
    Methods methodsClass = new Methods();
    AppDatabase appdatabase;
    List<Transfer> items = new ArrayList<>();
    RecyclerView Rv_Balance;
    public static RecyclerView.Adapter adapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_history);
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

        appdatabase = AppDatabase.getDatabaseInstance(TransferHistoryActivity.this);
        Rv_Balance = (RecyclerView) findViewById(R.id.rv_Transferhistory);

        LoadDatabase();

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
                items = appdatabase.transferDao().getAllItems();


                TransferHistoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Rv_Balance.setLayoutManager(new LinearLayoutManager(TransferHistoryActivity.this));

                        //show latest items first
                        Collections.reverse(items);

                        adapter= new TransferAdapter(TransferHistoryActivity.this, items);

                        try {
                            Rv_Balance.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            //Toast.makeText(TransferHistoryActivity.this, "notifydone", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(TransferHistoryActivity.this, "something amiss", Toast.LENGTH_SHORT).show();
                        }
                    }});
            }
        };

        Thread newThread= new Thread(r);
        newThread.start();

    }


}
