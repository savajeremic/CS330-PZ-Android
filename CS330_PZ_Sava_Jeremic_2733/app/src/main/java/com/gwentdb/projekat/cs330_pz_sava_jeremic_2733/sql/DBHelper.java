package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.model.Game;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // DB Properties
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "gog.db";

    //DB Tables(USER & GAME)
    static final String TABLE_USER = "user";
    static final String TABLE_GAME = "game";

    // DB USER Columns
    static final String COLUMN_USER_ID = "user_id";
    static final String COLUMN_USER_NAME = "user_name";
    static final String COLUMN_USER_EMAIL = "user_email";
    static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_AVATAR = "user_avatar";

    //DB GAME Columns
    static final String COLUMN_GAME_ID = "game_id";
    static final String COLUMN_GAME_NAME = "game_name";
    static final String COLUMN_GAME_GENRE = "game_genre";
    static final String COLUMN_GAME_DESCRIPTION = "game_description";
    static final String COLUMN_GAME_COMPANY = "game_company";
    static final String COLUMN_GAME_PRICE = "game_price";
    static final String COLUMN_GAME_IMG = "game_img";

    // Create USER Table Statement
    static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_AVATAR + " BLOB" + ")";

    // Create USER Table Statement
    static final String CREATE_GAME_TABLE = "CREATE TABLE " + TABLE_GAME + "("
            + COLUMN_GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_GAME_NAME + " TEXT,"
            + COLUMN_GAME_GENRE + " TEXT," + COLUMN_GAME_DESCRIPTION + " TEXT,"
            + COLUMN_GAME_COMPANY + " TEXT," + COLUMN_GAME_PRICE + " INTEGER,"
            + COLUMN_GAME_IMG + " TEXT" + ")";

    // Delete Table Statements(USER & GAME)
    static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    static final String DROP_GAME_TABLE = "DROP TABLE IF EXISTS " + TABLE_GAME;

    private SQLiteDatabase db;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_GAME_TABLE);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(DROP_USER_TABLE);
            db.execSQL(DROP_GAME_TABLE);
            onCreate(db);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void addUser(User user){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public boolean checkUser(String email){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(((cursor != null) && (cursor.getCount() > 0))){
            Log.d("CursorCount", "Cursor count: " + cursorCount);
            return true;
        }
        return false;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0){
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public String getUserByEmail(String email){

        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        String username = "";
        while(cursor.moveToNext()) {
            username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
        }
        cursor.close();
        db.close();
        return username;
    }

    public void updateImage(String email, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_AVATAR, image);

        db.update(TABLE_USER, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{email});
        db.close();
    }

    public byte[] getImage(String email){
        String[] columns = {
                COLUMN_USER_AVATAR
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        byte[] avatar = null;
        while(cursor.moveToNext()) {
            avatar = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_USER_AVATAR));
        }
        cursor.close();
        db.close();
        return avatar;
    }

    //insertGame
    public void insertGame(String name, String genre, String description, String company, int price, byte[] img){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_NAME, name);
        values.put(COLUMN_GAME_GENRE, genre);
        values.put(COLUMN_GAME_DESCRIPTION, description);
        values.put(COLUMN_GAME_COMPANY, company);
        values.put(COLUMN_GAME_PRICE, price);
        values.put(COLUMN_GAME_IMG, img);

        db.insert(TABLE_GAME, null, values);
        Log.d("NAME", "NAME: " + name);
        db.close();
    }

    //updateGame
    public void updateGame(int id, String name, String genre, String description, String company,
                           int price, byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_NAME, name);
        values.put(COLUMN_GAME_GENRE, genre);
        values.put(COLUMN_GAME_DESCRIPTION, description);
        values.put(COLUMN_GAME_COMPANY, company);
        values.put(COLUMN_GAME_PRICE, price);
        values.put(COLUMN_GAME_IMG, img);

        // updating row
        db.update(TABLE_GAME, values, COLUMN_GAME_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    //deleteGame
    public void deleteGame(int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to delete game using id
        String sql = "DELETE FROM game WHERE game_id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getAllGames() {
        String[] columns = {
                COLUMN_GAME_ID,
                COLUMN_GAME_NAME,
                COLUMN_GAME_DESCRIPTION,
                COLUMN_GAME_COMPANY,
                COLUMN_GAME_PRICE,
                COLUMN_GAME_IMG
        };

        return db.query(TABLE_GAME,
                columns,
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }
}
