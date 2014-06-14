package xy.game.puzzle.util;

public class MessageUtils {

	/**
	 * Update steps count.
	 */
	public static final int MSG_UPDATE_STEPS = 100;

	/**
	 * Complete game and show the result.
	 */
	public static final int MSG_RESULT = 101;

	/**
	 * Game Level changed.
	 */
	public static final int MSG_CHANGE_LEVEL = 102;

	/**
	 * To start the timer.
	 */
	public static final int MSG_START_TIMER = 103;

	public static final int MSG_INIT_TIMER = 104;

	/**
	 * keywords.
	 */
	public static final String KEY_STEPS = "steps_count";
	public static final String KEY_GAME_LEVEL = "game_level";
	public static final String KEY_RESULT_CONTENT = "result_content";
	public static final String KEY_FILE_PATH = "file_path";

	/**
	 * Activity Request Code
	 */
	public static final int CODE_PREVIEW_IMG = 101;
	public static final int CODE_FROM_CAMERA = 102;
	public static final int CODE_FROM_GALLERY = 103;
}
