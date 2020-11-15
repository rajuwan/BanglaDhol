package com.ebs.banglalinkbangladhol.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.activity.InfoActivity;
import com.ebs.banglalinkbangladhol.adapter.CustomDrawerAdapter;
import com.ebs.banglalinkbangladhol.model.DrawerItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {


    public InfoFragment() {
        // Required empty public constructor
    }

    private ListView ListView;
    List<DrawerItem> dataList;
    CustomDrawerAdapter adapter;

    public static final String FRAGMENT_TAG = "InfoFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        getActivity().setTitle("Info");

        dataList = new ArrayList<DrawerItem>();
        ListView = (ListView) view.findViewById(R.id.list);

        dataList.add(new DrawerItem("About App", R.drawable.ic_about)); //1
        dataList.add(new DrawerItem("Privacy Policy", R.drawable.ic_privacy)); //2
        dataList.add(new DrawerItem("License", R.drawable.ic_license)); //3
        dataList.add(new DrawerItem("Version", R.drawable.ic_appinfo)); //4

        adapter = new CustomDrawerAdapter(getActivity(), R.layout.custom_drawer_item,
                dataList);

        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, final int position, long id) {

                        try {

                            Intent infoIntent = new Intent(getActivity(), InfoActivity.class);

                            if(position == 0){

                                infoIntent.putExtra("Title", "About App");
                                infoIntent.putExtra("Type", "about");

                            } else if (position == 1){

                                infoIntent.putExtra("Title", "Privacy Policy");
                                infoIntent.putExtra("Type", "privacy");

                            } else if (position == 2){

                                infoIntent.putExtra("Title", "License");
                                infoIntent.putExtra("Type", "license");

                            } else if (position == 3){

                                infoIntent.putExtra("Title", "Version");
                                infoIntent.putExtra("Type", "info");

                            }

                            startActivity(infoIntent);

                        } catch (Exception e) {

                            Log.d("TAG", "Error in infoListView OnClickListener");

                        }
                    }
                });

        /*view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    Fragment fragment = new HomeFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.home_container, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                    return true;
                } else {
                    return false;
                }
            }
        });*/

        return view;
    }

}
