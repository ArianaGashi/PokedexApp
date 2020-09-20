package edu.harvard.cs50.pokedexapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {
    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout containerview;
    public TextView textView;

        PokedexViewHolder(View v){
            super(v);
            containerview= itemView.findViewById(R.id.pokedex_row);
            textView= itemView.findViewById(R.id.pokedex_row_text_view);
//          when the row is tapped this is going to be excecuted
            containerview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 Pokemon current =(Pokemon)containerview.getTag();
//                 second argument is the class we want to instantiate
                    Intent intent= new Intent(view.getContext(),ActivityPokemon.class);
                    intent.putExtra("url",current.getUrl());
//                    intent.putExtra("number",current.getNumber());

                    view.getContext().startActivity(intent);
                }
            });
        }
    }
//hadrcoding some pokemons
    private List<Pokemon> pokemon= new ArrayList<>();
// kick in the request to get data from api, this object requires a context (we will get that from activity )
    private RequestQueue requestQueue;

     PokedexAdapter(Context context){
         requestQueue= Volley.newRequestQueue(context);
         loadPokemon();
     }
    public void loadPokemon(){
        String url="https://pokeapi.co/api/v2/pokemon?limit=151";
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i=0;i<results.length();i++){
                        JSONObject result = results.getJSONObject(i);
                        pokemon.add(new Pokemon(result.getString("name"),
                                                result.getString("url")
                                    ));
                    }
                     notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("Exception:", "Json error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error response","Pokemon List error");
            }
        });
        requestQueue.add(request);
    }

    @NonNull
    @Override
//    before creating the PokedexViewHolder thi method is called to crete the class with
//    the view I'm getting from id=pokedex_row
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        convertet xml file into java file
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pokedex_row,parent,false);

        return new PokedexViewHolder(view);
    }

    @Override
//    this method is called whenever a view is scrolled into a screen and we want to set the values inside of this row
//    setting the different values in view
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
      Pokemon current=pokemon.get(position);
      holder.textView.setText(current.getName());

//      access to the curren pokemon from the viewholder
        holder.containerview.setTag(current);
    }

    @Override
    public int getItemCount() {
        return pokemon.size();
    }
}
