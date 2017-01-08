package dm.pivofinder.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import dm.pivofinder.models.Beer;

import static android.R.id.list;

public class DBManager {

    private SQLiteDatabase database;
    private DBDesigner dbHelper;

    public DBManager(Context context) {
        dbHelper = new DBDesigner(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public void insert(Beer beer) {
        ContentValues values = new ContentValues();
        values.put(DBDesigner.COLUMN_NAME, beer.name);
        values.put(DBDesigner.COLUMN_BAR, beer.bar);
        values.put(DBDesigner.COLUMN_PRICE, beer.price);
        values.put(DBDesigner.COLUMN_LAT, beer.lat);
        values.put(DBDesigner.COLUMN_LNG, beer.lng);
        values.put(DBDesigner.COLUMN_ADDRESS, beer.address);


        long insertId = database.insert(DBDesigner.TABLE_Beer, null,
                values);
    }

    public void delete(int id) {

        Log.v("DB", "Beer deleted with id: " + id);
        database.delete(DBDesigner.TABLE_Beer,
                DBDesigner.COLUMN_ID + " = " + id, null);
    }

    public void update(Beer beer) {
        // TODO Auto-generated method stub

        ContentValues values = new ContentValues();
        values.put(DBDesigner.COLUMN_NAME, beer.name);
        values.put(DBDesigner.COLUMN_BAR, beer.bar);
        values.put(DBDesigner.COLUMN_PRICE, beer.price);
        values.put(DBDesigner.COLUMN_ADDRESS, beer.address);
        values.put(DBDesigner.COLUMN_LAT, beer.lat);
        values.put(DBDesigner.COLUMN_LNG, beer.lng);

        long insertId = database.update(DBDesigner.TABLE_Beer,values, DBDesigner.COLUMN_ID + " = " + beer.beerId, null);

    }

    public List<Beer> getAll() {
        List<Beer> Beers = new ArrayList<Beer>();
        Cursor cursor = database.rawQuery("SELECT * FROM "
                + DBDesigner.TABLE_Beer, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Beer pojo = toBeer(cursor);
            Beers.add(pojo);
            cursor.moveToNext();
        }
        cursor.close();
        return Beers;
    }

    public Beer get(int id) {
        Beer pojo = null;

        Cursor cursor = database.rawQuery("SELECT * FROM "
                + DBDesigner.TABLE_Beer + " WHERE "
                + DBDesigner.COLUMN_ID + " = " + id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Beer temp = toBeer(cursor);
            pojo = temp;
            cursor.moveToNext();
        }
        cursor.close();
        return pojo;
    }



    private Beer toBeer(Cursor cursor) {
        Beer pojo = new Beer();
        pojo.beerId = cursor.getInt(0);
        pojo.name = cursor.getString(1);
        pojo.bar = cursor.getString(2);
        pojo.price = cursor.getDouble(3);
        pojo.address = cursor.getString(4);
        pojo.lat = cursor.getDouble(5);
        pojo.lng = cursor.getDouble(6);

        return pojo;
    }






}
