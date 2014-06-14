package xy.game.puzzle.logic;

import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.view.PuzzleSurfaceView;
import android.os.AsyncTask;

public class InitPuzzleTask extends AsyncTask<Integer, String, String>{
	private int[] mArray;
	private PuzzleSurfaceView mPuzzleView;
	public InitPuzzleTask(PuzzleSurfaceView view){
		this.mPuzzleView = view;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mPuzzleView.lockIndexData(true);
	}

	@Override
	protected String doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		mArray = Processor.randomRequences(params[0]);
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		for (int i = 0; i < values.length; i++) {
			LogUtil.d(values[i]);
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mPuzzleView.setIndexArray(this.mArray);
		mPuzzleView.resetScoreCounter();
		mPuzzleView.lockIndexData(false);

		mPuzzleView.savePuzzle();
	}

}
