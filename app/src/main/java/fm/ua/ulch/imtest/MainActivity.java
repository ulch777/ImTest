package fm.ua.ulch.imtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private ViewPager pager;
   private ArrayList<String> urls;

    //    PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);

        urls = new ArrayList<>();
        for(int i = 1; i <= 15; i++) {
            String url = "http://www.o-prirode.com/News/150/realnye-foto-" + i + ".jpg";
            urls.add(url);
        }
        setupViewPager(pager,urls);
    }

    private void setupViewPager(ViewPager viewPager, ArrayList<String> list) {

        Adapter adapter = new Adapter(getSupportFragmentManager());
        for (int i = 0; i < list.size(); i++) {
            adapter.addFragment(FirstFragment.newInstance(list.get(i)), String.valueOf(i));
        }
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

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
    }
}
