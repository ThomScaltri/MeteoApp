package ch.supsi.dti.isin.meteoapp.db;

import android.database.Cursor;

import ch.supsi.dti.isin.meteoapp.model.Location;

public class CursorWrapper extends android.database.CursorWrapper {

    public CursorWrapper(Cursor cursor) {

        super(cursor);
    }

    //Da Wrappare la location
    /*public Location getEntry() {
        String id = getString(getColumnIndex(MeteoDbSchema.TestTable.Cols.UUID));
        String name = getString(getColumnIndex(MeteoDbSchema.TestTable.Cols.NAME));
        return new Location(UUID.fromString(id), name);
    }*/
}
