package tingtel.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mehdi.sakout.fancybuttons.FancyButton;
import tingtel.app.BalanceActivity;
import tingtel.app.R;



public class TransferFragment extends Fragment {

    FancyButton btnMyTransfers, btnNewTransfers, btnHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.fragment_transfer, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {

        btnMyTransfers = (FancyButton) view.findViewById(R.id.btn_myTransfers);
        btnNewTransfers = (FancyButton) view.findViewById(R.id.btn_newTransfer);
        btnHistory = (FancyButton) view.findViewById(R.id.btn_history);



        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BalanceActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }

}