package edu.harvard.cs50.pokedexapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityPokemon extends AppCompatActivity {
    private TextView nameTextView;
    private TextView numberTextView;
    private String url;
    private TextView type1TextView;
    private TextView type2TextView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);


        requestQueue= Volley.newRequestQueue(getApplicationContext());
//      String name=  getIntent().getStringExtra("name");
         url =  getIntent().getStringExtra("url");
//      int number= getIntent().getIntExtra("number",0);
          nameTextView=findViewById(R.id.pokemon_name);
          numberTextView=findViewById(R.id.pokemon_number);
          type1TextView=findViewById(R.id.pokemon_type1);
          type2TextView=findViewById(R.id.pokemon_type2);

//      nameTextView.setText(name);
//      numberTextView.setText(Integer.toString(number));
        load();
    }
//    method that loads data from api

   public void load(){
       type1TextView.setText("");
       type2TextView.setText("");
       JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

           @Override
           public void onResponse(JSONObject response) {
               try {

               nameTextView.setText(response.getString("name"));
               numberTextView.setText(String.format("#%3d",response.getInt("id")));
                 JSONArray typeEntries= response.getJSONArray("types");
                    for(int i =0;i<typeEntries.length();i++){
                      JSONObject typeEntry=typeEntries.getJSONObject(i);
                      int slot=typeEntry.getInt("slot");
                      String type=typeEntry.getJSONObject("type").getString("name");

                      if(slot==1){
                          type1TextView.setText(type);
                      }
                      else if(slot==2){
                          type2TextView.setText(type);
                      }
                    }
               }
               catch (JSONException e) {
                   Log.e("Exception:", " Pokemon Json error");
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.e("Error:","Pokemon Details error");
           }
       });
 requestQueue.add(request);
   }
   }

