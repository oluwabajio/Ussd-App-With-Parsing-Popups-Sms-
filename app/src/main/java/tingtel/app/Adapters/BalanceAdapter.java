package tingtel.app.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;

import tingtel.app.Methods.Methods;
import tingtel.app.Models.Balance;
import tingtel.app.R;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.MyViewHolder> {
    private Context mContext ;
    private List<Balance> mData ;

    public BalanceAdapter(Context mContext, List lst){

        this.mContext = mContext;
        this.mData = lst;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.view_balance,parent,false);
        // click listener here
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.itemView.setTag(mData.get(position));
        holder.tvTitle.setText("" + mData.get(position).getMessage());
        holder.tvAmount.setText("#" + mData.get(position).getSimUuid());
        holder.tvAmount.setText("");
        holder.tvDate.setText(new SimpleDateFormat("MMMM dd, hh:mm a").format(mData.get(position).getDate()));
        holder.tvServiceName.setText(mData.get(position).getType());
      //  holder.imgLogo.setImageResource((mData.get(position).getBanklogo()));
        holder.tvSim.setText(mData.get(position).getSimName());
     //   holder.tvServiceName.setText(mData.get(position).getServiceName());

        setNetworkImage(holder, mData.get(position).getSimName());
    }


    private void setNetworkImage(BalanceAdapter.MyViewHolder holder, String tag) {
        String networkname = tag;
        if (networkname.toLowerCase().contains("mtn")) {
            Glide.with(mContext).load(R.drawable.mtn_logo).into(holder.imgLogo);



        } else if (networkname.toLowerCase().contains("airtel")) {
            Glide.with(mContext).load(R.drawable.airtel_logo).into(holder.imgLogo);


        } else if (networkname.toLowerCase().contains("glo")) {
            Glide.with(mContext).load(R.drawable.glo_logo).into(holder.imgLogo);


        } else if (networkname.toLowerCase().contains("9mobile")) {
            Glide.with(mContext).load(R.drawable.nmobile_logo).into(holder.imgLogo);

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvAmount, tvTitle, tvSim, tvServiceName;
        ImageView imgLogo;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSim = itemView.findViewById(R.id.tv_sim);
            tvServiceName = itemView.findViewById(R.id.tv_serviceName);

            imgLogo = itemView.findViewById(R.id.img_logo);

            Context context = itemView.getContext();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Balance BalanceModel = (Balance) view.getTag();
//                    Intent i = new Intent(view.getContext(), MainActivity.class);
//                    i.putExtra("desc", cpu.getCode());
//                    i.putExtra("title", cpu.getName());
//                    view.getContext().startActivity(i);
                    Methods method = new Methods();
                    // method.DialUssdCode((BanksBalancesActivity)context, BalanceModel.get, context, 0);



                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });


        }
    }




}
