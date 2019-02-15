package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.R;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.sql.DBHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class WelcomeFragment extends Fragment{

    private AppCompatTextView name;
    private AppCompatTextView mail;

    private CircleImageView profileImageView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    static final int SELECT_PHOTO = 1;
    Bitmap thumbnail;
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.welcome, container, false);
        dbHelper = new DBHelper(getContext());

        Bundle bundle = getArguments();

        String username = getActivity().getIntent().getStringExtra("NAME");
        final String email = getActivity().getIntent().getStringExtra("EMAIL");

        name = (AppCompatTextView) view.findViewById(R.id.welcomeName);
        name.setText(username);

        mail = (AppCompatTextView) view.findViewById(R.id.welcomeMail);
        mail.setText(email);

        profileImageView = (CircleImageView) view.findViewById(R.id.profile_image);

        byte[] photo = dbHelper.getImage(email);

        if(photo != null){
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage= BitmapFactory.decodeStream(imageStream);
            profileImageView.setImageBitmap(theImage);

        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
