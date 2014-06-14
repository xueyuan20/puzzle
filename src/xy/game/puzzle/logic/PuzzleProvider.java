package xy.game.puzzle.logic;

import xy.game.puzzle.util.RecordItem;
import android.content.Context;
import android.content.SharedPreferences;

public class PuzzleProvider {
	private static PuzzleProvider mInstance;

	private SharedPreferences mSharedPrefs;
	private SharedPreferences.Editor mEditor;
	private Context mContext;
	private DBOpenHelper mOpenHelper;

	private final String SHAREDPREFERENCE_NAME = "puzzle";
	private final String KEY_GAME_LEVEL = "key_game_level";

	private PuzzleProvider(Context context) {
		mContext = context;
		mInstance = this;
		mSharedPrefs = mContext.getSharedPreferences(SHAREDPREFERENCE_NAME,
				Context.MODE_PRIVATE);
		mEditor = mSharedPrefs.edit();
		mOpenHelper = new DBOpenHelper(mContext);

		initParams();
	}

	public static PuzzleProvider getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new PuzzleProvider(context);
		}
		return mInstance;
	}

	private void initParams() {
		int level = mSharedPrefs.getInt(KEY_GAME_LEVEL, 0);
		if (level == 0) {
			mEditor.putInt(KEY_GAME_LEVEL, 0);
			mEditor.commit();
		}
	}

	public void setGameLevel(int level) {
		mEditor.putInt(KEY_GAME_LEVEL, level);
		mEditor.commit();
		mOpenHelper.resetPuzzleTable();
	}

	public int getGameLevel() {
		return mSharedPrefs.getInt(KEY_GAME_LEVEL, 0);
	}

	public int[] getArray() {
		return mOpenHelper.queryPuzzleArray();
	}

	public void savePuzzle(int[] array, RecordItem scoreRecord){
		saveArray(array);
		saveScore(scoreRecord);
	}

	private void saveArray(int[] array) {
		mOpenHelper.savePuzzle(array);
	}

	private void saveScore(RecordItem scoreRecord) {
		// TODO Auto-generated method stub
		mOpenHelper.delUncompleteRecord();
		mOpenHelper.insertScore(scoreRecord);
	}

	public RecordItem queryRecord() {
		return mOpenHelper
				.queryRecord(mSharedPrefs.getInt(KEY_GAME_LEVEL, 0) + 3);
	}

	public void deleteRecord(RecordItem record){
		if (queryRecord() != null) {
			mOpenHelper.delete(record);
		}
	}

}
