package tingtel.app.Methods;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tingtel.app.MainActivity;
import tingtel.app.Models.Balance;
import tingtel.app.RequestPermissionActivity;

import static android.Manifest.permission.READ_PHONE_STATE;

public class Methods {




    MyApplication globalVariable;
    int REQUEST_PHONE_CALL = 101;
    int REQUEST_READ_PHONE_STATE = 105;

    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String SimStatus = "SIMSTATUS";
    String Sim1Uuid = "SIM1UUID";
    String Sim2Uuid = "SIM2UUID";
    String Sim1Name = "SIM1NAME";
    String Sim2Name = "SIM2NAME";

//    MainActivity mainActivity = new MainActivity();






    public void DialUssdCode(Activity a, String UssdCode, Context context, int simno) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //if android version doesnt require permission
            DialCode(UssdCode, context, simno);
        } else {

            //if permission is not enable, ask for permission
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            } else {
                DialCode(UssdCode, context, simno);
            }


        }


    }


    void DialCode(String UssdCode, Context context, int simno) {


        if (UssdCode != "nil") {

            //check if it begins and ends with #

            if (UssdCode.startsWith("*") && UssdCode.endsWith("#")) {
                UssdCode = UssdCode.substring(1, UssdCode.length() - 1);
            }

            //remove

            String ussdCode = "*" + UssdCode + Uri.encode("#");

            Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode));

            intent.putExtra("com.android.phone.extra.slot", simno); //For sim 1
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

    public void DialUssdCodeNewApi(final Activity activity, String UssdCode, final Context context, int simno, final String servicename, final int networklogo) {
        progressDialog = new ProgressDialog(context);
//        final TextView tv = (TextView) activity.findViewById(R.id.txtPopupMessage);
//        if (progressDialog != null && progressDialog.isShowing()) {
//            return;
//        }

        //isnt running
        progressDialog.show();
        progressDialog.setCancelable(false);
        // Setting Title
        progressDialog.setTitle("Ussd Running");
        progressDialog.setMessage("Ussd Running");

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Handler handler = new Handler();

        TelephonyManager.UssdResponseCallback responseCallback = null;

        //if sim 1 is clicked
        if (simno ==  0) {


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {




                responseCallback = new TelephonyManager.UssdResponseCallback() {
                    @Override
                    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                        super.onReceiveUssdResponse(telephonyManager, request, response);

                        //Dismiss the dialog
                        progressDialog.dismiss();

                        Log.e("logmessage", "success sim 2 : " + response.toString());
                        //showPopupBox(response.toString(), activity);

                        try {
//                            tv.setText(response.toString());
                        } catch (Exception e) {

                        }


                        // saveHistoryMethod(response.toString(), activity);
                        if (!servicename.equalsIgnoreCase("")) {
                            //save history
                        //    SaveHistory(activity, Integer.parseInt("3"), simname, servicename, response.toString(), networklogo);

                            ((MainActivity)context).checkPopupandSave(response.toString());

                        } else {
                            return;
                        }
//                        Toast.makeText(context, "success" + response.toString(), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(activity, ShowMessage.class);
//                        intent.putExtra("message", response.toString());
//                        activity.startActivity(intent);
//

                    }

                    @Override
                    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                        super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                        //Dismiss the dialog
                        progressDialog.dismiss();
                        Toast.makeText(context, "failed: " + String.valueOf(failureCode), Toast.LENGTH_SHORT).show();
                    }
                };


                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    telephonyManager.sendUssdRequest(UssdCode, responseCallback, handler);
                    //  Toast.makeText(a, UssdCode, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "not granted", Toast.LENGTH_SHORT).show();
                }



            }
            //if sim 2 is clicked
        }else {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);
                if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                    //if there are two sims in dual sim mobile
                    List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                    SubscriptionInfo simInfo2 = (SubscriptionInfo) localList.get(1);

                    TelephonyManager manager2 = telephonyManager.createForSubscriptionId(simInfo2.getSubscriptionId());

                    responseCallback = new TelephonyManager.UssdResponseCallback() {
                        @Override
                        public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                            super.onReceiveUssdResponse(telephonyManager, request, response);

                            //Dismiss the dialog
                            progressDialog.dismiss();
                            // Toast.makeText(context, "success sim 2" + response.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("logmessage", "success sim 2" + response.toString());
                            //showPopupBox(response.toString(), activity);

                            try {
                             //   tv.setText(response.toString());
                            } catch (Exception e) {

                            }
                            //save history
                            if (!servicename.equalsIgnoreCase("")) {
                                //save history
                              //  SaveHistory(activity, Integer.parseInt("3"), simname, servicename, response.toString(), networklogo);
                                ((MainActivity)context).checkPopupandSave(response.toString());

                            } else {
                                return;
                            }

                        }

                        @Override
                        public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                            super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);

                            //Dismiss the dialog
                            progressDialog.dismiss();
                            Toast.makeText(context, "failed: Error " + String.valueOf(failureCode), Toast.LENGTH_SHORT).show();
                        }
                    };

                    manager2.sendUssdRequest(UssdCode, responseCallback, new Handler());
                }
            }
        }

    }

