package xy.game.puzzle.activity;

import xy.game.puzzle.logic.CustomGesture;
import xy.game.puzzle.logic.CustomGestureListener;
import xy.game.puzzle.util.LogUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class SlideBaseActivity extends Activity implements
		CustomGestureListener {
	private CustomGesture mGesture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 mGesture = new CustomGesture(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		LogUtil.d("[debug] set content view...");
		super.setContentView(layoutResID);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mGesture.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public void onScrollToLeft() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollToRight() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onScrollToUp() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollToDown() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLongClick() {
		// TODO Auto-generated method stub
		LogUtil.d("[debug] onLongClick...");
	}
}
