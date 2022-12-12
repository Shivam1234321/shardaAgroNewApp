package digi.coders.shardaagroagency.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.shardaagroagency.Model.SliderModel;
import digi.coders.shardaagroagency.R;


public class OffersSliderAdapter extends PagerAdapter {

    List<SliderModel> dataList;
    Context ctx;

    public OffersSliderAdapter(Context ctx, List<SliderModel> dataList) {

        this.ctx = ctx;
        this.dataList = dataList;
    }

    private LayoutInflater layoutInflater;

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.item_card_payment,container,false);
        ImageView img=view.findViewById(R.id.card_logo);
        Picasso.get().load(dataList.get(position).getBanner()).placeholder(R.drawable.logo_sharda).into(img);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView (view);
        return view;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}


