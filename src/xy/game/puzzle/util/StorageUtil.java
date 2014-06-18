package xy.game.puzzle.util;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

public class StorageUtil {
	private static final String APP_ROOT = "/xy.game.Puzzle";
	private static final String SCREEN_SHOT_PATH = "/ScreenShots";
	private static final String TMP = "/tmp";
	private static final String BACKGROUND_FILE_NAME = "background";
	private static final String CACHE_DIR = "/.cache";

	public static final String[] DEFAULT_BK_NAME = new String[] {
			"default_33", "default_44", "default_55" };

	private static String initAppPath() {
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!sdcardExist) {
			LogUtil.e("Request SD card.");
			return null;
		}
		String path = Environment.getExternalStorageDirectory() + APP_ROOT;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(path + SCREEN_SHOT_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(path + TMP);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	private static String initTmpPath() {
		String path = initAppPath();
		if (path == null) {
			return null;
		}
		path = path + TMP;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	private static String initScreenShotPath() {
		String path = initAppPath();
		if (path == null) {
			return null;
		}
		path = path + SCREEN_SHOT_PATH;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	private static String initPath(final String path){
		String appRoot = initAppPath();
		if (appRoot == null) {
			return null;
		}
		String newPath = null;
		if (!path.startsWith("/")) {
			newPath = appRoot + "/"+path;
		} else {
			newPath = appRoot+path;
		}

		File file = new File(newPath);
		if (!file.exists()) {
			file.mkdirs();
		}

		return newPath;
	}

	/**
	 * 
	 * @param activity
	 * @param bmp
	 * @param fileName
	 *            , xx.png
	 * @return
	 */
	public static String saveToTmpPath(Activity activity, Bitmap bmp,
			String fileName) {

		if (bmp == null || bmp.isRecycled()) {
			LogUtil.e("Warning: Bitmap is recycled!");
			return null;
		}
		return saveBmpToSDcard(initTmpPath(), bmp, fileName);
	}

	/**
	 * 
	 * @param activity
	 * @param bmp
	 * @param fileName
	 *            , xx.png
	 * @return
	 */
	public static String saveTmpToScreenShot(Activity activity, Bitmap bmp,
			String fileName) {
		if (bmp == null || bmp.isRecycled()) {
			return null;
		}
		// 图片存储路径
		String savePath = initScreenShotPath();
		return saveBmpToSDcard(savePath, bmp, fileName);
	}

	/**
	 * 
	 * @param savePath
	 * @param bmp
	 * @param fileName
	 *            , file.png
	 * @return
	 */
	private static String saveBmpToSDcard(String savePath, Bitmap bmp,
			String fileName) {
		LogUtil.e("save image file. [path: " + savePath + ", fileName: "
				+ fileName);
		// 保存Bitmap
		try {
			File path = new File(savePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			// 文件
			String fileFullPath = savePath + "/" + fileName + ".png";
			File file = new File(fileFullPath);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
			return fileFullPath;
		} catch (Exception e) {
			LogUtil.printCodeStack(e);
		}
		return null;
	}

	/**
	 * 获取SDCard的目录路径功能
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	public static String saveCacheFile(Bitmap bmpFile, String fileName){
		if (bmpFile == null || bmpFile.isRecycled()) {
			return null;
		}
		// 图片存储路径
		String savePath = initPath(CACHE_DIR);
		return saveBmpToSDcard(savePath, bmpFile, fileName);
	}

	public static boolean isCacheFileExist(String fileName){
		File file = new File(initPath(CACHE_DIR)+"/"+fileName);
		return file.exists();
	}

	public static String getCacheFilePath(final int size){
		String path = initPath(CACHE_DIR)+"/"+DEFAULT_BK_NAME[size%3]+".png";
		if ((new File(path)).exists()) {
			return path;
		}
		return null;
	}

	public static String saveBackground(Bitmap backgroundBmp) {
		return saveToTmpPath(null, backgroundBmp, BACKGROUND_FILE_NAME);
	}
}
