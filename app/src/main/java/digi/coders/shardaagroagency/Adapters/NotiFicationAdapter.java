package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Helper.RefreshData;
import digi.coders.shardaagroagency.Model.NotificatioModel;
import digi.coders.shardaagroagency.Model.ProductModel;
import digi.coders.shardaagroagency.R;

public class NotiFicationAdapter extends RecyclerView.Adapter<NotiFicationAdapter.ViewHolder> {

    ArrayList<NotificatioModel> arrayList;
    Context ctx;

    public NotiFicationAdapter(ArrayList<NotificatioModel> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public NotiFicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false);

        return new NotiFicationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiFicationAdapter.ViewHolder holder, final int position) {


        holder.title.setText(arrayList.get(position).getTitle());
        holder.date.setText(arrayList.get(position).getDate());
        holder.desc.setText(arrayList.get(position).getDesc());
        if(arrayList.get(position).getStatus().equalsIgnoreCase("true")){
            holder.download.setVisibility(View.VISIBLE);
        }else{
            holder.download.setVisibility(View.GONE);
        }
        holder.download.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(position).getPdf()));
                        ctx.startActivity(browserIntent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        TextView title,desc,date;
        Button download;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.description);
            download=itemView.findViewById(R.id.download);
            date=itemView.findViewById(R.id.date);
        }
    }
}
