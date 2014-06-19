package xy.game.puzzle.logic;

import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.ScreenUtil;
import xy.game.puzzle.util.StorageUtil;
import xy.game.puzzle.view.PreviewSurfaceView;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

public class RefreshByLocalFileTask extends AsyncTask<String, String, String> {
	private ImageView mImageView;
	private Bitmap mBmp;
	private PreviewSurfaceView mPreviewSurfaceview;
	private boolean mIsPreview;

	public RefreshByLocalFileTask(ImageView imgView) {
		mImageView = imgView;
	}

	public RefreshByLocalFileTask(boolean isPreview,
			PreviewSurfaceView svPreview) {
		// TODO Auto-generated constructor stub
		mPreviewSurfaceview = svPreview;
		mIsPreview = isPreview;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		// mBmp = ScreenUtil.getBitmapByWidth(
		// params[0],
		// ScreenUtil.dip2px(mImageView.getContext(),
		// mImageView.getWidth()), 0);
		mBmp = ScreenUtil.getBitmapByWidth(params[0], ScreenUtil.dip2px(
				mPreviewSurfaceview.getContext(),
				mPreviewSurfaceview.getWidth()), 0);
		LogUtil.e("[RefreshByLocalFileTask]image width : "
				+ mPreviewSurfaceview.getWidth());

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (mImageView != null) {
			mImageView.setImageBitmap(mBmp);
		}
		if (mPreviewSurfaceview == null) {
			LogUtil.e("[RefreshByLocalFileTask] end ...");
			return;
		}
		mPreviewSurfaceview.setImageAsBackground(false, mBmp);

		if (!mIsPreview) {
			PuzzleProvider provider = PuzzleProvider
					.getInstance(mPreviewSurfaceview.getContext());

			String path = StorageUtil.getInstance().saveBackground(
					mPreviewSurfaceview.getBackgroundBmp());
			if ((path != null) && (!TextUtils.isEmpty(path))) {
				provider.setUseDefaultBk(false);
				provider.setCustomBkPath(path);
			}
		}
	}
}
