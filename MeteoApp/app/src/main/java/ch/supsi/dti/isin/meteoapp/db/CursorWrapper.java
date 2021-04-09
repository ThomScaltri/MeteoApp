package ch.supsi.dti.isin.meteoapp.db;

import android.database.Cursor;

import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.model.Location;

public class CursorWrapper extends android.database.CursorWrapper {

    public CursorWrapper(Cursor cursor) {

        super(cursor);
    }

    public Location getLocation() {
        String id = getString(getColumnIndex(MeteoDbSchema.TestTable.Cols.UUID));
        String name = getString(getColumnIndex(MeteoDbSchema.TestTable.Cols.NAME));
        Double latitude = getDouble(getColumnIndex(MeteoDbSchema.TestTable.Cols.LATITUDE));
        Double longitude =getDouble(getColumnIndex(MeteoDbSchema.TestTable.Cols.LONGITUDE));

        return new Location(UUID.fromString(id), name,latitude,longitude);
    }
}
