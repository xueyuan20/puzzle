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
	 * ��ȡ�ͱ��浱ǰ��Ļ�Ľ�ͼ
	 */
	public static String getAndSaveCurrentImage(int screenW, int screenH,
			Activity activity, Bitmap bmp) {
		if (bmp == null) {
			return null;
		}
		// // ����Bitmap
		// Bitmap bmp = Bitmap.createBitmap(screenW, screenH, Config.ARGB_4444);
		// // ��ȡ��Ļ
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
		// ͼƬ�洢·��
		String savePath = getSDCardPath() + "/Puzzle/ScreenShots";

		// ����Bitmap
		try {
			File path = new File(savePath);
			if (!path.exists()) {
				path.mkdirs();
			}
			// �ļ�
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
						"�����ļ��ѱ�����SDCard/Puzzle/ScreenImages/Ŀ¼��",
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
	 * ��ȡSDCard��Ŀ¼·������
	 * 
	 * @return
	 */
	private static String getSDCardPath() {
		File sdcardDir = null;
		// �ж�SDCard�Ƿ����
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
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
}
