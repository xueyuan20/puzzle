package xy.game.puzzle.util;

import java.io.File;
import java.io.FileOutputStream;

import xy.game.puzzle.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class ScreenShot {

	/**
	 * 获取和保存当前屏幕的截图
	 */
	public static String getAndSaveCurrentImage(int screenW, int screenH,
			Activity activity, Bitmap bmp) {
		if (bmp == null) {
			return null;
		}
		// // 构建Bitmap
		// Bitmap bmp = Bitmap.createBitmap(screenW, screenH, Config.ARGB_4444);
		// // 获取屏幕
		// View decorview = activity.getWindow().getDecorView();
		// if (decorview == null) {
		// return;
		// }
		// decorview.setDrawingCacheEnabled(true);
		// bmp = decorview.getDrawingCache();
		return saveBmpToSDcard(
				activity,
				bmp,
				"/ScreenShot_"
						+ String.valueOf(System.currentTimeMillis() / 1000));
	}

	private static String saveBmpToSDcard(Activity activity, Bitmap bmp,
			String fileName) {
		// 图片存储路径
		String savePath = getSDCardPath() + "/Puzzle/ScreenShots";

		// 保存Bitmap
		try {
			File path = new File(savePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			// 文件
			String filepath = savePath + fileName + ".png";
			File file = new File(filepath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
				Toast.makeText(activity,
						"截屏文件已保存至SDCard/Puzzle/ScreenImages/目录下",
						Toast.LENGTH_LONG).show();
			}
			return filepath;
		} catch (Exception e) {
			LogUtil.printCodeStack(e);
		}
		return null;
	}

	public static String getIconPath(Activity activity) {
		String iconFilePath = getSDCardPath() + "/Puzzle";
		File path = new File(iconFilePath);
		String iconFileName = iconFilePath + "/icon.png";
		File file = new File(iconFileName);
		if (!path.exists()) {
			path.mkdirs();
		}

		if (file.exists()) {
			return iconFileName;
		}

		Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.ic_launcher);
		saveBmpToSDcard(activity, bmp, iconFileName);

		bmp.recycle();
		bmp = null;

		if (file.exists()) {
			return iconFileName;
		} else {
			return null;
		}

	}

	/**
	 * 获取SDCard的目录路径功能
	 * 
	 * @return
	 */
	private static String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	/**
	 * 分享功能
	 * 
	 * @param context
	 *            上下文
	 * @param activityTitle
	 *            Activity的名字
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 * @param imgPath
	 *            图片路径，不分享图片则传null
	 */
	public static void shareMsg(Context context, String activityTitle,
			String msgTitle, String msgText, String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/png");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}
}
