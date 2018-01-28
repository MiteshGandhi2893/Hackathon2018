package com.example.miteshgandhi.mydietryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Data.ItemRecycler;
import Model.Item;

public class ItemList extends AppCompatActivity {


    private Item item;
    private String RequestUrl="https://wegmans-es.azure-api.net/pricepublic/pricing/current_prices/";
    private RecyclerView recyclerView;
    private ItemRecycler itemRecycler;
    private List<Item> itemList;
    private RequestQueue requestQueue,requestQueue1;
     Map<String,String>Price=new HashMap<>();
    private String URL="https://wegmans-es.azure-api.net/productpublic/products/";

private  HashMap<String,String> Data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Intent intent=getIntent();
Data=(HashMap<String,String>)intent.getSerializableExtra("Data");

         requestQueue= Volley.newRequestQueue(this);

        requestQueue1=Volley.newRequestQueue(this);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList=new ArrayList<>();

        List<String> lists = new ArrayList<String>(Data.keySet());


        for(int i=0;i<10;i++)
        {
            Log.d("second ",lists.get(i));
            getMovieList(lists.get(i));
        }


itemRecycler=new ItemRecycler(this,itemList);
        recyclerView.setAdapter(itemRecycler);
        itemRecycler.notifyDataSetChanged();












    }
     boolean flag=false;

    public void getMovieList(final String sku)
    {




        JsonArrayRequest priceRequest=new JsonArrayRequest(Request.Method.GET, RequestUrl + sku, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                try {
                   Price.put(sku,response.getJSONObject(0).getString("Display"));
                    Log.d("pricve :: ",response.getJSONObject(0).getString("Display")+"       "+Price.get(sku));
                  flag=true;


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Price-Subscription-Key", "f52dfdf2593b4dd984fb27de072925eb");
                headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSIsImtpZCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSJ9.eyJhdWQiOiJodHRwczovL3dlZ21hbnMtZXMuYXp1cmUtYXBpLm5ldCIsImlzcyI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzEzMThkNTdmLTc1N2ItNDViMy1iMWIwLTliM2MzODQyNzc0Zi8iLCJpYXQiOjE1MTcxNTI3NTAsIm5iZiI6MTUxNzE1Mjc1MCwiZXhwIjoxNTE3MTU2NjUwLCJhaW8iOiJZMk5nWUZnOXB5SmlsOFNDZXhGVlBycDdOL2FLQWdBPSIsImFwcGlkIjoiMmZhOGY3MWYtY2VjNS00OWU5LWJkMGEtMjI3ODBkYzI2YTliIiwiYXBwaWRhY3IiOiIxIiwiaWRwIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvMTMxOGQ1N2YtNzU3Yi00NWIzLWIxYjAtOWIzYzM4NDI3NzRmLyIsIm9pZCI6ImY0NTIwYmRmLTc1NWItNGY5Yi1iNWJkLTI4NGJiYTI2MTEwOSIsInN1YiI6ImY0NTIwYmRmLTc1NWItNGY5Yi1iNWJkLTI4NGJiYTI2MTEwOSIsInRpZCI6IjEzMThkNTdmLTc1N2ItNDViMy1iMWIwLTliM2MzODQyNzc0ZiIsInV0aSI6ImFEN25Fejk1LWtLQXQ5dmJjUVlTQUEiLCJ2ZXIiOiIxLjAifQ.OP_VqYYv2cNxd9Tk4oHW1cXHTC7rUc8vOztJeyR5msbdKqsaGHuT28i9MRxNuJqlC94xOEeeNz5bTmHv-dGRAHw6EhV4AV4H9W5bMRG4FxrJb2qKsSNwj3S12AnAT9btfpM5qxpDvYQUWyIqgEKDEp5uWe4v7poNwabqzfoaXpbdVnp6GUuJC2-ky6F6UkE9-HY1PsJ4psb0eb43f5OTEDIlTdmp7n1m4YvIM6skwUnegRFXc0VB1I0ta7lTaDw-zluiWgAhRS2IPuOmoUyMUhVOhYzPadT2--cFgTRwQ6G3kqfNG4bCXqnWiJqDspiNX77Iv82d1f3yPUKM3z9L4A");

                return headers;
            }
            };



        requestQueue.add(priceRequest);




    //Log.d("count :",count+"");

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL + sku, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            try {
                Item items = new Item();
                JSONArray TradeIdentifierConfigurations = response.getJSONArray("TradeIdentifierConfigurations");
                JSONArray TradeArray = TradeIdentifierConfigurations.getJSONObject(0).getJSONArray("TradeIdentifiers");
                JSONArray Imageurl = TradeArray.getJSONObject(0).getJSONArray("Images");
                String imagePath = Imageurl.getJSONObject(0).getString("Url");
                imagePath = "https://www.wegmans.com/" + imagePath;
                Toast.makeText(getApplicationContext(), "Second  " + sku + "    " + Price.get(sku), Toast.LENGTH_LONG).show();
                Log.d("resss ", "Second  " + sku + "    " + Price.get(sku));
                items.setDescription(response.getString("Description"));
                items.setBrand(response.getString("BrandName"));
                items.setProduct(response.getString("NounName"));
                items.setImagePath(imagePath);
                items.setPrice(Price.get(sku));
                items.setSku(sku);
                itemList.add(items);
                itemRecycler.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Product-Subscription-Key", "f52dfdf2593b4dd984fb27de072925eb");
            headers.put("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSIsImtpZCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSJ9.eyJhdWQiOiJodHRwczovL3dlZ21hbnMtZXMuYXp1cmUtYXBpLm5ldCIsImlzcyI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzEzMThkNTdmLTc1N2ItNDViMy1iMWIwLTliM2MzODQyNzc0Zi8iLCJpYXQiOjE1MTcxNTIyOTgsIm5iZiI6MTUxNzE1MjI5OCwiZXhwIjoxNTE3MTU2MTk4LCJhaW8iOiJZMk5nWUpoVUZzTGYyVlNzMUhoTjFhbnUvNjBBQUE9PSIsImFwcGlkIjoiMjQ5NjBkOTctNGZiZS00MzNkLWFiOGEtZWZlYjg5YWE1MjRlIiwiYXBwaWRhY3IiOiIxIiwiaWRwIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvMTMxOGQ1N2YtNzU3Yi00NWIzLWIxYjAtOWIzYzM4NDI3NzRmLyIsIm9pZCI6ImRlNTA1ODBiLWY3NTItNDIzMi05Y2I2LWI4OWE1ZDhjNGM3MCIsInN1YiI6ImRlNTA1ODBiLWY3NTItNDIzMi05Y2I2LWI4OWE1ZDhjNGM3MCIsInRpZCI6IjEzMThkNTdmLTc1N2ItNDViMy1iMWIwLTliM2MzODQyNzc0ZiIsInV0aSI6Ik1ERS1kcXh2MmtpRGR0TldlNlVVQUEiLCJ2ZXIiOiIxLjAifQ.hKfQUh1JpdjYhfmJ2jDBUBVrZ3ApQLcxeDOfLR3HHh1dQW0g1aLAUBvbegyupoljyi_xAidun53IqXVC4Neydg-XGyLhHI7Ra3ay1zvl7yw8-IpTVjfnAjiAiQgtGgPJGsGUU_6OivfHXiyQ1Y78TbZVOr3UR701dAXZsyOkUfnv_ka8X_5zn4vUt9mJ44QlnHlYKx-Xq-cPMRMaIyu2b5maVBFLZyc8tjEmfu17BWFAvMhsx58C2Ru9Rn7kd2XESc1qzq2bv0L66dZwUsWsJYycUp_tS8DRkQ5Nd2q2pkUoXPh5hxhcUjo2BL9rmd7ALDBCFTr3Ee_9-1gZVxEGvw");

            return headers;
        }
    };
        requestQueue1.add(jsonObjectRequest);










    }





}
