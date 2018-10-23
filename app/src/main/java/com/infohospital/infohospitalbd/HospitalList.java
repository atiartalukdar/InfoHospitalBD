package com.infohospital.infohospitalbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import adapter.CustomListAdapter;
import adapter.CustomListAdapterHospitalDetails;
import bp.Data;
import bp.DataHospital;

public class HospitalList extends AppCompatActivity {
    ListView listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        CustomListAdapterHospitalDetails adapter=new CustomListAdapterHospitalDetails(this,
                DataHospital.icons,
                DataHospital.header,
                DataHospital.subHeader
        );
        listDetails=(ListView)findViewById(R.id.listHospita);
        listDetails.setAdapter(adapter);
        listDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(HospitalList.this,HospitalService.class);
                startActivity(intent);

            }
        });
    }
}
