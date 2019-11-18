package tingtel.app.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import tingtel.app.Fragments.BottomSheetFragment;
import tingtel.app.ListUssdActivity;
import tingtel.app.Methods.Methods;
import tingtel.app.Models.NetworksCode;
import tingtel.app.R;

public class NetworkCodeAdapter extends RecyclerView.Adapter<NetworkCodeAdapter.MyViewHolder> {


    private Context mContext ;
    private List<NetworksCode> mData ;
    public String networkname;
    private int SimNo;
    private int networkcolor;



    public NetworkCodeAdapter(Context mContext, List lst, String networkname, int SimNo) {


        this.mContext = mContext;
        this.mData = lst;
        this.networkname = networkname;
        this.SimNo = SimNo;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.network_view,parent,false);
        // click listener here
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.itemView.setTag(mData.get(position));
        holder.imgInformetion.setTag(mData.get(position));
        holder.txtUssdTitle.setText(mData.get(position).getTitle());
        //Toast.makeText(mContext, mData.get(position).getTitle(), Toast.LENGTH_SHORT).show();

        setNetworkImage(holder, networkname);

    }


    private void setNetworkImage(MyViewHolder holder, String tag) {
        String networkname = tag;
        if (networkname.equalsIgnoreCase("mtn")) {
            Glide.with(mContext).load(R.drawable.mtn_logo).into(holder.imgNetwork);
            holder.txtUssdTitle.setTextColor(Color.parseColor("#F57F17"));
            networkcolor = Color.parseColor("#F57F17");

        } else if (networkname.equalsIgnoreCase("airtel")) {
            Glide.with(mContext).load(R.drawable.airtel_logo).into(holder.imgNetwork);
            holder.txtUssdTitle.setTextColor(Color.parseColor("#E8252D"));
            networkcolor = Color.parseColor("#E8252D");

        } else if (networkname.equalsIgnoreCase("glo")) {
            Glide.with(mContext).load(R.drawable.glo_logo).into(holder.imgNetwork);
            holder.txtUssdTitle.setTextColor(Color.parseColor("#174A12"));
            networkcolor = Color.parseColor("#174A12");

        } else if (networkname.equalsIgnoreCase("9mobile")) {
            Glide.with(mContext).load(R.drawable.nmobile_logo).into(holder.imgNetwork);
            holder.txtUssdTitle.setTextColor(Color.parseColor("#0D1D10"));
            networkcolor = Color.parseColor("#0D1D10");

        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtUssdTitle;
        ImageView imgNetwork;
        ImageView imgInformetion;


        public MyViewHolder(final View itemView) {
            super(itemView);
            txtUssdTitle = itemView.findViewById(R.id.txt_NetworkTitle);
            imgNetwork = itemView.findViewById(R.id.img_logo);
            imgInformetion = itemView.findViewById(R.id.img_information);

            final Context context = itemView.getContext();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    NetworksCode cpu = (NetworksCode) view.getTag();
//                    Intent i = new Intent(view.getContext(), MainActivity.class);
//                    i.putExtra("desc", cpu.getCode());
//                    i.putExtra("title", cpu.getName());
//                    view.getContext().startActivity(i);
                    Toast.makeText(context, cpu.getTitle(), Toast.LENGTH_SHORT).show();
                    Methods method = new Methods();
                    method.DialUssdCode((ListUssdActivity)context, cpu.getCode(), context, SimNo);



                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    NetworksCode cpu = (NetworksCode) v.getTag();
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    Bundle args = new Bundle();
                    args.putString("title", cpu.getTitle());
                    args.putString("desc", cpu.getDesc());
                    args.putString("code", cpu.getCode());
                    args.putString("networkname", networkname);
                    args.putInt("networkcolor", networkcolor);
                    args.putInt("simno", SimNo);

                    bottomSheetFragment.setArguments(args);
                    bottomSheetFragment.show(((FragmentActivity) itemView.getContext()).getSupportFragmentManager(), bottomSheetFragment.getTag());

                    return true;
                }
            });


            imgInformetion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    NetworksCode cpu = (NetworksCode) v.getTag();
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    Bundle args = new Bundle();
                    args.putString("title", cpu.getTitle());
                    args.putString("desc", cpu.getDesc());
                    args.putString("code", cpu.getCode());
                    args.putString("networkname", networkname);
                    args.putInt("networkcolor", networkcolor);

                    bottomSheetFragment.setArguments(args);
                    bottomSheetFragment.show(((FragmentActivity) itemView.getContext()).getSupportFragmentManager(), bottomSheetFragment.getTag());


                }
            });

        }
    }


}

