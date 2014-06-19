package xy.game.puzzle.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
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
		// 构建Bitmap
		// 获取屏幕
		View decorview = activity.getWindow().getDecorView();
		if (decorview == null) {
			return null;
		}

		decorview.setDrawingCacheEnabled(true);
		decorview.buildDrawingCache();
		Bitmap bmp = decorview.getDrawingCache();

		Bitmap newBitmap = combineBitmap(bmp, frontBmp);
		decorview.destroyDrawingCache();
		String path = StorageUtil.getInstance().saveTmpToScreenShot(
				activity,
				newBitmap,
				"/ScreenShot_"
						+ String.valueOf(System.currentTimeMillis() / 1000));

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

	/**
	 * 合并图片
	 * 
	 * @param background
	 * @param foreground
	 * @return
	 */
	private static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
		if (background == null || background.isRecycled()) {
			return null;
		}
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

	/**
	 * 根据宽度从本地图片路径获取该图片的缩略图
	 * 
	 * @param localImagePath
	 *            本地图片的路径
	 * @param width
	 *            缩略图的宽
	 * @param addedScaling
	 *            额外可以加的缩放比例
	 * @return bitmap 指定宽高的缩略图
	 */
	public static Bitmap getBitmapByWidth(String localImagePath, int width,
			int addedScaling) {
		if (TextUtils.isEmpty(localImagePath)) {
			return null;
		}

		Bitmap temBitmap = null;

		try {
			BitmapFactory.Options outOptions = new BitmapFactory.Options();

			// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
			outOptions.inJustDecodeBounds = true;

			// 加载获取图片的宽高
			BitmapFactory.decodeFile(localImagePath, outOptions);

			int height = outOptions.outHeight;

			if (outOptions.outWidth > width) {
				// 根据宽设置缩放比例
				outOptions.inSampleSize = (width == 0) ? 1
						: (outOptions.outWidth / width + addedScaling);
				outOptions.outWidth = width;

				// 计算缩放后的高度
				height = outOptions.outHeight / outOptions.inSampleSize;
				outOptions.outHeight = height;
			}

			// 重新设置该属性为false，加载图片返回
			outOptions.inJustDecodeBounds = false;
			outOptions.inPurgeable = true;
			outOptions.inInputShareable = true;
			temBitmap = BitmapFactory.decodeFile(localImagePath, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return temBitmap;
	}

	/**
	 * 根据宽度从本地图片路径获取该图片的缩略图
	 * 
	 * @param localImagePath
	 *            本地图片的路径
	 * @param width
	 *            缩略图的宽
	 * @param addedScaling
	 *            额外可以加的缩放比例
	 * @return bitmap 指定宽高的缩略图
	 */
	public static Bitmap getBitmap(Resources res, int resId, int width,
			int addedScaling) {

		Bitmap temBitmap = null;

		try {
			BitmapFactory.Options outOptions = new BitmapFactory.Options();

			// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
			outOptions.inJustDecodeBounds = true;

			// 加载获取图片的宽高
			BitmapFactory.decodeResource(res, resId, outOptions);

			int height = outOptions.outHeight;

			if (outOptions.outWidth > width) {
				// 根据宽设置缩放比例
				outOptions.inSampleSize = width == 0 ? 1 : outOptions.outWidth
						/ width + addedScaling;
				outOptions.outWidth = width;

				// 计算缩放后的高度
				height = outOptions.outHeight / outOptions.inSampleSize;
				outOptions.outHeight = height;
			}

			// // 重新设置该属性为false，加载图片返回
			outOptions.inJustDecodeBounds = false;
			outOptions.inPurgeable = true;
			outOptions.inInputShareable = true;
			temBitmap = BitmapFactory.decodeResource(res, resId, outOptions);

			LogUtil.e("bitmap width = " + temBitmap.getWidth() + "; height = "
					+ temBitmap.getHeight());
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return temBitmap;
	}
}
