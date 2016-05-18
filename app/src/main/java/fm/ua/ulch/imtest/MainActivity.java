package fm.ua.ulch.imtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager pager;
    private ArrayList<String> urls;
    static int screenHeight;
    static int screenWidth;
    static DisplayMetrics metrics;
    WindowManager w;
    ImageView ball;
    ImageView indicator;
    Animation translateAnimation;
    int pos = 0;
    int deltaX = 0;
    int prevDeltaX = 0;
    int pagePos;


    //    PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ball = (ImageView) findViewById(R.id.ball);
        indicator = (ImageView) findViewById(R.id.indicator);
//        int indWidth = indicator.getLayoutParams().width;
        final int indWidth = indicator.getDrawable().getMinimumWidth();
        final int ballWidth = ball.getDrawable().getMinimumWidth();
        Log.d("Resolution", "indicator: " + indWidth);
//        int indWidth1 = indicator.getDrawable().getIntrinsicHeight();
//        Log.d("Resolution", "indicator1: " + indWidth1);


//        anim = AnimationUtils.loadAnimation(this, R.anim.trans);
//        ball.startAnimation(anim);

//        ball.setTranslationX(deltaX);

        w = getWindowManager();
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels - getStatusBarHeight();
        screenWidth = metrics.widthPixels;
        Log.d("Resolution", "resolution: " + screenWidth + " x " + screenHeight);

        pager = (ViewPager) findViewById(R.id.pager);

        urls = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String url = "http://www.o-prirode.com/News/150/realnye-foto-" + i + ".jpg";
            urls.add(url);
        }
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                int deltaX = 0;
//                int prevDeltaX = 0;
                int animSign = 1;

                if (pos < position) {
                    deltaX += (indWidth - ballWidth / 2) / (urls.size()+1);
//                    animSign = 1/(urls.size());

                } else if (pos > position) {
                    deltaX += -(indWidth - ballWidth / 2) / (urls.size()+1);
                    animSign = -1;

                }



                translateAnimation = new TranslateAnimation(
                        Animation.ABSOLUTE,  prevDeltaX,
//                        animSign *
                        animSign * Animation.ABSOLUTE, deltaX,
//                        Animation.RELATIVE_TO_SELF, animDelta,
//                        Animation.RELATIVE_TO_PARENT, animDelta,
                        0, 0,
                        0, 0
                );
                translateAnimation.setDuration(1000);
                translateAnimation.setFillAfter(true);



                ball.startAnimation(translateAnimation);
//                ball.setTranslationX(deltaX);

                Log.d("Position", "position: " + position);
                Log.d("Position", "pos: " + pos);
                Log.d("Position", "deltaX: " + deltaX);
                Log.d("Position", "urls.size(): " + urls.size());
                Log.d("Position", "1/(urls.size(): " + (float) 1/(urls.size()));

                pos = position;
                prevDeltaX = deltaX;
                translateAnimation = null;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(pager, urls);
    }

    private void setupViewPager(ViewPager viewPager, ArrayList<String> list) {

        Adapter adapter = new Adapter(getSupportFragmentManager());
//        Adapter adapter = new Adapter(getSupportFragmentManager(), this.getApplicationContext());
        for (int i = 0; i < list.size(); i++) {
            adapter.addFragment(FirstFragment.newInstance(list.get(i)), String.valueOf(i));
        }
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater;

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }
//        public Adapter(FragmentManager fm, Context context) {
//            super(fm);
//            mContext = context;
//            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//
//            SpannableStringBuilder sb = new SpannableStringBuilder(" Page " + (position + 1)); // space added before text for convenience
//
//            Drawable drawable = mContext.getResources().getDrawable( R.drawable.ball);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
//            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            return sb;
//        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
