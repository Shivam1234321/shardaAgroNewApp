package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Helper.RefreshData;
import digi.coders.shardaagroagency.Model.ChatsModel;
import digi.coders.shardaagroagency.Model.ProductModel;
import digi.coders.shardaagroagency.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<ChatsModel> arrayList;
    Context ctx;

    public ChatAdapter(ArrayList<ChatsModel> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout,parent,false);

        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {


        holder.orderID.setText("Order ID : "+arrayList.get(position).getOrderID());
        String[] str=arrayList.get(position).getProductName().split("sharda");
        String[] qty=arrayList.get(position).getId().split("sharda");

        String name="";
        if(str.length<=1){
            if(!arrayList.get(position).getId().equalsIgnoreCase("")) {
                holder.productName.setText(arrayList.get(position).getProductName() + "  >  " + arrayList.get(position).getId());
            }else{
                holder.productName.setText(arrayList.get(position).getProductName());
            }
        }else {
            for (int i = 0; i < str.length; i++) {
                if(i==0){
                    name=(i+1)+". "+str[i]+"  >  "+qty[i]+"\n";
                }else if(i==str.length-1){
                    name = name + (i+1)+". "+ str[i]+"  >  "+qty[i];
                }else{
                    name = name + (i+1)+". "+ str[i]+"  >  "+qty[i]+"\n";
                }
            }
            holder.productName.setText(name);
        }
        holder.dateTime.setText(arrayList.get(position).getDateTime());
        if(arrayList.get(position).getReply().equalsIgnoreCase("")) {
            holder.reply_layout.setVisibility(View.GONE);
        }else{
            holder.reply_layout.setVisibility(View.VISIBLE);
            holder.reply.setText(arrayList.get(position).getReply());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView orderID,productName,dateTime,reply;

        LinearLayout reply_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderID=itemView.findViewById(R.id.OrderID);
            productName=itemView.findViewById(R.id.productsName);
            dateTime=itemView.findViewById(R.id.dateTime);
            reply_layout=itemView.findViewById(R.id.reply_layout);
            reply=itemView.findViewById(R.id.reply);

        }
    }
}
