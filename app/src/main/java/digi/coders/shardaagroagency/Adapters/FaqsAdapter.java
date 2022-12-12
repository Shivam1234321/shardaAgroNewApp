package digi.coders.shardaagroagency.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Model.CouponsModel;
import digi.coders.shardaagroagency.Model.FaqModel;
import digi.coders.shardaagroagency.R;

public class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.ViewHolder> {


    ArrayList<FaqModel> str;
    Context ctx;
    public FaqsAdapter(ArrayList<FaqModel> str, Context ctx) {
        this.str = str;
        this.ctx = ctx;

    }



    @NonNull
    @Override
    public FaqsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.faqs_layout,parent,false);
        return new FaqsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FaqsAdapter.ViewHolder holder, final int position) {


        holder.setIsRecyclable(false);

        holder.qes.setText(str.get(position).getQusestion());
        holder.ans.setText(str.get(position).getAns());


    }

    @Override
    public int getItemCount() {
        return str.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialRippleLayout card;

        TextView qes,ans;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qes=itemView.findViewById(R.id.qes);
            ans=itemView.findViewById(R.id.ans);

        }
    }

}
