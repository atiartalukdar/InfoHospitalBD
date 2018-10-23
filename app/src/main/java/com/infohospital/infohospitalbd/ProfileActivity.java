package com.infohospital.infohospitalbd;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import bp.FirebaseRealtimeDB;
import bp.I;
import bp.SP;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    final String  TAG = "profile - atiar ";
    @BindView(R.id.profile_name) EditText _name;
    @BindView(R.id.profile_email) EditText _email;
    @BindView(R.id.profile_phone) EditText _phone;
    @BindView(R.id.profile_address) EditText _address;
    @BindView(R.id.profile_button) Button _editButton;
    FirebaseRealtimeDB firebaseRealtimeDB;
    FirebaseAuth firebaseAuth;

    //Declaring the variable
    String userID = "", name = "", email = "", phone = "", address = "";
    //Declaring the key for using in Sharepreferences value retrival.
    public static String nameKey = "name", emailKey = "email", phoneKey = "phone", addressKey = "address";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mContext = getApplicationContext();

        I.p(mContext,TAG,firebaseAuth.getInstance().getCurrentUser().getUid());

        userID = firebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseRealtimeDB = new FirebaseRealtimeDB();
        firebaseAuth = FirebaseAuth.getInstance();

        //Reading the profile name, number, email, address from shared preferences
        readProfileValue();

    }



    private void readProfileValue(){
        _name.setEnabled(false);
        _email.setEnabled(false);
        _phone.setEnabled(false);
        _address.setEnabled(false);

        _name.setText(SP.getPreference(mContext,nameKey));
        _email.setText(SP.getPreference(mContext,emailKey));
        _phone.setText(SP.getPreference(mContext,phoneKey));
        _address.setText(SP.getPreference(mContext,addressKey));
    }

    public void profileUpdate(View view) {




        if (_editButton.getTag().equals(getString(R.string.profile_view_mode))){
            I.p(mContext,TAG,"edit mode");
            _editButton.setTag(getString(R.string.profile_edit_mode));
            _editButton.setText("Save");
            _name.setEnabled(true);
            _email.setEnabled(true);
            _phone.setEnabled(true);
            _address.setEnabled(true);
            return;
        }

        if (_editButton.getTag().equals(getString(R.string.profile_edit_mode))){
            I.p(mContext,TAG,"view mode");

            _editButton.setTag(getString(R.string.profile_view_mode));
            _editButton.setText("Update");
            _name.setEnabled(false);
            _email.setEnabled(false);
            _phone.setEnabled(false);
            _address.setEnabled(false);

            I.p(mContext,TAG,userID+
                    _name.getText().toString()+
                    _email.getText().toString()+
                    _phone.getText().toString()+
                    _address.getText().toString()+ "updated");
            try{
                firebaseRealtimeDB.updateUser(userID,
                        _name.getText().toString(),
                        _email.getText().toString(),
                        _phone.getText().toString(),
                        _address.getText().toString(), null);
            }catch (Exception e){
                e.printStackTrace();
            }

            return;

        }
    }


}
