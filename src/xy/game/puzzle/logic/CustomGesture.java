package xy.game.puzzle.logic;

import xy.game.puzzle.util.Position;
import android.view.MotionEvent;

/**
 * CustomGesture class, deal with gesture.
 * 
 * @author 80070307
 * 
 */
public class CustomGesture {
	private CustomGestureListener mListener;
	private Position mStartPos;
	private final long PRERESSED_TIME_THRESHOLD = 1500;// in: ms
	private final float SLIDE_DISTANCE_MIN = 10;

	public CustomGesture(CustomGestureListener listener) {
		mListener = listener;
	}

	public void onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mStartPos = new Position(event.getX(), event.getY());
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if ((mStartPos != null) && (mListener != null)) {
				Position endPos = new Position(event.getX(), event.getY());
				// 判断位置
				float divX = endPos.getX() - mStartPos.getX();
				float divY = endPos.getY() - mStartPos.getY();
				if (Math.max(Math.abs(divX), Math.abs(divY)) > SLIDE_DISTANCE_MIN) {
					// 滑动事件
					if (Math.abs(divX) > Math.abs(divY)) {
						// 水平方向滑动
						if (divX > 0) {
							mListener.onScrollToRight();
						} else {
							mListener.onScrollToLeft();
						}
					} else {
						// 垂直方向滑动
						if (divY > 0) {
							mListener.onScrollToDown();
						} else {
							mListener.onScrollToUp();
						}
					}
				} else {
					long downTime = event.getDownTime();
					if (downTime > PRERESSED_TIME_THRESHOLD) {
						// 长按操作
						mListener.onLongClick();
					}
				}
			}
			reset();
		}
	}

	public void reset() {
		mStartPos = null;
	}
}
