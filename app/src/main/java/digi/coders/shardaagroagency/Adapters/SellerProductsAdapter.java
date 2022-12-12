package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

import digi.coders.shardaagroagency.Activities.SellerQuoteActivity;
import digi.coders.shardaagroagency.Helper.RefreshData;
import digi.coders.shardaagroagency.Model.RequirementModel;
import digi.coders.shardaagroagency.R;

public class SellerProductsAdapter extends RecyclerView.Adapter<SellerProductsAdapter.ViewHolder> {

    ArrayList<RequirementModel> arrayList;
    public static ArrayList<RequirementModel> listforReqdata;
    Context ctx;
    RefreshData refreshData;

    public static String[] str;

    public SellerProductsAdapter(ArrayList<RequirementModel> arrayList, Context ctx, RefreshData refreshData) {
        this.arrayList = arrayList;
        this.ctx = ctx;
        this.refreshData=refreshData;
        str=new String[arrayList.size()];
        listforReqdata=new ArrayList<>(arrayList.size());
    }

    @NonNull
    @Override
    public SellerProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_product_layout, parent, false);

        return new SellerProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SellerProductsAdapter.ViewHolder holder, final int position) {


        holder.productName.setText(arrayList.get(position).getProductName());
        holder.quantity.setText(arrayList.get(position).getProductQuantity());
        holder.txt_avail.setText(arrayList.get(position).getProductRequirementType());

        str[position] = "";
        listforReqdata.add(null);

        holder.checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                            str[position] = arrayList.get(position).getRequirementId();
                            listforReqdata.set(position,new RequirementModel(arrayList.get(position).getRequirementId(), arrayList.get(position).getProductName(),
                                    holder.quantity.getText().toString(), arrayList.get(position).getProductRequirementType()));
                            refreshData.onRefresh(isChecked);
                            Log.i("Anand Debug if",arrayList.get(position).getProductName()+"   "+listforReqdata);

                        }else{
                            str[position]="";
                            listforReqdata.remove(position);
                            refreshData.onRefresh(isChecked);
                            Log.i("Anand Debug else",arrayList.get(position).getProductName()+"  "+listforReqdata);

                        }
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView productName;
        TextView txt_avail;
        TextView avalibility;
        LinearLayout line;

        EditText quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            line=itemView.findViewById(R.id.line);
            quantity=itemView.findViewById(R.id.quantity);
            avalibility=itemView.findViewById(R.id.availibility);
            productName=itemView.findViewById(R.id.productName);
            checkBox=itemView.findViewById(R.id.checkbox);
            txt_avail=itemView.findViewById(R.id.txt_avail);
        }
    }
}
