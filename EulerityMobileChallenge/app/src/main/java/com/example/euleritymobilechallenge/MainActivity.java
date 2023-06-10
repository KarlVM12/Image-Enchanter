package com.example.euleritymobilechallenge;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    public static List<String> imageUrls = new ArrayList<String>(11);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {


        //System.out.println("Here 0 ");

        // GET images
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://eulerity-hackathon.appspot.com/image").build();

        //System.out.println("Here 1");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //System.out.println("Here call fail");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Get response as a JSONArray
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);
                        List<String> imageUrlsList = new ArrayList<>();

                        // Parse the url for each each into imageUrls
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String imageUrl = jsonObject.getString("url");
                            imageUrlsList.add(imageUrl);
                            MainActivity.imageUrls.add(imageUrl);
                            //System.out.println(imageUrl);
                        }

                        // Update UI for when images are done loading
                        Activity activity = MainActivity.this;
                        activity.runOnUiThread(() -> {
                            // Load each url image asynchronously
                            System.out.println("Capacity: " + imageUrlsList.size());
                            for (int i = 0; i < imageUrlsList.size(); i++) {
                                // Async load image into each image button
                                // !!! this is going to be moved over the first fragment and called from in the main activity
                                // this way we can referrence the imageButton in the First Fragment easier since
                                // the function will already be declared in the first fragment

                                System.out.println("IB Before loadImage: " + i);
                                loadImage(imageUrlsList.get(i), i);
                            }
                        });
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("Here Response Fail");
                }
            }



        });

        return super.onCreateView(name, context, attrs);
    }

    public void loadImage(String imageUrl, int index) {
        /*RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error);*/
        System.out.println("IB Before Glide: " + index);
        Glide.with(this)
                .load(imageUrl)
                //.apply(requestOptions)
                .into(new Target<Drawable>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        // Called when the image load is cleared, if needed
                        System.out.println("load Started");
                    }


                    @Override
                    public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {

                        // Get the current destination from the NavController
                        NavDestination currentDestination = navController.getCurrentDestination();

                        // Check if the current destination is the FirstFragment
                        if (currentDestination != null && currentDestination.getId() == R.id.FirstFragment) {
                            FirstFragment firstFragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getFragments().get(0);
                            // Will be null if not created first
                            //FirstFragment firstFragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.FirstFragment);
                            System.out.println("IB Before firstFragment null: " + firstFragment);
                            if (firstFragment != null) {
                                System.out.println("IB Before firstFragment.updateImageButton: " + index);
                                firstFragment.updateImageButton(resource, index);
                            }
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Called when the image load is cleared, if needed
                        System.out.println("load Cleared");
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // Called when the image load fails, if needed
                        System.out.println("load Cleared");
                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {

                        //cb.onSizeReady(108, 108);

                        // Get the current destination from the NavController
                        NavDestination currentDestination = navController.getCurrentDestination();

                        // Check if the current destination is the FirstFragment
                        if (currentDestination != null && currentDestination.getId() == R.id.FirstFragment) {
                            FirstFragment firstFragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getFragments().get(0);
                            if (firstFragment != null) {
                                // Fits to imageButton size
                                //FirstFragment firstFragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.FirstFragment);
                                ImageButton imageButton = firstFragment.imageButtonArrayList.get(index);

                                int targetWidth = imageButton.getWidth();
                                int targetHeight = imageButton.getHeight();

                                // resizes image
                                if (targetWidth > 0 && targetHeight > 0) {
                                    cb.onSizeReady(targetWidth, targetHeight);
                                } else {
                                    imageButton.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                        @Override
                                        public boolean onPreDraw() {
                                            imageButton.getViewTreeObserver().removeOnPreDrawListener(this);
                                            int updatedWidth = imageButton.getWidth();
                                            int updatedHeight = imageButton.getHeight();
                                            cb.onSizeReady(updatedWidth, updatedHeight);
                                            return true;
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void removeCallback(SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable com.bumptech.glide.request.Request request) {

                    }

                    @Nullable
                    @Override
                    public com.bumptech.glide.request.Request getRequest() {
                        return null;
                    }

                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getLabel().equals("Second Fragment")) {
            navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
        }
    }
}