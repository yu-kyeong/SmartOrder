package com.kyeong.smartorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DialogOptionActivity extends AppCompatActivity {

    TextView count , opName , opPrice;
    ImageView exit2;
    Button reset , choice;

    String s;
    String price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_option);

        Intent intent = getIntent();

        opName = (TextView)findViewById(R.id.optionName);
        opPrice = (TextView)findViewById(R.id.optionPrice);
        count = (TextView)findViewById(R.id.countNum2);

        exit2 = (ImageView)findViewById(R.id.exit2);
        reset = (Button)findViewById(R.id.reset);
        choice = (Button)findViewById(R.id.choice);

        s = intent.getStringExtra("opValue");

        if (s.equals("C")){
            Log.d("s", " = C");
            opName.setText("샷추가");
            opPrice.setText("500");
        }else if(s.equals("NC")){
            opName.setText("두유로 변경");
            opPrice.setText("500");
        }else if (s.equals("P")){
            opName.setText("아이스크림 추가");
            opPrice.setText("1,000");
        }


        //수량 +,-
        Button minus = (Button)findViewById(R.id.minus);
        Button plus = (Button)findViewById(R.id.plus);

        BtnMinus btnMinus = new BtnMinus();
        minus.setOnClickListener(btnMinus);
        BtnPlus btnPlus = new BtnPlus();
        plus.setOnClickListener(btnPlus);

        //나가기
        BtnExit btnExit = new BtnExit();
        exit2.setOnClickListener(btnExit);
        //초기화
        ResetBtn resetBtn = new ResetBtn();
        reset.setOnClickListener(resetBtn);
        //선택
        ChoiceBtn choiceBtn = new ChoiceBtn();
        choice.setOnClickListener(choiceBtn);

    }

    class BtnMinus implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            String str = count.getText().toString().trim();
            int num = Integer.parseInt(str);
            Log.d("MENU","Minus num : "+num);
            if (num == 0){
                num = 0;
            }else{
                num--;
                count.setText(num+"");
                Log.d("MENU","Minus 누른 후 : "+count.getText());
            }
            //count.setText(num+"");
        }

    }
    class BtnPlus implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String str = count.getText().toString().trim();
            int num = Integer.parseInt(str);
            Log.d("MENU","Plus num : "+num);
            //Log.d("얍","num : "+num);

            if (num == 5){
                num = 5;
            }else{
                num++;
                if (s.equals("P")){
                    num = 1;
                }
                count.setText(num+"");
                Log.d("MENU","plus 누른 후 : "+count.getText());
            }
        }
    }

    class BtnExit implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    class ResetBtn implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            count.setText("0");
        }
    }

    class ChoiceBtn implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = getIntent();
            int sumStr;
            int countNum = Integer.parseInt((String) count.getText());

            //상품 원가격 가져오기
            price = intent.getStringExtra("price");

            //500원 1,000원 구별
            if (s.equals("P")) {
                sumStr = Integer.parseInt(opPrice.getText().toString().replace(",", ""));
            }else{
                sumStr = Integer.parseInt(opPrice.getText().toString());
            }

            int oriStr = Integer.parseInt(price.replace(",",""));
            Log.d("price1", price);
            //수량체크
            if ( countNum > 1 && countNum != 1  ) {
                for (int i = 0; i < countNum; i++) {
                    oriStr += sumStr;
                }
            }else if(countNum == 0) {
                oriStr = oriStr;
                opName.setText("선택");
            }else{
                oriStr += sumStr;
            }
            Log.d("price2", price);
            // 숫자 사이에 , 넣기
            DecimalFormat formater = new DecimalFormat("###,###");
            String sum = formater.format(oriStr);

            //옵션 추가된 금액 set
            ((DialogActivity)DialogActivity.context_main).setText(sum , opName.getText().toString());

            //선택옵션 String 정의
            String opNames = opName.getText().toString();
            String opPrices = opPrice.getText().toString();
            String opCounts = count.getText().toString();

            //선택옵션 정보 지정
            MainActivity activity = ((MainActivity) MainActivity.context_main);
            if(countNum != 0) {
                DialogActivity dialogActivity = ((DialogActivity) DialogActivity.context_main);
                dialogActivity.opName = opNames;
                dialogActivity.opPrice = opPrices;
                dialogActivity.opCount = opCounts;
            }
            Log.d("Option", opName.getText().toString()+  count.getText().toString() + opPrice.getText().toString() );

            finish();

        }
    }

}