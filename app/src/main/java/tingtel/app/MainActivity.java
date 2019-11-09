package tingtel.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tingtel.app.Fragments.HistoryFragment;
import tingtel.app.Fragments.MainFragment;
import tingtel.app.Methods.Methods;
import tingtel.app.Methods.MyApplication;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Methods  methodsClass = new Methods();

    MyApplication globalVariable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "HOME");
        adapter.addFragment(new HistoryFragment(), "HISTORY");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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



                if((senderNum.equalsIgnoreCase("Airtel")) && (message.toLowerCase().contains("your balances are"))) {

                    servicename = "Data Balance";
                    simname = "9mobile";
                    serviceLogo = R.drawable.airtel_logo;

                    Log.e("logmessage", "supposed to receive here");
                    Log.e("logmessage", message);
                    //   Toast.makeText(context, "mainactivity sms", Toast.LENGTH_SHORT).show();

                    //    CheckSender(senderNum, message);


                } else if ((senderNum.equalsIgnoreCase("9mobile")) && (message.toLowerCase().contains("you have"))) {


                    servicename = "Data Balance";
                    simname = "Airtel NG";
                    serviceLogo = R.drawable.nmobile_logo;

                    Log.e("logmessage", "supposed to receive here");
                    Log.e("logmessage", message); }

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
                startActivity(intent1);


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
              Toast.makeText(context, "received" + message, Toast.LENGTH_SHORT).show();

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

        if (ussdservice.equalsIgnoreCase("mtn-airtime")){
            servicelogo = R.drawable.mtn_logo;
            simname = "Mtn Ng";
            ServiceName = "Mtn Airtime";
        }  else if (ussdservice.equalsIgnoreCase("glo-airtime")) {
            servicelogo = R.drawable.glo_logo;
            simname = "Glo Ng";
            ServiceName = "Glo Airtime";

        } else if (ussdservice.equalsIgnoreCase("airtel-airtime")) {

            servicelogo = R.drawable.airtel_logo;
            simname = "Airtel Ng";
            ServiceName = "Airtime Balance";

        } else if (ussdservice.equalsIgnoreCase("9mobile-airtime")) {

            servicelogo = R.drawable.nmobile_logo;
            simname = "9mobile";
            ServiceName = "Airtime Balance";

        } else if (ussdservice.equalsIgnoreCase("mtn-data")) {

            servicelogo = R.drawable.mtn_logo;
            simname = "Mtn Ng";
            ServiceName = "Data Balance";

        } else if (ussdservice.equalsIgnoreCase("glo-data")) {

            servicelogo = R.drawable.glo_logo;
            simname = "Glo Ng";
            ServiceName = "Data Balance";

        }else if (ussdservice.equalsIgnoreCase("airtel-data")) {

            servicelogo = R.drawable.airtel_logo;
            simname = "Airtel Ng";
            ServiceName = "Data Balance";

        }else if (ussdservice.equalsIgnoreCase("9mobile-data")) {

            servicelogo = R.drawable.nmobile_logo;
            simname = "9mobile";
            ServiceName = "Data Balance";

        } else if (ussdservice.equalsIgnoreCase("mtn-phone")) {

            servicelogo = R.drawable.mtn_logo;
            simname = "Mtn Ng";
            ServiceName = "Phone Number";

        } else if (ussdservice.equalsIgnoreCase("glo-phone")) {

            servicelogo = R.drawable.glo_logo;
            simname = "Glo Ng";
            ServiceName = "Phone Number";

        }  else if (ussdservice.equalsIgnoreCase("airtel-phone")) {

            servicelogo = R.drawable.airtel_logo;
            simname = "Airtel Ng";
            ServiceName = "Phone Number";

        }  else if (ussdservice.equalsIgnoreCase("9mobile-phone")) {

            servicelogo = R.drawable.nmobile_logo;
            simname = "9mobile";
            ServiceName = "Phone Number";

        }



        else {

           // servicelogo = R.drawable.history;
            simname = "";
            ServiceName = "";
            return;
        }

        String SimName = "";
        String simiccid = "";

        methodsClass.SaveAirtimeOrData(getApplicationContext(), Integer.parseInt("4323"), simiccid,  SimName, message, servicelogo);
        globalVariable.setUssdservice("");




    }


}
