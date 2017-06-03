package com.yankon.smart.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Evan on 14/11/27.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    public static final String DB_NAME = "yankon.sqlite";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion){
            case 0:
                db.execSQL("alter table lights add column media_state BOOL");
                db.execSQL("alter table lights add column UID TEXT");
                break;

            case 1:
                db.execSQL("alter table lights add column AP_state BOOL");
                db.execSQL("alter table lights add column AP_BSSID TEXT");
                db.execSQL("alter table lights add column AP_SSID TEXT");
                db.execSQL("alter table lights add column AP_Pass TEXT");
                break;
        }
    }

    protected void createDB(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS lights ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "MAC TEXT NOT NULL,"
                        + "ver INTEGER,"
                        + "ThingID TEXT,"
                        + "remote_pwd TEXT,"
                        + "admin_pwd TEXT,"
                        + "name TEXT,"
                        + "color INTEGER,"
                        + "model TEXT,"
                        + "firmware_version TEXT, "
                        + "brightness INTEGER,"
                        + "CT INTEGER,"
                        + "type INTEGER,"
                        + "number INTEGER,"
                        + "sens INTEGER,"
                        + "lux INTEGER,"
                        + "runmodel BOOL DEFAULT 0,"
                        + "mode INTEGER,"
                        + "IP INTEGER DEFAULT 0,"
                        + "subIP INTEGER DEFAULT 0,"
                        + "is_mine BOOL,"
                        + "state BOOL DEFAULT 0,"
                        + "connected BOOL,"
                        + "remote TEXT,"
                        + "synced BOOL,"
                        + "owned_time INTEGER,"
                        + "last_active INTEGER,"
                        + "deleted INTEGER DEFAULT 0,"
                        + "media_state BOOL DEFAULT 0,"
                        + "UID TEXT DEFAULT NULL,"
                        + "AP_state BOOL DEFAULT 0,"
                        + "AP_BSSID TEXT,"
                        + "AP_SSID TEXT,"
                        + "AP_Pass TEXT"
                        + ");"
        );
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS lights_mac ON lights (MAC);");
        db.execSQL("CREATE TABLE IF NOT EXISTS models ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "model TEXT,"
                        + "pic TEXT,"
                        + "des TEXT, "
                        + "firmware_version TEXT, "
                        + "UNIQUE(model)"
                        + ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS colors ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "objectID TEXT,"
                        + "ver INTEGER,"
                        + "name TEXT,"
                        + "value INTEGER,"
                        + "synced BOOL,"
                        + "created_time INTEGER,"
                        + "deleted INTEGER DEFAULT 0"
                        + ");"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS light_groups ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "objectID TEXT,"
                        + "ver INTEGER,"
                        + "name TEXT,"
                        + "state BOOL DEFAULT 0,"
                        + "color INTEGER,"
                        + "brightness INTEGER,"
                        + "CT INTEGER,"
                        + "mode INTEGER,"
                        + "synced BOOL,"
                        + "created_time INTEGER,"
                        + "deleted INTEGER DEFAULT 0"
                        + ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS light_group_rel ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "light_id INTEGER,"
                        + "group_id INTEGER,"
                        + "created_time INTEGER,"
                        + "UNIQUE(light_id, group_id)"
                        + ");"
        );

        db.execSQL(
                "CREATE VIEW IF NOT EXISTS group_light_view AS SELECT * FROM light_group_rel,lights "
                        + " WHERE light_group_rel.light_id=lights._id;");

        db.execSQL("CREATE VIEW IF NOT EXISTS light_groups_view AS SELECT "
                + "light_groups.*,(select count(_id) FROM light_group_rel where light_group_rel.group_id=light_groups._id) as num,"
                + "(select sum(state) FROM group_light_view where group_light_view.group_id=light_groups._id) as on_num "
                + " FROM light_groups;");

        db.execSQL("CREATE TABLE IF NOT EXISTS scenes ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "objectID TEXT,"
                        + "ver INTEGER,"
                        + "name TEXT,"
                        + "synced BOOL,"
                        + "created_time INTEGER,"
                        + "last_used_time INTEGER,"
                        + "deleted INTEGER DEFAULT 0"
                        + ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS scenes_detail ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "scene_id INTEGER,"
                        + "light_id INTEGER DEFAULT -1,"
                        + "group_id INTEGER DEFAULT -1,"
                        + "objectID TEXT,"
                        + "state BOOL DEFAULT 0,"
                        + "color INTEGER,"
                        + "mode INTEGER,"
                        + "brightness INTEGER,"
                        + "CT INTEGER,"
                        + "action_id INTEGER,"
                        + "created_time INTEGER"
                        + ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS schedule ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "enabled BOOL DEFAULT 0,"
                        + "name TEXT,"
                        + "scene_id TEXT DEFAULT NULL,"
                        + "light_id TEXT DEFAULT NULL,"
                        + "group_id TEXT DEFAULT NULL,"
                        + "objectID TEXT,"
                        + "ver INTEGER,"
                        + "color INTEGER,"
                        + "brightness INTEGER,"
                        + "CT INTEGER,"
                        + "mode INTEGER,"
                        + "state BOOL DEFAULT 0,"
                        + "closeAll BOOL,"
                        + "time INTEGER,"
                        + "repeat TEXT,"
                        + "synced BOOL,"
                        + "created_time INTEGER,"
                        + "deleted INTEGER DEFAULT 0"
                        + ");"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS actions ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "objectID TEXT,"
                        + "ver INTEGER,"
                        + "name TEXT,"
                        + "content TEXT,"
                        + "created_time INTEGER"
                        + ");"
        );

        /*
        db.execSQL("CREATE TRIGGER IF NOT EXISTS light_group_delete"
                + " BEFORE DELETE ON light_groups"
                + " FOR EACH ROW"
                + " BEGIN"
                + " DELETE FROM light_group_rel WHERE light_group_rel.group_id=old._id;"
                + " DELETE FROM scenes_detail WHERE scenes_detail.group_id=old._id;"
                + " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS light_delete"
                + " BEFORE DELETE ON lights"
                + " FOR EACH ROW"
                + " BEGIN"
                + " DELETE FROM light_group_rel WHERE light_group_rel.light_id=old._id;"
                + " DELETE FROM scenes_detail WHERE scenes_detail.light_id=old._id;"
                + " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS light_group_rel_delete"
                + " BEFORE DELETE ON light_group_rel"
                + " FOR EACH ROW"
                + " BEGIN"
                + " UPDATE light_groups SET synced=0 WHERE light_groups._id=old.group_id;"
                + " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS scenes_detail_delete"
                + " BEFORE DELETE ON scenes_detail"
                + " FOR EACH ROW"
                + " BEGIN"
                + " UPDATE scenes SET synced=0 WHERE scenes._id=old.scene_id;"
                + " END;");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS scenes_detail_update"
                + " BEFORE UPDATE ON scenes_detail"
                + " FOR EACH ROW"
                + " BEGIN"
                + " UPDATE scenes SET synced=0 WHERE scenes._id=old.scene_id;"
                + " END;");

        ContentValues values = new ContentValues();
        values.put("objectID", UUID.randomUUID().toString());
        values.put("ver", 1);
        values.put("name", "Red");
        values.put("value", Color.RED);
        values.put("created_time", 1);
        db.insert("colors", null, values);
        values = new ContentValues();
        values.put("objectID", UUID.randomUUID().toString());
        values.put("ver", 1);
        values.put("name", "Green");
        values.put("value", Color.GREEN);
        values.put("created_time", 2);
        db.insert("colors", null, values);
        values = new ContentValues();
        values.put("objectID", UUID.randomUUID().toString());
        values.put("ver", 1);
        values.put("name", "Blue");
        values.put("value", Color.BLUE);
        values.put("created_time", 3);
        db.insert("colors", null, values);
        values = new ContentValues();
        values.put("objectID", UUID.randomUUID().toString());
        values.put("ver", 1);
        values.put("name", "Black");
        values.put("value", Color.BLACK);
        values.put("created_time", 4);
        db.insert("colors", null, values);
        values = new ContentValues();
        values.put("objectID", UUID.randomUUID().toString());
        values.put("ver", 1);
        values.put("name", "Turn On");
        values.put("created_time", 1);
        db.insert("actions", null, values);
        values = new ContentValues();
        values.put("objectID", UUID.randomUUID().toString());
        values.put("ver", 1);
        values.put("name", "Turn Off");
        values.put("created_time", 2);
        db.insert("actions", null, values);
        */
        //TODO Below is mock data, need to be removed
    }

    protected void createSwitchsDB(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS switchs ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "MAC TEXT NOT NULL,"
                        + "ver INTEGER,"
                        + "ThingID TEXT,"
                        + "remote_pwd TEXT,"
                        + "admin_pwd TEXT,"
                        + "name TEXT,"
                        + "model TEXT,"
                        + "firmware_version TEXT, "
                        + "mode INTEGER,"
                        + "IP INTEGER DEFAULT 0,"
                        + "subIP INTEGER DEFAULT 0,"
                        + "is_mine BOOL,"
                        + "state BOOL DEFAULT 0,"
                        + "connected BOOL,"
                        + "remote TEXT,"
                        + "synced BOOL,"
                        + "owned_time INTEGER,"
                        + "last_active INTEGER,"
                        + "key1 BOOL DEFAULT 0,"
                        + "key2 BOOL DEFAULT 0,"
                        + "key3 BOOL DEFAULT 0,"
                        + "deleted INTEGER DEFAULT 0"
                        + ");"
        );
    }

}
