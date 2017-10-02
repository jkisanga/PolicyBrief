package tzchoice.kisanga.joshua.policybrief.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.activity.LoginActivity;
import tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler;
import tzchoice.kisanga.joshua.policybrief.helper.SessionManager;
import tzchoice.kisanga.joshua.policybrief.model.User;
import tzchoice.kisanga.joshua.policybrief.retrofit.IRetrofit;

import static android.content.ContentValues.TAG;
import static tzchoice.kisanga.joshua.policybrief.app.Config.url;
import static tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler.KEY_EMAIL;
import static tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler.KEY_ID;
import static tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler.KEY_PIN;

/**
 * Created by user on 8/1/2017.
 */

public class ChangePinDialog extends DialogFragment {
    private EditText mCurrentPIN, mNewPIN;
    private Button btnChangePIN;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;




    public static ChangePinDialog newInstance(String title) {

        ChangePinDialog frag = new ChangePinDialog();

        Bundle args = new Bundle();

        args.putString("title", title);

        frag.setArguments(args);

        return frag;

    }



    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.change_pin, container);

        mCurrentPIN = (EditText) view.findViewById(R.id.txt_current_pin);
        mNewPIN = (EditText) view.findViewById(R.id.txt_new_pin);
        final String currentPin = mCurrentPIN.getText().toString();
        final String newPIN = mNewPIN.getText().toString();
        Log.d(TAG, "onViewCreated: " + mCurrentPIN.getText() + " " +  mNewPIN.getText());
        btnChangePIN = (Button) view.findViewById(R.id.btnChangePin);
        btnChangePIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chengePincode(currentPin, newPIN);
            }
        });

        return view;

    }



    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());
        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());
        // Get field from view

        // Fetch arguments from bundle and set title

        String title = getArguments().getString("title", "Enter Name");

        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field

        mCurrentPIN.requestFocus();

        getDialog().getWindow().setSoftInputMode(

                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    private void chengePincode(String currentPin, String newPIN) {
        pDialog.setMessage("...");

        db.getUserDetails();
        if(currentPin != db.getUserDetails().get(KEY_PIN)) {
            showDialog();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            IRetrofit service = retrofit.create(IRetrofit.class);

            User user = new User();
            user.setPassword(String.valueOf(currentPin));
            user.setEmail(db.getUserDetails().get(KEY_EMAIL));
            user.setId(Integer.valueOf(db.getUserDetails().get(KEY_ID)));


            try {

                Call<User> call = service.changePIN(Integer.valueOf(db.getUserDetails().get(KEY_ID)), Integer.parseInt(newPIN));
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Response<User> response, Retrofit retrofit) {

                        message(response.message());

                        if (response.isSuccess()) {
                            //Starting login activity
                            session.setLogin(false);

                            db.deleteUsers();

                            User userObj = response.body();
                           // db.addUser(userObj.getId(), userObj.getEmail(), Integer.parseInt(userObj.getPassword()));

                            hideDialog();
                            // Launching the login activity
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            message("Successful change PIN");
                            getActivity().finish();
                        } else {
                            message("no such record");
                            hideDialog();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        message("please try again");
                        hideDialog();

                    }
                });
            } catch (Exception e) {
                message(e.getMessage());
                Log.d(TAG, "chengePincode: " + e.getMessage());
                hideDialog();
            }
        }else {
            message(currentPin + " is not your current PIN CODE, Inter correct PIN");
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void message(String message){
         Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}
