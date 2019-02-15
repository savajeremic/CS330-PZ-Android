package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.R;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.sql.DBHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersProfileFragment extends Fragment {

    private AppCompatTextView name;
    private AppCompatTextView mail;
    private AppCompatButton selectPic;
    private AppCompatButton uploadPic;

    DBHelper dbHelper;



    private CircleImageView profileImageView;

    private static final int MY_CAMERA_REQUEST_CODE = 100;


    static final int SELECT_PHOTO = 1;


    Bitmap thumbnail;


    public UsersProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.users_profile, container, false);
        dbHelper = new DBHelper(getContext());

        Bundle bundle = getArguments();

        String username = getActivity().getIntent().getStringExtra("NAME");
        final String email = getActivity().getIntent().getStringExtra("EMAIL");

        name = (AppCompatTextView) view.findViewById(R.id.name);
        name.setText(username);

        mail = (AppCompatTextView) view.findViewById(R.id.mail);
        mail.setText(email);

        profileImageView = (CircleImageView) view.findViewById(R.id.profile_image);

        byte[] photo = dbHelper.getImage(email);

        if(photo != null){
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage= BitmapFactory.decodeStream(imageStream);
            profileImageView.setImageBitmap(theImage);

        }
        selectPic = (AppCompatButton) view.findViewById(R.id.selectBtn);
        uploadPic = (AppCompatButton) view.findViewById(R.id.uploadBtn);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
        }

        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImageView.setDrawingCacheEnabled(true);
                profileImageView.buildDrawingCache();
                Bitmap bitmap = profileImageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                dbHelper.updateImage(email, data);
                Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                getActivity().recreate();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    profileImageView.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
    }




}
