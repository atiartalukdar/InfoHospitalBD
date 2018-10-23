package bp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infohospital.infohospitalbd.ProfileActivity;
import com.infohospital.infohospitalbd.ProfileDataModel;

public class FirebaseRealtimeDB {
    private static final String TAG = "FirebaseDB";

    //Firebase Realtime Database
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;


    public FirebaseRealtimeDB() {
    }

    /**
     * Creating new user node under 'users'
     */

    public void createUser(String userId, String name, String email, String phone, String address, String password, String profileImageLink) {

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");


        //creating user profile object
        ProfileDataModel user = new ProfileDataModel(name, email,phone,address,password,profileImageLink);

        Log.e(TAG, user.toString());
        addUserChangeListener(userId,user);


    }

    /**
     * User data change listener
     */
    public void addUserChangeListener(final String userId, final ProfileDataModel user) {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user
                    Log.e(TAG,"Data Exists");
                    mFirebaseDatabase.child(userId).setValue(user);
                }

                ProfileDataModel user = dataSnapshot.getValue(ProfileDataModel.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }
                Log.e(TAG, "User data is changed!" + user.name + ", " + user.email + ", " + user.phone + ", " + user.address + ", " + user.password);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    public void updateUser(String userId, String name, String email, String phone, String address,String profileImageLink) {
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");



        // updating the user via child nodes

        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);

        if (!TextUtils.isEmpty(phone))
            mFirebaseDatabase.child(userId).child("phone").setValue(phone);

        if (!TextUtils.isEmpty(address))
            mFirebaseDatabase.child(userId).child("address").setValue(address);

        if (!TextUtils.isEmpty(profileImageLink))
            mFirebaseDatabase.child(userId).child("profileImageLink").setValue(profileImageLink);
    }


    public void readUserDataAndSaveToSharedPreferences(final Context context, String userId){

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ProfileDataModel user = dataSnapshot.getValue(ProfileDataModel.class);

                try {
                    SP.setPreference(context, ProfileActivity.nameKey,user.name);
                    SP.setPreference(context, ProfileActivity.emailKey,user.email);
                    SP.setPreference(context, ProfileActivity.phoneKey,user.phone);
                    SP.setPreference(context, ProfileActivity.addressKey,user.address);
                    Log.e(TAG,user.toString());

                }catch (Exception e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
