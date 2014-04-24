package com.nearsoft.reminder.main.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nearsoft.reminder.main.models.Reminder;
import com.nearsoft.reminder.main.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 *this class is used to get connection to the data base an create the database in case it doesn't exist.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "ReminderDB";

    private static final String USER_TABLE_NAME = "user";
    private static final String REMINDER_TABLE_NAME = "reminder";

    private static final String USER_ID_COLUMN_NAME = "_id";
    private static final String USER_FACEBOOK_ID = "idFacebook";
    private static final String USER_NAME = "name";
    private static final String USER_EMAIL = "email";
    private static final String USER_BIO = "bio";
    private static final String USER_PASSWORD = "password";

    private static final String[] USER_COLUMNS = {USER_ID_COLUMN_NAME,USER_FACEBOOK_ID,USER_NAME,
            USER_EMAIL,USER_BIO,USER_PASSWORD};

    private static final String REMINDER_ID = "_id";
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
        String CREATE_USER_TABLE = "CREATE TABLE "+ USER_TABLE_NAME +" ( " +
                USER_ID_COLUMN_NAME +" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                USER_FACEBOOK_ID+" TEXT, "+
                USER_NAME+" TEXT, "+
                USER_EMAIL+" TEXT, "+
                USER_BIO+" TEXT, "+
                USER_PASSWORD+" TEXT )";
        String CREATE_REMINDER_TABLE = "CREATE TABLE "+ REMINDER_TABLE_NAME +" ( " +
                REMINDER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                REMINDER_USER_ID+" INTEGER NOT NULL, "+
                REMINDER_TITTLE+" TEXT, "+
                REMINDER_DESCRIPTION+" TEXT, "+
                REMINDER_START_DATE+" INTEGER, "+
                REMINDER_END_DATE+" INTEGER, "+
                "FOREIGN KEY("+REMINDER_USER_ID+") REFERENCES "+ USER_TABLE_NAME +"("+ USER_ID_COLUMN_NAME +") )";
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
            db.insert(USER_TABLE_NAME,
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
        values.put(REMINDER_TITTLE, reminder.getTitle());
        values.put(REMINDER_DESCRIPTION,reminder.getDescription());
        values.put(REMINDER_START_DATE,reminder.getStartDateToMillis());
        values.put(REMINDER_END_DATE,reminder.getEndDateToMillis());

        //insert values to database
        if (db != null) {
            db.insert(REMINDER_TABLE_NAME,
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
            cursor = db.query(REMINDER_TABLE_NAME,
                    REMINDER_COLUMNS,REMINDER_USER_ID+"=? and "+
                    REMINDER_END_DATE+">?",
                    new String[] { String.valueOf(idUser), String.valueOf(System.currentTimeMillis()) },
                    null,
                    null,
                    REMINDER_START_DATE+" ASC",
                    null);
        }
//      return the cursor
        return cursor;
    }

    public List<Reminder> getNotStartedReminders(){
//        reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        //build query
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(REMINDER_TABLE_NAME,
                    REMINDER_COLUMNS,
                    REMINDER_START_DATE+">?",
                    new String[] {String.valueOf(System.currentTimeMillis())},
                    null,
                    null,
                    REMINDER_START_DATE+" ASC",
                    null);
        }

        List<Reminder> reminderList = new ArrayList<Reminder>();
        while(cursor.moveToNext()){
            reminderList.add(populateReminder(cursor));
        }
        if(!reminderList.isEmpty()){
            return reminderList;
        }
//      return the cursor
        return null;
    }

    private Reminder populateReminder(Cursor cursor){
        Reminder reminder = new Reminder();
        reminder.setId(cursor.getInt(cursor.getColumnIndex(REMINDER_ID)));
        reminder.setTitle(cursor.getString(cursor.getColumnIndex(REMINDER_TITTLE)));
        reminder.setDescription(cursor.getString(cursor.getColumnIndex(REMINDER_DESCRIPTION)));
        reminder.setStartDate(cursor.getLong(cursor.getColumnIndex(REMINDER_START_DATE)));
        reminder.setEndDate(cursor.getLong(cursor.getColumnIndex(REMINDER_END_DATE)));
        reminder.setUserID(cursor.getInt(cursor.getColumnIndex(REMINDER_USER_ID)));
        return reminder;
    }

    public Cursor getPassedReminders(int idUser){
//        reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        //build query
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(REMINDER_TABLE_NAME,
                    REMINDER_COLUMNS,REMINDER_USER_ID+"=? and "+
                    REMINDER_END_DATE+"<=?",
                    new String[] { String.valueOf(idUser), String.valueOf(System.currentTimeMillis()) },
                    null,
                    null,
                    REMINDER_START_DATE+" ASC",
                    null);
        }
        return cursor;
    }

    public Reminder getReminder(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //build query
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(REMINDER_TABLE_NAME,
                    REMINDER_COLUMNS,
                    " _id=?",
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
            reminder.setTitle(cursor.getString(2));
            reminder.setDescription(cursor.getString(3));
            reminder.setStartDate(cursor.getLong(4));
            reminder.setEndDate(cursor.getLong(5));
        }
        db.close();

        return reminder;
    }

    public User getUser(int id){
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

//        build Query
        Cursor cursor = null;
        if(db!=null){
            cursor = db.query(USER_TABLE_NAME,
                    USER_COLUMNS,
                    " _id=?",
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
            cursor = db.query(USER_TABLE_NAME,
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
            cursor = db.query(USER_TABLE_NAME,
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
            db.delete(USER_TABLE_NAME,
                    USER_ID_COLUMN_NAME +"=?",
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
            db.delete(REMINDER_TABLE_NAME,
                    REMINDER_ID+"=?",
                    new String[]{String.valueOf(reminder.getId())});
        }
//        close connection
        db.close();
    }
    public void deleteReminder(int id){
        SQLiteDatabase db = this.getWritableDatabase();

//        delete
        if (db != null) {
            db.delete(REMINDER_TABLE_NAME,
                    REMINDER_ID+"=?",
                    new String[]{String.valueOf(id)});
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
            i = db.update(USER_TABLE_NAME,
                    values,
                    USER_ID_COLUMN_NAME +"=?",
                    new String[]{String.valueOf(user.getId())});
        }

        db.close();

        return i;
    }

    public int updateReminder(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REMINDER_USER_ID,reminder.getUserID());
        values.put(REMINDER_TITTLE, reminder.getTitle());
        values.put(REMINDER_DESCRIPTION,reminder.getDescription());
        values.put(REMINDER_START_DATE,reminder.getStartDateToMillis());
        values.put(REMINDER_END_DATE,reminder.getEndDateToMillis());

        int i = 0;
        if (db != null) {
            i= db.update(REMINDER_TABLE_NAME,
                    values,
                    REMINDER_ID+"=?",
                    new String[]{String.valueOf(reminder.getId())});
        }

        db.close();

        return i;
    }
}
