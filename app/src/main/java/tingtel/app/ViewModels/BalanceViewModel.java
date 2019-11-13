package tingtel.app.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class BalanceViewModel extends ViewModel {


    // Create a LiveData with a String
    private MutableLiveData<String> currentBalance = new MutableLiveData<>();

    public MutableLiveData<String> getCurrentBalance() {
        if (currentBalance == null) {
            currentBalance = new MutableLiveData<String>();
        }
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {

        this.currentBalance.setValue(currentBalance);
    }
}
