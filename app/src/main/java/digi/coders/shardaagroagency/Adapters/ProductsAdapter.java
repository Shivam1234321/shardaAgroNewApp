package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.graphics.Color;
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

import digi.coders.shardaagroagency.Helper.RefreshData;
import digi.coders.shardaagroagency.Model.ProductModel;
import digi.coders.shardaagroagency.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    ArrayList<ProductModel> arrayList;
    public static ArrayList<ProductModel> listfordata;
    Context ctx;
    RefreshData refreshData;

   public static String[] str;

    public ProductsAdapter(ArrayList<ProductModel> arrayList, Context ctx,RefreshData refreshData) {
        this.arrayList = arrayList;
        this.ctx = ctx;
        this.refreshData=refreshData;
        str=new String[arrayList.size()];
        listfordata=new ArrayList<>(arrayList.size());
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);

        return new ProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsAdapter.ViewHolder holder, final int position) {


        holder.productName.setText(arrayList.get(position).getName());

        str[position]="";
        holder.quantity.setVisibility(View.GONE);
        listfordata.add(null);

        if(arrayList.get(position).getStock().equalsIgnoreCase("yes")){

            holder.avalibility.setBackgroundResource(R.drawable.rounded_green);
            holder.txt_avail.setText("In Stock");
            holder.txt_avail.setTextColor(Color.parseColor("#66af3a"));

           // holder.quantity.setText("1");
        }else{
            holder.avalibility.setBackgroundResource(R.drawable.round_red);
            holder.txt_avail.setText("Transit");
            holder.txt_avail.setTextColor(Color.parseColor("#FE0303"));

//            holder.quantity.setText("0");
//            holder.quantity.setEnabled(false);
        }

//        if(position%2==0){
//            holder.line.setBackgroundColor(Color.parseColor("#BAB8FBBC"));
//        }else{
//            holder.line.setBackgroundColor(Color.parseColor("#AEFECECE"));
//        }

//        holder.quantity.addTextChangedListener(new
//                                                       TextWatcher() {
//                                                           @Override
//                                                           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                                                           }
//
//                                                           @Override
//                                                           public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                                                                 }
//
//                                                           @Override
//                                                           public void afterTextChanged(Editable s) {
//                                                               if(holder.checkBox.isChecked()) {
//                                                                   str[position] = arrayList.get(position).getId();
//                                                                   listfordata.add(position, new ProductModel(arrayList.get(position).getId(), arrayList.get(position).getName(), holder.quantity.getText().toString()));
//                                                                   refreshData.onRefresh(true);
//                                                               }else{
//                                                                   str[position] = arrayList.get(position).getId();
//                                                                   listfordata.add(position, new ProductModel(arrayList.get(position).getId(), arrayList.get(position).getName(), holder.quantity.getText().toString()));
//                                                                   refreshData.onRefresh(true);
//                                                                   holder.checkBox.setChecked(true);
//                                                               }
//                                                           }
//                                                       });

        holder.checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            if(arrayList.get(position).getStock().equalsIgnoreCase("yes")) {
                                str[position] = arrayList.get(position).getId();
                                listfordata.add(position,new ProductModel(arrayList.get(position).getId(),arrayList.get(position).getName(),holder.quantity.getText().toString()));
                                refreshData.onRefresh(isChecked);
                            }else{
                                holder.checkBox.setChecked(false);
                                TastyToast.makeText(ctx, "This Product is out of Stock !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                //Toast.makeText(ctx,"This Product is out of Stock !",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            str[position]="";
                            listfordata.remove(position);
                            refreshData.onRefresh(isChecked);
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

            quantity=itemView.findViewById(R.id.quantity);
            line=itemView.findViewById(R.id.line);
            avalibility=itemView.findViewById(R.id.availibility);
            productName=itemView.findViewById(R.id.productName);
            checkBox=itemView.findViewById(R.id.checkbox);
            txt_avail=itemView.findViewById(R.id.txt_avail);
        }
    }
}
