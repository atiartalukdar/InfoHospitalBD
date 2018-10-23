package com.infohospital.infohospitalbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

import adapter.CustomListAdapter;
import bp.Data;

public class Hospitals extends AppCompatActivity {

    private WebView mWebview ;
    ListView hospitalList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);

         /*       //WEBVIEW
        mWebview  =  (WebView)findViewById(R.id.webview);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        final Activity activity = this;
        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        mWebview.loadUrl("http://infobdhospital.com");
        //setContentView(mWebview );
*/

        CustomListAdapter adapter=new CustomListAdapter(this, Data.hospitalLogo,
                Data.hospitalName,
                Data.contactDetails,
                Data.emergencyList,
                Data.locations
        );
        hospitalList=(ListView)findViewById(R.id.list);
        hospitalList.setAdapter(adapter);

        hospitalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= Data.hospitalName[+position];
                Intent intent = new Intent(Hospitals.this,HospitalList.class);
                startActivity(intent);
            }
        });
    }
}
