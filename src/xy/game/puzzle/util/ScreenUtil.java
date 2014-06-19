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
	 * ��ȡ�ͱ��浱ǰ��Ļ�Ľ�ͼ
	 * 
	 * @param screenW
	 * @param screenH
	 * @param activity
	 * @param frontBmp
	 * @return
	 */
	public static String saveScreenshot(int screenW, int screenH,
			Activity activity, Bitmap frontBmp) {
		// ����Bitmap
		// ��ȡ��Ļ
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
	 * �ϲ�ͼƬ
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
				sourceImg.getWidth(), sourceImg.getHeight());// ���ͼƬ��ARGBֵ
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			if ((argb[i] & 0x00FFFFFF) == 0) {
				argb[i] = 0x00000000;
			} else {
				argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);// �޸����2λ��ֵ
			}
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
				sourceImg.getHeight(), Config.ARGB_8888);

		return sourceImg;
	}

	/**
	 * ������
	 * 
	 * @param context
	 *            ������
	 * @param activityTitle
	 *            Activity������
	 * @param msgTitle
	 *            ��Ϣ����
	 * @param msgText
	 *            ��Ϣ����
	 * @param imgPath
	 *            ͼƬ·����������ͼƬ��null
	 */
	public static void shareMsg(Context context, String activityTitle,
			String msgTitle, String msgText, String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // ���ı�
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
	 * ���ݿ�ȴӱ���ͼƬ·����ȡ��ͼƬ������ͼ
	 * 
	 * @param localImagePath
	 *            ����ͼƬ��·��
	 * @param width
	 *            ����ͼ�Ŀ�
	 * @param addedScaling
	 *            ������Լӵ����ű���
	 * @return bitmap ָ����ߵ�����ͼ
	 */
	public static Bitmap getBitmapByWidth(String localImagePath, int width,
			int addedScaling) {
		if (TextUtils.isEmpty(localImagePath)) {
			return null;
		}

		Bitmap temBitmap = null;

		try {
			BitmapFactory.Options outOptions = new BitmapFactory.Options();

			// ���ø�����Ϊtrue��������ͼƬ���ڴ棬ֻ����ͼƬ�Ŀ�ߵ�options�С�
			outOptions.inJustDecodeBounds = true;

			// ���ػ�ȡͼƬ�Ŀ��
			BitmapFactory.decodeFile(localImagePath, outOptions);

			int height = outOptions.outHeight;

			if (outOptions.outWidth > width) {
				// ���ݿ��������ű���
				outOptions.inSampleSize = (width == 0) ? 1
						: (outOptions.outWidth / width + addedScaling);
				outOptions.outWidth = width;

				// �������ź�ĸ߶�
				height = outOptions.outHeight / outOptions.inSampleSize;
				outOptions.outHeight = height;
			}

			// �������ø�����Ϊfalse������ͼƬ����
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
	 * ���ݿ�ȴӱ���ͼƬ·����ȡ��ͼƬ������ͼ
	 * 
	 * @param localImagePath
	 *            ����ͼƬ��·��
	 * @param width
	 *            ����ͼ�Ŀ�
	 * @param addedScaling
	 *            ������Լӵ����ű���
	 * @return bitmap ָ����ߵ�����ͼ
	 */
	public static Bitmap getBitmap(Resources res, int resId, int width,
			int addedScaling) {

		Bitmap temBitmap = null;

		try {
			BitmapFactory.Options outOptions = new BitmapFactory.Options();

			// ���ø�����Ϊtrue��������ͼƬ���ڴ棬ֻ����ͼƬ�Ŀ�ߵ�options�С�
			outOptions.inJustDecodeBounds = true;

			// ���ػ�ȡͼƬ�Ŀ��
			BitmapFactory.decodeResource(res, resId, outOptions);

			int height = outOptions.outHeight;

			if (outOptions.outWidth > width) {
				// ���ݿ��������ű���
				outOptions.inSampleSize = width == 0 ? 1 : outOptions.outWidth
						/ width + addedScaling;
				outOptions.outWidth = width;

				// �������ź�ĸ߶�
				height = outOptions.outHeight / outOptions.inSampleSize;
				outOptions.outHeight = height;
			}

			// // �������ø�����Ϊfalse������ͼƬ����
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
