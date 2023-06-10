package com.example.euleritymobilechallenge;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class FirstFragment extends Fragment {
    public ArrayList<ImageButton> imageButtonArrayList = new ArrayList<ImageButton>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // creating array of imageButtons
        imageButtonArrayList.add(view.findViewById(R.id.imageButton0));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton1));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton2));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton3));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton4));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton5));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton6));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton7));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton8));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton9));
        imageButtonArrayList.add(view.findViewById(R.id.imageButton10));
        //System.out.println("IB: " + (view.findViewById(R.id.imageButton0) instanceof ImageButton));

        for (int i = 0; i < imageButtonArrayList.size(); i++) {
            final int index = i;
            imageButtonArrayList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Nav to editing fragment
                    NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);

                    // Let editing fragment know which drawable image we want to edit
                    SecondFragment.editImageViewDrawable = imageButtonArrayList.get(index).getDrawable();
                    SecondFragment.originalUrl = MainActivity.imageUrls.get(index);
                }
            });
        }

        /*view.findViewById(R.id.imageButton0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }

    public void updateImageButton(Drawable drawable, int index) {
        // Update the ImageButton with the new drawable
        imageButtonArrayList.get(index).setImageDrawable(drawable);
        System.out.println("IB Update: " + index + " ");
    }

}