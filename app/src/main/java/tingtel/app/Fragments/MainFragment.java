package tingtel.app.Fragments;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;
import tingtel.app.BalanceActivity;
import tingtel.app.ListUssdActivity;
import tingtel.app.MainActivity;
import tingtel.app.Methods.AppDatabase;
import tingtel.app.Methods.Methods;
import tingtel.app.Methods.MyApplication;
import tingtel.app.Models.Balance;
import tingtel.app.R;
import tingtel.app.Services.USSDCODEService;
import tingtel.app.Services.UpdateAirtimeNotification;
import tingtel.app.ViewModels.BalanceViewModel;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.content.Context.NOTIFICATION_SERVICE;

public class MainFragment extends Fragment {

    TextView tvSim1Airtime, tvSim2Airtime, tvSim1Data, tvSim2Data, tvSim1Network, tvSim2Network;
    ImageView refSim1Airtime, refSim2Airtime, refSim1Data, refSim2Data;
    LinearLayout Sim1Layout, Sim2Layout;
    private CircleImageView networkLogoSim1, networkLogoSim2;
    private Button customerSupportButton1, customerSupportButton2;
    FancyButton btn_sim1UssdCodes, btn_sim2UssdCodes;
    CardView cd_AirtimeSim1, cd_AirtimeSim2, cd_DataSim1, cd_DataSim2;

    RemoteViews notificationlayout;
    NotificationManager Nmanager;
    NotificationCompat.Builder builder;

    Methods methodsClass = new Methods();

    int REQUEST_PHONE_STATE = 101;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean accessibilityStatus;

    String SimStatus = "SIMSTATUS";

    MyApplication globalVariable;

    private BalanceViewModel balancemodel;


