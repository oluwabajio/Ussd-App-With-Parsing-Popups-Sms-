package tingtel.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;
import tingtel.app.Methods.AppDatabase;
import tingtel.app.Models.Balance;
import tingtel.app.Models.Beneficiary;
import tingtel.app.R;


public class BeneficiaryAddFragment extends BottomSheetDialogFragment {

    EditText txtName, txtPhone;
    FancyButton btnAddBeneficiary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_beneficiary_add, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {

        txtName = (EditText) view.findViewById(R.id.txt_BeneficiaryName);
        txtPhone = (EditText) view.findViewById(R.id.txt_BeneficiaryPhone);

        btnAddBeneficiary = (FancyButton) view.findViewById(R.id.btn_AddBeneficiary);


        btnAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddBeneficiary();
                dismiss();
            }
        });

    }

    private void AddBeneficiary() {

        final String Name = txtName.getText().toString();
        final String Phone = txtPhone.getText().toString();


        if (Name.isEmpty() || Phone.isEmpty()) {
            Toast.makeText(getContext(), "Please Input all Details", Toast.LENGTH_SHORT).show();
        } else {


            class SaveTask extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {

                    Date queryDate = Calendar.getInstance().getTime();
                    AppDatabase appdatabase = AppDatabase.getDatabaseInstance(getActivity());

                    //creating a task
                    Beneficiary beneficiary = new Beneficiary();

                    beneficiary.setName(Name);
                    beneficiary.setPhoneNumber(Phone);

                    //adding to database
                    appdatabase.beneficiaryDao().insert(beneficiary);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    //  startActivity(new Intent(getActivity(), .class));
//                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
                }
            }

            SaveTask st = new SaveTask();
            st.execute();

        }
    }



}
