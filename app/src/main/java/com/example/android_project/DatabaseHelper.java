package com.example.android_project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DB";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_LIKE_TABLE = "CREATE TABLE Like_List (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT, " +
            "type TEXT, " +
            "period TEXT, " +
            "eventPeriod TEXT, " +
            "eventSite TEXT, " +
            "charge TEXT, " +
            "contactPoint TEXT, " +
            "url TEXT, " +
            "imageObject TEXT, " +
            "description TEXT, " +
            "viewCount TEXT" +
            ");";
    private static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE Schedule (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT, " +
            "type TEXT, " +
            "period TEXT, " +
            "eventPeriod TEXT, " +
            "eventSite TEXT, " +
            "charge TEXT, " +
            "contactPoint TEXT, " +
            "url TEXT, " +
            "imageObject TEXT, " +
            "description TEXT, " +
            "viewCount TEXT" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "onCreate() is called");
        db.execSQL(CREATE_LIKE_TABLE);
        db.execSQL(CREATE_SCHEDULE_TABLE);
        for (int i = 1; i <= 90; i++) {
            if (i%2==0){
                db.execSQL("INSERT INTO Like_List VALUES (null, 'title" + i + "', 'type', '20231219 ~ 20231219', 'eventPeriod', 'eventSite', 'charge', 'contactPoint', 'url', 'https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1699417551991.jpg', 'description', 'viewCount');");

            }else{
                db.execSQL("INSERT INTO Like_List VALUES (null, 'title" + i + "', 'type', '20231219 ~ 20231219', 'eventPeriod', 'eventSite', 'charge', 'contactPoint', 'url', 'https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1702514134298.jpg', 'description', 'viewCount');");

            }
            }
        db = this.getWritableDatabase();
        // Schedule 테이블에 임시 데이터 삽입
        for (int i = 1; i <= 90; i++) {
            if (i%2==0){
                db.execSQL("INSERT INTO Schedule VALUES (null, 'title" + i + "', 'type', '20231219 ~ 20231219', 'eventPeriod', 'eventSite', 'charge', 'contactPoint', 'url', 'https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1699417551991.jpg', 'description', 'viewCount');");

            }else{
                db.execSQL("INSERT INTO Schedule VALUES (null, 'title" + i + "', 'type', '20231219 ~ 20231219', 'eventPeriod', 'eventSite', 'charge', 'contactPoint', 'url', 'https://www.mcst.go.kr/attachFiles/cultureInfoCourt/monthServ/1702514134298.jpg', 'description', 'viewCount');");

            }}

        Cursor cursor = db.rawQuery("SELECT * FROM Like_List", null);
        if (cursor.getCount() == 0) {
            Log.d("DatabaseHelper", "Like_List table is empty");
        } else {
            Log.d("DatabaseHelper", "Like_List table is not empty");
        }
        cursor.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Like_List");
        db.execSQL("DROP TABLE IF EXISTS " + "Schedule");
        onCreate(db);
    }

    public void insertShow(Show show, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // title과 period가 같은 데이터가 있는지 확인
        String selection = "title = ? AND period = ?";
        String[] selectionArgs = { show.title, show.period };
        Cursor cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);

        // title과 period가 같은 데이터가 있다면 삭제
        if (cursor != null && cursor.getCount() > 0) {
            db.delete(tableName, selection, selectionArgs);
        }

        // Show 객체 삽입
        ContentValues values = new ContentValues();
        values.put("title", show.title);
        values.put("type", show.type);
        values.put("period", show.period);
        values.put("eventPeriod", show.period);
        values.put("eventSite", show.period);
        values.put("charge", show.period);
        values.put("contactPoint", show.period);
        values.put("url", show.period);
        values.put("imageObject", show.period);
        values.put("description", show.period);
        values.put("viewCount", show.period);

        db.insert(tableName, null, values);

    }

    public List<Show> getAllLikeListShows() {
        List<Show> shows = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Like_List", null);

        if (cursor.moveToFirst()) {
            do {
                shows.add(cursorToShow(cursor));

            } while (cursor.moveToNext());
        }

        return shows;
    }

    public List<Show> getShowsByDate(String date) {
        List<Show> shows = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Schedule", null, null,null, null, null, null);

        Date selectedDate = parseDate(date);



        while (cursor.moveToNext()) {
            @SuppressLint("Range")
            List<Date> period = parsePeriod(cursor.getString(cursor.getColumnIndex("period")));

            if (isWithinPeriod(selectedDate, period)) {
                shows.add(cursorToShow(cursor));

            }
        }

        return shows;
    }

    public Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Date> parsePeriod(String periodStr) {
        String[] dates = periodStr.split(" ~ ");
        List<Date> period = new ArrayList<>();
        period.add(parseDate(dates[0]));
        period.add(parseDate(dates[1]));
        return period;
    }

    public boolean isWithinPeriod(Date date, List<Date> period) {
        return !date.before(period.get(0)) && !date.after(period.get(1));
    }

    @SuppressLint("Range")
    public Show cursorToShow(Cursor cursor) {
        Show show = new Show();
        show.title = cursor.getString(cursor.getColumnIndex("title"));
        show.type = cursor.getString(cursor.getColumnIndex("type"));
        show.period = cursor.getString(cursor.getColumnIndex("period"));
        show.eventPeriod = cursor.getString(cursor.getColumnIndex("eventPeriod"));
        show.eventSite = cursor.getString(cursor.getColumnIndex("eventSite"));
        show.charge = cursor.getString(cursor.getColumnIndex("charge"));
        show.contactPoint = cursor.getString(cursor.getColumnIndex("contactPoint"));
        show.url = cursor.getString(cursor.getColumnIndex("url"));
        show.imageObject = cursor.getString(cursor.getColumnIndex("imageObject"));
        show.description = cursor.getString(cursor.getColumnIndex("description"));
        show.viewCount = cursor.getString(cursor.getColumnIndex("viewCount"));
        return show;
    }



}