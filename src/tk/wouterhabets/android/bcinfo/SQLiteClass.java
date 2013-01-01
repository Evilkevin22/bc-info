package tk.wouterhabets.android.bcinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteClass {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";

	private static final String DATABASE_NAME = "UitvalDB";
	private static final String DATABASE_TABLE = "UitvalTable";
	private static final int DATABASE_VERSION = 1;

	private DBHelper dbhelper;
	private final Context context;
	private SQLiteDatabase database;
	
	public SQLiteClass(Context c) {
		context = c;
	}
	
	public SQLiteClass open() {
		dbhelper = new DBHelper(context);
		database = dbhelper.getWritableDatabase();
		return this;
	}
	
	public SQLiteClass close() {
		dbhelper.close();
		return null;
	}
	
	public long createEntry(String title, String description) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_TITLE, title);
		cv.put(KEY_DESCRIPTION, description);
		return database.insert(DATABASE_TABLE, null, cv);
	}

	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(
					"CREATE TABLE " + DATABASE_TABLE + " (" +
					 KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
					 KEY_TITLE + " TEXT NOT NULL, " +
					 KEY_DESCRIPTION + " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);

		}

	}
}
