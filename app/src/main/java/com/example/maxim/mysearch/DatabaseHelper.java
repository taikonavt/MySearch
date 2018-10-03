package com.example.maxim.mysearch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonReader;
import android.util.Log;

import com.example.maxim.mysearch.Contract.SearchEntry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.maxim.mysearch.Contract.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cities.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE =
                "CREATE VIRTUAL TABLE " +
                        SearchEntry.TABLE_NAME + " USING fts4(" +
                        SearchEntry.COLUMN_CITY_ID + ", " +
                        SearchEntry.COLUMN_NAME + ", " +
                        SearchEntry.COLUMN_COUNTRY + ", " +
                        SearchEntry.COLUMN_LON + ", " +
                        SearchEntry.COLUMN_LAT + ");";

        db.execSQL(SQL_CREATE_LOCATION_TABLE);

//        try {
//            populateDB(db);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.SearchEntry.TABLE_NAME);
        onCreate(db);
    }

    private void populateDB(SQLiteDatabase db) throws IOException {
        InputStream in = context.getResources().openRawResource(R.raw.city);
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        JsonReader reader = new JsonReader(isr);
        reader.beginArray();
        City city;
        while (reader.hasNext()){
            city = readCity(reader);
            insertToDb(db, city);
        }
        reader.endArray();
    }

    ContentValues cv = new ContentValues();
    private void insertToDb(SQLiteDatabase db, City city) {

        cv.put(Contract.SearchEntry.COLUMN_CITY_ID, city.id);
        cv.put(Contract.SearchEntry.COLUMN_NAME, city.name);
        cv.put(Contract.SearchEntry.COLUMN_COUNTRY, city.country);
        cv.put(Contract.SearchEntry.COLUMN_LON, city.coord.lon);
        cv.put(Contract.SearchEntry.COLUMN_LAT, city.coord.lat);

        long l = db.insert(Contract.SearchEntry.TABLE_NAME, null, cv);
        Log.d(TAG, city.name + " " + l);

        cv.clear();
    }

    private City readCity(JsonReader reader) throws IOException {
        int id = -1;
        String name = null;
        String country = null;
        Coord coord = null;

        reader.beginObject();
        while (reader.hasNext()){
            String objName = reader.nextName();

            if (objName.equals("id")){
                id = reader.nextInt();
            } else if (objName.equals("name")){
                name = reader.nextString();
            } else if (objName.equals("country")){
                country = reader.nextString();
            } else if (objName.equals("coord")){
                coord = readCoord(reader);
            } else
                reader.skipValue();
        }
        reader.endObject();
        return new City(id, name, country, coord);
    }

    private Coord readCoord(JsonReader reader) throws IOException {
        double lon = -1;
        double lat = -1;

        reader.beginObject();
        while (reader.hasNext()){
            String objName = reader.nextName();
            if (objName.equals("lon")){
                lon = reader.nextDouble();
            } else if (objName.equals("lat")){
                lat = reader.nextDouble();
            } else
                reader.skipValue();
        }
        reader.endObject();
        return new Coord(lon, lat);
    }

    class City{
        int id;
        String name;
        String country;
        Coord coord;

        City(int id, String name, String country, Coord coord){
            this.id = id;
            this.name = name;
            this.country = country;
            this.coord = coord;
        }
    }

    class Coord{
        double lon;
        double lat;
        Coord(double lon, double lat){
            this.lon = lon;
            this.lat = lat;
        }
    }
}