package com.kyeong.smartorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;


public class DeleteOption extends DialogFragment {

    boolean deleteBtn = false;
    int tag;

    TextView closeBtn, containBtn;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Log.d("Delete", "onCreateDialog 지나는중");

        MainActivity activity = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_delete_option, null);


        closeBtn = (TextView) view.findViewById(R.id.close_btn);
        containBtn = (TextView) view.findViewById(R.id.delete_btn);

        //아니오 버튼
        CloseBtnListener closeBtnListener = new CloseBtnListener();
        closeBtn.setOnClickListener(closeBtnListener);
        //네 버튼
        ContainBtnListener containBtnListener = new ContainBtnListener();
        containBtn.setOnClickListener(containBtnListener);

        builder.setView(view);

        AlertDialog alert = builder.create();

        return alert;
    }

    class CloseBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    }

    class ContainBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity activity = (MainActivity) getActivity();

//                activity.value_n.remove(tag);
//                activity.value_p.remove(tag);
//                activity.value_i.remove(tag);

            /*총 갯수, 금액 합*/
            int sumInt = Integer.parseInt(activity.allSum.getText().toString().replace(",", ""));
            int objCount = Integer.parseInt(activity.allCount.getText().toString());
            //for (int i=objCount; i > 0 ; i--){
            int sumStr = Integer.parseInt(activity.list_view.get(tag).getValue_p().replace(",", ""));
            sumInt -= sumStr;
            objCount -= 1;
            //}
            DecimalFormat formater = new DecimalFormat("###,###");
            String sum = formater.format(sumInt);

            Log.d("PRICE", "sumI format : " + sum);
            activity.allSum.setText(sum);
            activity.allSum2.setText(sum);
            activity.allCount.setText(objCount + "");
            activity.allCount2.setText(objCount + "");

            activity.list_view.remove(tag);
            activity.containList.remove(tag);


            if (objCount == 0) {
                activity.hiddenPage2.setVisibility(View.INVISIBLE);
                activity.hiddenPage.setVisibility(View.INVISIBLE);
                activity.isPageOpen = true;
            }

            dismiss();
            activity.containListAdapter();
            deleteBtn = true;


        }
    }


}