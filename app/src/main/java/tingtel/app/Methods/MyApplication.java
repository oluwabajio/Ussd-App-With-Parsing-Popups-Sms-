package tingtel.app.Methods;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application implements LifecycleObserver {

    private String name;
    private String appstate;
    private String ussdservice;
    private String iccid;
    private String clickedItem;

    private static Context context;
    public
    Methods method;


    @Override
    public void onCreate() {
        super.onCreate();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        MyApplication.context = getApplicationContext();

        method = new Methods();

    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    // Check if app is in foreground (lifecycle library)
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void appInResumeState() {
        // Toast.makeText(this,"In Foreground",Toast.LENGTH_LONG).show();
        Log.e("logmessage", "In Foreground");

        appstate = "foreground";

        //Check if sim card is changed
        method.checkSimCards(MyApplication.getAppContext());

    }


    //Check if app is in background
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void appInPauseState() {
        Log.e("logmessage", "In Background");
        appstate = "background";
    }


    public String getName() {

        return name;
    }

    public void setName(String aName) {

        name = aName;

    }

    public String getAppstate() {
        return appstate;
    }

    public void setAppstate(String appstate) {
        this.appstate = appstate;
    }

    public String getUssdservice() {
        return ussdservice;
    }

    public void setUssdservice(String ussdservice) {
        this.ussdservice = ussdservice;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getClickedItem() {
        return clickedItem;
    }

    public void setClickedItem(String clickedItem) {
        this.clickedItem = clickedItem;
    }
}
