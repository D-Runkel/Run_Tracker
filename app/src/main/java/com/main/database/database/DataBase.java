package com.main.database.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;
import java.time.*;

import java.util.Objects;
import java.util.Vector;


public class DataBase extends SQLiteOpenHelper {

    //increment on update
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RunTracker.db";

    /*
     *   Defining how our data looks, unique ID, username, email and password
     */
    public static class userTable implements BaseColumns {
        public static final String TABLE_NAME = "USER_TABLE";
        public static final String COLUMN_NAME_USERNAME = "user_name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    public static class runTable implements BaseColumns {
        public static final String TABLE_NAME = "RUN_TABLE";
        public static final String COLUMN_NAME_USERID = "user_id";
        public static final String COLUMN_NAME_RUN_LEN_MI = "run_len_mi";
        public static final String COLUMN_NAME_RUN_LEN_KM = "run_len_km";
        public static final String COLUMN_NAME_RUN_TIME = "run_time";
        public static final String COLUMN_NAME_RUN_DATE = "run_date";
    }

    /*
    * Make sure only 1 DB instance
     */
    private static DataBase instance;

    public static DataBase getInstance(Context ctx){
        if(instance == null){
            instance = new DataBase(ctx);
        }
        return instance;
    }

    /*
     * Commands for creation and deletion of the user database
     */
    private static final String SQL_CREATE_USER_ENTRIES = "CREATE TABLE " + userTable.TABLE_NAME
            + " (" + userTable._ID + " LONG PRIMARY KEY, " + userTable.COLUMN_NAME_USERNAME + " TEXT, "
            + userTable.COLUMN_NAME_EMAIL + " TEXT, " + userTable.COLUMN_NAME_PASSWORD + " TEXT)";

    private static final String SQL_CREATE_RUN_ENTRIES = "CREATE TABLE " + runTable.TABLE_NAME +
            " (" + runTable._ID + " LONG PRIMARY KEY, " + runTable.COLUMN_NAME_USERID + " LONG, "
            + runTable.COLUMN_NAME_RUN_LEN_MI + " FLOAT, " + runTable.COLUMN_NAME_RUN_LEN_KM + " FLOAT, "
            +  runTable.COLUMN_NAME_RUN_TIME + " TEXT, " +  runTable.COLUMN_NAME_RUN_DATE + " TEXT)";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + DataBase.userTable.TABLE_NAME;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_ENTRIES);
        db.execSQL(SQL_CREATE_RUN_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Define a method here when the DB is updated, I have no plans for this
    }

    /*
    *   @param email, the email the user is logging in with
    *   @param password, the password the user logs in with
    *   @returns user if valid, returns empty user if not valid
    * <-- LOCAL VALIDATION ONLY, USE FIREBASE CONSOLE TO ADD EMAIL AUTH -->
     */
    public User validateUser(String email, String password, Context ctx){

        User user = getUserByEmail(email);

        if(user.getEmail() == null){
            Toast.makeText(ctx,"Invalid Email Address",Toast.LENGTH_SHORT).show();
            return new User();
        }

        if(user.getPassword().equals(password)){
            Toast.makeText(ctx,"Login Success!",Toast.LENGTH_SHORT).show();
            return user;
        }

        return new User();
    }

    /*
    *   @param email, the email of the user
    *   @param password, the users password
    *   @param userName, the users full name
    *   @returns bool, true if operation success
     */
    public boolean createUser(String email, String userName, String password){

        if(getUserByEmail(email).getId() != -1){
            return false;
        }

        long id = Objects.hash(email,userName,password);
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(userTable._ID, id);
        values.put(userTable.COLUMN_NAME_EMAIL, email);
        values.put(userTable.COLUMN_NAME_USERNAME, userName);
        values.put(userTable.COLUMN_NAME_PASSWORD, password);

        db.insert(userTable.TABLE_NAME,null, values);

        return true;
    }

    /*
    *       @param email, email used when user made account
    *       @returns, User object
    */
    public User getUserByEmail(String email){

        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + userTable.TABLE_NAME + " WHERE " +
                        userTable.COLUMN_NAME_EMAIL + " = " + '"'+email+'"';

        Cursor cursor = db.rawQuery( sql ,null);

        if(cursor.moveToFirst()){
            return new User(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }

        cursor.close();
        return new User();
    }

    /*
    *   @param userId, the userId from the user object
    *   @returns a list of all user runs, empty list if none found
    */

    public Vector<Run> getRunsByUserId(long id){

        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " +userTable.TABLE_NAME+ "," +
                 runTable.TABLE_NAME + " INNER JOIN " + userTable.TABLE_NAME + " ON " +
                runTable.COLUMN_NAME_USERID + " = " + userTable._ID;

        Cursor cursor = db.rawQuery(sql,null);

        Vector<Run> runs = new Vector<Run>();
        do{
            new Run(cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getFloat(2),
                    cursor.getFloat(3),
                    Duration.parse(cursor.getString(4)),
                    LocalDate.parse(cursor.getString(5)));
        }while( cursor.moveToFirst() );

        return runs;
    }
}
