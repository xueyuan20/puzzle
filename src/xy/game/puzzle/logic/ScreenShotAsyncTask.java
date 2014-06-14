package xy.game.puzzle.logic;

import xy.game.puzzle.util.ScreenUtil;
import xy.game.puzzle.view.PuzzleSurfaceView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

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
		String path =  ScreenUtil.saveScreenshot(mPuzzleView.getScreenWidth(),
				mPuzzleView.getScreenHeight(), mParent,
				bmp);
		if ((bmp!=null) && (!bmp.isRecycled()) ) {
			bmp.recycle();
			bmp = null;
		}
		return path;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + result), "image/*");
			mParent.startActivity(Intent.createChooser(intent, result));
		}
	}
}
