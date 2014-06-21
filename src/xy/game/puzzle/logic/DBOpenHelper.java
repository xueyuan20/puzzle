package xy.game.puzzle.logic;

import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.RecordItem;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "puzzle.db";
	private static final int DATABASE_VERSION = 1;

	private final String PUZZLE_TABLE = "puzzle";
	private final String SCORE_TABLE = "score";

	private final String[] PUZZLE_COLUMNS = { "_ID", "indexPos", "indexValue",
			"flag", };

	private final String[] SCORE_COLUMNS = { "_ID", "userName", "score",
			"stepsCount", "timerCount", "completeFlag", "completeTime", };

	public DBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + PUZZLE_TABLE + "(" + PUZZLE_COLUMNS[0]
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + PUZZLE_COLUMNS[1]
				+ " INTEGER," + PUZZLE_COLUMNS[2]
				+ " INTEGER NOT NULL DEFAULT 0," + PUZZLE_COLUMNS[3]
				+ " INTEGER NOT NULL DEFAULT 0" + ");";

		db.execSQL(sql);

		sql = "CREATE TABLE " + SCORE_TABLE + "(" + SCORE_COLUMNS[0]
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + SCORE_COLUMNS[1]
				+ " TEXT, " + SCORE_COLUMNS[2] + " INTEGER NOT NULL DEFAULT 0,"
				+ SCORE_COLUMNS[3] + " INTEGER NOT NULL DEFAULT 0,"
				+ SCORE_COLUMNS[4] + " INTEGER NOT NULL DEFAULT 0,"
				+ SCORE_COLUMNS[5] + " INTEGER NOT NULL DEFAULT 0,"
				+ SCORE_COLUMNS[6] + " TEXT" + ");";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		if (newVersion > oldVersion) {
			LogUtil.d("Not Supported!");
		}
	}

	public void savePuzzle(int[] array) {
		if (array == null) {
			return;
		}
		resetPuzzleTable();
		SQLiteDatabase db = getWritableDatabase();
		for (int j = 0; j < array.length; j++) {
			ContentValues values = new ContentValues();
			values.put(PUZZLE_COLUMNS[0], j);
			values.put(PUZZLE_COLUMNS[1], j);
			values.put(PUZZLE_COLUMNS[2], array[j]);
			db.insertOrThrow(PUZZLE_TABLE, null, values);
		}
	}

	public int[] queryPuzzleArray() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(PUZZLE_TABLE, PUZZLE_COLUMNS,
				PUZZLE_COLUMNS[3] + "=?", new String[] { String.valueOf(0) },
				null, null, null);

		if (cursor == null) {
			return null;
		}
		int[] array = new int[cursor.getCount()];

		int puzzlePosInd;
		while (cursor.moveToNext()) {
			if (cursor.getColumnIndex(PUZZLE_COLUMNS[0]) < 0) {
				continue;
			}
			puzzlePosInd = cursor.getInt(cursor
					.getColumnIndex(PUZZLE_COLUMNS[0]));
			if (puzzlePosInd < array.length) {
				array[puzzlePosInd] = cursor.getInt(cursor
						.getColumnIndex(PUZZLE_COLUMNS[2]));
			}
		}
		cursor = null;
		return array;
	}

	public void resetPuzzleTable() {
		SQLiteDatabase db = getWritableDatabase();
		if (db != null) {
			db.execSQL("DROP TABLE IF EXISTS " + PUZZLE_TABLE);
			String sql = "CREATE TABLE " + PUZZLE_TABLE + "("
					+ PUZZLE_COLUMNS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ PUZZLE_COLUMNS[1] + " INTEGER," + PUZZLE_COLUMNS[2]
					+ " INTEGER NOT NULL DEFAULT 0," + PUZZLE_COLUMNS[3]
					+ " INTEGER NOT NULL DEFAULT 0" + ");";

			db.execSQL(sql);
		}
	}

	public void insertScore(RecordItem scoreRecord) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getWritableDatabase();
		if (db != null) {
			ContentValues values = new ContentValues();
			values.put(SCORE_COLUMNS[1], scoreRecord.getUserName());
			values.put(SCORE_COLUMNS[2], scoreRecord.getTotalScore());
			values.put(SCORE_COLUMNS[3], scoreRecord.getStepCount());
			values.put(SCORE_COLUMNS[4], scoreRecord.getTimerCount());
			values.put(SCORE_COLUMNS[5], scoreRecord.getCompleteFlag());
			values.put(SCORE_COLUMNS[6], scoreRecord.getCompleteTime());

			LogUtil.d("save Score : " + values.toString() + " insert: "
					+ db.insertOrThrow(SCORE_TABLE, SCORE_COLUMNS[6], values));
		}
	}

	public RecordItem queryRecord(int puzzleSize) {
		SQLiteDatabase db = getWritableDatabase();
		if (db != null) {
			String whereClause = SCORE_COLUMNS[5] + "=0";
			Cursor cursor = db.query(SCORE_TABLE, SCORE_COLUMNS, whereClause,
					null, null, null, null);
			if (cursor != null) {
				if (cursor.moveToNext()) {
					RecordItem recordItem = new RecordItem(puzzleSize,
							cursor.getInt(cursor
									.getColumnIndex(SCORE_COLUMNS[3])),
							cursor.getInt(cursor
									.getColumnIndex(SCORE_COLUMNS[4])));

					recordItem.setId(cursor.getInt(cursor
							.getColumnIndex(SCORE_COLUMNS[0])));
					recordItem.setCompleteFlag(cursor.getInt(cursor
							.getColumnIndex(SCORE_COLUMNS[5])) > 0);

					cursor = null;
					return recordItem;
				}
				cursor = null;
			}
		}
		return null;
	}

	public void delete(RecordItem scoreRecord) {
		SQLiteDatabase db = getWritableDatabase();
		if (db != null) {
			String whereClause = SCORE_COLUMNS[0] + "=?";
			String[] whereArgs = new String[] { String.valueOf(scoreRecord
					.getId()) };
			db.delete(SCORE_TABLE, whereClause, whereArgs);
		}
	}

	public void updateScore(RecordItem scoreRecord) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getWritableDatabase();
		if (db != null) {
			String whereClause = SCORE_COLUMNS[0] + "=?";
			String[] whereArgs = new String[] { String.valueOf(scoreRecord
					.getId()) };
			Cursor cursor = db.query(SCORE_TABLE, SCORE_COLUMNS, whereClause,
					whereArgs, null, null, null);
			if (cursor != null) {
				if (cursor.moveToNext()) {
					ContentValues values = new ContentValues();
					values.put(SCORE_COLUMNS[0], scoreRecord.getId());
					values.put(SCORE_COLUMNS[1], scoreRecord.getUserName());
					values.put(SCORE_COLUMNS[2], scoreRecord.getTotalScore());
					values.put(SCORE_COLUMNS[3], scoreRecord.getStepCount());
					values.put(SCORE_COLUMNS[4], scoreRecord.getTimerCount());
					values.put(SCORE_COLUMNS[5], scoreRecord.getCompleteFlag());
					values.put(SCORE_COLUMNS[6], scoreRecord.getCompleteTime());
					db.update(SCORE_TABLE, values, whereClause, whereArgs);
				}
				cursor = null;
			}
		}
	}

	public void delUncompleteRecord() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getWritableDatabase();
		if (db != null) {
			String whereClause = SCORE_COLUMNS[5] + "=0";
			db.delete(SCORE_TABLE, whereClause, null);
	
		}
	}
}