//    private void saveHistoryMethod(String response, Activity activity) {
//
//        String servicename = "";
//        String simname = "";
//
//        int bankLogoo = R.drawable.history;
//
//        if (response.contains("Main Bal:")){
//            //this is airtel check airtime balance
//            servicename = "Balance Balance";
//            simname = "Airtel";
//            bankLogoo = R.drawable.airtel_logo;
//        } else if (response.toLowerCase().contains("recharge was successful")){
//            //this is airtel load recharge
//            servicename = "Recharge";
//            simname = "Airtel";
//            bankLogoo = R.drawable.airtel_logo;
//        } else if(response.) {
//
//        }
//
//        else {
//            return;
//        }
//
//        //save history
//        SaveHistory(activity, Integer.parseInt("3"), simname,  servicename, response, bankLogoo);
//    }

    private void showPopupBox(String response, Activity activity) {

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);

        alert.setTitle("Tingtel Response");
        alert.setMessage(response);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();


            }
        });


        alert.show();


    }


    private final static String simSlotName[] = {
            "extra_asus_dial_use_dualsim",
            "com.android.phone.extra.slot",
            "slot",
            "simslot",
            "sim_slot",
            "subscription",
            "Subscription",
            "phone",
            "com.android.phone.DialingMode",
            "simSlot",
            "slot_id",
            "simId",
            "simnum",
            "phone_type",
            "slotId",
            "slotIdx"
    };





    public void checkSimCards(Context appContext) {
    }


    public String[] DetectSimCards(Activity activity) {

        sharedPreferences = activity.getSharedPreferences("TingTelPref", 0);
        editor = sharedPreferences.edit();



        if (Build.VERSION.SDK_INT > 22) {
            //for dual sim mobile
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(activity);

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //ask permission
                ActivityCompat.requestPermissions(activity, new String[]{READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
//                DetectSimCards(activity);
                String[] NetworkArray = new String[1];
                NetworkArray[0] =  "No Sim Card Detected";

                editor.putString("SIMSTATUS", "");
                editor.putString("SIM1", "");
                editor.putString("SIM2", "");
                editor.commit();

                return NetworkArray;
            } else {
                if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                    //if there are two sims in dual sim mobile
                    List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                    SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(0);
                    SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(1);

//                    Sim1Network = simInfo.getDisplayName().toString();
//                    Sim2Network = simInfo1.getDisplayName().toString();

                    if ((simInfo.getMcc() == 621) && (simInfo1.getMcc() == 621)) {

                        String[] NetworkArray = new String[2];
                        NetworkArray[0] = simInfo.getDisplayName().toString();
                        NetworkArray[1] = simInfo1.getDisplayName().toString();

                        editor.putString(SimStatus, "SIM1SIM2");
                        editor.putString("SIM2", "");
                        editor.putString("SIM1", "");
                        editor.putString("SIM2", "");
                        editor.commit();

                        return NetworkArray;
                    } else {

                        Toast.makeText(activity, "This app can only work with Nigerian Networks for now", Toast.LENGTH_LONG).show();
                        String[] NetworkArray = new String[1];
                        NetworkArray[0] =  "No Sim Card Detected";

                        return NetworkArray;
                    }


                } else if (localSubscriptionManager.getActiveSubscriptionInfoCount() == 1) {
                    //if there is 1 sim in dual sim mobile
                    List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                    SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(0);


                    if (simInfo.getMcc() == 621) {
                        String[] NetworkArray = new String[1];
                        NetworkArray[0] = simInfo.getDisplayName().toString();

                        return NetworkArray;
                    } else {
                        Toast.makeText(activity, "This app can only work with Nigerian Networks for now", Toast.LENGTH_LONG).show();

                        String[] NetworkArray = new String[1];
                        NetworkArray[0] =  "No Sim Card Detected";

                        return NetworkArray;
                    }

                }
                else {
                    //No sim
                    String[] NetworkArray = new String[1];
                    NetworkArray[0] =  "No Sim Card Detected";
                    return NetworkArray;
                }

            }

        }else{
            //below android version 22
            TelephonyManager tManager = (TelephonyManager) activity.getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String sim1 = tManager.getNetworkOperatorName();

            String[] NetworkArray = new String[1];
            NetworkArray[0] =  sim1;

            return NetworkArray;

        }
    }







    public void getCarrierOfSim(Activity activity) {

        sharedPreferences = activity.getSharedPreferences("TingTelPref", 0);
        editor = sharedPreferences.edit();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                final SubscriptionManager subscriptionManager = SubscriptionManager.from(activity);
                final List<SubscriptionInfo> activeSubscriptionInfoList;

                activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                List<CharSequence> carrierNameList = new ArrayList<CharSequence>();
                int Simno = 0;
                for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                    final CharSequence carrierName = subscriptionInfo.getCarrierName();
                    final CharSequence displayName = subscriptionInfo.getDisplayName();
                    final int mcc = subscriptionInfo.getMcc();
                    final int mnc = subscriptionInfo.getMnc();
                    final String iccid = subscriptionInfo.getIccId();
                    final String subscriptionInfoNumber = subscriptionInfo.getNumber();

                  //  Toast.makeText(activity, "" + mnc + mcc, Toast.LENGTH_SHORT).show();

                    if (mcc == 621) {
                        carrierNameList.add(carrierName);
                        Simno += 1;
                        editor.putString("SIM" + Simno + "NAME", displayName.toString());
                        editor.putString("SIM" + Simno + "ICCID", iccid);
                        editor.putInt("Simno", Simno);
                        editor.commit();
                      //  Toast.makeText(activity, "mnc is true" + Simno, Toast.LENGTH_SHORT).show();
                         }
                 //   Toast.makeText(activity, "mnc is false", Toast.LENGTH_SHORT).show();

                }


                if (Simno == 0) {

                    editor.putString(SimStatus, "NOSIM");
                } else if (Simno == 1) {
                    editor.putString(SimStatus, "SIM1");
                } else if (Simno == 2) {
                    editor.putString(SimStatus, "SIM1SIM2");
                }
                    editor.commit();

         //       Toast.makeText(activity, "" + Simno, Toast.LENGTH_SHORT).show();



