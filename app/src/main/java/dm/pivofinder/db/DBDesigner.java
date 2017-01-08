package dm.pivofinder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBDesigner extends SQLiteOpenHelper {
    public static final String TABLE_Beer = "table_Beer";
    public static final String COLUMN_ID = "Beerid";
    public static final String COLUMN_NAME = "Beername";
    public static final String COLUMN_BAR = "Bar";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_ADDRESS = "Address";




    private static final String DATABASE_NAME = "PivoFinder.db";
    private static final int DATABASE_VERSION = 200223;


    private static final String DATABASE_CREATE_TABLE_Beer = "create table "
            + TABLE_Beer +      "( "
            + COLUMN_ID +       " integer primary key autoincrement, "
            + COLUMN_NAME +     " text not null,"
            + COLUMN_BAR +      " text not null,"
            + COLUMN_PRICE +    " double not null,"
            + COLUMN_ADDRESS +     " text,"
            + COLUMN_LAT +      " double,"
            + COLUMN_LNG +      " double);";

    public DBDesigner(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_TABLE_Beer);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBDesigner.class.getName(),
                "Upgrading Database " + oldVersion + " to "
                        + newVersion + ", deleting old version");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Beer);
        onCreate(db);
    }

}