package com.infohospital.infohospitalbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import adapter.CustomListAdapterHospitalDetails;
import bp.DataHospital;
import bp.DataHospitalService;

public class HospitalService extends AppCompatActivity {
    ListView listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        CustomListAdapterHospitalDetails adapter=new CustomListAdapterHospitalDetails(this,
                DataHospitalService.icons,
                DataHospitalService.header,
                DataHospitalService.subHeader
        );
        listDetails=(ListView)findViewById(R.id.listHospita);
        listDetails.setAdapter(adapter);
        listDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext()," More Features are comming soon", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
