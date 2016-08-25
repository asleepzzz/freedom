package application.com.a30togo.freedom;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private android.support.design.widget.TabLayout mTabs;
    private int tabIndex;
    private ViewPager mViewPager;
    private View mPagerView;
    private EditText editText;
    private Button okBtn;
    private ImageButton mRefresh;
    private GridView mGrid;
    private int ANDROID_ACCESS_INSTAGRAM_WEBSERVICES = 001;
    private ListView mDrawerList;
    private Switch mSwitch;

    private String[] mPlanetTitles;

    private TextView title ;
    private LinearLayout instagram_download ;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.getData().get("result");
            String obj = (String) msg.obj;//
            if (result.equals("complete")) {
                //Toast.makeText(getApplicationContext(),"download complete",Toast.LENGTH_SHORT).show();
                showSdcardAlbum.refresh(MainActivity.this,mGrid);
            }
            //activity_main_btn1.setText("请求结果为："+result);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
                if (tabIndex==1) {
                    //Log.e("kevin","press update");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mPlanetTitles = new String[]{"this app can help you to download instagram picture"};//, "two", "three"};

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));


        ClipMonitorService.startMyIntentService(getApplicationContext());
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
            title = (TextView) view.findViewById(R.id.item_title);
            mSwitch = (Switch) view.findViewById(R.id.mySwitch);

            instagram_download = (LinearLayout)mPagerView.findViewById(R.id.instagram_download);
            mGrid = (GridView)mPagerView.findViewById(R.id.gridview);
            mRefresh = (ImageButton)mPagerView.findViewById(R.id.refreshIcon);
            if (position == 0) {
                instagram_download.setVisibility(View.VISIBLE);
                title.setVisibility(View.GONE);
                mRefresh.setVisibility(View.GONE);
                mSwitch.setVisibility(View.GONE);
                mGrid.setVisibility(View.GONE);
                editText = (EditText) mPagerView.findViewById(R.id.inputUrl);
                if (showSdcardAlbum.imageItems.size()==0) {
                    editText.setText("https://www.instagram.com/p/BFsr8QwwZEV/");
                }

                okBtn = (Button) mPagerView.findViewById(R.id.save_button);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!instagramDownloader.isIGUrl(editText.getText().toString())) {
                            Toast.makeText(getApplicationContext(),"please input correct url",Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getApplicationContext(),"downloading",Toast.LENGTH_SHORT).show();
                            Thread accessWebServiceThread = new Thread(new WebServiceHandler(editText.getText().toString()));
                            accessWebServiceThread.start();
                            editText.setText("");
                        }
                    }
                });
            } else if (position == 1) {
                instagram_download.setVisibility(View.GONE);
                mRefresh.setVisibility(View.VISIBLE);
                mSwitch.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                mGrid.setVisibility(View.VISIBLE);
                showSdcardAlbum.show(MainActivity.this,mGrid);
                mRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSdcardAlbum.refresh(MainActivity.this,mGrid);
                    }
                });
            } else {
                instagram_download.setVisibility(View.GONE);
                mRefresh.setVisibility(View.GONE);
                mSwitch.setVisibility(View.VISIBLE);
                mGrid.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                title.setText("Just Press Copy Url in Instagram app, you can find the picture in album");
                title.setTextSize(13);


                mSwitch.setChecked(ClipMonitorService.isIsEverEnabled(getApplicationContext()));
                mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            ClipMonitorService.enableMyIntentService(getApplicationContext());
                        } else {
                            ClipMonitorService.disableMyIntentService(getApplicationContext());
                        }
                    }
                });
            }
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    class WebServiceHandler implements Runnable{

        private String igUrl;
        public WebServiceHandler (String url) {
            igUrl = url;
        }
        @Override
        public void run() {
            instagramDownloader.download(igUrl, getApplicationContext());
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
