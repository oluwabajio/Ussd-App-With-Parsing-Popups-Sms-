package tingtel.app.Services;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import tingtel.app.Methods.MyApplication;

public class MessageReceiver extends BroadcastReceiver {

    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                //check if app is in foreground or background
                MyApplication globalVariable = (MyApplication) context.getApplicationContext();

                String Appstate = globalVariable.getAppstate();

                if (Appstate.equalsIgnoreCase("background")) {
                    Log.e("logmessage", "App is in background, wont respond to sms");
                    return;
                }

                String phoneNumber = msgs[i].getOriginatingAddress();
                String messageBody = msgs[i].getMessageBody();

                Intent myIntent = new Intent("SmsMessage");
                myIntent.putExtra("senderNum", phoneNumber);
                myIntent.putExtra("message", messageBody);
                LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                //   Toast.makeText(context, "I am messagereceiver", Toast.LENGTH_SHORT).show();

            }

//        } catch (Exception e) {
//            Log.e("logmessage", "Exception smsReceiver" + e);
//

        }
    }
}

