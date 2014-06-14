package xy.game.puzzle.logic;

import xy.game.puzzle.util.ScreenUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

public class RefreshByLocalFileTask extends AsyncTask<String, String, String> {
	private ImageView mImageView;
	private Bitmap mBmp;

	public RefreshByLocalFileTask(ImageView imgView) {
		mImageView = imgView;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		mBmp = getBitmapByWidth(
				params[0],
				ScreenUtil.dip2px(mImageView.getContext(),
						mImageView.getWidth()), 0);

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mImageView.setImageBitmap(mBmp);
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
				outOptions.inSampleSize = width == 0 ? 1 : outOptions.outWidth
						/ width + 1 + addedScaling;
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
}
