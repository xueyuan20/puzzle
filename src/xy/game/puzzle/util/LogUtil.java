package xy.game.puzzle.util;

import android.content.Context;
import android.util.Log;

/**
 * Class to print log.
 * @author 80070307
 *
 */
public class LogUtil {
	private static final String TAG = "xy.game.puzzle";

	/**
	 * Print verbose information.
	 * @param log
	 */
	public static void v(String log){
		Log.v(TAG, log);
	}

	public static void v(Context context, String log){
		Log.v(context.getClass().getName(), log);
	}

	/**
	 * Print debug information.
	 * @param log
	 */
	public static void d(String log){
		Log.d(TAG, log);
	}

	public static void d(Context context, String log){
		Log.d(context.getClass().getName(), log);
	}

	/**
	 * Print normal information.
	 * @param log
	 */
	public static void i(String log){
		Log.i(TAG, log);
	}

	public static void i(Context context, String log){
		Log.i(context.getClass().getName(), log);
	}

	/**
	 * Print Warning information.
	 * @param log
	 */
	public static void w(String log){
		Log.w(TAG, log);
	}

	public static void w(Context context, String log){
		Log.w(context.getClass().getName(), log);
	}

	/**
	 * Print Error information.
	 * @param log
	 */
	public static void e(String log){
		Log.e(TAG, log);
	}

	public static void e(Context context, String log){
		Log.e(context.getClass().getName(), log);
	}

	public static void printCodeStack(Exception e){
		Log.e(TAG, ">>>>>> Serious error happened.");
		e.printStackTrace();
		Log.e(TAG, "<<<<<< Serious error happened.");
	}
}
