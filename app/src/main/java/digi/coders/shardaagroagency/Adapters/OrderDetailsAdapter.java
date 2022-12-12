package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Model.OrderDetails;
import digi.coders.shardaagroagency.R;
import digi.coders.shardaagroagency.databinding.OrderDetailsAdapterBinding;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    ArrayList<OrderDetails> arrayList;
    Context context;

    public OrderDetailsAdapter(ArrayList<OrderDetails> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  OrderDetailsAdapter.ViewHolder holder, int position) {
        OrderDetails model =arrayList.get(position);

        holder.binding.name.setText(model.getProductName());

        holder.binding.price.setText("₹"+model.getProductPrice());

        holder.binding.quantity.setText("QTY : "+model.getQuantity());

        Picasso.get().load(model.getProductImage()).into(holder.binding.image);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OrderDetailsAdapterBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=OrderDetailsAdapterBinding.bind(itemView);
        }
    }
}
