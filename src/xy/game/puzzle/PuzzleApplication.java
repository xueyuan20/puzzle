package xy.game.puzzle;

import xy.game.puzzle.logic.InitialDataTask;
import xy.game.puzzle.logic.PuzzleProvider;
import android.app.Application;
import android.content.Context;

public class PuzzleApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Context context = getApplicationContext();
		PuzzleProvider.getInstance(context);

		InitialDataTask task = new InitialDataTask(context);
		task.execute();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

}
