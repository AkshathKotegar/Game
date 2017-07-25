package com.improstech.housie.app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User2 on 21-09-2016.
 */
public class DataHelper extends SQLiteOpenHelper {

    public static String RECORDKEY, MODE, NAME, EMAIL, MOBILENO;
    public static final String DATABASE_NAME = "housie.db";
    public static final int DATABASE_VERSION = 1;
    public static final String USER_TABLE = "User";
    public static final String USER_RECORD_KEY = "RecordKey";
    public static final String USER_MODE = "Mode";
    public static final String USER_NAME = "Name";
    public static final String USER_EMAIL = "Email";
    public static final String USER_MOBILENO = "Mobile";


    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + USER_TABLE + " (" +
                USER_RECORD_KEY + " TEXT, " +
                USER_MODE + " TEXT, " +
                USER_NAME + " TEXT, " +
                USER_EMAIL + " TEXT, " +
                USER_MOBILENO + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int version_old, int current_version) {
        try {
            Cursor cursor = getUser(db);
            while (cursor.moveToNext()) {
                RECORDKEY = cursor.getString(0);
                MODE = cursor.getString(1);
                NAME = cursor.getString(2);
                EMAIL = cursor.getString(3);
                MOBILENO = cursor.getString(4);
            }
            String query1;
            query1 = "DROP TABLE IF EXISTS " + USER_TABLE;
            db.execSQL(query1);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cursor getUser(SQLiteDatabase db) {
        String selectQuery = "SELECT  * FROM " + USER_TABLE;
        return db.rawQuery(selectQuery, null);
    }

    /*Function to insert user details to database*/
    public String insertUserDetails(String recordKey, String mode, String name, String emailId, String mobileNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Str_Return_Value = null;
        long query_ret;
        try {
            ContentValues values = new ContentValues();
            values.put(USER_RECORD_KEY, recordKey);
            values.put(USER_MODE, mode);
            values.put(USER_NAME, name);
            values.put(USER_EMAIL, emailId);
            values.put(USER_MOBILENO, mobileNo);
            query_ret = db.insertWithOnConflict(USER_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            if (query_ret != -1) {
                Str_Return_Value = "success";
            } else {
                Str_Return_Value = "failure";
            }
        } catch (Exception e) {
        }
        db.close();
        return Str_Return_Value;
    }



    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + USER_TABLE);
    }


    public Cursor getUserDetails() {
        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + USER_TABLE;
        return database.rawQuery(selectQuery, null);
    }



    public String updateUserDetails(String newEmail, String userKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Str_Return_Value = null;
        String Update_Query = "Update User SET Email ='" + newEmail + "' WHERE UserKey ='" + userKey + "'";
        db.execSQL(Update_Query);
        db.close();
        return Str_Return_Value;
    }

    public String updateCardDetails(String templateId, int cardHolderKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Str_Return_Value = null;
        String Update_Query = "Update CardHolderDetails SET TemplateId ='" + templateId + "' WHERE CardHolderKey ='" + cardHolderKey + "'";
        db.execSQL(Update_Query);
        db.close();
        return Str_Return_Value;
    }

}