//                if (activeSubscriptionInfoList.size() == 0 ){
//                    Toast.makeText(this, "No Sim Exist", Toast.LENGTH_SHORT).show();
//
//                } else if (activeSubscriptionInfoList.size() == 1) {
//                    Toast.makeText(this, "Single Sim Exist", Toast.LENGTH_SHORT).show();
//                    Sim1Network = "" + carrierNameList.get(0);
//
//                } else if (activeSubscriptionInfoList.size() == 2) {
//                    Toast.makeText(this, "Dual Sim Exist", Toast.LENGTH_SHORT).show();
//                    Sim1Network = "" + carrierNameList.get(0);
//                    Sim2Network = "" + carrierNameList.get(1);
//                } else if (activeSubscriptionInfoList.size() == 3) {
//                    Toast.makeText(this, "3 Sims detected", Toast.LENGTH_SHORT).show();
//                }
            } else {

                //request Permission
                Intent intent = new Intent(activity, RequestPermissionActivity.class);
                activity.startActivity(intent);
            }

        }

    }















    public void SaveAirtimeOrData(final Context context, final float amount, final String simiccid, final String simName, final String message, final int banklogo, final String serviceType){
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Date queryDate = Calendar.getInstance().getTime();
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(context);

                //creating a task
                Balance balance = new Balance();

                balance.setSimName(simName);
                balance.setSimUuid(simiccid);
                balance.setType(serviceType);
                balance.setBalance(amount);
                balance.setDate(queryDate);
                balance.setMessage(message);



                //adding to database
                appdatabase.balanceDao().insert(balance);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }






    //capitalize all words
    public String capitalizer(String word){

        String[] words = word.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
            }
        }
        return  sb.toString();

    }



}
