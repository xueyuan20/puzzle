package xy.game.puzzle.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

public class StorageUtil {
	private final String APP_ROOT = "/xy.game.Puzzle";
	private final String SCREEN_SHOT_PATH = "/ScreenShots";
	private final String CACHE_DIR = "/.cache";
	private final String CAMERA_DIR = "/camera";

	private final String BACKGROUND_FILE_NAME = "background";

	public static final String[] DEFAULT_BK_NAME = new String[] { "default_33",
			"default_44", "default_55" };

	private static StorageUtil mInstance;

	public static StorageUtil getInstance() {
		if (mInstance == null) {
			mInstance = new StorageUtil();
		}
		return mInstance;
	}

	private ArrayList<String> mAppDirArray;
	public final static int DIR_APP_ROOT = 0;
	public final static int DIR_SCREENSHOT = 1;
	public final static int DIR_CACHE = 2;
	public final static int DIR_CAMERA = 3;

	private StorageUtil() {
		initAppPath();
		mInstance = this;
	}

	private boolean initAppPath() {
		// 判断SDCard是否存在
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			LogUtil.e("SD card does not mounted.");
			return false;
		}

		mAppDirArray = new ArrayList<String>();
		mAppDirArray.add(Environment.getExternalStorageDirectory() + APP_ROOT);
		mAppDirArray.add(mAppDirArray.get(0) + SCREEN_SHOT_PATH);
		mAppDirArray.add(mAppDirArray.get(0) + CACHE_DIR);
		mAppDirArray.add(mAppDirArray.get(0) + CAMERA_DIR);

		File file;
		for (int i = 0; i < mAppDirArray.size(); i++) {
			file = new File(mAppDirArray.get(i));
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		return true;
	}

	public String getAppRootByType(int dirType) {
		if ((mAppDirArray != null) && (dirType < mAppDirArray.size())) {
			return mAppDirArray.get(dirType);
		}
		return null;
	}

	/**
	 * 
	 * @param activity
	 * @param bmp
	 * @param fileName
	 *            , xx.png
	 * @return
	 */
	public String saveTmpToScreenShot(Activity activity, Bitmap bmp,
			String fileName) {
		if (bmp == null || bmp.isRecycled()) {
			return null;
		}
		// 图片存储路径
		return saveBmpToSDcard(mAppDirArray.get(DIR_SCREENSHOT), bmp, fileName);
	}

	/**
	 * 
	 * @param savePath
	 * @param bmp
	 * @param fileName
	 *            , file.png
	 * @return
	 */
	private String saveBmpToSDcard(String savePath, Bitmap bmp, String fileName) {
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
		// 判断SDCard是否存在
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	public String saveCacheFile(Bitmap bmpFile, String fileName) {
		if (bmpFile == null || bmpFile.isRecycled()) {
			return null;
		}
		// 图片存储路径
		return saveBmpToSDcard(mAppDirArray.get(DIR_CACHE), bmpFile, fileName);
	}

	public boolean isCacheFileExist(String fileName) {
		File file = new File(mAppDirArray.get(DIR_CACHE) + "/" + fileName);
		return file.exists();
	}

	public String getCacheFilePath(final int puzzleSize) {
		String path = mAppDirArray.get(DIR_CACHE) + "/"
				+ DEFAULT_BK_NAME[puzzleSize % 3] + ".png";
		if ((new File(path)).exists()) {
			return path;
		}
		return null;
	}

	public String saveBackground(Bitmap backgroundBmp) {
		return saveCacheFile(backgroundBmp, BACKGROUND_FILE_NAME);
	}
}
