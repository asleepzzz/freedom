package application.com.a30togo.freedom;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends Activity {

    private android.support.design.widget.TabLayout mTabs;
    private int tabIndex;
    private ViewPager mViewPager;
    private View mPagerView;
    private int ANDROID_ACCESS_INSTAGRAM_WEBSERVICES = 001;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.getData().get("result");
            String obj = (String) msg.obj;//
            //activity_main_btn1.setText("请求结果为："+result);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
        mTabs.addTab(mTabs.newTab().setText("Download IG Pic"));
        mTabs.addTab(mTabs.newTab().setText("Stored Pictures"));
        mTabs.addTab(mTabs.newTab().setText("Easy Download"));
        mTabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorInstagram));
        mTabs.setTabTextColors(Color.GRAY,getResources().getColor(R.color.colorInstagram));

        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabIndex = tab.getPosition();
                mViewPager.setCurrentItem(tabIndex);
                if (tabIndex == 1) {
                    Thread accessWebServiceThread = new Thread(new WebServiceHandler());
                    accessWebServiceThread.start();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            mPagerView = view;
            container.addView(view);
            TextView title = (TextView) view.findViewById(R.id.item_title);
            title.setText(String.valueOf(position + 1));
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    class WebServiceHandler implements Runnable{
        @Override
        public void run() {
            instagramDownloader.download("", getApplicationContext());
            Looper.prepare();
            String result = "complete";
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("result", result);
            message.what = ANDROID_ACCESS_INSTAGRAM_WEBSERVICES;//设置消息标示
            message.obj = "zxn";
            message.setData(bundle);//消息内容
            handler.sendMessage(message);//发送消息
            Looper.loop();
        }

    }
}
