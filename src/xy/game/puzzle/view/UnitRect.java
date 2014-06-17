package xy.game.puzzle.view;

import android.graphics.Rect;

/**
 * Unit Rect Class,
 * @author 80070307
 *
 */
public class UnitRect{
	private Rect mRect;

	public UnitRect(Rect rect){
		mRect = new Rect();
		mRect.left = rect.left;
		mRect.top = rect.top;
		mRect.right = rect.right;
		mRect.bottom = mRect.bottom;
	}

	public UnitRect(int left, int top, int right, int bottom){
		mRect = new Rect(left, top, right, bottom);
	}

	public Rect getRect(){
		return mRect;
	}

	public int getCenterX(){
		return (mRect.right+mRect.left)/2;
	}

	public int getCenterY(){
		return (mRect.top+mRect.bottom)/2;
	}

	public int left(){
		return mRect.left;
	}

	public int right(){
		return mRect.right;
	}

	public int top(){
		return mRect.top;
	}

	public int bottom(){
		return mRect.bottom;
	}
}
