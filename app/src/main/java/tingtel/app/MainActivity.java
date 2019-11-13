package tingtel.app;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import tingtel.app.Fragments.MainFragment;
import tingtel.app.Fragments.SettingsFragment;
import tingtel.app.Fragments.TransferFragment;
import tingtel.app.Methods.Methods;
import tingtel.app.Methods.MyApplication;
import tingtel.app.ViewModels.BalanceViewModel;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Methods  methodsClass = new Methods();

    BalanceViewModel balanceViewModel;

    MyApplication globalVariable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      initViews();
      initObjects();

        toolbar.setTitle("Home");
        Fragment fragment = new MainFragment();
        loadFragment(fragment);




    }

    private void initObjects() {
        LocalBroadcastManager.getInstance(this).registerReceiver(UssdReceiver, new IntentFilter("TintelIntentMessage"));

//        globalVariable = (MyApplication) this.getApplicationContext();
//
//        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class);
//
//
//
//
//        balanceViewModel.getCurrentBalance().observe(MainActivity.this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String balance) {
//
//                Toast.makeText(MainActivity.this, balance, Toast.LENGTH_SHORT).show();
//
//
//            }
//        });

    }

    private void initViews() {

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    fragment = new MainFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_beneficiary:
                    toolbar.setTitle("Transfer");
                    fragment = new TransferFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_settings:
                    toolbar.setTitle("Settings");
                    fragment = new SettingsFragment();
                    loadFragment(fragment);
                    return true;

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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



    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(SmsReceiver, new IntentFilter("SmsMessage"));

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(SmsReceiver);
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(UssdReceiver);
    }

    @Override
    public void onDestroy() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(UssdReceiver);
        super.onDestroy();
    }



    private BroadcastReceiver SmsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("SmsMessage")) {

                final String message = intent.getStringExtra("message");
                final String senderNum = intent.getStringExtra("senderNum");

                String servicename = "";
                String simname = "";
                String servicetype;
                int serviceLogo = R.drawable.ic_launcher_background;





                if (message.toLowerCase().contains("free sms to 9mobile numbers")){
                    return;
                }
//                if (senderNum.equalsIgnoreCase("131") || senderNum.equalsIgnoreCase("mtn n")){
//                    return;
//                }


                //Airtel Data Balance
                if((senderNum.equalsIgnoreCase("Airtel")) && (message.toLowerCase().contains(""))) {

                    servicename = "Data Balance";
                    simname = "9mobile";
                    serviceLogo = R.drawable.airtel_logo;

                    Log.e("logmessage", "supposed to receive here");
                    Log.e("logmessage", message);
                    //   Toast.makeText(context, "mainactivity sms", Toast.LENGTH_SHORT).show();

                    //    CheckSender(senderNum, message);


                } else if ((senderNum.equalsIgnoreCase("9mobile")) && (message.toLowerCase().contains("your data balance as at"))  ) {

                    Toast.makeText(MainActivity.this, "bbbbn" + message, Toast.LENGTH_SHORT).show();
                    servicename = "Data Balance";
                    simname = "Airtel NG";
                    serviceLogo = R.drawable.nmobile_logo;


                    String[] datamessage = message.toLowerCase().split("your data balance as at");
                    Toast.makeText(MainActivity.this, datamessage[1], Toast.LENGTH_SHORT).show();

                    Pattern pattern = Pattern.compile("\\d+mb");
                    Matcher matcher = pattern.matcher(datamessage[1]);

                    String balance = "";

                    if (matcher.find())
                    {
                        Toast.makeText(MainActivity.this, matcher.group(0), Toast.LENGTH_SHORT).show();
                        balance = matcher.group(0);

                    } else {
                        Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();
                    }


                    updateBalance(balance);

                }

                else if ((senderNum.equalsIgnoreCase("Airtel")) && (message.toLowerCase().contains("Your have"))) {


                    servicename = "Data Balance";
                    simname = "Airtel NG";
                    serviceLogo = R.drawable.nmobile_logo;

                    Log.e("logmessage", "supposed to receive here");
                    Log.e("logmessage", message);

                }

                else {
                    Log.e("logmessage", "wrong message");
                    return;
                }

                String simiccid = "";

                methodsClass.SaveAirtimeOrData(getApplicationContext(), Integer.parseInt("3"), simiccid, simname, message, serviceLogo);
                //methodsClass.SaveHistory(getApplicationContext(), Integer.parseInt("4323"), "Airtel", "Bank Transfer", "hggh", R.drawable.mtn_logo);


                Intent intent1 = new Intent(MainActivity.this, ShowMessage.class);
                intent1.putExtra("message", message);
                intent1.putExtra("senderNum", senderNum);
             //   startActivity(intent1);


            } else {
                //   Toast.makeText(MainActivity.this, "not received", Toast.LENGTH_SHORT).show();
                Log.e("sms", "not received");
                return;
            }


        }
    };

    //Broadcast receiver to receive ussd code
    public BroadcastReceiver UssdReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("TingtelMessage");
             // Toast.makeText(context, "received" + message, Toast.LENGTH_SHORT).show();

            Log.e("logmessage", "2222222" + message);




            String Appstate = globalVariable.getAppstate();

            if (Appstate.equalsIgnoreCase("background")) {

                //populate airtime notification


            }else {

                checkPopupandSave(message);
            }


            //    methodsClass.SaveHistory(getApplicationContext(), Integer.parseInt("4323"), "Airtel",  "Bank Transfer","hggh", R.drawable.mtn_logo);

