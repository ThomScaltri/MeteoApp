package ch.supsi.dti.isin.meteoapp.db;

import android.database.Cursor;

public class CursorWrapper extends android.database.CursorWrapper {

    public CursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /*public TestEntry getEntry() {
        String id = getString(getColumnIndex(MeteoDbSchema.TestTable.Cols.UUID));
        String name = getString(getColumnIndex(MeteoDbSchema.TestTable.Cols.NAME));
        return new TestEntry(UUID.fromString(id), name);
    }*/
}
