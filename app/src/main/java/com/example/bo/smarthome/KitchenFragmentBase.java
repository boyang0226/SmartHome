package com.example.bo.smarthome;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class KitchenFragmentBase extends Fragment {

    protected int applianceId = 0;
    protected String applianceName = "";
    protected  View frgRootView;
    protected SQLiteDatabase db = null;

    public KitchenFragmentBase() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bun = getArguments();
        applianceId = bun.getInt("applianceId");
        applianceName = bun.getString("applianceName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

}
