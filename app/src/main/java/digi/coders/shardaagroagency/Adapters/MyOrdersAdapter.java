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
import digi.coders.shardaagroagency.Model.CartItemModel;
import digi.coders.shardaagroagency.Model.OrderModel;
import digi.coders.shardaagroagency.R;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    ArrayList<OrderModel> arrayList;
    Context ctx;

    public MyOrdersAdapter(ArrayList<OrderModel> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_details,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.ViewHolder holder, final int position) {

        final OrderModel model=arrayList.get(position);



        holder.status.setVisibility(View.VISIBLE);
        holder.layout_transport.setVisibility(View.VISIBLE);
        holder.status.setText("Order "+model.getStatus());
        holder.name.setText(Html.fromHtml("<b>Transporter Name : "+"</b>"+model.getName()));
        holder.id.setText(Html.fromHtml("<b>Transporter ID : "+"</b>"+model.getTransportrterid()));
        holder.description.setText(Html.fromHtml("<b>Description : "+"</b>"+model.getDesc()));
        holder.productName.setText("ORDER ID : "+model.getOrderid());
        holder.qty.setText("Rs. "+model.getAmount());
        holder.total_amount.setText(model.getDate());
        holder.transport_name.setText("Transport name : "+model.getTransportName());
        holder.del_date.setText("Delivery Date : "+model.getDeliveryDate());
        holder.grno_nos.setText("GR No : "+model.getGRNO()+"     -   NOS :"+model.getNOS());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOredrsDeatils.id=model.getOrderid();
                Intent intent =new Intent(ctx, MyOredrsDeatils.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView productName,qty,total_amount,transport_name,grno_nos,del_date;
        CardView card;

        LinearLayout layout_transport;
        TextView status,name,id,description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qty=itemView.findViewById(R.id.qty);
            card=itemView.findViewById(R.id.card);
            productName=itemView.findViewById(R.id.productName);
            total_amount=itemView.findViewById(R.id.cutAmount);
            layout_transport=itemView.findViewById(R.id.layout_transport);
            status=itemView.findViewById(R.id.orderStatus);
            name=itemView.findViewById(R.id.name);
            id=itemView.findViewById(R.id.id);
            description=itemView.findViewById(R.id.description);
            transport_name=itemView.findViewById(R.id.transport_name);
            grno_nos=itemView.findViewById(R.id.grno_nos);
            del_date=itemView.findViewById(R.id.del_date);

        }
    }
}
