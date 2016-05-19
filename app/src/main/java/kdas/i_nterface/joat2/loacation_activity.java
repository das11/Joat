package kdas.i_nterface.joat2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class loacation_activity extends AppCompatActivity {

    String[] location = {"beltola", "Panbazar", "Guwahati Club"};
    String data;
    private String url = "";

    String place;

    //String info[];
    List<String> info = new ArrayList<String>();

    int control, mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loacation_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, location);


        final AutoCompleteTextView actv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

        actv.setAdapter(arrayAdapter);
        actv.setThreshold(2);




        final CardView ser1 = (CardView)findViewById(R.id.ser1);
        final CardView ser2 = (CardView)findViewById(R.id.ser2);
        final CardView ser3 = (CardView)findViewById(R.id.ser3);

        final FloatingActionButton done = (FloatingActionButton)findViewById(R.id.btn_next);



        ser1.setVisibility(View.INVISIBLE);
        ser2.setVisibility(View.INVISIBLE);
        ser3.setVisibility(View.INVISIBLE);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place = actv.getText().toString();
                Toast.makeText(getApplicationContext(), place,Toast.LENGTH_LONG).show();
                actv.setVisibility(View.INVISIBLE);

                ser1.setVisibility(View.VISIBLE);
                ser2.setVisibility(View.VISIBLE);
                ser3.setVisibility(View.VISIBLE);
            }
        });

    }//////////

    private class get_data extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {

//            HttpHandler httpHandler = new HttpHandler();
//            data = httpHandler.GetHTTPData(url);

            StringBuffer sb = new StringBuffer();
            BufferedReader br;

            String temp;

            try {
                br = new BufferedReader(new InputStreamReader(getAssets().open("test.json")));

                while ((temp = br.readLine()) != null){
                  sb.append(temp);
                }

            }catch (IOException e){
                e.printStackTrace();
            }

            data = sb.toString();

            return data;
        }

        @Override
        protected void onPostExecute(String data){

            try{

                JSONObject root = new JSONObject(data);

                switch (control){
                    case 1 : {
                        inject(place, "Carpenter", root);
                        break;
                    }
                    case 2 : {
                        inject(place, "plumber", root);
                        break;
                    }
                    case 3 : {
                        inject(place, "mechanic", root);
                        break;
                    }

                    default://Error
                }

                //inject("beltola", "Carpenter", root);




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
                        Log.d("av",av);
                        if(av.equals("1")){

                            info.add(joat.getString("Name"));
                            info.add(joat.getString("Contact"));
                            info.add(joat.getString("Exp"));

                            Intent intent = new Intent(loacation_activity.this, Main2Activity.class);
                            intent.putExtra("name", info.get(0).toString());
                            intent.putExtra("cont", info.get(1).toString());
                            intent.putExtra("exp", info.get(2).toString());
                            startActivity(intent);
                            finish();


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

    public void clickfunc(View v){
        switch (v.getId()){
            case R.id.ser1 : {
                control = 1;
                new get_data().execute("");
                break;
            }

            case R.id.ser2 : {
                control = 2;
                new get_data().execute("");
                break;
            }

            case R.id.ser3 : {
                control = 3;
                new get_data().execute("");
                break;
            }

            default://error
        }
    }



}
