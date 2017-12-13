package com.wordpress.zeel.notepad;

/**
 * Created by zeel on 02-06-2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zeel on 02-06-2016.
 */
public class NotesDb {

    public static final String COLUMN_TITLE="title";
    public static final String COLUMN_BODY="body";
    public static final String COLUMN_TIME="time";
    public static final String COLUMN_IMAGE="image";
    public static final String COLUMN_BOOLEAN = "boolean";
    public static final String COLUMN_PASSWORD="password";
    public static final String COLUMN_BOOLEAN_IMAGE="isimage";

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String TAG=NotesDb.class.getSimpleName();

    private static final String DATABASE_NAME="notes6.db";
    private static final int DATABASE_VERSION=2;
    private static final String TABLE_NAME="notes";

    private final Context mContext;

    private static class DbHelper extends SQLiteOpenHelper{

        public DbHelper(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+ TABLE_NAME+" ("+ COLUMN_TITLE +" TEXT NOT NULL, "+ COLUMN_BODY +" TEXT NOT NULL, "+ COLUMN_IMAGE +" TEXT, "+ COLUMN_BOOLEAN +" BOOL, "+
                    COLUMN_PASSWORD +" INTEGER PRIMARY KEY, "+ COLUMN_BOOLEAN_IMAGE +" BOOL, "+ COLUMN_TIME + " TEXT NOT NULL);");
           // Log.d(TAG, "onCreate() database");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
            Log.d(TAG,"onUpgrade() database");
        }
    }

    public NotesDb(Context context){
        this.mContext=context;
    }

    public NotesDb open() throws SQLException{
        mDbHelper=new DbHelper(mContext);
        mDb=mDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDbHelper.close();
    }

    public ArrayList<CardItem> getAllData(){
        ArrayList<CardItem> cardItems = new ArrayList<>();
        String selectQuery= "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = mDb.rawQuery(selectQuery,null);
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            CardItem cardItem = new CardItem();
            cardItem.setTitle1(cursor.getString(0));
            cardItem.setSubtitle(cursor.getString(1));
            cardItem.setImage(cursor.getString(2));
            cardItem.setBool(cursor.getInt(3));
            cardItem.setPassword(cursor.getInt(4));
            cardItem.setImagebool(cursor.getInt(5));
            cardItem.setTime1(cursor.getString(6));
            cardItems.add(cardItem);
        }

        cursor.close();
        return cardItems;
    }

    public ArrayList<String> getData(){
        String[] columns = new String[]{COLUMN_TITLE};
        Cursor c= mDb.query(TABLE_NAME,columns,null,null,null,null,null);
        ArrayList<String> result=new ArrayList<>();
        int iCol = c.getColumnIndex(COLUMN_TITLE);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            result.add(c.getString(iCol));
        }

        return result;
    }

    public String getBody(String title){
        String[] col =new String[]{COLUMN_BODY};
        Cursor c= mDb.query(TABLE_NAME,col,null,null,null,null,null,null);
        String[] columns = new String[]{COLUMN_TITLE};
        Cursor cursor= mDb.query(TABLE_NAME,columns,null,null,null,null,null);
        int iCol=cursor.getColumnIndex(COLUMN_TITLE);
        int Col=c.getColumnIndex(COLUMN_BODY);

        for(cursor.moveToFirst(),c.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            if(cursor.getString(iCol).equals(title)){
                return c.getString(Col);
            }
            c.moveToNext();

        }
        return null;
    }

    public long createEntry(String body,String title,String time){
        Random random = new Random();
        int pass = random.nextInt(10000)+9999;
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_BODY, body);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_TIME,time);
        cv.put(COLUMN_BOOLEAN_IMAGE,false);
        cv.put(COLUMN_BOOLEAN,false);
        cv.put(COLUMN_PASSWORD,pass);
        return mDb.insert(TABLE_NAME,null,cv);
    }

    public long createEntry(String body,String title,String time,String imagePath){
        Random random = new Random();
        int pass = random.nextInt(10000)+9999;
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_BODY, body);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_TIME,time);
        cv.put(COLUMN_IMAGE,imagePath);
        cv.put(COLUMN_BOOLEAN,false);
        cv.put(COLUMN_BOOLEAN_IMAGE,true);
        cv.put(COLUMN_PASSWORD,pass);
        return mDb.insert(TABLE_NAME,null,cv);
    }

    public void deleteEntry(String del){
       // mDb.delete(TABLE_NAME,null,null);
        mDb.delete(TABLE_NAME, COLUMN_TITLE + "=?", new String[]{del});
    }

    public void deleteAll(){
        mDb.delete(TABLE_NAME, null, null);
    }

    public void upgradeEntry(String title,String newBody,String time,String image,Boolean lock,int password,Boolean imgeShow){
        ContentValues cvUpdate=new ContentValues();
        cvUpdate.put(COLUMN_BODY,newBody);
        cvUpdate.put(COLUMN_TITLE,title);
        cvUpdate.put(COLUMN_TIME,time);
        cvUpdate.put(COLUMN_IMAGE,image);
        cvUpdate.put(COLUMN_BOOLEAN,lock);
        cvUpdate.put(COLUMN_PASSWORD,password);
        cvUpdate.put(COLUMN_BOOLEAN_IMAGE,imgeShow);
        mDb.update(TABLE_NAME, cvUpdate, COLUMN_TITLE + "=?", new String[]{title});
    }

    public void updateTime(String title,String time){
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(COLUMN_TIME,time);
        cvUpdate.put(COLUMN_TITLE,title);
        mDb.update(TABLE_NAME,cvUpdate,COLUMN_TITLE + "=?",new String []{title});
    }

    public int getBoolean(String title){
        String[] col =new String[]{COLUMN_BOOLEAN};
        Cursor c= mDb.query(TABLE_NAME,col,null,null,null,null,null,null);
        String[] columns = new String[]{COLUMN_TITLE};
        Cursor cursor= mDb.query(TABLE_NAME,columns,null,null,null,null,null);
        int iCol=cursor.getColumnIndex(COLUMN_TITLE);
        int Col=c.getColumnIndex(COLUMN_BOOLEAN);

        for(cursor.moveToFirst(),c.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            if(cursor.getString(iCol).equals(title)){
                return c.getInt(Col);
            }
            c.moveToNext();

        }
        return 0;
    }

    public int  getPassword(String title){
        String[] col =new String[]{COLUMN_PASSWORD};
        Cursor c= mDb.query(TABLE_NAME,col,null,null,null,null,null,null);
        String[] columns = new String[]{COLUMN_TITLE};
        Cursor cursor= mDb.query(TABLE_NAME,columns,null,null,null,null,null);
        int iCol=cursor.getColumnIndex(COLUMN_TITLE);
        int Col=c.getColumnIndex(COLUMN_PASSWORD);

        for(cursor.moveToFirst(),c.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            if(cursor.getString(iCol).equals(title)){
                return c.getInt(Col);
            }
            c.moveToNext();

        }
        return 0;
    }


    public ArrayList<String> get_body(){
        String[] columns = new String[]{COLUMN_BODY};
        Cursor c= mDb.query(TABLE_NAME,columns,null,null,null,null,null);
        ArrayList<String> result=new ArrayList<>();
        int iCol = c.getColumnIndex(COLUMN_BODY);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            result.add(c.getString(iCol));
        }
        c.close();
        return result;
    }


    public ArrayList<String> getTime(){
        String[] columns = new String[]{COLUMN_TIME};
        Cursor c= mDb.query(TABLE_NAME,columns,null,null,null,null,null);
        ArrayList<String> result=new ArrayList<>();
        int iCol = c.getColumnIndex(COLUMN_TIME);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            result.add(c.getString(iCol));
        }
        c.close();
        return result;
    }


}
