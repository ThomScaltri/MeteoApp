package ch.supsi.dti.isin.meteoapp.db;

import ch.supsi.dti.isin.meteoapp.model.Location;
import android.content.ContentValues;

public class DBContentValues {

        public static ContentValues getContentValues(Location entry) {
            ContentValues values = new ContentValues();
            values.put(MeteoDbSchema.TestTable.Cols.UUID, entry.getId().toString());
            values.put(MeteoDbSchema.TestTable.Cols.NAME, entry.getName());
            values.put(MeteoDbSchema.TestTable.Cols.LATITUDE,entry.getLati());
            values.put(MeteoDbSchema.TestTable.Cols.LONGITUDE,entry.getLongi());
            return values;
        }
}
