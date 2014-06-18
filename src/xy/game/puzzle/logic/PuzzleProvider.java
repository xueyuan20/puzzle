package xy.game.puzzle.logic;

import xy.game.puzzle.util.LogUtil;
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
	private final String KEY_USE_DEFAULT_BK = "key_user_default_bk";
	private final String KEY_CUSTOM_BK_PATH = "key_custom_bk_path";
	private final String KEY_WETHER_USE_HINT = "key_wether_use_hint";

	private PuzzleProvider(Context context) {
		mContext = context;
		mInstance = this;
		mSharedPrefs = mContext.getSharedPreferences(SHAREDPREFERENCE_NAME,
				Context.MODE_PRIVATE);
		mEditor = mSharedPrefs.edit();
		mOpenHelper = new DBOpenHelper(mContext);

		initParams();
	}

	/**
	 * Called to get the only instance of PuzzleProvider class. 
	 * @param context
	 * @return
	 */
	public static PuzzleProvider getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new PuzzleProvider(context);
		}
		return mInstance;
	}

	/**
	 * Initial parameters.
	 */
	private void initParams() {
		int level = mSharedPrefs.getInt(KEY_GAME_LEVEL, -1);
		if (level < 0) {
			mEditor.putInt(KEY_GAME_LEVEL, 0);
			mEditor.putBoolean(KEY_USE_DEFAULT_BK, true);
			mEditor.putString(KEY_CUSTOM_BK_PATH, null);
			mEditor.putBoolean(KEY_WETHER_USE_HINT, false);
			mEditor.commit();
		}
	}

	public void changeHintType(boolean showHint){
		mEditor.putBoolean(KEY_WETHER_USE_HINT, showHint);
		mEditor.commit();
	}

	public boolean checkWetherUseHint(){
		return mSharedPrefs.getBoolean(KEY_WETHER_USE_HINT, false);
	}

	/**
	 * Called to set game level.
	 * @param level
	 */
	public void setGameLevel(int level) {
		mEditor.putInt(KEY_GAME_LEVEL, level);
		mEditor.commit();
		mOpenHelper.resetPuzzleTable();
	}

	/**
	 * Called to get game level.
	 * @return
	 */
	public int getGameLevel() {
		return mSharedPrefs.getInt(KEY_GAME_LEVEL, 0);
	}

	/**
	 * set to use default background.
	 * 
	 * @param useDefault
	 */
	public void setUseDefaultBk(boolean useDefault) {
//		LogUtil.e("[SharedPrefs] user default = "+useDefault);
		mEditor.putBoolean(KEY_USE_DEFAULT_BK, useDefault);
		mEditor.commit();
	}

	/**
	 * check whether to use default background.
	 * 
	 * @return
	 */
	public boolean checkUseDefaultBk() {
		return mSharedPrefs.getBoolean(KEY_USE_DEFAULT_BK, true);
	}

	/**
	 * set custom puzzle background image file's path.
	 * 
	 * @param path
	 */
	public void setCustomBkPath(String path) {
//		LogUtil.e("[SharedPrefs] user default file path = "+path);
		mEditor.putString(KEY_CUSTOM_BK_PATH, path);
		mEditor.commit();
	}

	/**
	 * set custom puzzle background image file's path.
	 * 
	 * @return
	 */
	public String getCustomBkPath() {
		LogUtil.e("[SharedPrefs] custom background file path = "
				+ mSharedPrefs.getString(KEY_CUSTOM_BK_PATH, null));
		return mSharedPrefs.getString(KEY_CUSTOM_BK_PATH, null);
	}

	/**
	 * SQLite Database.
	 * 
	 * @return
	 */
	public int[] getArray() {
		return mOpenHelper.queryPuzzleArray();
	}

	/**
	 * Save Puzzle index and score.
	 * @param array
	 * @param scoreRecord
	 */
	public void savePuzzle(int[] array, RecordItem scoreRecord) {
		saveArray(array);
		saveScore(scoreRecord);
	}

	/**
	 * Save Puzzle index.
	 * @param array
	 */
	private void saveArray(int[] array) {
		mOpenHelper.savePuzzle(array);
	}

	/**
	 * Save user's score.
	 * @param scoreRecord
	 */
	private void saveScore(RecordItem scoreRecord) {
		// TODO Auto-generated method stub
		mOpenHelper.delUncompleteRecord();
		mOpenHelper.insertScore(scoreRecord);
	}

	/**
	 * Called to get user's record.
	 * @return
	 */
	public RecordItem queryRecord() {
		return mOpenHelper
				.queryRecord(mSharedPrefs.getInt(KEY_GAME_LEVEL, 0) + 3);
	}

	/**
	 * Called to delete user's record.
	 * @param record
	 */
	public void deleteRecord(RecordItem record) {
		if (queryRecord() != null) {
			mOpenHelper.delete(record);
		}
	}
}
