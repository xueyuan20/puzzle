package xy.game.puzzle.logic;

import xy.game.puzzle.util.LogUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "puzzle.db";
	private static final int DATABASE_VERSION = 1;

	private final String PUZZLE_TABLE = "puzzle";

	private final String[] PUZZLE_COLUMNS = {
			"_ID",
			"indexPos",
			"indexValue",
			"flag",
	};

	public DBOpenHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE "+ PUZZLE_TABLE + "(" +
				PUZZLE_COLUMNS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				PUZZLE_COLUMNS[1] + " INTEGER," + 
				PUZZLE_COLUMNS[2] + " INTEGER NOT NULL DEFAULT 0," +
				PUZZLE_COLUMNS[3] + " INTEGER NOT NULL DEFAULT 0" +
				");";
		
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		// TODO Auto-generated method stub

		if (newVersion > oldVersion) {
			LogUtil.d("Not Supported!");
		}
	}

	public void savePuzzle(int[] array){
		clearDatabase();
		SQLiteDatabase db = getWritableDatabase();
		for (int j = 0; j < array.length; j++) {
			ContentValues values = new ContentValues();
			values.put(PUZZLE_COLUMNS[0], j);
			values.put(PUZZLE_COLUMNS[1], j);
			values.put(PUZZLE_COLUMNS[2], array[j]);
			db.insertOrThrow(PUZZLE_TABLE, null, values);
		}
	}

	public int[] queryPuzzleArray(){
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(PUZZLE_TABLE, PUZZLE_COLUMNS, 
				PUZZLE_COLUMNS[3]+"=?", new String[]{String.valueOf(0)}, 
				 null, null, null);

		if (cursor == null) {
			return null;
		}
		int[] array = new int[cursor.getCount()];

		int puzzlePosInd;
		while (cursor.moveToNext()) {
			if (cursor.getColumnIndex(PUZZLE_COLUMNS[0])<0) {
				continue;
			}
			puzzlePosInd = cursor.getInt(cursor.getColumnIndex(PUZZLE_COLUMNS[0]));
			if (puzzlePosInd<array.length) {				
				array[puzzlePosInd] = cursor.getInt(cursor.getColumnIndex(PUZZLE_COLUMNS[2]));
			}
		}
		return array;
	}

	private void clearDatabase(){
		SQLiteDatabase db = getWritableDatabase();
		if (db != null) {
			db.execSQL("DROP TABLE IF EXISTS " + PUZZLE_TABLE);
			String sql = "CREATE TABLE "+ PUZZLE_TABLE + "(" +
					PUZZLE_COLUMNS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					PUZZLE_COLUMNS[1] + " INTEGER," + 
					PUZZLE_COLUMNS[2] + " INTEGER NOT NULL DEFAULT 0," +
					PUZZLE_COLUMNS[3] + " INTEGER NOT NULL DEFAULT 0" +
					");";
			
			db.execSQL(sql);
		}
	}

	public void resetDatabase(){
		clearDatabase();
	}
}
