package tingtel.app.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class BalanceViewModel extends ViewModel {


    // Create a LiveData with a String
    private MutableLiveData<String> currentAirtimeBalanceSim1 = new MutableLiveData<>();
    private MutableLiveData<String> currentDataBalanceSim1 = new MutableLiveData<>();
    private MutableLiveData<String> currentAirtimeBalanceSim2 = new MutableLiveData<>();
    private MutableLiveData<String> currentDataBalanceSim2 = new MutableLiveData<>();



    public MutableLiveData<String> getCurrentAirtimeBalanceSim1() {
        if (currentAirtimeBalanceSim1 == null) {
            currentAirtimeBalanceSim1 = new MutableLiveData<String>();
        }
        return currentAirtimeBalanceSim1;
    }

    public void setCurrentAirtimeBalanceSim1(String currentBalance) {

        this.currentAirtimeBalanceSim1.setValue(currentBalance);
    }


    public MutableLiveData<String> getCurrentDataBalanceSim1() {
        if (currentDataBalanceSim1 == null) {
            currentDataBalanceSim1 = new MutableLiveData<String>();
            Log.e("logmessage", "null null");
        }
        Log.e("logmessage", "not null");
        return currentDataBalanceSim1;
    }

    public void setCurrentDataBalanceSim1(String currentBalance) {

        this.currentDataBalanceSim1.setValue(currentBalance);
    }


    public MutableLiveData<String> getCurrentAirtimeBalanceSim2() {
        if (currentAirtimeBalanceSim2 == null) {
            currentAirtimeBalanceSim2 = new MutableLiveData<String>();
        }
        return currentAirtimeBalanceSim2;
    }

    public void setCurrentAirtimeBalanceSim2(String currentBalance) {

        this.currentAirtimeBalanceSim2.setValue(currentBalance);
    }


    public MutableLiveData<String> getCurrentDataBalanceSim2() {
        if (currentDataBalanceSim2 == null) {
            currentDataBalanceSim2 = new MutableLiveData<String>();
        }
        return currentDataBalanceSim2;
    }

    public void setCurrentDataBalanceSim2(String currentBalance) {

        this.currentDataBalanceSim2.setValue(currentBalance);
    }
}
