package com.example.bo.smarthome;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Open on 3/21/2017.
 */

public class LivingroomFragment extends Fragment {

    Long id;
    String text;
    boolean isTablet;
    LivingroomList livingroomList = null;
    public LivingroomFragment(){}

    public LivingroomFragment(LivingroomList lrList){
        livingroomList = lrList;
    }

    //no matter how you got here, the data is in the getArguments
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        id = bun.getLong("id");
        text = bun.getString("text");
        isTablet = bun.getBoolean("isTablet");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View gui = inflater.inflate(R.layout.message_detail_content, null);

        TextView messageText = (TextView)gui.findViewById(R.id.message_text);
        messageText.setText(text);

        TextView messageID = (TextView)gui.findViewById(R.id.message_id);
        messageID.setText(String.valueOf(id));

        Button deleteButton = (Button)gui.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isTablet){
                    Intent intent = new Intent();
                    intent.putExtra("id" ,id);
                    getActivity().setResult(5, intent);
                    getActivity().finish();
                }
                else{
                    livingroomList.deleteDbMessage(id);
                    livingroomList.removeFragment();
                }
            }
        });
        return gui;
    }

}



