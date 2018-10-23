package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.infohospital.infohospitalbd.R;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final Integer[] hospitalIconID;
    private final String[] hospitalName;
    private final String[] hospitalContactDetails;
    private final String[] emergency;
    private final String[] location;

    public CustomListAdapter(Activity context, Integer[] hospitalIconID, String[] hospitalName, String[] hospitalContactDetails,
    String[] emergency, String[] location) {
        super(context, R.layout.h_custom_hospital_list, hospitalName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.hospitalIconID=hospitalIconID;
        this.hospitalName=hospitalName;
        this.hospitalContactDetails=hospitalContactDetails;
        this.emergency = emergency;
        this.location = location;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.h_custom_hospital_list, null,true);

        ImageView hospitalIcon = (ImageView) rowView.findViewById(R.id.hospitalIcon);
        TextView hspitalName = (TextView) rowView.findViewById(R.id.hospitalName);
        TextView contactDetails = (TextView) rowView.findViewById(R.id.contactDetails);
        TextView emergencyContact = (TextView) rowView.findViewById(R.id.emergency);
        TextView locationT = (TextView) rowView.findViewById(R.id.location);

        hospitalIcon.setImageResource(hospitalIconID[position]);
        hspitalName.setText(hospitalName[position]);
        contactDetails.setText(hospitalContactDetails[position]);
        emergencyContact.setText(emergency[position]);
        locationT.setText(location[position]);

        return rowView;

    };
}
