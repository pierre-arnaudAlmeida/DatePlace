package fr.blackmamba.dateplaceapp.backgroundtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "users";
    private static final String TABLE_NAME2 = "user_connected";
    private static final String COL1 = "user_id";
    private static final String COL2 = "last_name";
    private static final String COL3 = "name";
    private static final String COL4 = "adress_mail";
    private static final String COL5 = "password";
    private static final String COL6 = "birthday";
    private static final String COL7 = "goal";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable_users = "CREATE TABLE " + TABLE_NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                COL6 + " TEXT, " +
                COL7 + " TEXT);";
        db.execSQL(createTable_users);

        String createTable_user_connected = "CREATE TABLE " + TABLE_NAME2 + " (" +
                COL1 + " INTEGER PRIMARY KEY, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                COL6 + " TEXT, " +
                COL7 + " TEXT);";
        db.execSQL(createTable_user_connected);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME+";");
        onCreate(db);

    }
    public void deleteUserCOnnected(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME2+";");
    }

    public boolean addDataUser(int user_id,String last_name,String name,String adress_mail,String password,String birthday,String goal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(COL1, user_id);
        value.put(COL2, last_name);
        value.put(COL3, name);
        value.put(COL4, adress_mail);
        value.put(COL5, password);
        value.put(COL6, birthday);
        value.put(COL7, goal);

        long result = db.insert(TABLE_NAME, null, value);

        if (result== -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean addDataUserConnected(int user_id,String last_name,String name,String adress_mail,String password,String birthday,String goal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(COL1, user_id);
        value.put(COL2, last_name);
        value.put(COL3, name);
        value.put(COL4, adress_mail);
        value.put(COL5, password);
        value.put(COL6, birthday);
        value.put(COL7, goal);

        long result = db.insert(TABLE_NAME2, null, value);

        if (result== -1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor getDataUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getDataUserConnected(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME2;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean updateDataUser(int user_id,String last_name,String name,String adress_mail,String password,String birthday,String goal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(COL2, last_name);
        value.put(COL3, name);
        value.put(COL4, adress_mail);
        value.put(COL5, password);
        value.put(COL6, birthday);
        value.put(COL7, goal);

        long result = db.update(TABLE_NAME, value, "user_id="+user_id , null );

        if (result== -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateDataUserConnected(int user_id,String last_name,String name,String adress_mail,String password,String birthday,String goal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(COL2, last_name);
        value.put(COL3, name);
        value.put(COL4, adress_mail);
        value.put(COL5, password);
        value.put(COL6, birthday);
        value.put(COL7, goal);

        long result = db.update(TABLE_NAME2, value, "user_id="+user_id , null );

        if (result== -1){
            return false;
        }else{
            return true;
        }
    }
}