//            if (Appstate.equalsIgnoreCase("background")) {
//                Toast.makeText(globalVariable, "background", Toast.LENGTH_SHORT).show();
//
//            } else {
//
//                Toast.makeText(context, "tingtel" + message, Toast.LENGTH_SHORT).show();
//                Intent in = new Intent(context, ShowMessage.class);
//                in.putExtra("message", message);
//                context.startActivity(in);
//            }

        }
    };

    private void checkPopupandSave(String message) {
        String ussdservice = globalVariable.getUssdservice();
        int servicelogo;
        String simname;
        String ServiceName;
        String balance = "";

        if (ussdservice.equalsIgnoreCase("mtn-airtime")){
            servicelogo = R.drawable.mtn_logo;
            simname = "Mtn Ng";
            ServiceName = "Mtn Balance";


            Pattern pattern = Pattern.compile("N\\d+.\\d+");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find())
            {
                Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show();
                balance = matcher.group(0);
            } else {
                Toast.makeText(this, "not foundjj", Toast.LENGTH_SHORT).show();
            }




            globalVariable.setUssdservice("");
            updateBalance(balance);
        }  else if (ussdservice.equalsIgnoreCase("glo-airtime")) {
            servicelogo = R.drawable.glo_logo;
            simname = "Glo Ng";
            ServiceName = "Glo Balance";

            globalVariable.setUssdservice("");

        } else if (ussdservice.equalsIgnoreCase("airtel-airtime")) {

            servicelogo = R.drawable.airtel_logo;
            simname = "Airtel Ng";
            ServiceName = "Balance Balance";

            Pattern pattern = Pattern.compile("N\\d+.\\d+");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find())
            {
                Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show();
                balance = matcher.group(0);
            } else {
                Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
            }

            globalVariable.setUssdservice("");
            updateBalance(balance);
        } else if (ussdservice.equalsIgnoreCase("9mobile-airtime")) {

            servicelogo = R.drawable.nmobile_logo;
            simname = "9mobile";
            ServiceName = "Balance Balance";

            Pattern pattern = Pattern.compile("N \\d+.\\d+");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find())
            {
                Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show();
                balance = matcher.group(0);
            } else {
                Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
            }

            globalVariable.setUssdservice("");
            updateBalance(balance);
        } else if (ussdservice.equalsIgnoreCase("mtn-data")) {

            servicelogo = R.drawable.mtn_logo;
            simname = "Mtn Ng";
            ServiceName = "Data Balance";

            Pattern pattern = Pattern.compile("\\d+.\\d+MB");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find())
            {
                Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show();
                balance = matcher.group(0);
                Toast.makeText(this, "This matcher: "+matcher.group(0),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
            }

            globalVariable.setUssdservice("");
            updateBalance(balance);
        } else if (ussdservice.equalsIgnoreCase("glo-data")) {

            servicelogo = R.drawable.glo_logo;
            simname = "Glo Ng";
            ServiceName = "Data Balance";

            globalVariable.setUssdservice("");
            updateBalance(balance);
        }else if (ussdservice.equalsIgnoreCase("airtel-data")) {

            servicelogo = R.drawable.airtel_logo;
            simname = "Airtel Ng";
            ServiceName = "Data Balance";

            globalVariable.setUssdservice("");
            updateBalance(balance);
        }else if (ussdservice.equalsIgnoreCase("9mobile-data")) {

            servicelogo = R.drawable.nmobile_logo;
            simname = "9mobile";
            ServiceName = "Data Balance";


        } else if (ussdservice.equalsIgnoreCase("mtn-phone")) {

            servicelogo = R.drawable.mtn_logo;
            simname = "Mtn Ng";
            ServiceName = "Phone Number";

            globalVariable.setUssdservice("");
            updateBalance(balance);
        } else if (ussdservice.equalsIgnoreCase("glo-phone")) {

            servicelogo = R.drawable.glo_logo;
            simname = "Glo Ng";
            ServiceName = "Phone Number";

            globalVariable.setUssdservice("");
            updateBalance(balance);
        }  else if (ussdservice.equalsIgnoreCase("airtel-phone")) {

            servicelogo = R.drawable.airtel_logo;
            simname = "Airtel Ng";
            ServiceName = "Phone Number";

            globalVariable.setUssdservice("");
            updateBalance(balance);
        }  else if (ussdservice.equalsIgnoreCase("9mobile-phone")) {

            servicelogo = R.drawable.nmobile_logo;
            simname = "9mobile";
            ServiceName = "Phone Number";

        }



        else {

           // servicelogo = R.drawable.history;
            simname = "";
            ServiceName = "";
            globalVariable.setUssdservice("");
            return;
        }


        String simiccid = globalVariable.getIccid();

        String Amount =  balance.replaceAll("[^0-9]", "");
        methodsClass.SaveAirtimeOrData(getApplicationContext(), Integer.parseInt("66"), simiccid,  simname, message, servicelogo);







    }

    private void updateBalance(String balance) {
        Toast.makeText(this, "i dey", Toast.LENGTH_SHORT).show();
        balanceViewModel.setCurrentBalance(balance);



    }


}
