package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Activities.MyOredrsDeatils;
import digi.coders.shardaagroagency.Model.MyRequirementsModel;
import digi.coders.shardaagroagency.Model.MyRequirementsProModel;
import digi.coders.shardaagroagency.Model.OrderModel;
import digi.coders.shardaagroagency.R;

public class MyReqProductAdapter extends RecyclerView.Adapter<MyReqProductAdapter.ViewHolder> {

    ArrayList<MyRequirementsProModel> arrayList;
    Context ctx;

    public MyReqProductAdapter(ArrayList<MyRequirementsProModel> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public MyReqProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_req_product,parent,false);

        return new MyReqProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReqProductAdapter.ViewHolder holder, final int position) {

        holder.productName.setText(arrayList.get(position).getName());
        holder.qty.setText(arrayList.get(position).getQuantity());
        holder.rate.setText("₹ "+arrayList.get(position).getRate());
        holder.available.setText(arrayList.get(position).getAvailability());
        holder.remark.setText(arrayList.get(position).getRemark());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView productName,qty,rate,available,remark;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card=itemView.findViewById(R.id.card);
            productName=itemView.findViewById(R.id.productName);
            qty=itemView.findViewById(R.id.qty);
            rate=itemView.findViewById(R.id.rate);
            available=itemView.findViewById(R.id.available);
            remark=itemView.findViewById(R.id.remark);

        }
    }
}