    View view;
    AppDatabase dbb;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_main, container, false);

        initObjects(view);
        initViews(view);

        //  populateViews(view);

        getSimcardDetails();
        loadExistingValues();

        ShowAirtimeNotification();

        balancemodel.getCurrentAirtimeBalanceSim1().observe(Objects.requireNonNull(getActivity()), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String balance) {
                tvSim1Airtime.setText(balance);
                updateNotification(balance, "Sim1Airtime");
            }
        });

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void changeSim1AirtimeTextView(String balance) {

        balancemodel.getCurrentAirtimeBalanceSim1().observe(Objects.requireNonNull(getActivity()), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String balance) {
                tvSim1Airtime.setText(balance);
                updateNotification(balance, "Sim1Airtime");
            }
        });


        // tvSim1Airtime.setText(balance);


    }

    public void changeSim2AirtimeTextView(String balance) {

        tvSim2Airtime.setText(balance);
    }

    public void changeSim1DataTextView(String balance) {

        tvSim1Data.setText(balance);
    }

    public void changeSim2DataTextView(String balance) {

        tvSim2Data.setText(balance);
    }

    public void changenothing(String balance) {

        tvSim2Data.setText("nothing");
    }

    private void loadExistingValues() {
        final String Sim1Iccid = sharedPreferences.getString("SIM1ICCID", "");
        final String Sim2Iccid = sharedPreferences.getString("SIM2ICCID", "");

        try {
            balancemodel.setCurrentAirtimeBalanceSim1(dbb.balanceDao().getLastAirtimeOrData(Sim1Iccid, "Airtime").getMessage());
        } catch (Exception ex) {
            Log.e("logmessage", "Exception CurrentAirtimeBalanceSim1 " + ex.getMessage());
        }


        try {
            balancemodel.setCurrentAirtimeBalanceSim2(dbb.balanceDao().getLastAirtimeOrData(Sim2Iccid, "Airtime").getMessage());
        } catch (Exception ex) {
            Log.e("logmessage", "Exception CurrentAirtimeBalanceSim " + ex.getMessage());
        }


        try {
            balancemodel.setCurrentDataBalanceSim1(dbb.balanceDao().getLastAirtimeOrData(Sim1Iccid, "Data").getMessage());
        } catch (Exception ex) {
            Log.e("logmessage", "Exception CurrentDataBalanceSim1 " + ex.getMessage());
        }


        try {

            balancemodel.setCurrentDataBalanceSim2(dbb.balanceDao().getLastAirtimeOrData(Sim2Iccid, "Data").getMessage());
        } catch (Exception ex) {
            Log.e("logmessage", "Exception CurrentDataBalanceSim " + ex.getMessage());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getSimcardDetails() {
        methodsClass.getCarrierOfSim(Objects.requireNonNull(getActivity()));

        String NoOfSIm = sharedPreferences.getString(SimStatus, "");
        String Sim1Network = sharedPreferences.getString("SIM1NAME", "");
        String Sim2Network = sharedPreferences.getString("SIM2NAME", "");

        //  Toast.makeText(getActivity(), "fall "+ Sim1Network + Sim2Network + NoOfSIm, Toast.LENGTH_SHORT).show();

        if (Objects.requireNonNull(NoOfSIm).equalsIgnoreCase("NOSIM")) {


        } else if (NoOfSIm.equalsIgnoreCase("SIM1")) {
            switch (Sim1Network) {
                case "MTN NG":
                    networkLogoSim1.setImageDrawable(getResources().getDrawable(R.drawable.mtn_logo));
                    break;
                case "Airtel NG":
                    networkLogoSim1.setImageDrawable(getResources().getDrawable(R.drawable.airtel_logo));
                    break;
            }
            Sim1Layout.setVisibility(View.VISIBLE);
            Sim2Layout.setVisibility(View.GONE);
            //tvSim1Network.setText(Sim1Network);

        } else if (NoOfSIm.equalsIgnoreCase("SIM1SIM2")) {


            switch (Sim1Network) {
                case "MTN NG":
                    networkLogoSim1.setImageDrawable(getResources().getDrawable(R.drawable.mtn_logo));
                    break;
                case "Airtel NG":
                    networkLogoSim2.setImageDrawable(getResources().getDrawable(R.drawable.airtel_logo));
                    break;
                case "9mobile":
                    networkLogoSim2.setImageDrawable(getResources().getDrawable(R.drawable.nmobile_logo));
                    break;
                case "Etisalat":
                    networkLogoSim2.setImageDrawable(getResources().getDrawable(R.drawable.nmobile_logo));
                    break;
                case "Glo Ng":
                    networkLogoSim2.setImageDrawable(getResources().getDrawable(R.drawable.glo_logo));
                    break;
            }
            switch (Sim2Network) {
                case "MTN NG":
                    networkLogoSim1.setImageDrawable(getResources().getDrawable(R.drawable.mtn_logo));
                    break;
                case "Airtel NG":
                    networkLogoSim2.setImageDrawable(getResources().getDrawable(R.drawable.airtel_logo));
                    break;
                case "9mobile":
                    networkLogoSim2.setImageDrawable(getResources().getDrawable(R.drawable.nmobile_logo));
                    break;
                case "Etisalat":
                    networkLogoSim2.setImageDrawable(getResources().getDrawable(R.drawable.nmobile_logo));
                    break;
                case "Glo Ng":
                    networkLogoSim2.setImageDrawable(getResources().getDrawable(R.drawable.glo_logo));
                    break;
            }
            Sim1Layout.setVisibility(View.VISIBLE);
            Sim2Layout.setVisibility(View.VISIBLE);
            //tvSim1Network.setText(Sim1Network);
            //tvSim2Network.setText(Sim2Network);

        }


    }

    private void initObjects(View view) {
        sharedPreferences = getActivity().getSharedPreferences("TingTelPref", 0);
        editor = sharedPreferences.edit();

        globalVariable = (MyApplication) getActivity().getApplicationContext();

        balancemodel = ViewModelProviders.of(getActivity()).get(BalanceViewModel.class);


    }

//    private void updateBalanceView(String balance) {
//
//       // Toast.makeText(getActivity(), "Click0" + globalVariable.getClickedItem() + globalVariable.getUssdservice() + balance, Toast.LENGTH_SHORT).show();
//
//        String clickedItem = globalVariable.getClickedItem();
//
//        if (clickedItem.equalsIgnoreCase("Sim1Airtime")) {
//
//            tvSim1Airtime.setText(balance);
//
//        } else if (clickedItem.equalsIgnoreCase("Sim2Airtime")) {
//
//            tvSim2Airtime.setText(balance);
//
//        }  else if (clickedItem.equalsIgnoreCase("Sim1Data")) {
//
//            tvSim1Data.setText(balance);
//
//        }  else if (clickedItem.equalsIgnoreCase("Sim2Data")) {
//            tvSim2Data.setText(balance);
//        } else {
//
//          //  Toast.makeText(getActivity(), "This is else", Toast.LENGTH_SHORT).show();
//        }
//       globalVariable.setClickedItem("");
//    }

    private void initViews(View view) {
        dbb = AppDatabase.getInstance(getActivity());

        networkLogoSim1 = view.findViewById(R.id.network_logo_image);
        networkLogoSim2 = view.findViewById(R.id.network_logo_image1);

        tvSim1Airtime = (TextView) view.findViewById(R.id.tv_AirtimeSim1);
        tvSim1Data = (TextView) view.findViewById(R.id.tv_DataSim1);
        //tvSim1Network = (TextView) view.findViewById(R.id.tv_sim1network);
        tvSim2Airtime = (TextView) view.findViewById(R.id.tv_AirtimeSim2);
        tvSim2Data = (TextView) view.findViewById(R.id.tv_DataSim2);
        //tvSim2Network = (TextView) view.findViewById(R.id.tv_sim2network);

        refSim1Airtime = (ImageView) view.findViewById(R.id.ref_AirtimeSim1);
        refSim1Data = (ImageView) view.findViewById(R.id.ref_DataSim1);
        refSim2Airtime = (ImageView) view.findViewById(R.id.ref_AirtimeSim2);
        refSim2Data = (ImageView) view.findViewById(R.id.ref_DataSim2);

        Sim1Layout = (LinearLayout) view.findViewById(R.id.Sim1Layout);
        Sim2Layout = (LinearLayout) view.findViewById(R.id.Sim2Layout);

        btn_sim1UssdCodes = (FancyButton) view.findViewById(R.id.btn_sim1UssdCodes);
        btn_sim2UssdCodes = (FancyButton) view.findViewById(R.id.btn_sim2UssdCodes);

        cd_AirtimeSim1 = (CardView) view.findViewById(R.id.cd_AirtimeSim1);
        cd_AirtimeSim2 = (CardView) view.findViewById(R.id.cd_AirtimeSim2);
        cd_DataSim1 = (CardView) view.findViewById(R.id.cd_DataSim1);
        cd_DataSim2 = (CardView) view.findViewById(R.id.cd_DataSim2);

        customerSupportButton1 = view.findViewById(R.id.supportButtonOne);
        customerSupportButton2 = view.findViewById(R.id.supportButtonTwo);

        tvSim1Airtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SimIccid, SimName;
                SimIccid = sharedPreferences.getString("SIM1ICCID", "");
                SimName = sharedPreferences.getString("SIM1NAME", "");

                Intent intent = new Intent(getActivity(), BalanceActivity.class);
                intent.putExtra("SimIccid", SimIccid);
                intent.putExtra("Type", "Airtime");
                intent.putExtra("SimName", SimName);
                startActivity(intent);
            }
        });

        tvSim2Airtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SimIccid, SimName;
                SimIccid = sharedPreferences.getString("SIM2ICCID", "");
                SimName = sharedPreferences.getString("SIM2NAME", "");

                Intent intent = new Intent(getActivity(), BalanceActivity.class);
                intent.putExtra("SimIccid", SimIccid);
                intent.putExtra("Type", "Airtime");
                intent.putExtra("SimName", SimName);
                startActivity(intent);
            }
        });


        tvSim1Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SimIccid, SimName;
                SimIccid = sharedPreferences.getString("SIM1ICCID", "");
                SimName = sharedPreferences.getString("SIM1NAME", "");

                Intent intent = new Intent(getActivity(), BalanceActivity.class);
                intent.putExtra("SimIccid", SimIccid);
                intent.putExtra("Type", "Data");
                intent.putExtra("SimName", SimName);
                startActivity(intent);
            }
        });


        tvSim2Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SimIccid, SimName;
                SimIccid = sharedPreferences.getString("SIM2ICCID", "");
                SimName = sharedPreferences.getString("SIM2NAME", "");
                ;

                Intent intent = new Intent(getActivity(), BalanceActivity.class);
                intent.putExtra("SimIccid", SimIccid);
                intent.putExtra("Type", "Data");
                intent.putExtra("SimName", SimName);
                startActivity(intent);
            }
        });


        refSim1Airtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request airtime
                String SimName = sharedPreferences.getString("SIM1NAME", "");
                String SimIccid = sharedPreferences.getString("SIM1ICCID", "");
                requestAirtime(0, SimName, SimIccid);

                globalVariable.setClickedItem("Sim1Airtime");

            }
        });


        refSim2Airtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request airtime
                String SimName = sharedPreferences.getString("SIM2NAME", "");
                String SimIccid = sharedPreferences.getString("SIM2ICCID", "");
                requestAirtime(1, SimName, SimIccid);

                globalVariable.setClickedItem("Sim2Airtime");

            }
        });


        refSim1Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request airtime
                String SimName = sharedPreferences.getString("SIM1NAME", "");
                String SimIccid = sharedPreferences.getString("SIM1ICCID", "");
                requestData(0, SimName, SimIccid);

                globalVariable.setClickedItem("Sim1Data");

            }
        });


        refSim2Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request airtime
                String SimName = sharedPreferences.getString("SIM2NAME", "");
                String SimIccid = sharedPreferences.getString("SIM2ICCID", "");
                requestData(1, SimName, SimIccid);

                globalVariable.setClickedItem("Sim2Data");

            }
        });


        btn_sim1UssdCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SimName = sharedPreferences.getString("SIM1NAME", "");

                Intent intent = new Intent(getActivity(), ListUssdActivity.class);
                intent.putExtra("NetworkName", SimName);
                intent.putExtra("SimNo", 0);
                Toast.makeText(getActivity(), "SIm Name is" + SimName, Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });


        btn_sim2UssdCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SimName = sharedPreferences.getString("SIM2NAME", "");

                Intent intent = new Intent(getActivity(), ListUssdActivity.class);
                intent.putExtra("NetworkName", SimName);
                intent.putExtra("SimNo", 1);
                startActivity(intent);

            }
        });


        customerSupportButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Support One clicked", Toast.LENGTH_SHORT).show();
            }
        });
        customerSupportButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Support Two clicked", Toast.LENGTH_SHORT).show();
            }
        });


        balancemodel.getCurrentAirtimeBalanceSim1().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String balance) {


                //This is running multiple times
                Toast.makeText(getContext(), "balance Airtime Sim1 " + balance, Toast.LENGTH_SHORT).show();
                tvSim1Airtime.setText(balance);
                updateNotification(balance, "Sim1Airtime");


            }

        });


        balancemodel.getCurrentAirtimeBalanceSim2().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String balance) {


                //This is running multiple times
                Toast.makeText(getContext(), "balance Airtime Sim2 " + balance, Toast.LENGTH_SHORT).show();
                tvSim2Airtime.setText(balance);
                updateNotification(balance, "Sim2Airtime");


            }
        });

        balancemodel.getCurrentDataBalanceSim1().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String balance) {


                //This is running multiple times
                Toast.makeText(getContext(), "balance Data Sim1 " + balance, Toast.LENGTH_SHORT).show();
                tvSim1Data.setText(balance);
                updateNotification(balance, "Sim1Data");


            }
        });

        balancemodel.getCurrentDataBalanceSim2().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String balance) {


                //This is running multiple times
                Toast.makeText(getActivity(), "balance Data Sim2 " + balance, Toast.LENGTH_SHORT).show();
                tvSim2Data.setText(balance);
                updateNotification(balance, "Sim2Data");


            }
        });


    }

    private void updateNotification(String balance, String status) {

        try {


            if (status.equalsIgnoreCase("Sim1Airtime")) {
                notificationlayout.setTextViewText(R.id.tv_airtime_sim1, "Airtime: " + balance);
            } else if (status.equalsIgnoreCase("Sim2Airtime")) {
                notificationlayout.setTextViewText(R.id.tv_airtime_sim2, "Airtime: " + balance);
            } else if (status.equalsIgnoreCase("Sim1Data")) {
                notificationlayout.setTextViewText(R.id.tv_data_sim1, "Airtime: " + balance);
            } else if (status.equalsIgnoreCase("Sim2Data")) {
                notificationlayout.setTextViewText(R.id.tv_data_sim2, "Airtime: " + balance);

            }
        } catch (Exception e) {

        }

    }


    private void requestAirtime(int sim, String SimName, String SimIccid) {
        //check if android version is below android o
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.O) {
            //check Accessibility Service Status whether enable or not
            if (isAccessServiceEnabled(getContext(), USSDCODEService.class) == false) {
                checkAccessibilityServiceStatus();
                return;
            }

        }

        // Toast.makeText(getActivity(), "step 1", Toast.LENGTH_SHORT).show();

        int networklogo;
        String ussdservice;
        int NoOfSim = sharedPreferences.getInt("Simno", 0);


        //Toast.makeText(getActivity(), "ccc" + SimIccid + SimName, Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //ask permission

            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_PHONE_STATE, RECEIVE_SMS, CALL_PHONE}, REQUEST_PHONE_STATE);

            //     Toast.makeText(getActivity(), "ask permission", Toast.LENGTH_SHORT).show();
        } else {

            //  Toast.makeText(getActivity(), "dont ask permission", Toast.LENGTH_SHORT).show();

            if (SimName.equalsIgnoreCase("")
                    || (SimIccid.equalsIgnoreCase(""))) {

                //    Toast.makeText(getActivity(), "No Sim Card Detected", Toast.LENGTH_SHORT).show();

            } else {

                String ussd;
                String Network = SimName;

                Toast.makeText(getActivity(), SimName, Toast.LENGTH_SHORT).show();

                if (Network.toLowerCase().contains("9mobile") || Network.toLowerCase().contains("etisalat")) {
                    ussd = "*232#";
                    networklogo = R.drawable.nmobile_logo;
                    ussdservice = "9mobile-airtime";
                } else if (Network.toLowerCase().contains("glo")) {
                    ussd = "*124*1#";
                    networklogo = R.drawable.glo_logo;
                    ussdservice = "glo-airtime";
                } else if (Network.toLowerCase().contains("mtn")) {
                    ussd = "*556#";
                    networklogo = R.drawable.mtn_logo;
                    ussdservice = "mtn-airtime";
                } else if (Network.toLowerCase().contains("airtel")) {
                    ussd = "*123#";
                    networklogo = R.drawable.airtel_logo;
                    ussdservice = "airtel-airtime";
                } else {
                    ussd = null;
                    networklogo = R.drawable.airtel_logo;
                    ussdservice = "";
                    //     Toast.makeText(getActivity(), "else else", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(ussd == null)) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        //   Toast.makeText(getActivity(), "Run Usssd =" + ussd, Toast.LENGTH_SHORT).show();
                        globalVariable.setUssdservice(ussdservice);
                        globalVariable.setIccid(SimIccid);
                        Toast.makeText(getActivity(), "ussdservice" + ussdservice, Toast.LENGTH_SHORT).show();
                        methodsClass.DialUssdCodeNewApi(getActivity(), ussd, getActivity(), sim, ussdservice, networklogo);


                    } else {
                        //  Toast.makeText(getActivity(), "Run Usssd =" + ussd, Toast.LENGTH_SHORT).show();


                        methodsClass.DialUssdCode(getActivity(), ussd, getActivity(), sim);
                        globalVariable.setUssdservice(ussdservice);
                        globalVariable.setIccid(SimIccid);
                    }
                }
            }
        }


    }


    private void requestData(int sim, String SimName, String SimIccid) {


        //check if android version is below android o
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.O) {
            //check Accessibility Service Status whether enable or not
            if (isAccessServiceEnabled(getContext(), USSDCODEService.class) == false) {
                checkAccessibilityServiceStatus();
                return;
            }

        }

        //  Toast.makeText(getActivity(), "step 1", Toast.LENGTH_SHORT).show();

        int networklogo;
        String ussdservice;
        int NoOfSim = sharedPreferences.getInt("Simno", 0);

        String Sim1Iccid = sharedPreferences.getString("SIM1ICCID", "");
        String Sim2Iccid = sharedPreferences.getString("SIM2ICCID", "");


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //ask permission

            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_PHONE_STATE, RECEIVE_SMS, CALL_PHONE}, REQUEST_PHONE_STATE);

            Toast.makeText(getActivity(), "ask permission", Toast.LENGTH_SHORT).show();
        } else {

            //    Toast.makeText(getActivity(), "dont ask permission", Toast.LENGTH_SHORT).show();

            if (SimName.equalsIgnoreCase("")
                    || (Sim1Iccid.equalsIgnoreCase(""))) {

                Toast.makeText(getActivity(), "No Sim Card Detected", Toast.LENGTH_SHORT).show();

            } else {

                String ussd;
                String Network = SimName;

                //   Toast.makeText(getActivity(), SimName, Toast.LENGTH_SHORT).show();

                if (Network.toLowerCase().contains("9mobile") || Network.toLowerCase().contains("etisalat")) {
                    ussd = "*228#";
                    networklogo = R.drawable.nmobile_logo;
                    ussdservice = "9mobile-data";
                } else if (Network.toLowerCase().contains("glo")) {
                    ussd = "*127*0#";
                    networklogo = R.drawable.glo_logo;
                    ussdservice = "glo-data";
                } else if (Network.toLowerCase().contains("mtn")) {
                    ussd = "*131*4#";
                    networklogo = R.drawable.mtn_logo;
                    ussdservice = "mtn-data";
                } else if (Network.toLowerCase().contains("airtel")) {
                    ussd = "*140#";
                    networklogo = R.drawable.airtel_logo;
                    ussdservice = "airtel-data";
                } else {
                    ussd = null;
                    networklogo = R.drawable.airtel_logo;
                    ussdservice = "";
                    // Toast.makeText(getActivity(), "else else", Toast.LENGTH_SHORT).show();
                }
                if (!(ussd == null)) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        globalVariable.setIccid(SimIccid);
                        methodsClass.DialUssdCodeNewApi(getActivity(), ussd, getActivity(), sim, ussdservice, networklogo);

                    } else {
                        methodsClass.DialUssdCode(getActivity(), ussd, getActivity(), sim);
                        globalVariable.setUssdservice(ussdservice);
                        globalVariable.setIccid(SimIccid);
                    }
                }
            }
        }


    }

    private void populateViews(View view) {

        int Simno = sharedPreferences.getInt("Simno", 0);
        List<Balance> balanceList = new ArrayList<>();

        for (int i = 1; i <= Simno; i++) {
            String iccid = sharedPreferences.getString("SIM" + Simno + "NAME", "");
            Balance balance = dbb.balanceDao().getLastAirtimeOrData(iccid, "balance");
            balanceList.add(balance);

        }

        if (balanceList.size() == 0) {

            Toast.makeText(getActivity(), "No Last Balance", Toast.LENGTH_SHORT).show();
        } else if (balanceList.size() == 1) {

            Toast.makeText(getActivity(), "1 sim has its last airtime detected", Toast.LENGTH_SHORT).show();
            tvSim1Airtime.setText(balanceList.get(0).getBalance() + "");

        } else if (balanceList.size() == 2) {

            Toast.makeText(getActivity(), "1 sim has its last airtime detected", Toast.LENGTH_SHORT).show();
            tvSim1Airtime.setText(balanceList.get(0).getBalance() + "");
            tvSim2Airtime.setText(balanceList.get(1).getBalance() + "");
        }


    }


    //To check Accessibility Service Status
    public boolean isAccessServiceEnabled(Context context, Class accessibilityServiceClass) {
        String prefString = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        return prefString != null && prefString.contains(context.getPackageName() + "/" + accessibilityServiceClass.getName());
    }


    private void checkAccessibilityServiceStatus() {
        //check if android version is less than android 0 (versions that dont have official ussd api)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {

            if (isAccessServiceEnabled(getContext(), USSDCODEService.class) == true) {

                accessibilityStatus = true;
                // Toast.makeText(MainActivity.this, "accessibility is true", Toast.LENGTH_SHORT).show();
                Log.e("logmessage", "accessibility is true");

            } else {
                accessibilityStatus = false;
                //Toast.makeText(MainActivity.this, "accessibility is false", Toast.LENGTH_SHORT).show();
                Log.e("logmessage", "accessibility is false");

                //request user to enable accesibilty service
                enableAccessibilityService();
                return;
            }
        } else {

        }
    }

    //method to enable accessibility service
    private void enableAccessibilityService() {


        // load the customized_dialog.xml layout and inflate to view
        LayoutInflater layoutinflater = LayoutInflater.from(getContext());
        View customizedUserView = layoutinflater.inflate(R.layout.customized_dialog, null);


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setView(customizedUserView);


        alert.setTitle("Tingtel");
        // alert.setMessage("Hello, We need you to enable accessibility settings for this app, so the app can read your ussd response popups and allow us serve you better");

        alert.setMessage("To Use Tingtel USSD App, Please Enable Accessibility Service in Settings");

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.dismiss();

            }
        });

        alert.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);


            }
        });


        alert.show();


        return;

    }

    private void ShowAirtimeNotification() {

        //very important to create a notification channel in android 8 and above
        CreateNotificationChannel();


        notificationlayout = new RemoteViews(getActivity().getPackageName(), R.layout.notificationlayout);


        Intent checkBalanceIntent = new Intent(getActivity(), UpdateAirtimeNotification.class);
        checkBalanceIntent.setAction("update_airtime");
//  checkBalanceIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent checkBalancePendingIntent =
                PendingIntent.getBroadcast(getActivity(), 0, checkBalanceIntent, 0);


        Intent checkBalanceSim1Intent = new Intent(getActivity(), MainActivity.class);
        checkBalanceSim1Intent.putExtra("update_sim1_airtime", "update_sim1_airtime");
//        PendingIntent psim1 = pendingIntent.getActivity(this, 0, checkBalanceSim1Intent, 0);
        PendingIntent psim1 = PendingIntent.getActivity(getActivity(), 0, checkBalanceSim1Intent, 0);


        Intent checkBalanceSim2Intent = new Intent(getActivity(), MainActivity.class);
        checkBalanceSim2Intent.putExtra("update_sim2_airtime", "update_sim2_airtime");
        PendingIntent psim2 = PendingIntent.getActivity(getActivity(), 0, checkBalanceSim2Intent, 0);


        updateNotificationTextViews();


        notificationlayout.setOnClickPendingIntent(R.id.btnSim1Airtime, psim1);

        notificationlayout.setOnClickPendingIntent(R.id.btnSim2Airtime, psim2);


        Nmanager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(getActivity(), "01")
                .setSmallIcon(R.drawable.history)
                .setCustomContentView(notificationlayout)
                .setOngoing(true)
                .setChannelId("01")
                .setAutoCancel(true);


        Nmanager.notify(01, builder.build());
    }

    public void updateNotificationTextViews() {

        final String Sim1Iccid = sharedPreferences.getString("SIM1ICCID", "");
        final String Sim2Iccid = sharedPreferences.getString("SIM2ICCID", "");

        try {
            notificationlayout.setTextViewText(R.id.tv_airtime_sim1, "Airtime: " + dbb.balanceDao().getLastAirtimeOrData(Sim1Iccid, "Airtime").getMessage());
        } catch (Exception ex) {
            Log.e("logmessage", "Exception CurrentAirtimeBalanceSim1 " + ex.getMessage());
        }
        try {

            notificationlayout.setTextViewText(R.id.tv_data_sim1, "Data: " + dbb.balanceDao().getLastAirtimeOrData(Sim1Iccid, "Data").getMessage());
        } catch (Exception ex) {
            Log.e("logmessage", "Exception CurrentDataBalanceSim1 " + ex.getMessage());
        }
        try {


            notificationlayout.setTextViewText(R.id.tv_airtime_sim2, "Airtime: " + dbb.balanceDao().getLastAirtimeOrData(Sim2Iccid, "Airtime").getMessage());
        } catch (Exception ex) {
            Log.e("logmessage", "Exception CurrentAirtimeBalanceSim2 " + ex.getMessage());
        }
        try {

            notificationlayout.setTextViewText(R.id.tv_data_sim2, "Data: " + dbb.balanceDao().getLastAirtimeOrData(Sim2Iccid, "Data").getMessage());
        } catch (Exception ex) {
            Log.e("logmessage", "Exception CurrentAirtimeBalanceSim2 " + ex.getMessage());
        }
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
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}