package com.kyeong.smartorder;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ListAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuList extends ListFragment {


    int[] images = {R.drawable.coffee_hot_ameri, R.drawable.coffee_ice_ameri, R.drawable.coffee_hot_latte, R.drawable.coffee_ice_latte
            , R.drawable.coffee_hot_orizin, R.drawable.coffee_ice_orizin , R.drawable.coffee_clodbrew , R.drawable.coffee_clodbrew_latte
        , R.drawable.coffee_hot_condensed , R.drawable.coffee_ice_condensed };
    String[] names = {"아메리카노(HOT)", "아메리카노(ICE)", "라떼(HOT)", "라떼(ICE)", "원조커피(HOT)", "원조커피(ICE)"
                    , "콜드브루", "콜드브루라떼", "연유라떼(HOT)" , "연유라떼(ICE) "};
    String[] prices = {"1,500", "2,000", "2,500", "3,000", "2,000", "2,500","3,000","3,500","2,500","2,500"};

    TextView name , price ;
    ImageView image ;

    public static MenuList newInstance() {
        return new MenuList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_list_fragment, container, false);

        name = (TextView)view.findViewById(R.id.name);
        price = (TextView)view.findViewById(R.id.price);
        image = (ImageView)view.findViewById(R.id.image);

        /////ListView////
        ArrayList<HashMap<String, Object>> list_view = new ArrayList<HashMap<String, Object>>();
        //데이터를 담는다
        for (int i = 0; i < images.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("name", names[i]);
            map.put("price", prices[i]);

            list_view.add(map);
        }

        String[] keys = {"image", "name", "price"};
        int[] ids = {R.id.image, R.id.name, R.id.price};

        MainActivity activity = (MainActivity)getActivity();
        SimpleAdapter adapter = new SimpleAdapter( activity , list_view, R.layout.menu_list, keys, ids);
        setListAdapter(adapter);

        /////ListView////
        Log.d("print","view 직전");
        return view;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        Log.d("ListClick",names[position].trim());
        Log.d("ListClick",prices[position].trim());



        Intent intent = new Intent(getActivity(), DialogActivity.class);
        intent.putExtra("names" , names[position].trim());
        intent.putExtra("prices" , prices[position].trim());
        intent.putExtra("images" , images[position]);
        intent.putExtra("opValue" , "C");

        startActivity(intent);

    }
}