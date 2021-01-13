package com.kyeong.smartorder;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuList3 extends ListFragment {

    int[] images = {R.drawable.ccino_choch,R.drawable.ccino_strawberry , R.drawable.ccino_green , R.drawable.ccino_mint,
            R.drawable.ccino_cookie ,R.drawable.ccino_orizin , R.drawable.ccino_pistachio};

    String[] names = {"완전초코","완전딸기","녹차","민트","쿠키앤크림","원조","피스타치오"};
    String[] prices = {"4,000","4,000","4,000","4,000","4,000","3,500","5,000"};

    TextView name , price;
    ImageView image;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_list3_fragment, container, false);

        name = (TextView)view.findViewById(R.id.name);
        price = (TextView)view.findViewById(R.id.price);
        image = (ImageView)view.findViewById(R.id.image);

        ////ListView////

        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i=0; i<images.length; i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("image", images[i]);
            map.put("name",names[i]);
            map.put("price", prices[i]);

            list.add(map);
        }
        String[] keys = {"image","name","price"};
        int[] ids = {R.id.image, R.id.name ,R.id.price};

        MainActivity activity = (MainActivity)getActivity();
        SimpleAdapter adapter = new SimpleAdapter(activity, list,R.layout.menu_list , keys, ids);
        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        Log.d("LIST",names[position].trim());
        Log.d("LIST",prices[position].trim());

        Intent intent = new Intent(getActivity(), DialogActivity.class);
        intent.putExtra("names" , names[position].trim());
        intent.putExtra("prices" , prices[position].trim());
        intent.putExtra("images" , images[position]);
        intent.putExtra("opValue" , "P");

        startActivity(intent);


    }



}