package com.nearsoft.reminder.main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.nearsoft.reminder.main.helpers.AlarmManagerHelper;
import com.nearsoft.reminder.main.helpers.DBHelper;

/**
 * Created by Baniares on 4/8/14.
 */
public class CurrentRemindersFragment extends Fragment{
    private DBHelper db;
    private ListView currentList;
    private int idUser;
    private SimpleCursorAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_reminders,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        currentList = (ListView)getView().findViewById(R.id.currentListView);
        currentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ReminderFragmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("ReminderId",(int)id);
                intent.putExtra("mode",2);
                startActivity(intent);
            }
        });
        currentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteDialog(id);
                return true;
            }
        });
        updateDB();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDB();
    }


    public void updateDB(){
        SharedPreferences prefs = getActivity().getSharedPreferences("user",0);
        idUser = prefs.getInt("id",-200);
        db = new DBHelper(getActivity());
        Cursor c = db.getCurrentReminders(idUser);
        adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,c,
                new String[]{"tittle"},new int[]{android.R.id.text1},0);
        currentList.setAdapter(adapter);
        db.close();
    }
    private void deleteDialog(final long _id){
        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(getActivity());
        deleteBuilder.setMessage("You'll lose reminder data forever!")
                .setTitle("Delete reminder?").setIcon(R.drawable.ic_action_warning);
        deleteBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DBHelper db = new DBHelper(getActivity());
                AlarmManagerHelper.cancelAlarms(getActivity());
                db.deleteReminder((int)_id);
                db.close();
                AlarmManagerHelper.setAlarms(getActivity());
                updateDB();
                Toast.makeText(getActivity(),"Reminder deleted",
                        Toast.LENGTH_SHORT).show();
            }
        });
        deleteBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog deleteDialog = deleteBuilder.create();
        deleteDialog.show();
    }
}
