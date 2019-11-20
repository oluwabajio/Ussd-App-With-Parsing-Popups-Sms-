package tingtel.app.Adapters;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;

import tingtel.app.Fragments.BeneficiaryListFragment;
import tingtel.app.Methods.Methods;
import tingtel.app.Models.Beneficiary;
import tingtel.app.R;
import tingtel.app.TransferAirtimeActivity;
import tingtel.app.ViewModels.TransferAirtimeViewModel;

public class BeneficiaryAdapter  extends RecyclerView.Adapter<BeneficiaryAdapter.MyViewHolder> {
    private Context mContext ;
    private List<Beneficiary> mData ;
    private BeneficiaryListFragment beneficiaryListFragment;
    TransferAirtimeViewModel transferViewModel;

    public BeneficiaryAdapter(Context mContext, List lst, BeneficiaryListFragment beneficiaryListFragment){

        this.mContext = mContext;
        this.mData = lst;
        this.beneficiaryListFragment = beneficiaryListFragment;
    }

    @Override
    public BeneficiaryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.view_beneficiary,parent,false);
        // click listener here
        return new BeneficiaryAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final BeneficiaryAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setTag(mData.get(position));
        holder.tvName.setText("" + mData.get(position).getName());
        holder.tvPhone.setText("" + mData.get(position).getPhoneNumber());

    }




    @Override
    public int getItemCount() {
        return mData.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPhone;



        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_Name);
            tvPhone = itemView.findViewById(R.id.txt_Phone);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    beneficiaryListFragment.dismiss();

                    Beneficiary beneficiaryModel = (Beneficiary) view.getTag();

                    transferViewModel= ViewModelProviders.of((FragmentActivity) mContext).get(TransferAirtimeViewModel.class);
                    transferViewModel.sendPhoneNo(beneficiaryModel.getPhoneNumber());
                    Toast.makeText(mContext, "sent", Toast.LENGTH_SHORT).show();


                }
            });




        }
    }




}
