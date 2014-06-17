package xy.game.puzzle.logic;

import xy.game.puzzle.activity.PreviewActivity;
import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.MessageUtils;
import xy.game.puzzle.util.ScreenUtil;
import xy.game.puzzle.view.PuzzleSurfaceView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

public class ScreenShotAsyncTask extends AsyncTask<String, String, String> {
	private Activity mParent;
	private PuzzleSurfaceView mPuzzleView;

	public ScreenShotAsyncTask(Activity activity, PuzzleSurfaceView puzzle) {
		// TODO Auto-generated constructor stub
		mParent = activity;
		mPuzzleView = puzzle;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		Bitmap bmp = mPuzzleView.getScreenshot();
		LogUtil.d("save screenShot");
		String path = ScreenUtil.saveScreenshot(mPuzzleView.getScreenWidth(),
				mPuzzleView.getScreenHeight(), mParent, bmp);
		if ((bmp != null) && (!bmp.isRecycled())) {
			bmp.recycle();
			bmp = null;
		}
		return path;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// intent.setDataAndType(Uri.parse("file://" + result), "image/*");
		// mParent.startActivity(Intent.createChooser(intent, result));
		Intent intent = new Intent(mParent, PreviewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(MessageUtils.KEY_FILE_PATH, result);
		bundle.putBoolean(MessageUtils.KEY_IS_PREVIEW, true);
		intent.putExtras(bundle);
		mParent.startActivityForResult(intent, MessageUtils.CODE_PREVIEW_IMG);
	}
}
