package tzchoice.kisanga.joshua.policybrief.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.policybrief.R;
import tzchoice.kisanga.joshua.policybrief.helper.FileOpen;
import tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler;
import tzchoice.kisanga.joshua.policybrief.helper.SessionManager;
import tzchoice.kisanga.joshua.policybrief.model.User;
import tzchoice.kisanga.joshua.policybrief.retrofit.IRetrofit;

import static tzchoice.kisanga.joshua.policybrief.app.Config.url;
import static tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler.KEY_EMAIL;
import static tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler.KEY_ID;
import static tzchoice.kisanga.joshua.policybrief.helper.SQLiteHandler.KEY_PIN;

public class ChangePasswardActivity extends AppCompatActivity {

    private EditText mCurrentPIN, mNewPIN;
    private Button btnChangePIN;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
     String currentPin, newPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passward);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCurrentPIN = (EditText) findViewById(R.id.txt_current_pin);
        mNewPIN = (EditText) findViewById(R.id.txt_new_pin);

        btnChangePIN = (Button) findViewById(R.id.btnChangePin);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Session manager
        session = new SessionManager(getApplicationContext());

        btnChangePIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chengePincode(mCurrentPIN.getText().toString(), mNewPIN.getText().toString());
            }
        });


    }


    private void chengePincode(String currentPin, String newPIN) {

        if(FileOpen.isOnline(this)) {
            pDialog.setMessage("...");

            db.getUserDetails();
            String oldPIN = db.getUserDetails().get(KEY_PIN);
            Log.d("chengePincode", "chengePincode: " + oldPIN + "current : " + currentPin);
            if (oldPIN.equals(currentPin)) {
                showDialog();
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(30, TimeUnit.SECONDS);
                client.setReadTimeout(30, TimeUnit.SECONDS);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                IRetrofit service = retrofit.create(IRetrofit.class);

                User user = new User();
                user.setPassword(String.valueOf(currentPin));
                user.setEmail(db.getUserDetails().get(KEY_EMAIL));
                user.setId(Integer.valueOf(db.getUserDetails().get(KEY_ID)));


                try {

                    Call<User> call = service.changePIN(Integer.valueOf(db.getUserDetails().get(KEY_ID)), Integer.parseInt(newPIN));
                    // Call<User> call = service.changePIN(1, newPIN);
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
                                Intent intent = new Intent(ChangePasswardActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                message("Successful change PIN");
                                finish();
                            } else {
                                message("no such record");
                                hideDialog();
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            message("please try again");
                            Log.d("onFailure", "onFailure: " + t.toString());
                            hideDialog();

                        }
                    });
                } catch (Exception e) {
                    message("please try again");
                    hideDialog();
                }
            } else {
                message(currentPin + " is not your current PIN CODE, Inter correct PIN");
            }
        }else {
            message("Connect internet to change password");
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
