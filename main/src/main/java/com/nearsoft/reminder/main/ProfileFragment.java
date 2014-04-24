package com.nearsoft.reminder.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.nearsoft.reminder.main.helpers.DBHelper;
import com.nearsoft.reminder.main.models.User;

/**
 * Created by Baniares on 4/8/14.
 */
public class ProfileFragment extends Fragment {
    private EditText name;
    private EditText email;
    private EditText bio;
    private EditText password;
    private EditText rePassword;
    private TextView nameText;
    private TextView emailText;
    private TextView bioText;
    private TextView offlinePasswordText;
    private Button submit;
    private ProfilePictureView profilePic;
    private ProgressBar loading;
    private final String DEFAULT = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeVariables();
        initializeViewItems();
        submitOnClick();
    }

    private void initializeViewItems(){
        SharedPreferences prefs = getActivity().getSharedPreferences("user",0);
        name.setText(prefs.getString("name", DEFAULT));
        email.setText(prefs.getString("email",DEFAULT));
        bio.setText(prefs.getString("bio",DEFAULT));
        password.setText(prefs.getString("password",DEFAULT));
        rePassword.setText(prefs.getString("password",DEFAULT));
        profilePic.setProfileId(prefs.getString("idFacebook",DEFAULT));
        nameText.setText(name.getText());
        emailText.setText(email.getText());
        bioText.setText(bio.getText());
        submit.setText("Logout");

        nameText.setVisibility(View.VISIBLE);
        emailText.setVisibility(View.VISIBLE);
        bioText.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        bio.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        rePassword.setVisibility(View.GONE);
        offlinePasswordText.setVisibility(View.GONE);
    }

    public void setEditMode(){
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        bio.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        rePassword.setVisibility(View.VISIBLE);
        offlinePasswordText.setVisibility(View.VISIBLE);

        nameText.setVisibility(View.GONE);
        emailText.setVisibility(View.GONE);
        bioText.setVisibility(View.GONE);

        submit.setText("Save");
    }

    private void submitOnClick(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(submit.getText().toString().equals("Logout")){
                    Session session = Session.getActiveSession();
                    session.closeAndClearTokenInformation();
                    clearSharedPreferences();
                    getActivity().finish();
                    Intent i = new Intent(getActivity(),Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }else if(submit.getText().toString().equals("Save")){
                    User profile = new User();
                    if(String.valueOf(password.getText()).equals(String.valueOf(rePassword.getText()))){
                        SharedPreferences prefs = getActivity().getSharedPreferences("user",0);
                        profile.setId(prefs.getInt("id",-200));
                        profile.setBio(String.valueOf(bio.getText()));
                        profile.setName(String.valueOf(name.getText()));
                        profile.setEmail(String.valueOf(email.getText()));
                        profile.setPassword(String.valueOf(password.getText()));
                        profile.setIdFacebook(prefs.getString("idFacebook",""));

                        DBHelper db = new DBHelper(getActivity());
                        db.updateUser(profile);
                        db.close();

                        updateSharedPreferences(profile);
                        initializeViewItems();
                    }else{
                        Toast.makeText(getActivity(),"Passwords not equal", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateSharedPreferences(User profile){
        SharedPreferences.Editor setPrefs = getActivity().getSharedPreferences("user",0).edit();
        setPrefs.putString("name",profile.getName());
        setPrefs.putString("email",profile.getEmail());
        setPrefs.putString("idFacebook",profile.getIdFacebook());
        setPrefs.putString("password",profile.getPassword());
        setPrefs.putString("bio",profile.getBio());
        setPrefs.putInt("id",profile.getId());
        setPrefs.commit();
    }

    private void clearSharedPreferences(){
        SharedPreferences.Editor setPrefs = getActivity().getSharedPreferences("user",0).edit();
        setPrefs.putString("name","");
        setPrefs.putString("email","");
        setPrefs.putString("idFacebook","");
        setPrefs.putString("password","");
        setPrefs.putString("bio","");
        setPrefs.putInt("id",-200);
        setPrefs.commit();
    }

    private void initializeVariables(){
        name = (EditText) getView().findViewById(R.id.nameEditText);
        email = (EditText) getView().findViewById(R.id.mailEditText);
        bio = (EditText) getView().findViewById(R.id.bioEditText);
        password = (EditText) getView().findViewById(R.id.passwordEditText);
        rePassword = (EditText) getView().findViewById(R.id.rePasswordEditText);
        profilePic = (ProfilePictureView) getView().findViewById(R.id.profilepic);
        loading = (ProgressBar) getView().findViewById(R.id.progressBar);
        submit = (Button) getView().findViewById(R.id.submit);
        nameText = (TextView) getView().findViewById(R.id.profileNameTextView);
        emailText = (TextView) getView().findViewById(R.id.profileEmailTextView);
        bioText = (TextView) getView().findViewById(R.id.profileBioTextView);
        offlinePasswordText = (TextView) getView().findViewById(R.id.profileOfflinePasswordTextView);
    }
}
