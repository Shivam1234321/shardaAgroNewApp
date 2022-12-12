package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import digi.coders.shardaagroagency.Helper.Constants;
import digi.coders.shardaagroagency.Helper.RefreshData;
import digi.coders.shardaagroagency.Model.ProductModel;
import digi.coders.shardaagroagency.Model.RequirementModel;
import digi.coders.shardaagroagency.R;

public class SellerAfterSelectAdapter extends RecyclerView.Adapter<SellerAfterSelectAdapter.ViewHolder> {

    ArrayList<RequirementModel> arrayList;
    Context ctx;
    RefreshData refreshData;
    public static List<String> ratelist;
    public static List<String> availablebilitylist;
    public static List<String> stockType;
    public static List<String> remarklist;
    public static List<String> productid;

//    public static String qty="";
//    public static String[] quantity;

    public SellerAfterSelectAdapter(Context ctx, ArrayList<RequirementModel> arrayList) {
        this.arrayList = arrayList;
        //quantity=new String[arrayList.size()];
        ratelist = new ArrayList<>();
        availablebilitylist = new ArrayList<>();
        stockType = new ArrayList<>();
        remarklist = new ArrayList<>();
        productid = new ArrayList<>();
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public SellerAfterSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_after_select_layout, parent, false);
        return new SellerAfterSelectAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final SellerAfterSelectAdapter.ViewHolder holder, final int position) {

        holder.productName.setText((position + 1) + ". " + arrayList.get(position).getProductName());
        holder.quantity.setText(arrayList.get(position).getProductQuantity());
        ratelist.add("null");
        availablebilitylist.add("null");
        remarklist.add("null");
        stockType.add("null");
        productid.add(arrayList.get(position).getRequirementId());

        holder.rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    ratelist.set(position, "null");
                } else {
                    ratelist.set(position, editable.toString());
                }
            }
        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item, new String[]{"Ready", "On Order"});
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.available.setAdapter(dataAdapter);
        holder.available.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                ((TextView) holder.available.getSelectedView()).setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                availablebilitylist.set(position, holder.available.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item, new String[]{"New Stock", "Old Stock"});
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.stock.setAdapter(dataAdapter1);
        holder.stock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                ((TextView) holder.stock.getSelectedView()).setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                stockType.set(position, holder.stock.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    remarklist.set(position, "null");
                } else {
                    remarklist.set(position, editable.toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, quantity;
        LinearLayout line;
        EditText rate, remark;
        Spinner available, stock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            quantity = itemView.findViewById(R.id.quantity);
            line = itemView.findViewById(R.id.line);
            productName = itemView.findViewById(R.id.productName);
            rate = itemView.findViewById(R.id.rate);
            available = itemView.findViewById(R.id.available);
            remark = itemView.findViewById(R.id.remark);
            stock = itemView.findViewById(R.id.stock);

        }
    }
}
