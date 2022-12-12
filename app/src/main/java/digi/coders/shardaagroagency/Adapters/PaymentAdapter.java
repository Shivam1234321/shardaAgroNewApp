package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Model.PaymentModel;
import digi.coders.shardaagroagency.R;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    ArrayList<PaymentModel> arrayList;
    Context context;

    public PaymentAdapter(ArrayList<PaymentModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentModel model=arrayList.get(position);

        holder.name.setText(model.getName());
        holder.amt.setText("₹"+model.getAmount());
        holder.date.setText(model.getDateTime());
        holder.remark.setText("Remark : "+model.getRemark());
        if (model.getStatus().equals("true")){
            holder.status.setText("Payment Status : Success");
        }else {
            holder.status.setText("Payment Status : Failed");
        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView amt,name,date,remark,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amt=itemView.findViewById(R.id.amt);
            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            remark=itemView.findViewById(R.id.remark);
            status=itemView.findViewById(R.id.status);
        }
    }
}
