package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Model.ChattingModel;
import digi.coders.shardaagroagency.R;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ViewHolder> {
    ArrayList<ChattingModel> arrayList;
    Context context;

    public ChattingAdapter(ArrayList<ChattingModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_layout,parent,false);
        return new ChattingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChattingModel model=arrayList.get(position);
        if (model.getMsgBy().equals("user")){
            holder.senderText.setText(model.getMsg());
            holder.line_receiver.setVisibility(View.GONE);
            holder.senderDate.setText(model.getDateTime());

        }else {
            holder.receiverText.setText(model.getMsg());
            holder.line_sender.setVisibility(View.GONE);
            holder.receiverDate.setText(model.getDateTime());

        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView senderText,receiverText,receiverDate,senderDate;
        LinearLayout line_receiver,line_sender;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            senderText=itemView.findViewById(R.id.senderText);
            receiverText=itemView.findViewById(R.id.receiverText);
            line_sender=itemView.findViewById(R.id.line_sender);
            line_receiver=itemView.findViewById(R.id.line_receiver);
            senderDate=itemView.findViewById(R.id.senderDate);
            receiverDate=itemView.findViewById(R.id.receiverDate);

        }
    }
}
