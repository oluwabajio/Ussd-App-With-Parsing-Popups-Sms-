package tingtel.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import tingtel.app.Adapters.BalanceAdapter;
import tingtel.app.Fragments.MainFragment;
import tingtel.app.Fragments.SettingsFragment;
import tingtel.app.Fragments.TransferFragment;
import tingtel.app.Methods.Methods;
import tingtel.app.Methods.MyApplication;
import tingtel.app.Services.UpdateAirtimeNotification;
import tingtel.app.ViewModels.BalanceViewModel;
import tingtel.app.ViewModels.TransferAirtimeViewModel;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Methods  methodsClass = new Methods();

    RemoteViews notificationlayout;
    NotificationManager Nmanager;
    NotificationCompat.Builder builder;

    BalanceViewModel balanceViewModel;
    TransferAirtimeViewModel transferViewModel;

    MyApplication globalVariable;

    Fragment mainfragment, transferfragment, settingsfragment;



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

        ShowAirtimeNotification();

       // balanceViewModel.setCurrentAirtimeBalanceSim1("nougat");



        balanceViewModel.getCurrentAirtimeBalanceSim1().observe(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String balance) {


                //This is running multiple times
                Toast.makeText(MainActivity.this, "balance Airtime Sim11 "+balance, Toast.LENGTH_SHORT).show();



            }

        });



    }

    private void initObjects() {
        LocalBroadcastManager.getInstance(this).registerReceiver(UssdReceiver, new IntentFilter("TintelIntentMessage"));
      //  Toast.makeText(this, "ussd receiver activated", Toast.LENGTH_SHORT).show();

        globalVariable = (MyApplication) this.getApplicationContext();

        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class);




