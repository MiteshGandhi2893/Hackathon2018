package com.example.miteshgandhi.mydietryapp;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadPrescription extends AppCompatActivity {

    private static final String APPLICATION_NAME = "Google-VisionLabelSample/1.0";
    private Vision vision;
    private Bitmap bitmap;
    private ImageButton prescription;
    private ProgressDialog mprogress;
    private static final int STATUS=1;
    private Button process;
    private TextView textView;
    String userType="";
    private Uri resultUri=null;
    String criteria="";

    private RequestQueue requestQueue;

  private HashMap<String,String> SKU;






    private static String productURl="https://wegmans-es.azure-api.net/productpublic/products/search?criteria=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);

       process=(Button)findViewById(R.id.processImage);

        mprogress=new ProgressDialog(this);


        prescription=(ImageButton)findViewById(R.id.prescription);


        requestQueue= Volley.newRequestQueue(this);
         SKU=new HashMap<String,String>();




process.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


        TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();

            if(textRecognizer.isOperational())
            {
                Log.i("Text Recog ","Operational");
                Frame frame=new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock>items=textRecognizer.detect(frame);
                StringBuilder sb=new StringBuilder();
                for (int i=0;i<items.size();i++)
                {
                    //Log.d("Text ",items.get(i).toString());
                    TextBlock textBlock=items.valueAt(i);
                    if (textBlock != null && textBlock.getValue() != null) {
                        sb.append(textBlock.getValue().trim());
                        sb.append("\n");
                    }
                }
                  criteria=sb.toString();
                    //textView.setText(sb.toString());Log.d("Data ",sb.toString());



                JsonObjectRequest productRequest=new JsonObjectRequest(Request.Method.GET, productURl + criteria, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONArray jsonArray= null;
                        try {
                            jsonArray = response.getJSONArray("Results");
                            for(int i=0;i<jsonArray.length();i++)
                            {



                                JSONObject object=jsonArray.getJSONObject(i);
                                String sku=object.getString("ItemNumber");

                                String product=object.getString("Noun");



                              //  Log.d("dsjbfh    ",sku);
                                SKU.put(sku,product);


                            }


                          Intent intent=new Intent(UploadPrescription.this,ItemList.class);
                            intent.putExtra("Data",SKU);
                            startActivity(intent);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



















                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Product-Subscription-Key", "f52dfdf2593b4dd984fb27de072925eb");
                        headers.put("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSIsImtpZCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSJ9.eyJhdWQiOiJodHRwczovL3dlZ21hbnMtZXMuYXp1cmUtYXBpLm5ldCIsImlzcyI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzEzMThkNTdmLTc1N2ItNDViMy1iMWIwLTliM2MzODQyNzc0Zi8iLCJpYXQiOjE1MTcxNTIyOTgsIm5iZiI6MTUxNzE1MjI5OCwiZXhwIjoxNTE3MTU2MTk4LCJhaW8iOiJZMk5nWUpoVUZzTGYyVlNzMUhoTjFhbnUvNjBBQUE9PSIsImFwcGlkIjoiMjQ5NjBkOTctNGZiZS00MzNkLWFiOGEtZWZlYjg5YWE1MjRlIiwiYXBwaWRhY3IiOiIxIiwiaWRwIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvMTMxOGQ1N2YtNzU3Yi00NWIzLWIxYjAtOWIzYzM4NDI3NzRmLyIsIm9pZCI6ImRlNTA1ODBiLWY3NTItNDIzMi05Y2I2LWI4OWE1ZDhjNGM3MCIsInN1YiI6ImRlNTA1ODBiLWY3NTItNDIzMi05Y2I2LWI4OWE1ZDhjNGM3MCIsInRpZCI6IjEzMThkNTdmLTc1N2ItNDViMy1iMWIwLTliM2MzODQyNzc0ZiIsInV0aSI6Ik1ERS1kcXh2MmtpRGR0TldlNlVVQUEiLCJ2ZXIiOiIxLjAifQ.hKfQUh1JpdjYhfmJ2jDBUBVrZ3ApQLcxeDOfLR3HHh1dQW0g1aLAUBvbegyupoljyi_xAidun53IqXVC4Neydg-XGyLhHI7Ra3ay1zvl7yw8-IpTVjfnAjiAiQgtGgPJGsGUU_6OivfHXiyQ1Y78TbZVOr3UR701dAXZsyOkUfnv_ka8X_5zn4vUt9mJ44QlnHlYKx-Xq-cPMRMaIyu2b5maVBFLZyc8tjEmfu17BWFAvMhsx58C2Ru9Rn7kd2XESc1qzq2bv0L66dZwUsWsJYycUp_tS8DRkQ5Nd2q2pkUoXPh5hxhcUjo2BL9rmd7ALDBCFTr3Ee_9-1gZVxEGvw");

                        return headers;
                    }
                };
                requestQueue.add(productRequest);




            }
            else
            {
                Log.i("Text Reg"," is not operational ");
            }




    }
});

        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent,STATUS);
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            userType=extras.getString("usertype");
        }
        if(userType.equalsIgnoreCase("guest"))
        {
             /*   save.setEnabled(false);
                save.setOnHoverListener(new View.OnHoverListener() {
                    @Override
                    public boolean onHover(View view, MotionEvent motionEvent) {
                        Toast.makeText(getApplicationContext(),"This feature is only available for Registered user",Toast.LENGTH_LONG).show();


                        return true;
                    }
                });*/
        }




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==STATUS && resultCode==RESULT_OK)
        {
            Uri ImageUri=data.getData();
            CropImage.activity(ImageUri).setAspectRatio(1,1).setGuidelines(CropImageView.Guidelines.ON).start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                prescription.setImageBitmap(bitmap);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

       /* if(requestCode==STATUS&&resultCode==RESULT_OK)
        {
            resultUri=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            prescription.setImageBitmap(bitmap);

        }
*/

    }




}


