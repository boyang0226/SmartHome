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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;


public class HouseGarageFragment extends Fragment {


    Long id;
    Boolean DoorSwitch;
    Boolean LightSwitch;

    ImageView doorcontrol;
    ImageView lightcontrol;

 HousesettingDetail housesettingDetail = null;


    boolean garagedoor;
    boolean garagelight;
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


         doorcontrol =(ImageView)gui.findViewById(R.id.housedoor) ;
         lightcontrol =(ImageView) gui.findViewById(R.id.houselight);



        Button save = (Button)gui.findViewById(R.id.house_garage_save);

        if (door.isChecked()==true){
            doorcontrol.setImageResource(R.drawable.dooropen);
            lightcontrol.setImageResource(R.drawable.lighton);
        }else{
            doorcontrol.setImageResource(R.drawable.doorclose);
            lightcontrol.setImageResource(R.drawable.lightoff);
        }

        if(light.isChecked()==true){

                 lightcontrol.setImageResource(R.drawable.lighton);
        }else{
              lightcontrol.setImageResource(R.drawable.lightoff);
        }


      door.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (door.isChecked()==true) {
                                            Toast.makeText(view.getContext(), "Opening Garage", Toast.LENGTH_LONG).show();
                                        //    garagedoor = true;
                                            garagelight=true;

                                            doorcontrol.setImageResource(R.drawable.dooropen);
                                            lightcontrol.setImageResource(R.drawable.lighton);

                                        } else {
                                            Toast.makeText(view.getContext(), "Closing Garage", Toast.LENGTH_LONG).show();
                                         //   garagedoor = false;
                                            garagelight=false;
                                            doorcontrol.setImageResource(R.drawable.doorclose);
                                            lightcontrol.setImageResource(R.drawable.lightoff);
                                        }
                                        light.setChecked(garagelight);

                                    }
                                });

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (light.isChecked()==true) {
                    Toast.makeText(view.getContext(), "Opening light", Toast.LENGTH_LONG).show();
                    lightcontrol.setImageResource(R.drawable.lighton);

                } else {
                    Toast.makeText(view.getContext(), "Closing Garage", Toast.LENGTH_LONG).show();
                     lightcontrol.setImageResource(R.drawable.lightoff);
                }


            }
        });



//        DoorSwitch=   door.isChecked();
//        LightSwitch=  light.isChecked();
////
//        light.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (light.isChecked()) {
//                    Toast.makeText(view.getContext(), "Opening Garage", Toast.LENGTH_LONG).show();
//                    garagelight = true;
//                } else {
//                    Toast.makeText(view.getContext(), "Closing Garage", Toast.LENGTH_LONG).show();
//                    garagelight = false;
//                }
//
//
//            }
//        });

        save.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                DoorSwitch=   door.isChecked();
                LightSwitch=  light.isChecked();
       if (housesettingDetail==null){
                        Intent intent = new Intent();
                        intent.putExtra("DoorSwitch", DoorSwitch);
                        intent.putExtra("LightSwitch", LightSwitch);
                        intent.putExtra("_id", id);

                        getActivity().setResult(0, intent);
                        getActivity().finish();
                    } else            // callled from tablet
                    {
                        housesettingDetail.updateGarage(id,DoorSwitch,LightSwitch);
                        housesettingDetail.removeFragment();

                        Toast.makeText(view.getContext(), " Data saved", Toast.LENGTH_LONG).show();
                    }



            }
        });



        return gui;
    }

}
