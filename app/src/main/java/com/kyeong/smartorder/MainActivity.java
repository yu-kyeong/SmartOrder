package com.kyeong.smartorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    //장바구니 List , Adapter
    ArrayList<ContainData> list_view = new ArrayList<ContainData>();
    ArrayList<ContainOptionData> containList = new ArrayList<ContainOptionData>();

    //장바구니 총 합 변수
    TextView allCount , allSum , allCount2 , allSum2;
    //장바구니 상품 기존 정보
    String oriName , oriPrice , oriCount;
    //숨겨진 페이지가 열렸는지 확인하는 변수
    boolean isPageOpen = false;
    //애니메이션 변수
    Animation tranTopAnim;
    Animation tranBottomAnim;
    //page 변수
    ViewPager pager1;
    LinearLayout hiddenPage , hiddenPage2 , downButton;
    TabLayout tab1;
    ListView containerList;

    FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
    public static Context context_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_main = this;
        //viewList
        pager1 = (ViewPager) findViewById(R.id.pager);
        tab1 = (TabLayout) findViewById(R.id.tabLayout);
        setupViewPager(pager1);
        containerList = (ListView)findViewById(R.id.container_list);

        //장바구니 page , 총 가격
        hiddenPage = (LinearLayout)findViewById(R.id.hidden_page);
        hiddenPage2 = (LinearLayout)findViewById(R.id.hidden_page_2);
        downButton = (LinearLayout)findViewById(R.id.down_button);
        allCount = (TextView)findViewById(R.id.all_count);
        allCount2 = (TextView)findViewById(R.id.all_count_2);
        allSum = (TextView)findViewById(R.id.all_sum);
        allSum2 = (TextView)findViewById(R.id.all_sum_2);

        tab1.addTab(tab1.newTab().setText(""));
        tab1.addTab(tab1.newTab().setText(""));
        tab1.addTab(tab1.newTab().setText(""));
        tab1.addTab(tab1.newTab().setText(""));


        /*장바구니 Animation*/
        tranTopAnim = AnimationUtils.loadAnimation(this,R.anim.tranlate_top);
        tranBottomAnim = AnimationUtils.loadAnimation(this,R.anim.tranlate_bottom);


        //Animation이 시작됐는지 , 종료됐는지 감지
        SlidingPageAnimationListener animationListener = new SlidingPageAnimationListener();
        tranTopAnim.setAnimationListener(animationListener);
        tranBottomAnim.setAnimationListener(animationListener);
        //버튼누르면 container 나오기
        LinearLayout containBtn = (LinearLayout)findViewById(R.id.contain_button);
        ContainBtnListener containBtnListener = new ContainBtnListener();
        containBtn.setOnClickListener(containBtnListener);


        PageDownBtnListener pageDownBtnListener = new PageDownBtnListener();
        downButton.setOnClickListener(pageDownBtnListener);
        /////////////////////

        PagerListener listener = new PagerListener();
        pager1.setOnPageChangeListener(listener);

        SelectListener listener2 = new SelectListener();
        tab1.addOnTabSelectedListener(listener2);

        //teb 과 page 연결
        tab1.setupWithViewPager(pager1);

    }

    /*장바구니 List 생성*/
    public void containListAdapter(){
        class ContainListAdapter extends BaseAdapter{

            Intent intent = getIntent();
            BtnListener listener = new BtnListener();
            @Override
            public int getCount() {
                return list_view.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {

                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {


                if (view == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    view = inflater.inflate(R.layout.container_list, null);
                }
                //상품 id
                TextView name = (TextView)view.findViewById(R.id.contain_name);
                ImageView image = (ImageView)view.findViewById(R.id.contain_image);
                TextView price = (TextView)view.findViewById(R.id.contain_price);
                Button delete = (Button)view.findViewById(R.id.deleteObj);
                //상품 원가격 id
                TextView oName = (TextView)view.findViewById(R.id.oriName);
                TextView oPrice = (TextView)view.findViewById(R.id.oriPrice);
                TextView oCount = (TextView)view.findViewById(R.id.oriCount);
                //장바구니 상품 옵션
                TextView containOpName = (TextView)view.findViewById(R.id.containOpName);
                TextView containOpPrice = (TextView)view.findViewById(R.id.containOpPrice);
                TextView containOpCount = (TextView)view.findViewById(R.id.containOpCount);
                TextView won = (TextView)view.findViewById(R.id.won);

                //장바구니에 들어간 정보
                name.setText(list_view.get(i).getValue_n());
                image.setImageResource(list_view.get(i).getValue_i());
                price.setText(list_view.get(i).getValue_p());
                delete.setTag(i);
                //장바구니 상품의 기존 정보
                oName.setText("└\t"+list_view.get(i).getOriName());
                oPrice.setText(list_view.get(i).getOriPrice());
                oCount.setText(list_view.get(i).getOriCount());


                //옵션 추가했을시 set
                if (containList.get(i).getOpCount() != null ) {
                    Log.d("optin","== true");
                    //선택한 옵션 정보 set
                    containOpName.setText("└\t"+containList.get(i).getOpName());
                    containOpPrice.setText(containList.get(i).getOpPrice());
                    containOpCount.setText(containList.get(i).getOpCount());
                }else{
                    Log.d("optin","== false");
                    won.setVisibility(View.INVISIBLE);
                    containOpName.setText("");
                    containOpPrice.setText("");
                    containOpCount.setText("");
                }
                delete.setOnClickListener(listener);

                return view;
            }
        }
        ContainListAdapter listAdapter = new ContainListAdapter();
        containerList.setAdapter(listAdapter);
    }
    class BtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // 리스트의 인덱스 값을 추출한다.
            int position = (Integer)v.getTag();
            Log.d("position", String.valueOf(position));

            DeleteOption deleteOption = new DeleteOption();
            deleteOption.tag = position;
            FragmentManager manager = getSupportFragmentManager();
            deleteOption.show(manager , null);
        }
    }
    ////////////////////



    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new MenuList(), "COFFEE");
        adapter.addFragment(new MenuList2(), "NONCOFFEE");
        adapter.addFragment(new MenuList3(), "PAIKSCCINO");
        adapter.addFragment(new MenuList4(), "DESSERT");
        viewPager.setAdapter(adapter);
    }

    class SelectListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            pager1.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    class PagerListener implements ViewPager.OnPageChangeListener {
        @Override
        // View의 전환이 완료되었을 때 호출되는 메서드
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {

        }
    }

    class FragmentAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


    }

    class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isPageOpen){
                isPageOpen = false;
                Log.d("Animation", "false");
            }else{
                isPageOpen = true;
                Log.d("Animation", "true");
            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    class ContainBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isPageOpen){
                Log.d("Main","true?");
                containListAdapter();

                hiddenPage2.setVisibility(View.VISIBLE);
                hiddenPage2.startAnimation(tranTopAnim);
                hiddenPage.setVisibility(View.INVISIBLE);

            }else{
                Log.d("Main","false?");
                hiddenPage.startAnimation(tranBottomAnim);
            }

        }
    }

    class PageDownBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(isPageOpen){
                Log.d("Main_Down","true?");
                list_view.clear();
                adapter.notifyDataSetChanged();
                hiddenPage2.startAnimation(tranTopAnim);
                hiddenPage.setVisibility(View.INVISIBLE);



            }else{
                Log.d("Main_Down","false?");
                hiddenPage2.startAnimation(tranBottomAnim);
                hiddenPage2.setVisibility(View.INVISIBLE);
                hiddenPage.setVisibility(View.VISIBLE);


            }
        }
    }

}


