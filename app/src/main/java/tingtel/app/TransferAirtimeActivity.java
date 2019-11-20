package tingtel.app;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;
import tingtel.app.Fragments.BeneficiaryListFragment;
import tingtel.app.Methods.AppDatabase;
import tingtel.app.Methods.Methods;
import tingtel.app.Models.Balance;
import tingtel.app.Models.NetworksCode;
import tingtel.app.Models.Transfer;
import tingtel.app.ViewModels.TransferAirtimeViewModel;

public class TransferAirtimeActivity extends AppCompatActivity {

    EditText ed_Amount, ed_Phone, ed_Pin;
    FancyButton btnTransfer;
    int MY_PERMISSIONS_REQUEST_SEND_SMS = 101;

    private RadioGroup radioSimGroup;
    private RadioButton radioSimButton, radioSimButton1, radioSimButton2;
    TextView tvChooseBeneficiary;
    Methods methodsClass = new Methods();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int selectedtSim = -1;

    String SimStatus = "SIMSTATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_airtime);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initObjects();
        initViews();
        getSimcardDetails();

        getPhoneNumber();


    }

    private void getPhoneNumber() {



            TransferAirtimeViewModel mViewModel;

            mViewModel = ViewModelProviders.of(this).get(TransferAirtimeViewModel.class);
            mViewModel.getPhoneNo().observe(this, new Observer<String>() {

                @Override
                public void onChanged(@Nullable String s) {
                ed_Phone.setText(s);
                }
            });
        }


    private void initObjects() {
        sharedPreferences = getSharedPreferences("TingTelPref", 0);
        editor = sharedPreferences.edit();
    }

    private void initViews() {

        ed_Amount = (EditText) findViewById(R.id.ed_amount);
        ed_Phone = (EditText) findViewById(R.id.ed_phone_number);
        ed_Pin = (EditText) findViewById(R.id.ed_pin);
        radioSimGroup=(RadioGroup)findViewById(R.id.radioGroup);


        int selectedId=radioSimGroup.getCheckedRadioButtonId();

        radioSimButton=(RadioButton)findViewById(selectedId);

        tvChooseBeneficiary = (TextView)  findViewById(R.id.tv_chooseBeneficiary);

        radioSimButton1=(RadioButton)findViewById(R.id.radioButton);
        radioSimButton2=(RadioButton)findViewById(R.id.radioButton2);

        btnTransfer = (FancyButton) findViewById(R.id.btn_transfer);

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            if (!radioSimButton.isChecked()) {
                Toast.makeText(TransferAirtimeActivity.this, "Amount Required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ed_Amount.getText().toString().equalsIgnoreCase("")) {

                ed_Amount.setError("Amount Required");
                ed_Amount.requestFocus();
            }



                TransferAirtime();

            }
        });


        tvChooseBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeneficiaryListFragment bottomSheetFragment = new BeneficiaryListFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());



            }
        });

        radioSimGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radioButton:
                        selectedtSim=0;
                        break;
                    case R.id.radioButton2:
                        selectedtSim=1;
                        break;
                }
            }
        });

    }




    private void getSimcardDetails() {
        methodsClass.getCarrierOfSim(TransferAirtimeActivity.this);

        String NoOfSIm = sharedPreferences.getString(SimStatus, "");
        String Sim1Network = sharedPreferences.getString("SIM1NAME", "");
        String Sim2Network = sharedPreferences.getString("SIM2NAME", "");

        //  Toast.makeText(getActivity(), "fall "+ Sim1Network + Sim2Network + NoOfSIm, Toast.LENGTH_SHORT).show();

        if (NoOfSIm.equalsIgnoreCase("NOSIM")) {



        } else if (NoOfSIm.equalsIgnoreCase("SIM1")) {

            radioSimButton1.setVisibility(View.VISIBLE);
            radioSimButton2.setVisibility(View.GONE);
            radioSimButton1.setText(Sim1Network);

        } else  if (NoOfSIm.equalsIgnoreCase("SIM1SIM2")) {

            radioSimButton1.setVisibility(View.VISIBLE);
            radioSimButton2.setVisibility(View.VISIBLE);
            radioSimButton1.setText(Sim1Network);
            radioSimButton2.setText(Sim2Network);

        }


    }





    private void TransferAirtime() {

        String Recipient = ed_Phone.getText().toString();
        String Amount = ed_Amount.getText().toString();
        String PIN = ed_Pin.getText().toString();
        String ussd = "";







//        Toast.makeText(TransferAirtimeActivity.this,radioSimButton.getText(),Toast.LENGTH_SHORT).show();


        String Network = radioSimButton.getText().toString();


        if (Network.toLowerCase().contains("9mobile") || Network.toLowerCase().contains("etisalat")) {
            ussd =  "*223*" + PIN + "*" + Amount + "*" + Recipient + "#";
            runUssd(ussd);

        } else if (Network.toLowerCase().contains("glo")) {
            ussd =  "*131*" + Recipient + "*" + Amount + "*" + PIN + "#";
            runUssd(ussd);

        } else if (Network.toLowerCase().contains("mtn")) {
            ussd =  "*600*" + Recipient + "*" + Amount + "*" + PIN + "#";
            runUssd(ussd);

        } else if (Network.toLowerCase().contains("airtel")) {
            checkSmsPermission();

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("432", null,"2u " + Recipient + " "+  Amount + " " + PIN, null, null);

            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();

        } else {
            ussd = null;

            //     Toast.makeText(getActivity(), "else else", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "a" + ussd + " ", Toast.LENGTH_SHORT).show();
        saveTransferHistory(Network, Recipient, Amount);

    }

    private void saveTransferHistory(final String network, final String recipient, final String amount) {

        final String simiccid;

        if (selectedtSim == 0) {
            simiccid = sharedPreferences.getString("SIM1ICCID", "");
        } else if (selectedtSim == 1) {
            simiccid = sharedPreferences.getString("SIM2ICCID", "");
        } else {
            simiccid = "";
        }


        Toast.makeText(this, "simiccic: " + simiccid + selectedtSim, Toast.LENGTH_SHORT).show();



        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Date queryDate = Calendar.getInstance().getTime();
                AppDatabase appdatabase = AppDatabase.getDatabaseInstance(TransferAirtimeActivity.this);

                //creating a task
                Transfer transfer = new Transfer();

                transfer.setSimName(network);
                transfer.setSimUuid(simiccid);
                transfer.setPhoneNumber(recipient);
                transfer.setAmount(amount);
                transfer.setDate(queryDate);




                //adding to database
                appdatabase.transferDao().insert(transfer);

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


    private void runUssd(String ussd) {

        if (!(ussd == null)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                methodsClass.DialUssdCodeNewApi(TransferAirtimeActivity.this, ussd, TransferAirtimeActivity.this, selectedtSim, "",  R.drawable.history);

            } else {

                Toast.makeText(this, "selected sim" + selectedtSim, Toast.LENGTH_SHORT).show();
                methodsClass.DialUssdCode(TransferAirtimeActivity.this, ussd, TransferAirtimeActivity.this, selectedtSim);

            }
        }

    }

    private void checkSmsPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)  != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {


        }
    }

}
