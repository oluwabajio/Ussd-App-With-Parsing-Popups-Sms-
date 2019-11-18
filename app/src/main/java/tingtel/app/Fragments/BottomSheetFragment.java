package tingtel.app.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import tingtel.app.Methods.Methods;
import tingtel.app.R;


public class BottomSheetFragment extends BottomSheetDialogFragment {

    TextView txtCode, txtDesc, txtTitle, txtNetworkName;
    Button btnCode;
    LinearLayout bottom_sheet_header;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        initViews(view);

        Bundle mArgs = getArguments();
        String title = mArgs.getString("title");
        String desc = mArgs.getString("desc");
        final String code = mArgs.getString("code");
        String networkname = mArgs.getString("networkname");
        final int SimNo = mArgs.getInt("simno");

        try {
            int networkcolor = mArgs.getInt("networkcolor");
            btnCode.setBackgroundColor(networkcolor);
            bottom_sheet_header.setBackgroundColor(networkcolor);

        } catch (Exception e) {
        }


        //  Toast.makeText(getActivity(), bankname, Toast.LENGTH_SHORT).show();
        txtTitle.setText(title);
        txtDesc.setText(desc);
        txtCode.setText(code);
        txtNetworkName.setText(networkname);

        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods method = new Methods();
                int sim = SimNo;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    method.DialUssdCodeNewApi(getActivity(), code, getContext(), sim, "", R.drawable.airtel_logo);
                } else {
                    method.DialUssdCode(getActivity(), code, getContext(), sim);
                }
            }
        });


        return view;
    }

    private void initViews(View view) {
        txtCode = (TextView) view.findViewById(R.id.txt_code);
        txtDesc = (TextView) view.findViewById(R.id.txt_description);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtNetworkName = (TextView) view.findViewById(R.id.txt_networkname);
        btnCode = (Button) view.findViewById(R.id.btn_RunCode);
        bottom_sheet_header = (LinearLayout) view.findViewById(R.id.bottom_sheet_header);
    }
}