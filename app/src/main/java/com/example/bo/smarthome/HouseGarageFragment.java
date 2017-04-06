package com.example.bo.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class HouseGarageFragment extends Fragment {


    Long id;
    Boolean DoorSwitch;
    Boolean LightSwitch;
 HousesettingDetail housesettingDetail = null;
    boolean garagedoor=false;
    boolean garagelight=false;
    Switch door;
    Switch light;

    public HouseGarageFragment(){}
    public HouseGarageFragment (HousesettingDetail c){
        housesettingDetail= c;
    }

    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bun = getArguments();
        DoorSwitch=bun.getBoolean("DoorSwitch");
        LightSwitch=bun.getBoolean("LightSwitch");
        id = bun.getLong("_id");


    }




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View gui = inflater.inflate(R.layout.activity_house_garage, null);

         door = (Switch) gui.findViewById(R.id.house_garage_swith);
         light =(Switch) gui.findViewById(R.id.house_garage_swith2);


        door.setChecked(DoorSwitch);
        light.setChecked(LightSwitch);
        Button save = (Button)gui.findViewById(R.id.house_garage_save);


        door.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (door.isChecked()) {
                                            Toast.makeText(view.getContext(), "Opening Garage", Toast.LENGTH_LONG).show();
                                            garagedoor = true;
                                            garagelight=true;
                                        } else {
                                            Toast.makeText(view.getContext(), "Closing Garage", Toast.LENGTH_LONG).show();
                                            garagedoor = false;
                                            garagelight=false;
                                        }
                                        light.setChecked(garagedoor);

                                    }
                                });


        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (light.isChecked()) {
                    Toast.makeText(view.getContext(), "Opening Garage", Toast.LENGTH_LONG).show();
                    garagelight = true;
                } else {
                    Toast.makeText(view.getContext(), "Closing Garage", Toast.LENGTH_LONG).show();
                    garagelight = false;
                }


            }
        });

        save.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
       if (housesettingDetail==null){



                       Intent intent = new Intent();
                        intent.putExtra("DoorSwitch", garagedoor);
                        intent.putExtra("LightSwitch", garagelight);
                        intent.putExtra("_id", id);

                        getActivity().setResult(0, intent);
                        getActivity().finish();
                    } else            // callled from tablet
                    {






                        housesettingDetail.updateGarage(id,garagedoor,garagelight);
                        housesettingDetail.removeFragment();
                    }



            }
        });
        Toast.makeText(gui.getContext(), " Data saved", Toast.LENGTH_LONG).show();

        return gui;
    }

}
