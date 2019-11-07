package tingtel.app.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tingtel.app.Methods.AppDatabase;
import tingtel.app.Models.Airtime;
import tingtel.app.R;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_SMS;

public class MainFragment extends Fragment {

    TextView tvSim1Airtime, tvSim2Airtime, tvSim1Data, tvSim2Data;

    int REQUEST_PHONE_STATE = 101;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    AppDatabase dbb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;

        view = inflater.inflate(R.layout.fragment_main, container, false);

        initObjects(view);
        initViews(view);

      //  populateViews(view);

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

    }

    private void populateViews(View view) {

        int Simno = sharedPreferences.getInt("Simno", 0);
        List<Airtime> airtimeList = new ArrayList<>();

        for (int i = 1; i <= Simno; i++) {
            String iccid = sharedPreferences.getString("SIM" + Simno + "NAME", "");
            Airtime airtime = dbb.airtimeDao().getLastAirtime(iccid);
            airtimeList.add(airtime);

        }

        if (airtimeList.size() == 0) {

            Toast.makeText(getActivity(), "No Last Airtime", Toast.LENGTH_SHORT).show();
        } else if (airtimeList.size() == 1) {

            Toast.makeText(getActivity(), "1 sim has its last airtime detected", Toast.LENGTH_SHORT).show();
            tvSim1Airtime.setText(airtimeList.get(0).getAirtimeBalance() + "");

        } else if (airtimeList.size() == 2) {

            Toast.makeText(getActivity(), "1 sim has its last airtime detected", Toast.LENGTH_SHORT).show();
            tvSim1Airtime.setText(airtimeList.get(0).getAirtimeBalance() + "");
            tvSim2Airtime.setText(airtimeList.get(1).getAirtimeBalance() + "");
        }


    }


}