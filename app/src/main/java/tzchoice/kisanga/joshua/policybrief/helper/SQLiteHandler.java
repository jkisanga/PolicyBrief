package tzchoice.kisanga.joshua.policybrief.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tzchoice.kisanga.joshua.policybrief.pojo.Audio;
import tzchoice.kisanga.joshua.policybrief.pojo.AudioCategory;
import tzchoice.kisanga.joshua.policybrief.pojo.Category;
import tzchoice.kisanga.joshua.policybrief.pojo.DataCategory;
import tzchoice.kisanga.joshua.policybrief.pojo.Document;
import tzchoice.kisanga.joshua.policybrief.pojo.Statistic;
import tzchoice.kisanga.joshua.policybrief.pojo.Video;
import tzchoice.kisanga.joshua.policybrief.pojo.VideoCategory;

/**
 * Created by user on 1/28/2017.
 */


public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Names
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    private static final String TABLE_DOC_CATEGORY = "document_category";
    private static final String TABLE_DOCUMENTS = "documents";
    private static final String TABLE_DATA_CATEGORY = "data_category";
    private static final String TABLE_STATISTICS = "statistics";
    private static final String TABLE_VIDEO_CATEGORY = "video_category";
    private static final String TABLE_AUDIO_CATEGORY = "audio_category";
    private static final String TABLE_AUDIO = "audio";
    private static final String TABLE_VIDEO = "video";


    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PIN = "pin";


    public static final String KEY_NAME = "name";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "desc";
    public static final String KEY_THUMBNAIL = "thumbnail";
    public static final String KEY_FILE_PATH = "file_path";

    public static final String KEY_CATEGORY_ID = "category_id";


    public static final String KEY_DATE = "created_at";





    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
