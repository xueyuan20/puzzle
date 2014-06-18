package xy.game.puzzle.logic;

import xy.game.puzzle.R;
import xy.game.puzzle.util.StorageUtil;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class InitialDataTask extends AsyncTask<String, String, String> {
	private Context mContext;
	private int[] mDefaultBmpId;

	public InitialDataTask(Context context) {
		mContext = context;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mDefaultBmpId = new int[] { R.drawable.background_33,
				R.drawable.background_44, R.drawable.background_55 };
	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		Resources res = mContext.getResources();
		for (int i = 0; i < StorageUtil.DEFAULT_BK_NAME.length; i++) {
			if (!StorageUtil.isCacheFileExist(StorageUtil.DEFAULT_BK_NAME[i])) {
				StorageUtil.saveCacheFile(
						BitmapFactory.decodeResource(res, mDefaultBmpId[i]),
						StorageUtil.DEFAULT_BK_NAME[i]);
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
}
