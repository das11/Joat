package kdas.i_nterface.joat2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {


    String data;
    String url = "";
    List<String> info = new ArrayList<String>();
    String place;

    String mname, cont, exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView t_name = (TextView)findViewById(R.id.name);
        TextView t_contact = (TextView)findViewById(R.id.contact);
        TextView t_exp = (TextView)findViewById(R.id.exp);

        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                //do stuff later
            }else
            {
                mname = extras.getString("name");
                cont = extras.getString("cont");
                exp = extras.getString("exp");

            }
        }

        t_name.setText(mname);
        t_contact.setText(cont);
        t_exp.setText(exp);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //new get_data().execute("");


    }

    private class get_data extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler httpHandler = new HttpHandler();
            data = httpHandler.GetHTTPData(url);

            return data;
        }

        @Override
        protected void onPostExecute(String data){

            try{

                JSONObject root = new JSONObject(data);
                inject(place, "Carpenter", root);


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }

    private void inject(String area_, String service_, JSONObject root){

        String av;
        int c = 0;

        try{
            JSONArray location = root.getJSONArray("location");
            for (int i = 0; i < location.length(); ++i){
                JSONArray area = new JSONArray(location.getJSONObject(i).getString(area_));
                for (int j = 0; j < area.length(); ++j){
                    JSONArray service= new JSONArray(area.getJSONObject(j).getString(service_));
                    for (int k = 0; k < service.length(); ++k){

                        JSONObject joat = service.getJSONObject(k);
                        av = joat.getString("av");
                        Log.d("av", av);
                        if(av.equals("1")){

                            info.add(joat.getString("Name"));
                            info.add(joat.getString("Contact"));
                            info.add(joat.getString("Exp"));
                            for (int y = 0; y < 3; ++y){
                                Log.d("info", info.get(y).toString());
                            }

                        }else {
                            continue;
                        }


                    }
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }


}