//                + KEY_ID + " INTEGER PRIMARY KEY,"
//                +  KEY_PIN + " INTEGER, "
//                + KEY_EMAIL + " TEXT" + ")";
//        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_EMAIL + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_PIN + " INTEGER" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);


        String CREATE_DOC_CATEGORY_TABLE = "CREATE TABLE " + TABLE_DOC_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                +  KEY_NAME + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_DOC_CATEGORY_TABLE);

        String CREATE_DATA_CATEGORY_TABLE = "CREATE TABLE " + TABLE_DATA_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                +  KEY_NAME + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_DATA_CATEGORY_TABLE);

        String CREATE_AUDIO_CATEGORY_TABLE = "CREATE TABLE " + TABLE_AUDIO_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                +  KEY_NAME + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_AUDIO_CATEGORY_TABLE);

        String CREATE_VIDEO_CATEGORY_TABLE = "CREATE TABLE " + TABLE_VIDEO_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                +  KEY_NAME + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_VIDEO_CATEGORY_TABLE);

        String CREATE_DOCUMENT_TABLE = "CREATE TABLE " + TABLE_DOCUMENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY_ID + " INTEGER,"
                +  KEY_TITLE + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_FILE_PATH + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_DOCUMENT_TABLE);

        String CREATE_STATISTICS_TABLE = "CREATE TABLE " + TABLE_STATISTICS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY_ID + " INTEGER,"
                +  KEY_TITLE + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_FILE_PATH + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_STATISTICS_TABLE);

        String CREATE_AUDIO_TABLE = "CREATE TABLE " + TABLE_AUDIO + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY_ID + " INTEGER,"
                +  KEY_TITLE + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_FILE_PATH + " TEXT, "
                + KEY_DATE+ " TEXT, "
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_AUDIO_TABLE);


        String CREATE_VIDEO_TABLE = "CREATE TABLE " + TABLE_VIDEO + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY_ID + " INTEGER,"
                +  KEY_TITLE + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_FILE_PATH + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_VIDEO_TABLE);



    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOC_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIO_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);

        // Create tables again
        onCreate(db);
    }


    public void addUser(int user_id,  String name, String email, int pinCode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user_id);
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PIN, pinCode);


        // Inserting Row
        long id = db.insertWithOnConflict(TABLE_USER, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(KEY_ID, cursor.getString(0));
            user.put(KEY_NAME, cursor.getString(1));
            user.put(KEY_EMAIL, cursor.getString(2));
            user.put(KEY_PIN, cursor.getString(3));


        }
        cursor.close();
        db.close();
        // return user

        return user;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();
    }



    //ADD DOCUMENT CATEGORY
    public void addDocumentCategory(int id,  String name, String desc, String thumbnail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name);
        values.put(KEY_DESC, desc);
        values.put(KEY_THUMBNAIL, thumbnail);


        // Inserting Row
        long ID = db.insertWithOnConflict(TABLE_DOC_CATEGORY, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }

    //ADD DATA CATEGORY
    public void addDataCategory(int id,  String name, String desc, String thumbnail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name);
        values.put(KEY_DESC, desc);
        values.put(KEY_THUMBNAIL, thumbnail);


        // Inserting Row
        long ID = db.insertWithOnConflict(TABLE_DATA_CATEGORY, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }


    //ADD DATA CATEGORY
    public void addAudioCategory(int id,  String name, String desc, String thumbnail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name);
        values.put(KEY_DESC, desc);
        values.put(KEY_THUMBNAIL, thumbnail);


        // Inserting Row
        long ID = db.insertWithOnConflict(TABLE_AUDIO_CATEGORY, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }

    //ADD VIDEO CATEGORY
    public void addVideoCategory(int id,  String name, String desc, String thumbnail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name);
        values.put(KEY_DESC, desc);
        values.put(KEY_THUMBNAIL, thumbnail);


        // Inserting Row
        long ID = db.insertWithOnConflict(TABLE_VIDEO_CATEGORY, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }

    //ADD DOCUMENTS
    public void addDocument(int id, int category_id, String title, String desc, String thumbnail, String filePath, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_CATEGORY_ID, category_id);
        values.put(KEY_TITLE, title);
        values.put(KEY_DESC, desc);
        values.put(KEY_THUMBNAIL, thumbnail);
        values.put(KEY_FILE_PATH, filePath);
        values.put(KEY_DATE, date);


        // Inserting Row
        long ID = db.insertWithOnConflict(TABLE_DOCUMENTS, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }


    //ADD STATISTICS
    public void addStatistics(int id, int category_id, String title, String desc, String thumbnail, String filePath, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "addStatistics: " + thumbnail);
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_CATEGORY_ID, category_id);
        values.put(KEY_TITLE, title);
        values.put(KEY_DESC, desc);
        values.put(KEY_THUMBNAIL, thumbnail);
        values.put(KEY_FILE_PATH, filePath);
        values.put(KEY_DATE, date);


        // Inserting Row
        long ID = db.insertWithOnConflict(TABLE_STATISTICS, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }

    //ADD AUDIO
    public void addAudio(int id, int category_id, String title, String desc, String thumbnail, String filePath, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_CATEGORY_ID, category_id);
        values.put(KEY_TITLE, title);
        values.put(KEY_DESC, desc);
        values.put(KEY_THUMBNAIL, thumbnail);
        values.put(KEY_FILE_PATH, filePath);
        values.put(KEY_DATE, date);


        // Inserting Row
        long ID = db.insertWithOnConflict(TABLE_AUDIO, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }


    //ADD VIDEO
    public void addVideo(int id, int category_id, String title, String desc, String thumbnail, String filePath, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_CATEGORY_ID, category_id);
        values.put(KEY_TITLE, title);
        values.put(KEY_DESC, desc);
        values.put(KEY_THUMBNAIL, thumbnail);
        values.put(KEY_FILE_PATH, filePath);
        values.put(KEY_DATE, date);


        // Inserting Row
        long ID = db.insertWithOnConflict(TABLE_VIDEO, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }


    //List DocumentCategory
    public List<Category> categoryList(){
        Category ob;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DOC_CATEGORY;

        ArrayList<Category> resultList = new ArrayList<Category>();

        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext())
        {
            int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
            String name = c.getString(c.getColumnIndex(KEY_NAME));
            String desc = c.getString(c.getColumnIndex(KEY_DESC));
            String thumbnail = c.getString(c.getColumnIndex(KEY_THUMBNAIL));

            try
            {
                ob =  new Category();
                ob.setId(id);
                ob.setName(name);
                ob.setDesc(desc);
                ob.setThumbnail(thumbnail);
                resultList.add(ob);
            }
            catch (Exception e) {

            }

        }
        c.close();

        db.close();
        Log.d(TAG, "categoryList: " + resultList.size());
        return resultList;
    }

    //List Data Category
    public List<DataCategory> dataCategoryList(){
        DataCategory ob;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DATA_CATEGORY;

        ArrayList<DataCategory> resultList = new ArrayList<DataCategory>();

        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext())
        {
            int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
            String name = c.getString(c.getColumnIndex(KEY_NAME));
            String desc = c.getString(c.getColumnIndex(KEY_DESC));
            String thumbnail = c.getString(c.getColumnIndex(KEY_THUMBNAIL));

            try
            {
                ob =  new DataCategory();
                ob.setId(id);
                ob.setName(name);
                ob.setDesc(desc);
                ob.setThumbnail(thumbnail);
                resultList.add(ob);
            }
            catch (Exception e) {

            }

        }
        c.close();

        db.close();
        return resultList;
    }

    //List Data Category
    public List<AudioCategory> audioCategoryList(){
        AudioCategory ob;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_AUDIO_CATEGORY;

        ArrayList<AudioCategory> resultList = new ArrayList<AudioCategory>();

        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext())
        {
            int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
            String name = c.getString(c.getColumnIndex(KEY_NAME));
            String desc = c.getString(c.getColumnIndex(KEY_DESC));
            String thumbnail = c.getString(c.getColumnIndex(KEY_THUMBNAIL));

            try
            {
                ob =  new AudioCategory();
                ob.setId(id);
                ob.setTitle(name);
                ob.setDesc(desc);
                ob.setThumbnail(thumbnail);
                resultList.add(ob);
            }
            catch (Exception e) {

            }

        }
        c.close();

        db.close();
        return resultList;
    }

    //List Data Category
    public List<VideoCategory> videoCategoryList(){
        VideoCategory ob;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_CATEGORY;

        ArrayList<VideoCategory> resultList = new ArrayList<VideoCategory>();

        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext())
        {
            int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
            String name = c.getString(c.getColumnIndex(KEY_NAME));
            String desc = c.getString(c.getColumnIndex(KEY_DESC));
            String thumbnail = c.getString(c.getColumnIndex(KEY_THUMBNAIL));

            try
            {
                ob =  new VideoCategory();
                ob.setId(id);
                ob.setTitle(name);
                ob.setDesc(desc);
                ob.setThumbnail(thumbnail);
                resultList.add(ob);
            }
            catch (Exception e) {

            }

        }
        c.close();

        db.close();
        return resultList;
    }

    //List Data Category
    public List<Document> documentList(int category){
        Document ob;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DOCUMENTS + " WHERE " + KEY_CATEGORY_ID + " = " + category;

        ArrayList<Document> resultList = new ArrayList<Document>();

        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext())
        {
            int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
            int category_id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_CATEGORY_ID)));
            String title = c.getString(c.getColumnIndex(KEY_TITLE));
            String desc = c.getString(c.getColumnIndex(KEY_DESC));
            String filePath = c.getString(c.getColumnIndex(KEY_FILE_PATH));
            String date = c.getString(c.getColumnIndex(KEY_DATE));
            String thumbnail = c.getString(c.getColumnIndex(KEY_THUMBNAIL));

            try
            {
                ob =  new Document();
                ob.setId(id);
                ob.setCategoryId(category_id);
                ob.setTitle(title);
                ob.setSummary(desc);
                ob.setFilePath(filePath);
                ob.setCreatedAt(date);
                ob.setThumbnail(thumbnail);
                resultList.add(ob);
            }
            catch (Exception e) {

            }

        }
        c.close();

        db.close();
        return resultList;
    }



    //List Data
    public List<Statistic> statisticsList(int category){
        Statistic ob;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_STATISTICS + " WHERE " + KEY_CATEGORY_ID + " = " + category;

        ArrayList<Statistic> resultList = new ArrayList<Statistic>();

        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext())
        {
            int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
            int category_id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_CATEGORY_ID)));
            String title = c.getString(c.getColumnIndex(KEY_TITLE));
            String desc = c.getString(c.getColumnIndex(KEY_DESC));
            String filePath = c.getString(c.getColumnIndex(KEY_FILE_PATH));
            String date = c.getString(c.getColumnIndex(KEY_DATE));
            String thumbnail = c.getString(c.getColumnIndex(KEY_THUMBNAIL));

            try
            {
                ob =  new Statistic();
                ob.setId(id);
                ob.setDataCategoryId(category_id);
                ob.setTitle(title);
                ob.setSummary(desc);
                ob.setFilePath(filePath);
                ob.setCreatedAt(date);
                ob.setThumbnail(thumbnail);
                resultList.add(ob);
            }
            catch (Exception e) {

            }

        }
        c.close();

        db.close();
        Log.d(TAG, "statisticsList: " + resultList.size());
        return resultList;

    }


    //List Audio
    public List<Audio> audioList(int category){
        Audio ob;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_AUDIO + " WHERE " + KEY_CATEGORY_ID + " = " + category;

        ArrayList<Audio> resultList = new ArrayList<Audio>();

        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext())
        {
            int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
            int category_id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_CATEGORY_ID)));
            String title = c.getString(c.getColumnIndex(KEY_TITLE));
            String desc = c.getString(c.getColumnIndex(KEY_DESC));
            String filePath = c.getString(c.getColumnIndex(KEY_FILE_PATH));
            String date = c.getString(c.getColumnIndex(KEY_DATE));
            String thumbnail = c.getString(c.getColumnIndex(KEY_THUMBNAIL));

            try
            {
                ob =  new Audio();
                ob.setId(id);
                ob.setAudioCategoryId(category_id);
                ob.setTitle(title);
                ob.setDesc(desc);
                ob.setFilePath(filePath);
                ob.setCreatedAt(date);
                ob.setThumbnail(thumbnail);
                resultList.add(ob);
            }
            catch (Exception e) {

            }

        }
        c.close();

        db.close();
        return resultList;
    }

    //List Audio
    public List<Video> videoList(int category){
        Video ob;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_VIDEO + " WHERE " + KEY_CATEGORY_ID + " = " + category;

        ArrayList<Video> resultList = new ArrayList<Video>();

        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext())
        {
            int id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_ID)));
            int category_id = Integer.parseInt(c.getString(c.getColumnIndex(KEY_CATEGORY_ID)));
            String title = c.getString(c.getColumnIndex(KEY_TITLE));
            String desc = c.getString(c.getColumnIndex(KEY_DESC));
            String filePath = c.getString(c.getColumnIndex(KEY_FILE_PATH));
            String date = c.getString(c.getColumnIndex(KEY_DATE));
            String thumbnail = c.getString(c.getColumnIndex(KEY_THUMBNAIL));

            try
            {
                ob =  new Video();
                ob.setId(id);
                ob.setVideoCategoryId(category_id);
                ob.setTitle(title);
                ob.setDesc(desc);
                ob.setFilePath(filePath);
                ob.setCreatedAt(date);
                ob.setThumbnail(thumbnail);
                resultList.add(ob);
            }
            catch (Exception e) {

            }

        }
        c.close();

        db.close();
        return resultList;
    }



    public void deleteDocumentCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_DOC_CATEGORY, null, null);
        db.close();
    }

    public void deleteDocument() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_DOCUMENTS, null, null);
        db.close();
    }

    public void deleteDataCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_DATA_CATEGORY, null, null);
        db.close();
    }

    public void deleteStatistics() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_STATISTICS, null, null);
        db.close();
    }
    public void deleteAudioCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_AUDIO_CATEGORY, null, null);
        db.close();
    }

    public void deleteAudio() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_AUDIO, null, null);
        db.close();
    }
    public void deleteVideoCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_VIDEO_CATEGORY, null, null);
        db.close();
    }

    public void deleteVideo() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_VIDEO, null, null);
        db.close();
    }



}
