package com.nearsoft.reminder.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.nearsoft.reminder.main.objects.DBHelper;
import com.nearsoft.reminder.main.objects.User;

import java.util.Arrays;

public class FragmentLogin extends Fragment {
    private static final String TAG = "FragmentLogin";
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private UiLifecycleHelper uiHelper;
    private DBHelper db;
    String idFacebook="lol";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("basic_info","email"));
        authButton.setFragment(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        db = new DBHelper(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        /*Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }*/
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            //Log.i(TAG, "Logged in...");
            //getActivity().finish();
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    idFacebook = user.getId();
                    if(db.getUserExistFromFacebookID(idFacebook)){
                        Intent intent = new Intent(getActivity(), Reminders.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        User profile = db.getUserFromFacebookId(idFacebook);
                        intent.putExtra("idUser",profile.getId());
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), Registration.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                }
            }).executeAsync();
            Toast.makeText(getActivity(), "Logged in",
                    Toast.LENGTH_SHORT).show();
        } else if (state.isClosed()) {
            //Log.i(TAG, "Logged out...");
            Toast.makeText(getActivity(), "Logged out",
                    Toast.LENGTH_SHORT).show();
            session.close();
        }
    }
}
