package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.activities.GamesListActivity;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.sql.DBHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.R;

import java.io.ByteArrayOutputStream;

public class GamesFragment extends Fragment{
    EditText edtName, edtGenre, edtDescription, edtCompany, edtPrice;
    Button btnAdd, btnList;
    ImageView gameImageView;
    public static DBHelper DBHelper;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.games, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("New Game");

        edtName = getView().findViewById(R.id.edtName);
        edtGenre = getView().findViewById(R.id.edtGenre);
        edtDescription = getView().findViewById(R.id.edtDescription);
        edtCompany = getView().findViewById(R.id.edtCompany);
        edtPrice = getView().findViewById(R.id.edtPrice);
        btnAdd = getView().findViewById(R.id.btnAdd);
        btnList = getView().findViewById(R.id.btnList);
        gameImageView = getView().findViewById(R.id.edtImg);

        //creating database
        DBHelper = new DBHelper(getActivity());

        //select image by on imageview click
        gameImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //read external storage permission to select image from gallery
                //runtime permission for devices android 6.0 and above
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

        //add game to sqlite
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DBHelper.insertGame(
                            edtName.getText().toString().trim(),
                            edtGenre.getText().toString().trim(),
                            edtDescription.getText().toString().trim(),
                            edtCompany.getText().toString().trim(),
                            Integer.parseInt(edtPrice.getText().toString()),
                            imageViewToByte(gameImageView)
                    );
                    Toast.makeText(getActivity(), "Added successfully", Toast.LENGTH_SHORT).show();
                    //reset views
                    edtName.setText("");
                    edtGenre.setText("");
                    edtDescription.setText("");
                    edtCompany.setText("");
                    edtPrice.setText("");
                    gameImageView.setImageResource(R.drawable.ic_insert_image);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //show game list
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start gameView fragment
                startActivity(new Intent(getActivity(), GamesListActivity.class));
                /*GamesListFragment gameListFragment= new GamesListFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, gameListFragment,"GamesListFragment")
                        .addToBackStack(null)
                        .commit();*/
                /*GamesListFragment fragment2 = new GamesListFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, fragment2);
                fragmentTransaction.commit();*/
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getContext(), "Don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1,1)// image will be square
                    .start(getContext(), GamesFragment.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK){
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                gameImageView.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
