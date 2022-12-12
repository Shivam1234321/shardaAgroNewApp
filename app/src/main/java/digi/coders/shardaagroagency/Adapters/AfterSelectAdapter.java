package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AfterSelectAdapter extends RecyclerView.Adapter<AfterSelectAdapter.ViewHolder> {

    ArrayList<ProductModel> arrayList;
    Context ctx;
    RefreshData refreshData;

    public static String qty="";
    public static String[] quantity;

    public AfterSelectAdapter(Context ctx,ArrayList<ProductModel> arrayList) {
        this.arrayList = arrayList;
        quantity=new String[arrayList.size()];
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public AfterSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.after_select_layout,parent,false);

        return new AfterSelectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AfterSelectAdapter.ViewHolder holder, final int position) {


        holder.productName.setText((position+1)+". "+arrayList.get(position).getName());

        quantity[position]=holder.quantity.getText().toString();
        holder.quantity.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        quantity[position]=s.toString();
                    }
                }
        );
        if(position!=0){

            if(!qty.equalsIgnoreCase("")){
                qty=qty+"sharda"+holder.quantity.getText().toString();
            }else{
                qty=holder.quantity.getText().toString();
            }
        }else{
            qty=holder.quantity.getText().toString();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        LinearLayout line;
        EditText quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quantity=itemView.findViewById(R.id.quantity);
            line=itemView.findViewById(R.id.line);
            productName=itemView.findViewById(R.id.productName);

        }
    }
}
