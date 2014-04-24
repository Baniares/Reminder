package com.nearsoft.reminder.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.nearsoft.reminder.main.helpers.DBHelper;
import com.nearsoft.reminder.main.models.User;

/**
 * Created by Baniares on 3/28/14.
 */

public class Registration extends Activity {
    private ProfilePictureView profilePic;
    private Button submit;
    private UiLifecycleHelper uiHelper;
    private EditText userName;
    private EditText userEmail;
    private EditText userBio;
    private EditText password;
    private EditText repassword;
    private TextView name;
    private TextView email;
    private TextView bio;
    private ProgressBar loading;
    private User profile;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        profilePic=(ProfilePictureView)findViewById(R.id.profilepic);
        userName=(EditText)findViewById(R.id.nameEditText);
        userEmail=(EditText)findViewById(R.id.mailEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
        repassword=(EditText)findViewById(R.id.rePasswordEditText);
        userBio=(EditText)findViewById(R.id.bioEditText);
        submit = (Button) findViewById(R.id.submit);
        name = (TextView) findViewById(R.id.profileNameTextView);
        email = (TextView) findViewById(R.id.profileEmailTextView);
        bio = (TextView) findViewById(R.id.profileBioTextView);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        name.setVisibility(View.GONE);
        bio.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        Session session = Session.getActiveSession();
        profile = new User();
        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                submitClick();
            }
        });
        //loading.setVisibility(View.VISIBLE);
        Request.newMeRequest(session, new Request.GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    loading.setVisibility(View.VISIBLE);
                    // Display the parsed user info
                    profilePic.setProfileId(user.getId());
                    userName.setText(user.getName());
                    userEmail.setText(user.getProperty("email").toString());
                    if(user.getProperty("bio")!= null){
                        userBio.setText(user.getProperty("bio").toString());
                    }
                }
                loading.setVisibility(View.INVISIBLE);
            }
        }).executeAsync();
    }

    @Override
    protected void onStop() {
        super.onStop();
        uiHelper.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isClosed()) {
            session.close();
            Toast.makeText(this, "Logged out",
                    Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.INVISIBLE);
            finish();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    private void submitClick(){
        if(password.getText().toString().equals(repassword.getText().toString())&&!password.getText().toString().equals("")){
            profile.setIdFacebook(profilePic.getProfileId());
            profile.setName(userName.getText().toString());
            profile.setEmail(userEmail.getText().toString());
            profile.setBio(userBio.getText().toString());
            profile.setPassword(password.getText().toString());
            DBHelper db = new DBHelper(this);
            db.addUser(profile);
            db.close();
            SharedPreferences.Editor setPrefs = getSharedPreferences("user",0).edit();
            setPrefs.putString("name",profile.getName());
            setPrefs.putString("email",profile.getEmail());
            setPrefs.putString("idFacebook",profile.getIdFacebook());
            setPrefs.putString("password",profile.getPassword());
            setPrefs.putString("bio",profile.getBio());
            setPrefs.putInt("id",profile.getId());
            setPrefs.commit();
            finish();
            Intent i = new Intent(this,Reminders.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }else{
            Toast.makeText(this,"Invalid password",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
}
