package escan.com.networkingLibraryDemo.RecyclerViewDemo.NetowrkingDemo.volleyDemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import escan.com.networkingLibraryDemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGET, btnPOST, btnImageLoader, btnCustomRequest, btnImageRequest;
    NetworkImageView networkImageView;
    ImageView imageView;
    ArrayList<UserList.UserDataList> mUserDataList = new ArrayList<>();
    String BASE_URL = "https://reqres.in";
    String IMAGE_URL = "https://www.android.com/static/2016/img/share/oreo-lg.jpg";
    int numberOfRequestsCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley_activity_main);

        btnGET = findViewById(R.id.btnGET);
        btnPOST = findViewById(R.id.btnPOST);
        btnImageLoader = findViewById(R.id.btnImageLoader);
        btnImageRequest = findViewById(R.id.btnImageRequest);
        btnCustomRequest = findViewById(R.id.btnCustomRequest);
        networkImageView = findViewById(R.id.networkImageView);
        imageView = findViewById(R.id.imageView);
        btnGET.setOnClickListener(this);
        btnPOST.setOnClickListener(this);
        btnImageLoader.setOnClickListener(this);
        btnCustomRequest.setOnClickListener(this);
        btnImageRequest.setOnClickListener(this);

        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        networkImageView.setErrorImageResId(R.drawable.ic_error);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGET:
                GETStringAndJSONRequest("2", "4");
                break;
            case R.id.btnPOST:
                POSTStringAndJSONRequest();
                break;
            case R.id.btnImageLoader:
                imageLoader();
                break;
            case R.id.btnImageRequest:
                imageRequest();
                break;
            case R.id.btnCustomRequest:
                customRequest();
                break;
        }
    }

    private void GETStringAndJSONRequest(String page_1, String page_2) {
        mUserDataList.clear();
        numberOfRequestsCompleted = 0;
        VolleyLog.DEBUG = true;
        RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        String uri_page_one = String.format(BASE_URL + "/api/users?page=%1$s", page_1);
        final String uri_page_two = String.format(BASE_URL + "/api/users?page=%1$s", page_2);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri_page_one, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.d(response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                UserList userList = mGson.fromJson(response, UserList.class);
                Log.d("API123", userList.page + "");
                mUserDataList.addAll(userList.userDataList);
                ++numberOfRequestsCompleted;

            }
        }, errorListener) {

            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }

        };

        queue.add(stringRequest);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(uri_page_two, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                VolleyLog.wtf(response.toString(), "utf-8");
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                UserList userList = mGson.fromJson(response.toString(), UserList.class);
                mUserDataList.addAll(userList.userDataList);
                ++numberOfRequestsCompleted;

            }
        }, errorListener) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        queue.add(jsonObjectRequest);


        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                try {
                    if (request.getCacheEntry() != null) {
                        String cacheValue = new String(request.getCacheEntry().data, "UTF-8");
                        Log.d("API123", request.getCacheKey() + " " + cacheValue);

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (numberOfRequestsCompleted == 2) {
                    numberOfRequestsCompleted = 0;
                    startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class).putExtra("users", mUserDataList));
                }
            }
        });
    }

    private void POSTStringAndJSONRequest() {

        RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        VolleyLog.DEBUG = true;
        String uri = BASE_URL + "/api/users";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                VolleyLog.wtf(response);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

            }
        }, errorListener) {
            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("name", "XYZ");
                params.put("job", "Android Developer");


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }

        };


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "JournalDev.com");
            jsonObject.put("job", "To teach you the best");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(uri, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.wtf(response.toString());
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

            }
        }, errorListener) {

            @Override
            public int getMethod() {
                return Method.POST;
            }

            @Override
            public Priority getPriority() {
                return Priority.NORMAL;
            }
        };


        StringRequest stringRequestPOSTJSON = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                VolleyLog.wtf(response);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

            }
        }, errorListener) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", "Android Tutorials");
                    jsonObject.put("job", "To implement Volley in an Android Application.");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = jsonObject.toString();


                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }


        };

        queue.add(stringRequest);
        queue.add(jsonObjectRequest);
        queue.add(stringRequestPOSTJSON);


    }

    private void imageLoader() {
        RequestQueue mRequestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        networkImageView.setImageUrl(IMAGE_URL, imageLoader);
        imageLoader.get(IMAGE_URL, ImageLoader.getImageListener(
                networkImageView, R.mipmap.ic_launcher, R.drawable.ic_error));
    }

    private void imageRequest() {
        RequestQueue mRequestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        ImageRequest imageRequest = new ImageRequest(IMAGE_URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                if (response != null) {
                    imageView.setImageBitmap(response);
                }

            }
        }, 200, 200, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, errorListener);

        mRequestQueue.add(imageRequest);
    }


    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private void customRequest() {
        mUserDataList.clear();
        RequestQueue mRequestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        String url = String.format(BASE_URL + "/api/users?page=%1$s", "2");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

        GsonRequest<UserList> gsonRequest = new GsonRequest<>(url, UserList.class, headers, new Response.Listener<UserList>() {
            @Override
            public void onResponse(UserList response) {
                mUserDataList.addAll(response.userDataList);
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class).putExtra("users", mUserDataList));

            }
        }, errorListener);

        mRequestQueue.add(gsonRequest);

    }

}
