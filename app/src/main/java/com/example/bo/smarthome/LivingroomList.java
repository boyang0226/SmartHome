package com.example.bo.smarthome;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class LivingroomList extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "StartActivity";
    private ArrayList<String> messages = new ArrayList<>();
    Cursor results;
    public LivingroomDatabaseHelper dbHelper;
    public SQLiteDatabase db;
    private ChatAdapter messageAdapter;
    FragmentTransaction fragTransaction;
    LivingroomFragment frag;
    private class ChatAdapter extends ArrayAdapter<String>{
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount(){
            return messages.size();
        }

        public long getItemId(int position)
        {
            results.moveToPosition(position);
            return results.getLong(results.getColumnIndex(LivingroomDatabaseHelper.KEY_ID));
        }
        @Override
        public String getItem(int position){
            return messages.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = LivingroomList.this.getLayoutInflater();
            View result = null ;
            if (position %2  == 0)
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            else
                result = inflater.inflate(R.layout.chat_row_incoming, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position

            return result;

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        final ListView listView = (ListView) findViewById(R.id.list_view);
        Button SendButton = (Button) findViewById(R.id.Send_button);
        final EditText editText = (EditText)findViewById(R.id.editText);

        final boolean isTablet = findViewById(R.id.fragment_holder) != null;

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView view, View arg1, int position, long id) {
                String message = (String)(listView.getItemAtPosition(position));
                Bundle bun = new Bundle();
                if (isTablet) {

                    bun.putLong("id", id);
                    bun.putString("text", message);
                    bun.putBoolean("isTablet", true);
                    frag = new LivingroomFragment(LivingroomList.this);
                    frag.setArguments( bun );
                    fragTransaction = getFragmentManager().beginTransaction();
                    fragTransaction.add(R.id.fragment_holder, frag).commit();
                }else{
                    Intent intent = new Intent(LivingroomList.this, LivingroomItemDetails.class);
                    intent.putExtra("isTablet", false);
                    intent.putExtras(bun);
                    startActivityForResult(intent, 5);
                }
            }
        });

        refreshMessages();

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();

                values.put(LivingroomDatabaseHelper.KEY_MESSAGE, editText.getText().toString());

                db.insert(dbHelper.TABLE_NAME, null, values);

                editText.setText("");
                refreshMessages();
                Log.i(ACTIVITY_NAME, "In onCreate()");
            }
        });
    }

    private void refreshMessages(){
        dbHelper = new LivingroomDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        results = db.query(false, LivingroomDatabaseHelper.TABLE_NAME,
                new String[] { LivingroomDatabaseHelper.KEY_ID, LivingroomDatabaseHelper.KEY_MESSAGE },
                null, null, null, null, null, null);
        int rows = results.getCount() ; //number of rows returned
        results.moveToFirst(); //move to first result

        Log.i(ACTIVITY_NAME, "Cursor’s column count = " + results.getColumnCount());
        for(int i = 0; i < results.getColumnCount();i++)
            Log.i(ACTIVITY_NAME, "Cursor’s column name = " + results.getColumnName(i));

        messages.clear();
        while(!results.isAfterLast() ) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString(results.getColumnIndex(LivingroomDatabaseHelper.KEY_MESSAGE)));
            messages.add(results.getString(results.getColumnIndex(LivingroomDatabaseHelper.KEY_MESSAGE)));
            results.moveToNext();
        }
        messageAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }

    public void deleteDbMessage(Long messageID){
        db.delete(dbHelper.TABLE_NAME, LivingroomDatabaseHelper.KEY_ID + "=" + messageID, null);
        refreshMessages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 5) {
            final Bundle bun = data.getExtras();
            Long messageID = (Long)bun.getLong("id");
            deleteDbMessage(messageID);
        }
    }

    public void removeFragment()
    {
        FragmentManager fm = getFragmentManager();
        fragTransaction = fm.beginTransaction();
        fragTransaction.remove(frag);
        fragTransaction.commit();


    }
}

