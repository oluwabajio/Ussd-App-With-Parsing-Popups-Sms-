package tingtel.app.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import tingtel.app.Methods.MyApplication;

public class MessageReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                String phoneNumber = "";

                String messageBody = "";

                final SmsMessage[] messages = new SmsMessage[pdusObj.length];

                //sms are in pdus blocks, we need to join them together
                for (int i = 0; i < pdusObj.length; i++) {

                    messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    phoneNumber = messages[0].getDisplayOriginatingAddress();


                } // end for loop


                if (messages.length > -1) {
                    for (int i = 0; i < pdusObj.length; i++) {
                        messageBody += (messages[i].getDisplayMessageBody());
                    }
                }

                //check if app is in foreground or background
                MyApplication globalVariable = (MyApplication) context.getApplicationContext();

                String Appstate = globalVariable.getAppstate();

                if (Appstate.equalsIgnoreCase("background")){
                    Log.e("logmessage", "App is in background, wont respond to sms");
                    return;
                }


                Intent myIntent = new Intent("SmsMessage");
                myIntent.putExtra("senderNum", phoneNumber);
                myIntent.putExtra("message", messageBody);
                LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                //   Toast.makeText(context, "I am messagereceiver", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Log.e("logmessage", "Exception smsReceiver" + e);

        }
    }
}

