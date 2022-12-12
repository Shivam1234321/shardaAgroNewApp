package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Model.CartItemModel;
import digi.coders.shardaagroagency.Model.ChatsModel;
import digi.coders.shardaagroagency.R;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.ViewHolder> {

    ArrayList<CartItemModel> arrayList;
    Context ctx;

    public BillingAdapter(ArrayList<CartItemModel> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public BillingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_details,parent,false);

        return new BillingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingAdapter.ViewHolder holder, int position) {


        int oldprice=Integer.parseInt(arrayList.get(position).getQty())*Integer.parseInt(arrayList.get(position).getOldPrice());
        int offerprice=Integer.parseInt(arrayList.get(position).getQty())*Integer.parseInt(arrayList.get(position).getPrice());

        holder.productName.setText(arrayList.get(position).getName());
        holder.amount.setText("Rs. "+oldprice);
        holder.total_amount.setText(Html.fromHtml("<strike>Rs. " + offerprice + "</strike>"));
        holder.qty.setText("Quantity : "+arrayList.get(position).getQty());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView productName,qty,amount,total_amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qty=itemView.findViewById(R.id.qty);
            productName=itemView.findViewById(R.id.productName);
            amount=itemView.findViewById(R.id.amount);
            total_amount=itemView.findViewById(R.id.cutAmount);

        }
    }
}
