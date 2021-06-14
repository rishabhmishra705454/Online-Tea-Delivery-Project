package com.rishabh.teadelivery.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.rishabh.teadelivery.R;

public class SliderAdapter extends PagerAdapter {

    Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] = {
            R.drawable.ic_cup_of_tea,
            R.drawable.ic_cup_of_tea,
            R.drawable.ic_cup_of_tea,
            R.drawable.ic_cup_of_tea

    };

    String headings[] = {
            "Fast Delivery",
            "Family Pack",
            "Good Quality",
            "Proper Hygiene"
    };
    int descriptions[] = {
            R.string.our_mission_desc,
            R.string.harvesting_crop_desc,
            R.string.sell_crop_desc,
            R.string.search_machine_desc
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_layout, container, false);
        //hooks

        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView heading = view.findViewById(R.id.slider_heading);

        TextView desc = view.findViewById(R.id.slider_desc);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);

        desc.setText(descriptions[position]);

        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
