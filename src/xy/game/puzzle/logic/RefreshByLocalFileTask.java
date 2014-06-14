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
				outOptions.inSampleSize = width == 0 ? 1 : outOptions.outWidth
						/ width + 1 + addedScaling;
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
}
