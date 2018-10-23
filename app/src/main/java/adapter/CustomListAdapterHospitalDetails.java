package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infohospital.infohospitalbd.R;

public class CustomListAdapterHospitalDetails extends ArrayAdapter<String> {

    private final Activity context;
    private final Integer[] hospitalIconID;
    private final String[] hospitalName;
    private final String[] hospitalContactDetails;
    private final String[] emergency;

    public CustomListAdapterHospitalDetails(Activity context, Integer[] hospitalIconID, String[] hospitalName, String[] hospitalContactDetails,
                                            String[] emergency) {
        super(context, R.layout.h_custom_hospital_list, hospitalName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.hospitalIconID=hospitalIconID;
        this.hospitalName=hospitalName;
        this.hospitalContactDetails=hospitalContactDetails;
        this.emergency = emergency;

    }
    public CustomListAdapterHospitalDetails(Activity context, Integer[] hospitalIconID, String[] hospitalName, String[] hospitalContactDetails) {
        super(context, R.layout.h_custom_hospital_list, hospitalName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.hospitalIconID=hospitalIconID;
        this.hospitalName=hospitalName;
        this.hospitalContactDetails=hospitalContactDetails;
        emergency = new String[0];
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.h_custom_hospital_details, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageview);
        TextView header = (TextView) rowView.findViewById(R.id.itemHeader);
        TextView sub = (TextView) rowView.findViewById(R.id.subHeader);
        TextView sub1 = (TextView) rowView.findViewById(R.id.subHeader1);

        imageView.setImageResource(hospitalIconID[position]);
        header.setText(hospitalName[position]);
        sub.setText(hospitalContactDetails[position]);

        return rowView;

    };
}
