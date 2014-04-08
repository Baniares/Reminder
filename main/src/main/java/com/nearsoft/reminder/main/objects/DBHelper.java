package com.nearsoft.reminder.main.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nearsoft.reminder.main.objects.Reminder;
import com.nearsoft.reminder.main.objects.User;

/**
 * Created by Baniares on 4/2/14.
 */
public class DBHelper extends SQLiteOpenHelper{
    //Database version
    private static final int DATABASE_VERSION = 2;
    //Database name
    private static final String DATABASE_NAME = "ReminderDB";

    //DataBase table names
    private static final String TABLE_USER = "user";
    private static final String TABLE_REMINDER = "reminder";

    //Colums for table user
    private static final String USER_ID = "id";
    private static final String USER_FACEBOOK_ID = "idFacebook";
    private static final String USER_NAME = "name";
    private static final String USER_EMAIL = "email";
    private static final String USER_BIO = "bio";
    private static final String USER_PASSWORD = "password";

    private static final String[] USER_COLUMNS = {USER_ID,USER_FACEBOOK_ID,USER_NAME,
            USER_EMAIL,USER_BIO,USER_PASSWORD};

    //colums for table reminder
    private static final String REMINDER_ID = "id";
    private static final String REMINDER_USER_ID = "idUser";
    private static final String REMINDER_TITTLE = "tittle";
    private static final String REMINDER_DESCRIPTION = "description";
    private static final String REMINDER_START_DATE = "startDate";
    private static final String REMINDER_END_DATE = "endDate";

    private static final String[] REMINDER_COLUMNS ={REMINDER_ID,REMINDER_USER_ID,REMINDER_TITTLE,
            REMINDER_DESCRIPTION,REMINDER_START_DATE,REMINDER_END_DATE};

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE "+TABLE_USER+" ( " +
                USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                USER_FACEBOOK_ID+" TEXT, "+
                USER_NAME+" TEXT, "+
                USER_EMAIL+" TEXT, "+
                USER_BIO+" TEXT, "+
                USER_PASSWORD+" TEXT )";
        String CREATE_REMINDER_TABLE = "CREATE TABLE "+TABLE_REMINDER+" ( " +
                REMINDER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                REMINDER_USER_ID+" INTEGER NOT NULL, "+
                REMINDER_TITTLE+" TEXT, "+
                REMINDER_DESCRIPTION+" TEXT, "+
                REMINDER_START_DATE+" INTEGER, "+
                REMINDER_END_DATE+" INTEGER, "+
                "FOREIGN KEY("+REMINDER_USER_ID+") REFERENCES "+TABLE_USER+"("+USER_ID+") )";
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older users and reminders table if existed
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS reminder");

        // create fresh books table
        this.onCreate(db);
    }

    //methods for managing database
    public void addUser(User user){
        //reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values
        ContentValues values = new ContentValues();
        values.put(USER_NAME,user.getName());
        values.put(USER_FACEBOOK_ID, user.getIdFacebook());
        values.put(USER_EMAIL,user.getEmail());
        values.put(USER_BIO,user.getBio());
        values.put(USER_PASSWORD,user.getPassword());

        //insert values to database
        if (db != null) {
            db.insert(TABLE_USER,
                    null,
                    values);
        }

        //close database
        db.close();
    }
    public void addReminder(Reminder reminder){
        //reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values
        ContentValues values = new ContentValues();
        values.put(REMINDER_USER_ID,reminder.getUserID());
        values.put(REMINDER_TITTLE, reminder.getTittle());
        values.put(REMINDER_DESCRIPTION,reminder.getDescription());
        values.put(REMINDER_START_DATE,reminder.getStartDate());
        values.put(REMINDER_END_DATE,reminder.getEndDate());

        //insert values to database
        if (db != null) {
            db.insert(TABLE_REMINDER,
                    null,
                    values);
        }

        //close database
        db.close();
    }

    public Cursor getCurrentReminders(int idUser){
//        reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        //build query
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(TABLE_REMINDER,
                    REMINDER_COLUMNS,REMINDER_USER_ID+"=? and"+
                    REMINDER_END_DATE+">?",
                    new String[] { String.valueOf(idUser), String.valueOf(System.currentTimeMillis()) },
                    null,
                    null,
                    null,
                    null);
        }
//      return the cursor
        return cursor;
    }

    public Cursor getPassedReminders(int idUser){
//        reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        //build query
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(TABLE_REMINDER,
                    REMINDER_COLUMNS,REMINDER_USER_ID+"=? and"+
                    REMINDER_END_DATE+"<=?",
                    new String[] { String.valueOf(idUser), String.valueOf(System.currentTimeMillis()) },
                    null,
                    null,
                    null,
                    null);
        }
