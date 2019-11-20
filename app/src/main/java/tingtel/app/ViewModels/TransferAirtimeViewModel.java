package tingtel.app.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class TransferAirtimeViewModel extends ViewModel {

    private MutableLiveData<String> updatePhoneNo = new MutableLiveData<>();
    private MutableLiveData<String> updatePhoneNo2 = new MutableLiveData<>();


    public void sendPhoneNo(String msg)    {

        updatePhoneNo.setValue(msg);
    }

    public LiveData<String> getPhoneNo()
    {
        return updatePhoneNo;


    }






    public void sendPhoneNo2(String msg)    {

        updatePhoneNo2.setValue(msg);
    }

    public LiveData<String> getPhoneNo2()
    {
        return updatePhoneNo2;


    }
}
