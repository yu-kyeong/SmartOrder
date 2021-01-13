package com.kyeong.smartorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DialogActivity extends AppCompatActivity {

    boolean state = false;

    //장바구니에 담겨지는 변수
    String value1, value2;
    int value3;
    //상품 기존 정보 변수
    String oriName, oriPrice, oriCount;
    //옵션 추가 변수
    String opName , opPrice, opCount;

    TextView name, price, count, option;
    ImageView image, exit;
    LinearLayout optionMenu , optionLayout;
    //음료종류
    String s;

    public static Context context_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
        context_main = this;

        Intent intent = getIntent();

        //상품 이름,가격,이미지
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        image = (ImageView) findViewById(R.id.image);

        //x 버튼
        exit = (ImageView) findViewById(R.id.exit);

        //옵션
        count = (TextView) findViewById(R.id.countNum);
        option = (TextView) findViewById(R.id.option);
        optionMenu = (LinearLayout) findViewById(R.id.optionMenu);
        optionLayout = (LinearLayout)findViewById(R.id.optionLayout);

        value1 = intent.getStringExtra("names");
        value2 = intent.getStringExtra("prices");
        value3 = intent.getIntExtra("images", 0);

        name.setText(value1);
        price.setText(value2);
        image.setImageResource(value3);

        Button minus = (Button) findViewById(R.id.minus);
        Button plus = (Button) findViewById(R.id.plus);
        Button contain = (Button) findViewById(R.id.container);

        BtnMinus btnMinus = new BtnMinus();
        minus.setOnClickListener(btnMinus);

        BtnPlus btnPlus = new BtnPlus();
        plus.setOnClickListener(btnPlus);

        BtnContain btnContain = new BtnContain();
        contain.setOnClickListener(btnContain);

        BtnOption btnOption = new BtnOption();
        optionLayout.setOnClickListener(btnOption);

        BtnExit btnExit = new BtnExit();
        exit.setOnClickListener(btnExit);

        s = intent.getStringExtra("opValue");
        Log.d("opValue", s);
        if (s.equals("D")) {
            optionMenu.setVisibility(View.INVISIBLE);
        }
    }

    class BtnContain implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity activity = ((MainActivity) MainActivity.context_main);
            ContainData data;
            ContainOptionData optionData;
            //음료 원래 가격
            oriName = value1;
            oriPrice = value2;
            oriCount = "1";

            //옵션 금액 추가된 가격
            value2 = price.getText().toString();

            /*장바구니 List에 담기*/
            state = true;
            int countNum = Integer.parseInt((String) count.getText());
            Log.d("Option", "countNum : " + countNum);
            if (countNum > 1 && countNum != 1) {
                for (int i = 0; i < countNum; i++) {
                    data = new ContainData(value1, value2, value3 , oriName , oriPrice , oriCount);
                    optionData = new ContainOptionData(opName , opPrice , opCount);
                    activity.list_view.add(data);
                    activity.containList.add(optionData);
                }
            } else {
                data = new ContainData(value1, value2, value3 , oriName , oriPrice , oriCount);
                optionData = new ContainOptionData(opName , opPrice , opCount);
                activity.list_view.add(data);
                activity.containList.add(optionData);
            }
            Log.d("OPTION", "list :: " + activity.list_view);

            /*총 갯수, 금액 합*/
            int sumInt = 0;
            int objCount = 0;
            for (int i = 0; i < activity.list_view.size(); i++) {
                int sumStr = Integer.parseInt(activity.list_view.get(i).getValue_p().replace(",", ""));
                Log.d("sumStr", String.valueOf(sumStr));
                sumInt += sumStr;
                objCount = i + 1;
            }
            DecimalFormat formater = new DecimalFormat("###,###");
            String sum = formater.format(sumInt);
            //총 합계 , 갯수
            Log.d("PRICE", "sumI format : " + sum);
            activity.allSum.setText(sum + "");
            activity.allSum2.setText(sum + "");
            activity.allCount.setText(objCount + "");
            activity.allCount2.setText(objCount + "");

            /*장바구니 open, close */
            activity.isPageOpen = true;
            activity.hiddenPage.setVisibility(View.VISIBLE);
            if (activity.list_view == null) {
                activity.hiddenPage.setVisibility(View.INVISIBLE);
            }

            Log.d("OPTION", "view . " + activity.hiddenPage.getVisibility());
            Log.d("OPTION", "isPageOpen : " + activity.isPageOpen);

            /*장바구니에 추가 후 다이얼로그 종료*/
            finish();


        }
    }

    public void setText(String sum , String opName) {
        price.setText(sum);
        option.setText(opName+"\t");
    }

    class BtnMinus implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            String str = count.getText().toString().trim();
            Log.d("얍", "현재 srt : " + str);
            int num = Integer.parseInt(str);
            Log.d("얍", "Minus num : " + num);
            if (num == 0) {
                num = 0;
            } else {
                num--;
                count.setText(num + "");
                Log.d("얍", "Minus 누른 후 : " + count.getText());
            }
            //count.setText(num+"");
        }

    }

    class BtnPlus implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String str = count.getText().toString().trim();
            Log.d("얍", "현재 srt : " + str);
            int num = Integer.parseInt(str);
            Log.d("얍", "Plus num : " + num);
            //Log.d("얍","num : "+num);

            if (num == 20) {
                num = 20;
            } else {
                num++;
                count.setText(num + "");
                Log.d("얍", "plus 누른 후 : " + count.getText());
            }
        }
    }

    class BtnOption implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(DialogActivity.this, DialogOptionActivity.class);
            price.setText(value2);
            String p = price.getText().toString();
            Log.d("opValue",s);
            intent.putExtra("price", p);
            intent.putExtra("opValue" , s);
            startActivity(intent);


//            FragmentManager manager = getSupportFragmentManager();
//            option2.s = s;
//            String p = price.getText().toString();
//            option2.price = p;
//
//            option2.show(manager , null);
        }
    }

    class BtnExit implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

}