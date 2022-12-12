package digi.coders.shardaagroagency.Adapters;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sdsmdg.tastytoast.TastyToast;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Model.CouponsModel;
import digi.coders.shardaagroagency.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {


    ArrayList<CouponsModel> str;
    Context ctx;
    int status;
    public CouponsAdapter(ArrayList<CouponsModel> str, Context ctx) {
        this.str = str;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public CouponsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.coupons_layout,parent,false);
        return new CouponsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CouponsAdapter.ViewHolder holder, final int position) {


        holder.setIsRecyclable(false);


        holder.discount.setText(str.get(position).getDiscount()+"%  OFF");
        holder.desc.setText(str.get(position).getDesc());
        holder.code.setText(str.get(position).getCode());


    }

    @Override
    public int getItemCount() {
        return str.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView desc,code,discount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            desc=itemView.findViewById(R.id.desc);
            code=itemView.findViewById(R.id.code);
            discount=itemView.findViewById(R.id.discount);


        }
    }

}
