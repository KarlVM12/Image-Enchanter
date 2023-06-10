package com.example.euleritymobilechallenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SecondFragment extends Fragment {
    public static Drawable editImageViewDrawable;
    public static String originalUrl;

    private Drawable previousEdit;
    private ImageView editImageView;
    private ImageButton filterButton;
    private LinearLayout filterButtonVerticalLayout;
    private ImageButton textButton;
    private LinearLayout textButtonVerticalLayout;
    private ImageButton colorsButton;
    private LinearLayout colorsButtonVerticalLayout;
    private LinearLayout filterLayoutSelected;
    private LinearLayout textLayoutSelected;
    private LinearLayout colorsLayoutSelected;
    private ImageButton brightnessApply;
    private EditText brightnessValue;
    private ImageButton contrastApply;
    private EditText contrastValue;
    private ImageButton blurApply;
    private EditText blurValue;
    private ImageButton tintApply;
    private EditText tintRedValue;
    private EditText tintGreenValue;
    private EditText tintBlueValue;
    private EditText editText;
    private EditText xCoord;
    private EditText yCoord;
    private EditText textSize;
    private TextView enchantedTextView;
    private Button back;
    private Button save;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        previousEdit = editImageViewDrawable;
        editImageView = view.findViewById(R.id.editImageView);
        filterButton = view.findViewById(R.id.filter_button);
        filterButtonVerticalLayout = view.findViewById(R.id.editLinearLayoutVerticalOne);
        textButtonVerticalLayout = view.findViewById(R.id.editLinearLayoutVerticalTwo);
        colorsButtonVerticalLayout = view.findViewById(R.id.editLinearLayoutVerticalThree);
        textButton = view.findViewById(R.id.text_button);
        colorsButton = view.findViewById(R.id.colors_button);
        filterLayoutSelected = view.findViewById(R.id.filterLayoutSelected);
        textLayoutSelected = view.findViewById(R.id.textLayoutSelected);
        colorsLayoutSelected = view.findViewById(R.id.colorsLayoutSelected);
        brightnessApply = view.findViewById(R.id.brightnessApply);
        brightnessValue = view.findViewById(R.id.brightnessInputValue);
        contrastApply = view.findViewById(R.id.contrastApply);
        contrastValue = view.findViewById(R.id.contrastInputValue);
        blurApply = view.findViewById(R.id.blurApply);
        blurValue = view.findViewById(R.id.blurInputValue);
        tintApply = view.findViewById(R.id.tintApply);
        tintRedValue = view.findViewById(R.id.redTintValue);
        tintGreenValue = view.findViewById(R.id.greenTintValue);
        tintBlueValue = view.findViewById(R.id.blueTintValue);
        editText = view.findViewById(R.id.editText);
        xCoord = view.findViewById(R.id.xCoordEdit);
        yCoord = view.findViewById(R.id.yCoordEdit);
        textSize = view.findViewById(R.id.textSizeEdit);
        enchantedTextView = view.findViewById(R.id.enchanted);
        back = view.findViewById(R.id.back_button);
        save = view.findViewById(R.id.save_button);

        // Set editing image to currently selected image
        ImageView editImageView = view.findViewById(R.id.editImageView);
        editImageView.setImageDrawable(editImageViewDrawable);

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Nav to choosing image fragment
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterOptions();
            }
        });

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTextOptions();
            }
        });

        colorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorsOptions();
            }
        });

        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToEditOptionsCancel();
            }
        };

        view.findViewById(R.id.cancelButton).setOnClickListener(cancelListener);
        view.findViewById(R.id.cancelButton1).setOnClickListener(cancelListener);
        view.findViewById(R.id.cancelButton2).setOnClickListener(cancelListener);

        View.OnClickListener applyListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToEditOptionsApply();
            }
        };

        view.findViewById(R.id.applyButton).setOnClickListener(applyListener);
        view.findViewById(R.id.applyButton1).setOnClickListener(applyListener);
        view.findViewById(R.id.applyButton2).setOnClickListener(applyListener);


        // Grayscale filter
        view.findViewById(R.id.filterGrayscale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Glide will grayscale transform async
                Glide.with(view).load(editImageView.getDrawable())
                        .apply(RequestOptions.bitmapTransform(new GrayscaleTransformation()))
                        .into(editImageView);


            }
        });

        // Cartoon filter
        view.findViewById(R.id.filterCartoon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Glide will cartoon transform async
                Glide.with(view).load(editImageView.getDrawable())
                        .apply(RequestOptions.bitmapTransform(new ToonFilterTransformation()))
                        .into(editImageView);

            }
        });

        // Sepia filter
        view.findViewById(R.id.filterSepia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Glide will sepia transform async
                Glide.with(view).load(editImageView.getDrawable())
                        .apply(RequestOptions.bitmapTransform(new SepiaFilterTransformation()))
                        .into(editImageView);

            }
        });

        // Swirl filter
        view.findViewById(R.id.filterSwirl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Glide will swirl transform async
                Glide.with(view).load(editImageView.getDrawable())
                        .apply(RequestOptions.bitmapTransform(new SwirlFilterTransformation()))
                        .into(editImageView);

            }
        });

        // Kuwahara filter
        view.findViewById(R.id.filterKuwahara).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Glide will kuwahara transform async
                Glide.with(view).load(editImageView.getDrawable())
                        .apply(RequestOptions.bitmapTransform(new KuwaharaFilterTransformation()))
                        .into(editImageView);

            }
        });


        // Brightness Color option
        view.findViewById(R.id.brightnessApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // gets users value
                int valueInt = Integer.parseInt(brightnessValue.getText().toString());
                if (valueInt > 100) {
                    valueInt = 100;
                }
                // range of -1.0 to 1.0, 50 as middle number of 0.0
                float brightnessValue = ((float)(valueInt) / 50) - 1.0f;

                System.out.println("BV: " +brightnessValue);
                // Glide will brighten transform async based on value
                Glide.with(view).load(editImageView.getDrawable())
                        .apply(RequestOptions.bitmapTransform(new BrightnessFilterTransformation(brightnessValue)))
                        .into(editImageView);

            }
        });

        // Contrast Color option
        view.findViewById(R.id.contrastApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // gets users value
                int valueInt = Integer.parseInt(contrastValue.getText().toString());
                if (valueInt > 100) {
                    valueInt = 100;
                }

                //range of 0.0 to 2.0, 50 as middle value of 1.0
                float contrastValue = ((float)(valueInt) / 50);

                System.out.println("CV: " +contrastValue);
                // Glide will contrast transform async based on value
                Glide.with(view).load(editImageView.getDrawable())
                        .apply(RequestOptions.bitmapTransform(new ContrastFilterTransformation(contrastValue)))
                        .into(editImageView);

            }
        });

        // Blur Color option
        view.findViewById(R.id.blurApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // gets users value
                int valueInt = Integer.parseInt(blurValue.getText().toString());
                if (valueInt > 100) {
                    valueInt = 100;
                }

                //range of 0 to 25, 50 as middle value of 12.5 --> 12
                int blurValue = (valueInt) / 4;

                System.out.println("BRV: " +blurValue);
                if (blurValue == 0) { // no need to transform
                    return;
                }

                // Glide will blur transform async based on value
                Glide.with(view).load(editImageView.getDrawable())
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(blurValue)))
                        .into(editImageView);

            }
        });

        // Tint Color option
        view.findViewById(R.id.tintApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int red = Integer.parseInt(tintRedValue.getText().toString());
                red = red > 255 ? 255 : red;
                int green = Integer.parseInt(tintGreenValue.getText().toString());
                green = green > 255 ? 255 : green;
                int blue = Integer.parseInt(tintBlueValue.getText().toString());
                blue = blue > 255 ? 255 : blue;

                // Glide will blur transform async based on value
                Glide.with(view).load(editImageView.getDrawable())
                        .apply(RequestOptions.bitmapTransform(new ColorFilterTransformation(Color.argb(100, red, green, blue))))
                        .into(editImageView);

            }
        });

        view.findViewById(R.id.addTextButtonInside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextToImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndUploadImage();

                filterButtonVerticalLayout.setVisibility(View.GONE);
                textButtonVerticalLayout.setVisibility(View.GONE);
                colorsButtonVerticalLayout.setVisibility(View.GONE);
                textLayoutSelected.setVisibility(View.GONE);
                colorsLayoutSelected.setVisibility(View.GONE);
                filterLayoutSelected.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                view.findViewById(R.id.editLinearLayout).setVisibility(View.GONE);

                enchantedTextView.setVisibility(View.VISIBLE);

                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_FirstFragment);
                    }
                }.start();

            }
        });

    }

    public void showFilterOptions() {
        // hide unneccesary stuff, show filter options
        filterButtonVerticalLayout.setVisibility(View.GONE);
        textButtonVerticalLayout.setVisibility(View.GONE);
        colorsButtonVerticalLayout.setVisibility(View.GONE);
        textLayoutSelected.setVisibility(View.GONE);
        colorsLayoutSelected.setVisibility(View.GONE);

        filterLayoutSelected.setVisibility(View.VISIBLE);

        previousEdit = editImageView.getDrawable();
    }

    public void showTextOptions() {
        // hide unneccesary stuff, show text options
        filterButtonVerticalLayout.setVisibility(View.GONE);
        textButtonVerticalLayout.setVisibility(View.GONE);
        colorsButtonVerticalLayout.setVisibility(View.GONE);
        filterLayoutSelected.setVisibility(View.GONE);
        colorsLayoutSelected.setVisibility(View.GONE);

        textLayoutSelected.setVisibility(View.VISIBLE);

        previousEdit = editImageView.getDrawable();
    }

    public void showColorsOptions() {
        // hide unneccesary stuff, show colors options
        filterButtonVerticalLayout.setVisibility(View.GONE);
        textButtonVerticalLayout.setVisibility(View.GONE);
        colorsButtonVerticalLayout.setVisibility(View.GONE);
        textLayoutSelected.setVisibility(View.GONE);
        filterLayoutSelected.setVisibility(View.GONE);

        colorsLayoutSelected.setVisibility(View.VISIBLE);

        previousEdit = editImageView.getDrawable();
    }

    public void backToEditOptionsCancel() {
        textLayoutSelected.setVisibility(View.GONE);
        filterLayoutSelected.setVisibility(View.GONE);
        colorsLayoutSelected.setVisibility(View.GONE);

        filterButtonVerticalLayout.setVisibility(View.VISIBLE);
        textButtonVerticalLayout.setVisibility(View.VISIBLE);
        colorsButtonVerticalLayout.setVisibility(View.VISIBLE);

        // have to also revert any visual changes made in that option since it was cancelled
        editImageView.setImageDrawable(previousEdit);
        editImageViewDrawable = previousEdit;
    }

    public void backToEditOptionsApply() {
        textLayoutSelected.setVisibility(View.GONE);
        filterLayoutSelected.setVisibility(View.GONE);
        colorsLayoutSelected.setVisibility(View.GONE);

        filterButtonVerticalLayout.setVisibility(View.VISIBLE);
        textButtonVerticalLayout.setVisibility(View.VISIBLE);
        colorsButtonVerticalLayout.setVisibility(View.VISIBLE);

        editImageViewDrawable = editImageView.getDrawable();
    }

    public void addTextToImage() {
        Glide.with(this)
                .asBitmap()
                .load(((BitmapDrawable) editImageView.getDrawable()).getBitmap())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        // x
                        String xString = xCoord.getText().toString();
                        int x = 0;
                        if (xString.equals("")) {
                            x = 0;
                        } else {
                            x = Integer.parseInt(xString);
                        }

                        // y
                        String yString = yCoord.getText().toString();
                        int y = 0;
                        if (yString.equals("")) {
                            y = 0;
                        } else {
                            y = Integer.parseInt(yString);
                        }

                        // text
                        String text = editText.getText().toString();
                        System.out.println("Text: " + text);

                        // size
                        String textSizeString = textSize.getText().toString();
                        float textSize = 30f;
                        if (textSizeString.equals("")) {
                            textSize = 30f;
                        } else {
                            textSize = Float.parseFloat(textSizeString);
                            if (textSize < 1) {
                                textSize = 1.0f;
                            }
                        }

                        Bitmap bitmap = resource.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(bitmap);

                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setTextSize(textSize);
                        paint.setColor(Color.BLACK);
                        canvas.drawText(text, x, y, paint);

                        editImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void saveAndUploadImage() {
        // request url to upload
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://eulerity-hackathon.appspot.com/upload").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String uploadUrl = jsonObject.getString("url");

                        File file = editedImageToFile();

                        // multipart request for uploading edited image
                        OkHttpClient uploadClient = new OkHttpClient();
                        RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("appid", "karlmuller10@gmail.com")
                            .addFormDataPart("original", originalUrl)
                            .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                            .build();

                        // POST request
                        Request uploadRequest = new Request.Builder()
                                .url(uploadUrl)
                                .post(requestBody)
                                .build();

                        uploadClient.newCall(uploadRequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                // Handle upload failure
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                // Handle request success
                                if (response.isSuccessful()) {
                                    System.out.println("Uploaded!!");
                                } else {
                                    System.out.println(" Not Uploaded :(");
                                }
                                response.close();
                            }
                        });
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    public File editedImageToFile() {
        File file = null;

        Bitmap bitmap = ((BitmapDrawable) editImageView.getDrawable()).getBitmap();

        file = new File(getContext().getCacheDir(), "editImageTemp.png");

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


}