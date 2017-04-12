package com.example.bo.smarthome;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;


/**
 * Created by Bo on 2017-04-03.
 *
 * this is the frament class for house garage
 *
 * determine with fragment should to go
 *
 */
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

    //constructor
    public HouseGarageFragment(){}
    public HouseGarageFragment (HousesettingDetail c){
        housesettingDetail= c;
    }

    @Override
    public void onCreate(Bundle b)
    {

        //get the value of doorswitch and lightswitch
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

        //get the two switch
         door = (Switch) gui.findViewById(R.id.house_garage_swith);
         light =(Switch) gui.findViewById(R.id.house_garage_swith2);

        //set the door and light switch with the database stored
        door.setChecked(DoorSwitch);
        light.setChecked(LightSwitch);

       //thw imageview for the door and light
         doorcontrol =(ImageView)gui.findViewById(R.id.housedoor) ;
         lightcontrol =(ImageView) gui.findViewById(R.id.houselight);


   //button for save the status of door and light
        Button save = (Button)gui.findViewById(R.id.house_garage_save);

//if door is open, set the door image and light image be open
        if (door.isChecked()==true){
            doorcontrol.setImageResource(R.drawable.dooropen);
            lightcontrol.setImageResource(R.drawable.lighton);
        }else{  //if door witch is closed, then set the both image to be off
            doorcontrol.setImageResource(R.drawable.doorclose);
            lightcontrol.setImageResource(R.drawable.lightoff);
        }

        //set the light switch image
        if(light.isChecked()==true){

            lightcontrol.setImageResource(R.drawable.lighton);
        }else{
              lightcontrol.setImageResource(R.drawable.lightoff);
        }

       //door switch on click listener
      door.setOnClickListener(new View.OnClickListener() {
           @Override
              public void onClick(View view) {

                         //if the door is open, then make a toast
                         if (door.isChecked()==true) {
                             Toast.makeText(view.getContext(), R.string.house_dooropen, Toast.LENGTH_LONG).show();

                             garagelight=true;

                             doorcontrol.setImageResource(R.drawable.dooropen);
                             lightcontrol.setImageResource(R.drawable.lighton);

                         } else {
                             Toast.makeText(view.getContext(), R.string.house_doorclose, Toast.LENGTH_LONG).show();

                             garagelight=false;
                             doorcontrol.setImageResource(R.drawable.doorclose);
                             lightcontrol.setImageResource(R.drawable.lightoff);
                         }

                         light.setChecked(garagelight);
                                    }
                                });
//light switch onclick listener
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (light.isChecked()==true) {
                    Toast.makeText(view.getContext(), R.string.house_lightopen, Toast.LENGTH_LONG).show();
                    lightcontrol.setImageResource(R.drawable.lighton);

                } else {
                    Toast.makeText(view.getContext(), R.string.house_lightclose, Toast.LENGTH_LONG).show();
                     lightcontrol.setImageResource(R.drawable.lightoff);
                }


            }
        });


//save button onclick
        save.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                DoorSwitch=   door.isChecked();
                LightSwitch=  light.isChecked();

                //for phone
       if (housesettingDetail==null){
            //put the status of the door and light switch to the database
                        Intent intent = new Intent();
                        intent.putExtra("DoorSwitch", DoorSwitch);
                        intent.putExtra("LightSwitch", LightSwitch);
                        intent.putExtra("_id", id);
                       //set the results code to be 0, for update
                        getActivity().setResult(0, intent);
                        getActivity().finish();
                    } else            // callled from tablet
                    {
                        housesettingDetail.updateGarage(id,DoorSwitch,LightSwitch);
                        housesettingDetail.removeFragment();

                        Toast.makeText(view.getContext(), R.string.house_datasave, Toast.LENGTH_LONG).show();
                    }



            }
        });



        return gui;
    }

}
