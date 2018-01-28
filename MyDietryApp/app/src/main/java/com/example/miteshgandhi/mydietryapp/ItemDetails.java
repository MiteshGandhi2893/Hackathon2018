package com.example.miteshgandhi.mydietryapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Item;

public class ItemDetails extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Item item;
    private TextView view;
    private ImageView imageView;
    private String Url="https://wegmans-es.azure-api.net/productpublic/products/food/alcohol/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


            requestQueue= Volley.newRequestQueue(this);
            view=(TextView)findViewById(R.id.view);
            imageView=(ImageView)findViewById(R.id.imageView);










        item=(Item)getIntent().getSerializableExtra("items");
   String Sku=(String)getIntent().getStringExtra("sku");

        Picasso.with(getApplicationContext()).load(item.getImagePath()).into(imageView);

        final StringBuilder sb=new StringBuilder();

        sb.append("Product Description: "+item.getDescription());
        sb.append("\n");
        sb.append("\n");
        sb.append("Product Type: "+item.getProduct());
        sb.append("\n");
        sb.append("\n");
        requestQueue=Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Url + Sku, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray TradeIdentifierConfigurations=response.getJSONArray("TradeIdentifierConfigurations");
                    JSONArray TradeIdentifiers=TradeIdentifierConfigurations.getJSONObject(0).getJSONArray("TradeIdentifiers");

                    JSONArray ingrdients=TradeIdentifiers.getJSONObject(0).getJSONArray("Ingredients");

                    String ingridents=ingrdients.getJSONObject(0).getString("Statement");

                    sb.append("Ingredients : "+ingridents);
                    //Toast.makeText(getApplicationContext(),ingridents,Toast.LENGTH_LONG).show();

                    sb.append("\n");
                    sb.append("\n");
                    JSONArray nutrients=TradeIdentifiers.getJSONObject(0).getJSONArray("Nutrients");
                    List<String> nutrientsList=new ArrayList<>();


                    sb.append("Nutrition Content : \n\n");

                    for(int i=0;i<nutrients.length();i++)
                    {
                        JSONObject obj=nutrients.getJSONObject(i);
                        sb.append(obj.getString("NutrientType")+":\t\t"+obj.getString("QuantityContained"));
                        sb.append("\n");





                    }





                    view.setText(sb);

                    view.setMovementMethod(new ScrollingMovementMethod());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<>();
                headers.put("Product-Subscription-Key", "f52dfdf2593b4dd984fb27de072925eb");
                headers.put("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSIsImtpZCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSJ9.eyJhdWQiOiJodHRwczovL3dlZ21hbnMtZXMuYXp1cmUtYXBpLm5ldCIsImlzcyI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzEzMThkNTdmLTc1N2ItNDViMy1iMWIwLTliM2MzODQyNzc0Zi8iLCJpYXQiOjE1MTcxNTIyOTgsIm5iZiI6MTUxNzE1MjI5OCwiZXhwIjoxNTE3MTU2MTk4LCJhaW8iOiJZMk5nWUpoVUZzTGYyVlNzMUhoTjFhbnUvNjBBQUE9PSIsImFwcGlkIjoiMjQ5NjBkOTctNGZiZS00MzNkLWFiOGEtZWZlYjg5YWE1MjRlIiwiYXBwaWRhY3IiOiIxIiwiaWRwIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvMTMxOGQ1N2YtNzU3Yi00NWIzLWIxYjAtOWIzYzM4NDI3NzRmLyIsIm9pZCI6ImRlNTA1ODBiLWY3NTItNDIzMi05Y2I2LWI4OWE1ZDhjNGM3MCIsInN1YiI6ImRlNTA1ODBiLWY3NTItNDIzMi05Y2I2LWI4OWE1ZDhjNGM3MCIsInRpZCI6IjEzMThkNTdmLTc1N2ItNDViMy1iMWIwLTliM2MzODQyNzc0ZiIsInV0aSI6Ik1ERS1kcXh2MmtpRGR0TldlNlVVQUEiLCJ2ZXIiOiIxLjAifQ.hKfQUh1JpdjYhfmJ2jDBUBVrZ3ApQLcxeDOfLR3HHh1dQW0g1aLAUBvbegyupoljyi_xAidun53IqXVC4Neydg-XGyLhHI7Ra3ay1zvl7yw8-IpTVjfnAjiAiQgtGgPJGsGUU_6OivfHXiyQ1Y78TbZVOr3UR701dAXZsyOkUfnv_ka8X_5zn4vUt9mJ44QlnHlYKx-Xq-cPMRMaIyu2b5maVBFLZyc8tjEmfu17BWFAvMhsx58C2Ru9Rn7kd2XESc1qzq2bv0L66dZwUsWsJYycUp_tS8DRkQ5Nd2q2pkUoXPh5hxhcUjo2BL9rmd7ALDBCFTr3Ee_9-1gZVxEGvw");

                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);










    }
}
