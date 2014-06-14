package xy.game.puzzle.util;

import java.io.File;
import java.io.FileOutputStream;

import xy.game.puzzle.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

public class ScreenUtil {

	/**
	 * 获取和保存当前屏幕的截图
	 * 
	 * @param screenW
	 * @param screenH
	 * @param activity
	 * @param frontBmp
	 * @return
	 */
	public static String saveScreenshot(int screenW, int screenH,
			Activity activity, Bitmap frontBmp) {
		LogUtil.e("1111111");
		// 构建Bitmap
		// 获取屏幕
		View decorview = activity.getWindow().getDecorView();
		if (decorview == null) {
			return null;
		}

		LogUtil.e("222222222");
		decorview.setDrawingCacheEnabled(true);
		decorview.buildDrawingCache();
		Bitmap bmp = decorview.getDrawingCache();
		LogUtil.e("****333333333 _ ");

		Bitmap newBitmap = combineBitmap(bmp, frontBmp);
		decorview.destroyDrawingCache();
		LogUtil.e("333333333 _ "
				+ (newBitmap == null ? "null" : newBitmap.getByteCount()));
		String path = saveBmpToSDcard(activity, newBitmap, "/ScreenShot_"
				+ String.valueOf(System.currentTimeMillis() / 1000));

		LogUtil.e("4444444444 _ " + path);
		// free
		if (!bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
		if ((newBitmap != null) && (!newBitmap.isRecycled())) {
			newBitmap.recycle();
			newBitmap = null;
		}

		return path;
	}

	private static String saveBmpToSDcard(Activity activity, Bitmap bmp,
			String fileName) {
		if (bmp == null || bmp.isRecycled()) {
			return null;
		}
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
				// Toast.makeText(activity,
				// "截屏文件已保存至SDCard/Puzzle/ScreenImages/目录下",
				// Toast.LENGTH_LONG).show();
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

		if (!bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}

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
	 * 合并图片
	 * 
	 * @param background
	 * @param foreground
	 * @return
	 */
	private static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
		if (background == null || background.isRecycled()) {
			LogUtil.e("1 _ ");
			return null;
		}
		LogUtil.e("2 _ ");
		int bgWidth = background.getWidth();
		int bgHeight = background.getHeight();
		int fgWidth = foreground.getWidth();
		int fgHeight = foreground.getHeight();
		Bitmap newmap = Bitmap
				.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(newmap);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(background, 0, 0, null);
		foreground = setAlpha(foreground, 0xFF);
		canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,
				(bgHeight - fgHeight) / 2, null);
		canvas.drawBitmap(newmap, 0, 0, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newmap;
	}

	/**
	 * 
	 * @param sourceImg
	 * @param number
	 * @return
	 */
	private static Bitmap setAlpha(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
				sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			if ((argb[i] & 0x00FFFFFF) == 0) {
				argb[i] = 0x00000000;
			} else {
				argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);// 修改最高2位的值
			}
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
				sourceImg.getHeight(), Config.ARGB_8888);

		return sourceImg;
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

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
