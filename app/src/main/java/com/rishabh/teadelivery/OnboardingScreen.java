package com.rishabh.teadelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rishabh.teadelivery.Adapters.SliderAdapter;

public class OnboardingScreen extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;

    SliderAdapter sliderAdapter;
    TextView[] dots;

    Button letsGetStarted;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);

        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsGetStarted = findViewById(R.id.get_started_btn);

        sliderAdapter = new SliderAdapter(this);

        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);


    }


    private void addDots(int position) {
        dots =new TextView[4];
        dotsLayout.removeAllViews();

        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void let_get_started(View view) {
        Intent intent = new Intent( OnboardingScreen.this , LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void next(View view) {

        viewPager.setCurrentItem(currentPos+1);
    }

    public void skip(View view) {

        Intent intent = new Intent( OnboardingScreen.this , LoginActivity.class);
        startActivity(intent);
        finish();
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);
            currentPos = position;

            if (position == 0) {
                letsGetStarted.setVisibility(View.INVISIBLE);

            } else if (position == 1) {
                letsGetStarted.setVisibility(View.INVISIBLE);

            } else if (position == 2) {
                letsGetStarted.setVisibility(View.INVISIBLE);

            } else {
                letsGetStarted.setVisibility(View.VISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };
}