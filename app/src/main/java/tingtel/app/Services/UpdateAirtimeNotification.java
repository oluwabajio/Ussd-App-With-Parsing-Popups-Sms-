package tingtel.app.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import tingtel.app.Methods.Methods;
import tingtel.app.Methods.MyApplication;

public class UpdateAirtimeNotification extends BroadcastReceiver {

    MyApplication globalVariable;
    Methods methodsClass = new Methods();
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {

        globalVariable = (MyApplication) context.getApplicationContext();
        pref = context.getSharedPreferences("TingTelPref", 0);
        editor = pref.edit();

        Log.e("logmessage", "UpdateAirtimeNotification Onreceive");


        String whichAction = intent.getAction();

        Toast.makeText(context, "llllll", Toast.LENGTH_SHORT).show();

        switch (whichAction)
        {

            case "update_airtime":
                Toast.makeText(context, "jjy", Toast.LENGTH_SHORT).show();
                return;
            case "update_sim1_airtime":


                return;
            case "update_sim2_airtime":


                return;

        }

    }



}