//      return the cursor
        return cursor;
    }

    public Reminder getReminder(int id){
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        //build query
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(TABLE_REMINDER,
                    REMINDER_COLUMNS,
                    " id=?",
                    new String[] { String.valueOf(id) },
                    null,
                    null,
                    null,
                    null);
        }

        //get the first result
        if(cursor!=null)
            cursor.moveToFirst();

        //construct reminder object
        Reminder reminder = new Reminder();
        if (cursor != null) {
            reminder.setId(cursor.getInt(0));
            reminder.setUserID(cursor.getInt(1));
            reminder.setTittle(cursor.getString(2));
            reminder.setDescription(cursor.getString(3));
            reminder.setStartDate(cursor.getLong(4));
            reminder.setEndDate(cursor.getLong(5));
        }
//        close DB
        db.close();

        return reminder;
    }

    public User getUser(int id){
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

//        build Query
        Cursor cursor = null;
        if(db!=null){
            cursor = db.query(TABLE_USER,
                    USER_COLUMNS,
                    "id=?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null,
                    null);
        }
        //get the first result
        if(cursor!=null)
            cursor.moveToFirst();

//        construct user object
        User user = new User();
        if(cursor !=null){
            user.setId(cursor.getInt(0));
            user.setIdFacebook(cursor.getString(1));
            user.setName(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setBio(cursor.getString(4));
            user.setPassword(cursor.getString(5));
        }
//        close db
        db.close();

        return user;
    }

    public Boolean getUserExistFromFacebookID(String idFacebook){
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean exist = false;

//        build Query
        Cursor cursor = null;
        if(db!=null){
            cursor = db.query(TABLE_USER,
                    USER_COLUMNS,
                    USER_FACEBOOK_ID+"=?",
                    new String[]{idFacebook},
                    null,
                    null,
                    null,
                    null);
        }
        //get the first result
        if(cursor.getCount()>0)
            exist = true;

//        close db
        db.close();

        return exist;
    }
    public User getUserFromFacebookId(String idFacebook){
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

//        build Query
        Cursor cursor = null;
        if(db!=null){
            cursor = db.query(TABLE_USER,
                    USER_COLUMNS,
                    USER_FACEBOOK_ID+"=?",
                    new String[]{idFacebook},
                    null,
                    null,
                    null,
                    null);
        }
        //get the first result
        if(cursor!=null)
            cursor.moveToFirst();

//        construct user object
        User user = new User();
        if(cursor !=null){
            user.setId(cursor.getInt(0));
            user.setIdFacebook(cursor.getString(1));
            user.setName(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setBio(cursor.getString(4));
            user.setPassword(cursor.getString(5));
        }
//        close db
        db.close();

        return user;
    }

    public void deleteUser(User user){
//        reference to a writeable DB
        SQLiteDatabase db = this.getWritableDatabase();

//        delete
        if (db != null) {
            db.delete(TABLE_USER,
                    USER_ID+"=?",
                    new String[]{String.valueOf(user.getId())});
        }

//        close connection
        db.close();
    }

    public void deleteReminder(Reminder reminder){
//        reference to a writeable DB
        SQLiteDatabase db = this.getWritableDatabase();

//        delete
        if (db != null) {
            db.delete(TABLE_REMINDER,
                    REMINDER_ID+"=?",
                    new String[]{String.valueOf(reminder.getId())});
        }

//        close connection
        db.close();
    }
    public int updateUser(User user){
//        reference to a writeable DB
        SQLiteDatabase db = this.getWritableDatabase();

//        create content values to add
        ContentValues values = new ContentValues();
        values.put(USER_NAME,user.getName());
        values.put(USER_FACEBOOK_ID, user.getIdFacebook());
        values.put(USER_EMAIL,user.getEmail());
        values.put(USER_BIO,user.getBio());
        values.put(USER_PASSWORD,user.getPassword());

//        update row
        int i = 0;
        if (db != null) {
            i = db.update(TABLE_USER,
                    values,
                    USER_ID+"=?",
                    new String[]{String.valueOf(user.getId())});
        }

//        close DB
        db.close();

        return i;
    }

    public int updateReminder(Reminder reminder){
//        reference to a writeable DB
        SQLiteDatabase db = this.getWritableDatabase();

//        create conten values to add
        ContentValues values = new ContentValues();
        values.put(REMINDER_USER_ID,reminder.getUserID());
        values.put(REMINDER_TITTLE, reminder.getTittle());
        values.put(REMINDER_DESCRIPTION,reminder.getDescription());
        values.put(REMINDER_START_DATE,reminder.getStartDate());
        values.put(REMINDER_END_DATE,reminder.getEndDate());

//        update row
        int i = 0;
        if (db != null) {
            i= db.update(TABLE_REMINDER,
                    values,
                    REMINDER_ID+"=?",
                    new String[]{String.valueOf(reminder.getId())});
        }

//        close DB
        db.close();

        return i;
    }
}
