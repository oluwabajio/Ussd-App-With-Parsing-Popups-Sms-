package tingtel.app.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import tingtel.app.Adapters.BalanceAdapter;
import tingtel.app.Adapters.BeneficiaryAdapter;
import tingtel.app.BalanceActivity;
import tingtel.app.Methods.AppDatabase;
import tingtel.app.Models.Beneficiary;
import tingtel.app.R;


public class BeneficiaryListFragment extends BottomSheetDialogFragment {


    List<Beneficiary> items = new ArrayList<>();
    RecyclerView Rv_Beneficiary;
    public static RecyclerView.Adapter adapter;
    AppDatabase appdatabase;
    FancyButton btnAddBeneficiary;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view;

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_beneficiary_list, container, false);
        
        initObjects();
        initViews(view);
        LoadDatabase();


        return view;
    }

    private void initViews(View view) {
        Rv_Beneficiary = (RecyclerView) view.findViewById(R.id.rv_beneficiary);
        btnAddBeneficiary = (FancyButton) view.findViewById(R.id.btn_AddBeneficiary);

        btnAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                BeneficiaryAddFragment bottomSheetFragment = new BeneficiaryAddFragment();
                bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }

    private void initObjects() {
        appdatabase = AppDatabase.getDatabaseInstance(getActivity());
    }


    private void LoadDatabase() {

        // Toast.makeText(this, "i am here", Toast.LENGTH_SHORT).show();
        //load saved room data to recyclerview
        Runnable r = new Runnable(){
            @Override
            public void run() {
                //  final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production")
                //            .build();
                items.clear();
                items = appdatabase.beneficiaryDao().getAllItems();


              getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Rv_Beneficiary.setLayoutManager(new LinearLayoutManager(getActivity()));

                        //show latest items first
                        Collections.reverse(items);

                        adapter= new BeneficiaryAdapter(getActivity(), items, BeneficiaryListFragment.this);

                        try {
                            Rv_Beneficiary.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        //    Toast.makeText(getContext(), "notifydone", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "something amiss", Toast.LENGTH_SHORT).show();
                        }
                    }});
            }
        };

        Thread newThread= new Thread(r);
        newThread.start();

    }


}