//        balanceViewModel.getCurrentBalance().observe(MainActivity.this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String balance) {
//
//          //      Toast.makeText(MainActivity.this, balance, Toast.LENGTH_SHORT).show();
//
//
//            }
//        });

    }

    private void initViews() {

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

         mainfragment = new MainFragment();
         transferfragment = new TransferFragment();
         settingsfragment = new SettingsFragment();



    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    loadFragment(mainfragment);
                    return true;
                case R.id.navigation_beneficiary:
                    toolbar.setTitle("Transfer");
                    loadFragment(transferfragment);
                    return true;
                case R.id.navigation_settings:
                    toolbar.setTitle("Settings");
                    loadFragment(settingsfragment);
                    return true;

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
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
     //    LocalBroadcastManager.getInstance(this).unregisterReceiver(UssdReceiver);

    }

    @Override
    public void onDestroy() {

     //   LocalBroadcastManager.getInstance(this).unregisterReceiver(UssdReceiver);
        super.onDestroy();
    }



    private BroadcastReceiver SmsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("SmsMessage")) {

             //   Toast.makeText(MainActivity.this, "hhhh", Toast.LENGTH_SHORT).show();
                final String message = intent.getStringExtra("message");
                final String senderNum = intent.getStringExtra("senderNum");

                String servicename = "";
                String simname = "";
                String ServiceType = "";
                int serviceLogo = R.drawable.ic_launcher_background;
                String simiccid = globalVariable.getIccid();





                if (message.toLowerCase().contains("free sms to 9mobile numbers")){
                    return;
                }
//                if (senderNum.equalsIgnoreCase("131") || senderNum.equalsIgnoreCase("mtn n")){
//                    return;
//                }


                //Airtel Data Balance
                if((senderNum.equalsIgnoreCase("Glo")) && (message.toLowerCase().contains(""))) {

                    servicename = "Data Balance";
                    simname = "9mobile";
                    serviceLogo = R.drawable.airtel_logo;

                    Log.e("logmessage", "supposed to receive here");
                    Log.e("logmessage", message);
                    //   Toast.makeText(context, "mainactivity sms", Toast.LENGTH_SHORT).show();

                    //    CheckSender(senderNum, message);


                } else if ((senderNum.equalsIgnoreCase("9mobile")) && ((message.toLowerCase().contains("your data balance as at")) || (message.toLowerCase().contains("you have")) || (message.toLowerCase().contains("your monthly data")) || (message.toLowerCase().contains("valid till")) )) {

                   Toast.makeText(MainActivity.this, "bbbbn" + message, Toast.LENGTH_SHORT).show();
                    servicename = "Data Balance";
                    simname = "9Mobile";
                    serviceLogo = R.drawable.nmobile_logo;

                    ServiceType = "Data";

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();


                    Pattern pattern = Pattern.compile("\\d+[Mm][Bb]");
                    Matcher matcher = pattern.matcher(message);


                    Pattern pattern2 = Pattern.compile("[0123456789]+.[0123456789]+mb");
                    Matcher matcher2 = pattern2.matcher(message);

                    Pattern pattern3 = Pattern.compile("\\d[Mm][Bb]");
                    Matcher matcher3 = pattern3.matcher(message);


                    String balance = "";

                    if (matcher.find())
                    {
                        Toast.makeText(MainActivity.this, matcher.group(0), Toast.LENGTH_SHORT).show();
                        balance = matcher.group(0);

                    } else if (matcher2.find()) {
                        balance = matcher2.group(0);
                    }

                    else if (matcher3.find()) {
                        balance = matcher3.group(0);
                    }

                    else {
                        Toast.makeText(MainActivity.this, "not found" + message, Toast.LENGTH_SHORT).show();
                        return;
                    }



                    updateBalance(balance);
                    saveHistory(balance, simname, message, R.drawable.nmobile_logo, ServiceType);;

                }
                else if ((senderNum.equalsIgnoreCase("9mobile")) && (message.toLowerCase().contains("main bal:"))) {

                    Toast.makeText(MainActivity.this, "bbbbn" + message, Toast.LENGTH_SHORT).show();
                    servicename = "Airtime Balance";
                    simname = "9Mobile";
                    serviceLogo = R.drawable.nmobile_logo;

                    ServiceType = "Airtime";

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();


                    Pattern pattern = Pattern.compile("N\\s\\d+.\\d+");
                    Matcher matcher = pattern.matcher(message);




                    String balance = "";

                    if (matcher.find())
                    {
                        Toast.makeText(MainActivity.this, matcher.group(0), Toast.LENGTH_SHORT).show();
                        balance = matcher.group(0);

                    }  else {
                        Toast.makeText(MainActivity.this, "not found" + message, Toast.LENGTH_SHORT).show();
                        return;
                    }



                    updateBalance(balance);
                    saveHistory(balance, simname, message, R.drawable.nmobile_logo, ServiceType);

                }

                else if ((senderNum.equalsIgnoreCase("Airtel")) && (message.toLowerCase().contains("Your have"))) {


                    servicename = "Data Balance";
                    simname = "Airtel NG";
                    serviceLogo = R.drawable.nmobile_logo;

                    ServiceType = "Data";

                    Log.e("logmessage", "supposed to receive here");
                    Log.e("logmessage", message);

                }


                else if ((senderNum.equalsIgnoreCase("Airtel")) && (message.toLowerCase().contains("you do not have an active data plan"))) {


                    Toast.makeText(MainActivity.this, "vvf", Toast.LENGTH_SHORT).show();
                    servicename = "Data Balance";
                    simname = "Airtel";

                    ServiceType = "Data";




                    String balance = "0 Mb";


                    updateBalance(balance);
                    saveHistory(balance, simname, message, R.drawable.airtel_logo, ServiceType);;

                }

                else {
                    Log.e("logmessage", "wrong message");
                    return;
                }




                Log.e("logmessage", simiccid);


                Intent intent1 = new Intent(MainActivity.this, ShowMessage.class);
                intent1.putExtra("message", message);
                intent1.putExtra("senderNum", senderNum);

            } else {
                Toast.makeText(MainActivity.this, "ddddk", Toast.LENGTH_SHORT).show();
                   Log.e("sms", "not received");

              //  return;
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

    public void checkPopupandSave(String message) {


        globalVariable = (MyApplication) this.getApplicationContext();
        String ussdservice = "";
        int servicelogo;
        String simname;
        String ServiceName = "";
        String balance = "";
        String ServiceType = "";

        ussdservice = globalVariable.getUssdservice();

        if (ussdservice == null) {
            return;
        }

        if (ussdservice.equalsIgnoreCase("mtn-airtime")){
            servicelogo = R.drawable.mtn_logo;
            simname = "Mtn Ng";
            ServiceName = "Mtn Balance";
            ServiceType = "Airtime";

            try {
                Pattern pattern = Pattern.compile("N\\d+.\\d+");
                Matcher matcher = pattern.matcher(message);
                if (matcher.find())
                {
                    Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show();
                    balance = matcher.group(0);
                    updateBalance(balance);
                    saveHistory(balance, simname, message, servicelogo, ServiceType);;
                } else {
                    Toast.makeText(this, "not foundjj", Toast.LENGTH_SHORT).show();
                }

                globalVariable.setUssdservice("");

            } catch (Exception e) {
                Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
            }

        }  else if (ussdservice.equalsIgnoreCase("glo-airtime")) {
            servicelogo = R.drawable.glo_logo;
            simname = "Glo Ng";
            ServiceName = "Glo Balance";
            ServiceType = "Airtime";
            globalVariable.setUssdservice("");

        } else if (ussdservice.equalsIgnoreCase("airtel-airtime")) {

            servicelogo = R.drawable.airtel_logo;
            simname = "Airtel Ng";
            ServiceName = "Balance Balance";
            ServiceType = "Airtime";
            Pattern pattern = Pattern.compile("N\\d+.\\d+");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find())
            {
                Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show();
                balance = matcher.group(0);
                updateBalance(balance);
                saveHistory(balance, simname, message, servicelogo, ServiceType);;
            } else {
                Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
            }

            globalVariable.setUssdservice("");

        } else if (ussdservice.equalsIgnoreCase("9mobile-airtime")) {

            servicelogo = R.drawable.nmobile_logo;
            simname = "9mobile";
            ServiceName = "Balance Balance";
            ServiceType = "Airtime";
            Pattern pattern = Pattern.compile("N \\d+.\\d+");
            Matcher matcher = pattern.matcher(message);

            Pattern pattern2 = Pattern.compile("N \\d+.\\d+");
            Matcher matcher2 = pattern.matcher(message);

            if (matcher.find())
            {
                Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show();
                balance = matcher.group(0);
                updateBalance(balance);
                saveHistory(balance, simname, message, servicelogo, ServiceType);
            } else {
                Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
            }

            globalVariable.setUssdservice("");

        } else if (ussdservice.equalsIgnoreCase("mtn-data")) {

            servicelogo = R.drawable.mtn_logo;
            simname = "Mtn Ng";
            ServiceName = "Data Balance";
            ServiceType = "Data";
            Pattern pattern = Pattern.compile("\\d+.\\d+MB");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find())
            {
                Toast.makeText(this, matcher.group(0), Toast.LENGTH_SHORT).show();
                balance = matcher.group(0);
                Toast.makeText(this, "This matcher: "+matcher.group(0),Toast.LENGTH_SHORT).show();
                updateBalance(balance);
                saveHistory(balance, simname, message, servicelogo, ServiceType);
            } else {
                Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
            }

            globalVariable.setUssdservice("");

        } else if (ussdservice.equalsIgnoreCase("glo-data")) {

            servicelogo = R.drawable.glo_logo;
            simname = "Glo Ng";
            ServiceName = "Data Balance";
            ServiceType = "Data";

            globalVariable.setUssdservice("");
            updateBalance(balance);
        }else if (ussdservice.equalsIgnoreCase("airtel-data")) {

            servicelogo = R.drawable.airtel_logo;
            simname = "Airtel Ng";
            ServiceName = "Data Balance";
            ServiceType = "Data";


        }else if (ussdservice.equalsIgnoreCase("9mobile-data")) {

            servicelogo = R.drawable.nmobile_logo;
            simname = "9mobile";
            ServiceName = "Data Balance";
            ServiceType = "Data";


        } else if (ussdservice.equalsIgnoreCase("mtn-phone")) {

            servicelogo = R.drawable.mtn_logo;
            simname = "Mtn Ng";
            ServiceName = "Phone Number";
            ServiceType = "";

        } else if (ussdservice.equalsIgnoreCase("glo-phone")) {

            servicelogo = R.drawable.glo_logo;
            simname = "Glo Ng";
            ServiceName = "Phone Number";
            ServiceType = "";

        }  else if (ussdservice.equalsIgnoreCase("airtel-phone")) {

            servicelogo = R.drawable.airtel_logo;
            simname = "Airtel Ng";
            ServiceName = "Phone Number";
            ServiceType = "";

        }  else if (ussdservice.equalsIgnoreCase("9mobile-phone")) {

            servicelogo = R.drawable.nmobile_logo;
            simname = "9mobile";
            ServiceName = "Phone Number";
            ServiceType = "";

        }   else {

           // servicelogo = R.drawable.history;
            simname = "";
            ServiceName = "";
            ServiceType = "";
            globalVariable.setUssdservice("");
            return;
        }









    }

    private void saveHistory(String balance, String simname, String message, int servicelogo, String ServiceType) {

        String simiccid = globalVariable.getIccid();
        Log.e("logmessage", simiccid + balance);

        String Amount =  balance.replaceAll("[^0-9.]", "");

        Float fAmount = Float.valueOf(Amount.trim());


        Toast.makeText(this, "amount is " + fAmount, Toast.LENGTH_SHORT).show();
         methodsClass.SaveAirtimeOrData(getApplicationContext(), fAmount, simiccid,  simname, balance, servicelogo, ServiceType);





    }



    private void updateBalance(String balance) {


        String clickedItem = globalVariable.getClickedItem();

        FragmentManager fm = getSupportFragmentManager();
        MainFragment fragment =  (MainFragment) fm.findFragmentById(R.id.frame_container);

       

        if (clickedItem.equalsIgnoreCase("Sim1Airtime")) {

            fragment.changeSim1AirtimeTextView(balance);


        } else if (clickedItem.equalsIgnoreCase("Sim2Airtime")) {

            fragment.changeSim2AirtimeTextView(balance);

        }  else if (clickedItem.equalsIgnoreCase("Sim1Data")) {

            fragment.changeSim1DataTextView(balance);

        }  else if (clickedItem.equalsIgnoreCase("Sim2Data")) {

            fragment.changeSim2DataTextView(balance);
        } else {
            fragment.changeSim2DataTextView(balance);
                  Toast.makeText(MainActivity.this, "This is else", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(MainActivity.this, "Click1" + globalVariable.getClickedItem() + globalVariable.getUssdservice() + balance, Toast.LENGTH_SHORT).show();

        globalVariable.setClickedItem("");
    }










    private void ShowAirtimeNotification() {

        //very important to create a notification channel in android 8 and above
        CreateNotificationChannel();


        notificationlayout = new RemoteViews(getPackageName(), R.layout.notificationlayout);





        Intent checkBalanceIntent = new Intent(this, UpdateAirtimeNotification.class);
        checkBalanceIntent.setAction("update_airtime");
//  checkBalanceIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent checkBalancePendingIntent =
                PendingIntent.getBroadcast(this, 0, checkBalanceIntent, 0);


        Intent checkBalanceSim1Intent = new Intent(this, MainActivity.class);
        checkBalanceSim1Intent.putExtra("update_sim1_airtime", "update_sim1_airtime");
//        PendingIntent psim1 = pendingIntent.getActivity(this, 0, checkBalanceSim1Intent, 0);
        PendingIntent psim1 = PendingIntent.getBroadcast(this, 0, checkBalanceSim1Intent, 0);


        Intent checkBalanceSim2Intent = new Intent(this, MainActivity.class);
        checkBalanceSim2Intent.putExtra("update_sim2_airtime", "update_sim2_airtime");
        PendingIntent psim2 = PendingIntent.getActivity(this, 0, checkBalanceSim2Intent, 0);








        notificationlayout.setOnClickPendingIntent(R.id.btnSim1Airtime, psim1);

        notificationlayout.setOnClickPendingIntent(R.id.btnSim2Airtime, psim2);


        Nmanager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(this, "01")
                .setSmallIcon(R.drawable.history)
                .setCustomContentView(notificationlayout)
                .setOngoing(true)
                .setChannelId("01")
                .setAutoCancel(true);


        Nmanager.notify(01, builder.build());
    }

    private void CreateNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Android 8 Notification Channel";
            String description = "Android 8 Notification Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("01", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

}
