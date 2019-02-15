package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.R;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.adapter.GameListAdapter;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments.GamesFragment;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.model.Game;

public class GamesListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Game> list;
    GameListAdapter adapter = null;

    ImageView imageViewIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.games_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Game List");

        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new GameListAdapter(this, R.layout.game_row, list);
        listView.setAdapter(adapter);

        //get all data from sqlite
        Cursor cursor = GamesFragment.DBHelper.getData("SELECT * FROM game");
        list.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String genre = cursor.getString(2);
            String description = cursor.getString(3);
            String company = cursor.getString(4);
            int price = cursor.getInt(5);
            byte[] img  = cursor.getBlob(6);
            //add to list
            list.add(new Game(id, name, genre, description, company, price, img));
        }
        adapter.notifyDataSetChanged();
        if (list.size()==0){
            //if there is no game in table of database which means listview is empty
            Toast.makeText(this, "No game found...", Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //alert dialog to display options of update and delete
                final CharSequence[] items = {"Update", "Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(GamesListActivity.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            //update
                            Cursor c = GamesFragment.DBHelper.getData("SELECT game_id FROM game");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            //show update dialog
                            showDialogUpdate(GamesListActivity.this, arrID.get(position));
                        }
                        if (i==1){
                            //delete
                            Cursor c = GamesFragment.DBHelper.getData("SELECT game_id FROM game");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    private void showDialogDelete(final int idGame) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure to delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    GamesFragment.DBHelper.deleteGame(idGame);
                    Toast.makeText(GamesListActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateGameList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(FragmentActivity activity, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.games_update);
        dialog.setTitle("Update");

        imageViewIcon = dialog.findViewById(R.id.edtImg);
        final EditText edtName = dialog.findViewById(R.id.edtName);
        final EditText edtGenre = dialog.findViewById(R.id.edtGenre);
        final EditText edtDescription = dialog.findViewById(R.id.edtDescription);
        final EditText edtCompany = dialog.findViewById(R.id.edtCompany);
        final EditText edtPrice = dialog.findViewById(R.id.edtPrice);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        //set width of dialog
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        //set height of dialog
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        //in update dialog click image view to update image
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check external storage permission
                ActivityCompat.requestPermissions(
                        GamesListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GamesFragment.DBHelper.updateGame(
                            position,
                            edtName.getText().toString().trim(),
                            edtGenre.getText().toString().trim(),
                            edtDescription.getText().toString().trim(),
                            edtCompany.getText().toString().trim(),
                            Integer.parseInt(edtPrice.getText().toString()),
                            GamesFragment.imageViewToByte(imageViewIcon)
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update Successfull", Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.e("Update error", error.getMessage());
                }
                updateGameList();
            }
        });

    }

    private void updateGameList() {
        //get all data from sqlite
        Cursor cursor = GamesFragment.DBHelper.getData("SELECT * FROM game");
        list.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String genre = cursor.getString(2);
            String description = cursor.getString(3);
            String company = cursor.getString(4);
            int price = cursor.getInt(5);
            byte[] img  = cursor.getBlob(6);
            //add to list
            list.add(new Game(id, name, genre, description, company, price, img));
        }
        adapter.notifyDataSetChanged();
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
        if (requestCode == 888){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "Don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1,1)// image will be square
                    .start(GamesListActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                imageViewIcon.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
