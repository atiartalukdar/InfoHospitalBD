package com.infohospital.infohospitalbd;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import bp.FirebaseRealtimeDB;
import bp.I;
import bp.SP;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    ProgressDialog progressDialog;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.btn_login_phone) Button _loginButtonPhone;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.link_reset) TextView _resetLink;
    @BindView(R.id.area_password) TextInputLayout _areaPass;

    // Google Sign In button .
    com.google.android.gms.common.SignInButton signInButton;

    // Request sing in code. Could be anything as you required.
    public static final int RequestSignInCode = 7;

    // Firebase Auth Object.
    public FirebaseAuth firebaseAuth;

    // Google API Client object.
    public GoogleApiClient googleApiClient;

    Context mContext;

    FirebaseRealtimeDB firebaseRealtimeDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            SP.setPreference(this,"userID",user.getUid());
            // User is signed in
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.putExtra("userID",user.getUid());
            startActivity(i);
            finish();
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);

        mContext = getApplicationContext();

        firebaseRealtimeDB = new FirebaseRealtimeDB();



        //Google Signin Configuration/Init
        googleSigninInit();

        //All kinds of onClickListener are in this function
        allOnClick();
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            //onLoginFailed();
            return;
        }

        // _loginButton.setEnabled(false);

        //Loading screen for login
        popupAuth("Authenticating...");

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        //authenticate user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            I.p(mContext,TAG,getString(R.string.auth_failed));
                        }
                        else {
                            // Start the Signup activity
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.putExtra("userID",firebaseAuth.getCurrentUser().getUid());
                            SP.setPreference(mContext,"userID",firebaseAuth.getCurrentUser().getUid());
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            startActivity(intent);
                        }
                    }
                });
/*
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }

    private void popupAuth(String message){
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Authenticating...");
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestSignInCode){

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (googleSignInResult.isSuccess()){

                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                FirebaseUserAuth(googleSignInAccount);
            }

        }

        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public boolean validateEmail() {
        boolean valid = true;

        String email = _emailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }





    // Google Sign In function Starts From Here.
    public void UserSignInMethod(){

        // Passing Google Api Client into Intent.
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        Toast.makeText(LoginActivity.this,""+ authCredential.getProvider(),Toast.LENGTH_LONG).show();

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task AuthResultTask) {
                        progressDialog.dismiss();

                        if (AuthResultTask.isSuccessful()){

                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            String name = firebaseUser.getDisplayName().toString();
                            String email = firebaseUser.getEmail().toString();
                            String profileImageLink = firebaseUser.getPhotoUrl().toString();

                            I.p(mContext,TAG,"NAME =  "+ name);
                            I.p(mContext,TAG,"Email =  "+ email);
                            I.p(mContext,TAG,"Profile Image Link =  "+ profileImageLink);

                            //I.p(mContext,TAG,"Email =  "+ firebaseUser.getPhotoUrl().toString());

                            firebaseRealtimeDB.createUser(firebaseAuth.getUid(), name, email, "none"+"", "", "", profileImageLink);

                            //After Successfull Login forward to main activity
                            Intent intent = new Intent(mContext, MainActivity.class);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            startActivity(intent);

                        }else {
                            Toast.makeText(LoginActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void googleSigninInit(){

        signInButton = (com.google.android.gms.common.SignInButton)findViewById(R.id.btn_login_google);

        // Getting Firebase Auth Instance into firebaseAuth object.
        firebaseAuth = FirebaseAuth.getInstance();


        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


    }


    //All kind of onClickListener
    private void allOnClick(){
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(_loginButton.getText().toString().toLowerCase().equals("reset")){
                    resetPasswordViaEmail();
                }else {
                    login();
                }

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        _loginButtonPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), PhoneAuthActivity.class);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                startActivity(intent);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Loading screen for login
                popupAuth("Authenticating...");

                //Google Signin methode
                UserSignInMethod();
            }
        });

        _resetLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (_resetLink.getText().toString().toLowerCase().equals("login")){
                    changeUIafterReset();
                }else{
                    resetPassUI();
                }

            }
        });


    }


    //Forget Password
    private void resetPasswordViaEmail(){


        String email = _emailText.getText().toString();

        if (!validateEmail()){
            return;
        }
        popupAuth("Please check your mail...");

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            I.p(mContext,TAG,"We have sent you instructions to reset your password!");
                            changeUIafterReset();
                        } else {
                            I.p(mContext,TAG,"Failed to send reset email!");
                            changeUIafterReset();
                        }
                        progressDialog.dismiss();
                    }
                });



    }

    private void resetPassUI(){
        _areaPass.setVisibility(View.GONE);
        _loginButton.setText("Reset");
        _resetLink.setText("Login");
    }
    private void changeUIafterReset(){
        _areaPass.setVisibility(View.VISIBLE);
        _loginButton.setText("Login");
        _resetLink.setText(getString(R.string.forget_pass));
    }

}