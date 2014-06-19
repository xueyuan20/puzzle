package xy.game.puzzle.logic;

/**
 * CustomGestureListener, listen to gesture action.
 * 
 * @author 80070307
 * 
 */
public interface CustomGestureListener {

	/**
	 * called when customer scrolls view from right to left.
	 */
	public void onScrollToLeft();

	/**
	 * called when customer scrolls view from left to right.
	 */
	public void onScrollToRight();

	/**
	 * called when customer scrolls view from down to up.
	 */
	public void onScrollToUp();

	/**
	 * called when customer scrolls view from up to down.
	 */
	public void onScrollToDown();

	/**
	 * called when customer clicks on view for a long time.
	 */
	public void onLongClick();
}
