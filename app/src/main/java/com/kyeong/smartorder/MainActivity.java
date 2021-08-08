package com.kyeong.smartorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class MainActivity extends AppCompatActivity  {

    //장바구니 List , Adapter
    ArrayList<ContainData> list_view = new ArrayList<ContainData>();
    ArrayList<ContainOptionData> containList = new ArrayList<ContainOptionData>();

    //장바구니 총 합 변수
    TextView allCount , allSum , allCount2 , allSum2;
    //장바구니 상품 기존 정보
    TextView name;
    TextView price;
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
    //주문버튼 변수
    Button orderBtn;

    FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
    public static Context context_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_main = this;

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        // 앱에서 확인하지 말고 꼭 웹 사이트에서 확인하자. 앱의 application id 갖다 쓰면 안됨!!!

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

        //주문
        orderBtn = (Button)findViewById(R.id.order_button);

        OrderBtnListener orderBtnListener = new OrderBtnListener();
        orderBtn.setOnClickListener(orderBtnListener);

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
                name = (TextView)view.findViewById(R.id.contain_name);
                ImageView image = (ImageView)view.findViewById(R.id.contain_image);
                price = (TextView)view.findViewById(R.id.contain_price);
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

                //장바구니 컵 선택
                final Button multiUse = (Button)findViewById(R.id.multi_use);
                final Button disposable = (Button)findViewById(R.id.disposable);
                //장바구니 결제방식 선택
                final Button creditCard = (Button)findViewById(R.id.credit_card);
                final Button phonePayment = (Button)findViewById(R.id.phone_payment);
                final Button kakaoPay = (Button)findViewById(R.id.kakao_pay);

                //장바구니에 들어간 정보
                name.setText(list_view.get(i).getValue_n());
                image.setImageResource(list_view.get(i).getValue_i());
                price.setText(list_view.get(i).getValue_p());
                Log.d("price1",price.toString().replace(",",""));
                Log.d("price2",price.getText().toString().replace(",",""));
                delete.setTag(i);
                //장바구니 상품의 기존 정보
                oName.setText("└\t"+list_view.get(i).getOriName());
                oPrice.setText(list_view.get(i).getOriPrice());
                oCount.setText(list_view.get(i).getOriCount());

                multiUse.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        multiUse.setBackground(ContextCompat.getDrawable(getApplicationContext() ,R.drawable.button_change_style));
                        disposable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                        multiUse.setTextColor(getResources().getColorStateList(R.color.changeButton));
                        disposable.setTextColor(getResources().getColor(R.color.basicButton));
                    }
                });

                disposable.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        disposable.setBackground(ContextCompat.getDrawable(getApplicationContext() ,R.drawable.button_change_style));
                        multiUse.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));

                        disposable.setTextColor(getResources().getColorStateList(R.color.changeButton));
                        multiUse.setTextColor(getResources().getColor(R.color.basicButton));

                    }
                });

                creditCard.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        creditCard.setBackground(ContextCompat.getDrawable(getApplicationContext() ,R.drawable.button_change_style));
                        phonePayment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                        kakaoPay.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                        creditCard.setTextColor(getResources().getColorStateList(R.color.changeButton));
                        phonePayment.setTextColor(getResources().getColor(R.color.basicButton));
                        kakaoPay.setTextColor(getResources().getColor(R.color.basicButton));
                    }
                });

                phonePayment.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        phonePayment.setBackground(ContextCompat.getDrawable(getApplicationContext() ,R.drawable.button_change_style));
                        creditCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                        kakaoPay.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                        phonePayment.setTextColor(getResources().getColorStateList(R.color.changeButton));
                        creditCard.setTextColor(getResources().getColor(R.color.basicButton));
                        kakaoPay.setTextColor(getResources().getColor(R.color.basicButton));
                    }
                });

                kakaoPay.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        kakaoPay.setBackground(ContextCompat.getDrawable(getApplicationContext() ,R.drawable.button_change_style));
                        phonePayment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                        creditCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                        kakaoPay.setTextColor(getResources().getColorStateList(R.color.changeButton));
                        phonePayment.setTextColor(getResources().getColor(R.color.basicButton));
                        creditCard.setTextColor(getResources().getColor(R.color.basicButton));
                    }
                });

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

        //뒷배경 흐리게 하기
        /*WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;getWindow().setAttributes(layoutParams);*/

        // (장바구니 안 리스트) 리스트뷰의 높이를 계산에서 layout 크기를 설정
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i, null, containerList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = containerList.getLayoutParams();
        params.height = totalHeight + (containerList.getDividerHeight() * (listAdapter.getCount() - 1));
        containerList.setLayoutParams(params);

        //adapter 연결
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

    class OrderBtnListener implements View.OnClickListener{
        int stuck = 10;
        @Override
        public void onClick(View view) {

            int count = Integer.parseInt(allCount2.getText().toString())-1;

            BootUser bootUser = new BootUser().setPhone("010-1234-5678"); // !! 자신의 핸드폰 번호로 바꾸기
            BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0, 2, 3});

            Bootpay.init(getFragmentManager())
                    .setApplicationId("x") // 해당 프로젝트(안드로이드)의 application id 값(위의 값 복붙)
                    .setPG(PG.INICIS) // 결제할 PG 사
                    .setMethod(Method.CARD) // 결제수단
                    .setContext(MainActivity.this)
                    .setBootUser(bootUser)
                    .setBootExtra(bootExtra)
                    .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                    .setName(name.getText().toString()+" 외 "+count+"개") // 결제할 상품명
                    .setOrderId("1234") // 결제 고유번호 (expire_month)
                    .setPrice(Integer.parseInt(allSum2.getText().toString().replace(",","").replace("원",""))) // 결제할 금액
//                    .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
//                    .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                    /*.onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                        @Override
                        public void onConfirm(@Nullable String message) {

                            if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                            else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                            Log.d("confirm", message);
                        }
                    })*/
                    .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                        @Override
                        public void onDone(@Nullable String message) {
                            Log.d("done", message);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }
                    })
                    .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                        @Override
                        public void onReady(@Nullable String message) {
                            Log.d("ready", message);
                        }
                    })
                    .onCancel(new CancelListener() { // 결제 취소시 호출
                        @Override
                        public void onCancel(@Nullable String message) {

                            Log.d("cancel", message);
                        }
                    })
                    .onClose(
                            new CloseListener() { //결제창이 닫힐때 실행되는 부분
                                @Override
                                public void onClose(String message) {
                                    Log.d("close", "close");
                                }
                            })
                    .show();
        }
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


