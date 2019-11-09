package tingtel.app.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tingtel.app.Methods.AppDatabase;
import tingtel.app.Methods.Methods;
import tingtel.app.Models.Airtime;
import tingtel.app.R;
import tingtel.app.Services.USSDCODEService;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_SMS;

public class MainFragment extends Fragment {

    TextView tvSim1Airtime, tvSim2Airtime, tvSim1Data, tvSim2Data;
    ImageView refSim1Airtime, refSim2Airtime, refSim1Data, refSim2Data;

    Methods methodsClass = new Methods();

    int REQUEST_PHONE_STATE = 101;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean accessibilityStatus;



    AppDatabase dbb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;

        view = inflater.inflate(R.layout.fragment_main, container, false);

        initObjects(view);
        initViews(view);

      //  populateViews(view);
        methodsClass.getCarrierOfSim(getActivity());

        return view;
    }

    private void initObjects(View view) {
        sharedPreferences = getActivity().getSharedPreferences("TingTelPref", 0);
        editor = sharedPreferences.edit();
    }

    private void initViews(View view) {

        dbb = AppDatabase.getInstance(getActivity());

        tvSim1Airtime = (TextView) view.findViewById(R.id.tv_AirtimeSim1);
        tvSim1Data = (TextView) view.findViewById(R.id.tv_DataSim1);
        tvSim2Airtime = (TextView) view.findViewById(R.id.tv_AirtimeSim2);
        tvSim2Data = (TextView) view.findViewById(R.id.tv_DataSim2);



        refSim1Airtime = (ImageView) view.findViewById(R.id.ref_AirtimeSim1);
        refSim1Data = (ImageView) view.findViewById(R.id.ref_DataSim1);
        refSim2Airtime = (ImageView) view.findViewById(R.id.ref_AirtimeSim2);
        refSim2Data = (ImageView) view.findViewById(R.id.ref_DataSim2);


        refSim1Airtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request airtime
                String SimName = sharedPreferences.getString("SIM1NAME", "");
                String SimIccid = sharedPreferences.getString("SIM1ICCID", "");
                requestAirtime(0, SimName, SimIccid);

            }
        });


        refSim2Airtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request airtime
                String SimName = sharedPreferences.getString("SIM2NAME", "");
                String SimIccid = sharedPreferences.getString("SIM2ICCID", "");
                requestAirtime(1, SimName, SimIccid);

            }
        });



        refSim1Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request airtime
                String SimName = sharedPreferences.getString("SIM1NAME", "");
                String SimIccid = sharedPreferences.getString("SIM1ICCID", "");
                requestData(0,SimName, SimIccid);

            }
        });


        refSim2Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //request airtime
                String SimName = sharedPreferences.getString("SIM2NAME", "");
                String SimIccid = sharedPreferences.getString("SIM2ICCID", "");
                requestData(1,SimName, SimIccid);

            }
        });

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

        Toast.makeText(getActivity(), "step 1", Toast.LENGTH_SHORT).show();

        int networklogo;
        String ussdservice;
        int NoOfSim = sharedPreferences.getInt("Simno", 0);
        String Sim1Name = sharedPreferences.getString("SIM1NAME", "");

        Toast.makeText(getActivity(), "ccc" + SimIccid + SimName, Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //ask permission

            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_PHONE_STATE, RECEIVE_SMS, CALL_PHONE}, REQUEST_PHONE_STATE);

            Toast.makeText(getActivity(), "ask permission", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getActivity(), "dont ask permission", Toast.LENGTH_SHORT).show();

            if (Sim1Name.equalsIgnoreCase("")
                    || (SimIccid.equalsIgnoreCase(""))) {

                Toast.makeText(getActivity(), "No Sim Card Detected", Toast.LENGTH_SHORT).show();

            } else {

                String ussd;
                String Network = Sim1Name;

                Toast.makeText(getActivity(), Sim1Name, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getActivity(), "else else", Toast.LENGTH_SHORT).show();
                }
                if (!(ussd == null)) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        methodsClass.DialUssdCodeNewApi(getActivity(), ussd, getActivity(), sim, "Airtime Balance", Network, networklogo);

                    } else {
                        methodsClass.DialUssdCode(getActivity(), ussd, getActivity(), sim);
                        // globalVariable.setUssdservice(ussdservice);
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

        Toast.makeText(getActivity(), "step 1", Toast.LENGTH_SHORT).show();

        int networklogo;
        String ussdservice;
        int NoOfSim = sharedPreferences.getInt("Simno", 0);

        String Sim1Iccid = sharedPreferences.getString("SIM1ICCID", "");
        String Sim2Iccid = sharedPreferences.getString("SIM2ICCID", "");

        Toast.makeText(getActivity(), "ccc" + Sim1Iccid + SimName, Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //ask permission

            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_PHONE_STATE, RECEIVE_SMS, CALL_PHONE}, REQUEST_PHONE_STATE);

            Toast.makeText(getActivity(), "ask permission", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getActivity(), "dont ask permission", Toast.LENGTH_SHORT).show();

            if (SimName.equalsIgnoreCase("")
                    || (Sim1Iccid.equalsIgnoreCase(""))) {

                Toast.makeText(getActivity(), "No Sim Card Detected", Toast.LENGTH_SHORT).show();

            } else {

                String ussd;
                String Network = SimName;

                Toast.makeText(getActivity(), SimName, Toast.LENGTH_SHORT).show();

                if (Network.toLowerCase().contains("9mobile") || Network.toLowerCase().contains("etisalat")) {
                    ussd = "*228#";
                    networklogo = R.drawable.nmobile_logo;
                    ussdservice = "9mobile-airtime";
                } else if (Network.toLowerCase().contains("glo")) {
                    ussd = "*127*0#";
                    networklogo = R.drawable.glo_logo;
                    ussdservice = "glo-airtime";
                } else if (Network.toLowerCase().contains("mtn")) {
                    ussd = "*131*4#";
                    networklogo = R.drawable.mtn_logo;
                    ussdservice = "mtn-airtime";
                } else if (Network.toLowerCase().contains("airtel")) {
                    ussd = "*140#";
                    networklogo = R.drawable.airtel_logo;
                    ussdservice = "airtel-airtime";
                } else {
                    ussd = null;
                    networklogo = R.drawable.airtel_logo;
                    ussdservice = "";
                    Toast.makeText(getActivity(), "else else", Toast.LENGTH_SHORT).show();
                }
                if (!(ussd == null)) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        methodsClass.DialUssdCodeNewApi(getActivity(), ussd, getActivity(), sim, "Airtime Balance", Network, networklogo);

                    } else {
                        methodsClass.DialUssdCode(getActivity(), ussd, getActivity(), sim);
                        // globalVariable.setUssdservice(ussdservice);
                    }
                }
            }
        }




    }

    private void populateViews(View view) {

        int Simno = sharedPreferences.getInt("Simno", 0);
        List<Airtime> airtimeList = new ArrayList<>();

        for (int i = 1; i <= Simno; i++) {
            String iccid = sharedPreferences.getString("SIM" + Simno + "NAME", "");
            Airtime airtime = dbb.airtimeDao().getLastAirtimeOrData(iccid, "airtime");
            airtimeList.add(airtime);

        }

        if (airtimeList.size() == 0) {

            Toast.makeText(getActivity(), "No Last Airtime", Toast.LENGTH_SHORT).show();
        } else if (airtimeList.size() == 1) {

            Toast.makeText(getActivity(), "1 sim has its last airtime detected", Toast.LENGTH_SHORT).show();
            tvSim1Airtime.setText(airtimeList.get(0).getBalance() + "");

        } else if (airtimeList.size() == 2) {

            Toast.makeText(getActivity(), "1 sim has its last airtime detected", Toast.LENGTH_SHORT).show();
            tvSim1Airtime.setText(airtimeList.get(0).getBalance() + "");
            tvSim2Airtime.setText(airtimeList.get(1).getBalance() + "");
        }


    }



    //To check Accessibility Service Status
    public boolean isAccessServiceEnabled(Context context, Class accessibilityServiceClass)
    {
        String prefString = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        return prefString!= null && prefString.contains(context.getPackageName() + "/" + accessibilityServiceClass.getName());
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

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

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

}