package ch.supsi.dti.isin.meteoapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "test.db";

    // nome del database
    private static final int VERSION = 1;

    // versione
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MeteoDbSchema.TestTable.NAME + "("
                + " _id integer primary key autoincrement, "
                + MeteoDbSchema.TestTable.Cols.UUID
                + ","
                + MeteoDbSchema.TestTable.Cols.NAME
                + ","
                + MeteoDbSchema.TestTable.Cols.LATITUDE
                + ","
                + MeteoDbSchema.TestTable.Cols.LONGITUDE
                + ")"
        );

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
