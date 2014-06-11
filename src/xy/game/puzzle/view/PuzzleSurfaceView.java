package xy.game.puzzle.view;

import xy.game.puzzle.R;
import xy.game.puzzle.activity.MainActivity;
import xy.game.puzzle.logic.InitPuzzleTask;
import xy.game.puzzle.logic.Processor;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.MessageUtils;
import xy.game.puzzle.util.Position;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class PuzzleSurfaceView extends SurfaceView implements Callback,
		Runnable {
	private Bitmap mBitmap;

	/**
	 * 难度级别
	 * @author 80070307
	 *
	 */
	public enum DIFFICULTY_LEVEL{
		SIMPLE,
		MEDIUM,
		DIFFICULT,
	}
	private Context mContext;
	private SurfaceHolder mHolder;
	
	private Resources mRes;

	/**
	 * 画布和画笔
	 */
	private Canvas mCanvas;
	private Paint mPaint;

	/**
	 * 页面刷新开关标识
	 */
	private boolean mUpdateEnable = true;

	/**
	 * 屏幕宽度和高度
	 */
	private int mScreenWidth, mScreenHeight;

	/**
	 * 小拼块尺寸和间隔
	 */
	private int mGridSize,mGridBorder;

	/**
	 * 拼板区域
	 */
	private Rect mPuzzleRect;

	/**
	 * 拼板规格
	 */
	private int mPuzzleSize = 3;

	/**
	 * 画笔数组
	 */
	private Bitmap[] mBmpPaint;

	/**
	 * 记录画板状态数组
	 */
	private int[] mUnitIndexArray;
	private UnitRect[] mUnitRectArray;

	private int mSpaceIndex;
	private int mStepCount;
	private int mTimerCount;

	/**
	 * Lock Flag
	 */
	private boolean mDataLockFlag = false;

	/**
	 * 构造方法
	 * @param context
	 */
	public PuzzleSurfaceView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		initSurfaceView(context);
	}

	public PuzzleSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initSurfaceView(context);
	}

	/**
	 * 初始化控件
	 * @param context
	 */
	private void initSurfaceView(Context context) {
		// TODO Auto-generated method stub
		mContext = context;
		mHolder = getHolder();
		mHolder.addCallback(this);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		setFocusable(true);
		mRes = getResources();
	}

	/**
	 * 刷新页面
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (mUpdateEnable ) {
			drawViews();
			long start = System.currentTimeMillis();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end-start < 50) {
					Thread.sleep(50- (end - start));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * deal with something after update view.
	 */
	private void logic() {
		// TODO Auto-generated method stub
//		LogUtil.e("Unsupported.");
	}

	/**
	 * draw views on the surfaceView.
	 * ???? Differences between SurfaceView and view
	 */
	private void drawViews() {
		// TODO Auto-generated method stub
		try {
			mCanvas = mHolder.lockCanvas();
			Canvas canvas = new Canvas(mBitmap);
			if (mCanvas != null) {
				// 绘制背景
				mCanvas.drawColor(Color.argb(0xFF, 0xF8, 0xF3, 0xD1));// 米白色背景
				canvas.drawColor(Color.argb(0xFF, 0xF8, 0xF3, 0xD1));// 米白色背景
				// 绘制拼板
				mPaint.setColor(Color.DKGRAY);
				mPaint.setAlpha(0x55);
				mPaint.setStyle(Style.FILL);
				mCanvas.drawRect(mPuzzleRect, mPaint);
				canvas.drawRect(mPuzzleRect, mPaint);

				// 绘制图片网格
				mPaint.setColor(Color.argb(0x55, 0xF8, 0xF3, 0xD1));
				mPaint.setStyle(Style.FILL);
				for (int i = 0; i < mUnitRectArray.length; i++) {
					mCanvas.drawRect(mUnitRectArray[i].getRect(), mPaint);
					canvas.drawRect(mUnitRectArray[i].getRect(), mPaint);
				}

				// 绘制number
				if (mUnitIndexArray != null && !mDataLockFlag) {
					mPaint.setTextAlign(Align.CENTER);
					mPaint.setTextSize(mRes.getDimension(R.dimen.font_zise));
					mPaint.setStyle(Style.FILL);
					mPaint.setColor(Color.WHITE);
					for (int i = 0; i < mUnitIndexArray.length; i++) {
						if (mUnitIndexArray[i]>0) {
							mCanvas.drawText(String.valueOf(mUnitIndexArray[i]),
									mUnitRectArray[i].getRect().centerX(),
									mUnitRectArray[i].getRect().centerY()
									+ mPaint.getTextSize()/3,
									mPaint);
							canvas.drawText(String.valueOf(mUnitIndexArray[i]),
									mUnitRectArray[i].getRect().centerX(),
									mUnitRectArray[i].getRect().centerY()
									+ mPaint.getTextSize()/3,
									mPaint);
						}
					}

					// 绘制图块
					if (mBmpPaint!=null && mBmpPaint.length>0) {
//						LogUtil.e("Unsurportted!");
					}
				}
				

			}
			canvas = null;
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.printCodeStack(e);
		} finally {
			if (mCanvas != null) {
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1,
			int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mScreenWidth = getWidth();
		mScreenHeight = getHeight();
		mBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight,
				Config.ARGB_8888);
		// 初始化拼板
		initPuzzle(false);
		mUpdateEnable = true;
		(new Thread(this)).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mUpdateEnable = false;
		if (mBitmap!=null) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}

	/**
	 * 处理用户动作
	 */
	private Position mMotionStartPos;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mMotionStartPos = new Position(event.getX(), event.getY());
			break;

		case MotionEvent.ACTION_MOVE:
			
			break;

		case MotionEvent.ACTION_UP:
			if (mMotionStartPos == null || mDataLockFlag) {
				break;
			}
			Position endPos = new Position(event.getX(), event.getY());
			switch (checkDir(mMotionStartPos, endPos)) {
			case UP:
				if (mSpaceIndex<mPuzzleSize*(mPuzzleSize-1)) {
					mUnitIndexArray[mSpaceIndex] = mUnitIndexArray[mSpaceIndex+mPuzzleSize];
					mUnitIndexArray[mSpaceIndex+mPuzzleSize] = 0;
					mSpaceIndex = mSpaceIndex+mPuzzleSize;
					mStepCount++;
					updateSteps(mStepCount);
				} else {
					LogUtil.e("Cannot Move from bottom!");
				}
				break;

			case DOWN:
				if (mSpaceIndex<mPuzzleSize) {
					LogUtil.e("Cannot Move from above!");
				} else {
					mUnitIndexArray[mSpaceIndex] = mUnitIndexArray[mSpaceIndex-mPuzzleSize];
					mUnitIndexArray[mSpaceIndex-mPuzzleSize] = 0;
					mSpaceIndex = mSpaceIndex-mPuzzleSize;
					mStepCount++;
					updateSteps(mStepCount);
				}
				break;

			case LEFT:
				if (mSpaceIndex%mPuzzleSize+1==mPuzzleSize) {
					LogUtil.e("Cannot Move from right");
				} else {
					mUnitIndexArray[mSpaceIndex] = mUnitIndexArray[mSpaceIndex+1];
					mUnitIndexArray[mSpaceIndex+1] = 0;
					mSpaceIndex = mSpaceIndex+1;
					mStepCount++;
					updateSteps(mStepCount);
				}
				break;

			case RIGHT:
				if (mSpaceIndex%mPuzzleSize > 0) {
					mUnitIndexArray[mSpaceIndex] = mUnitIndexArray[mSpaceIndex-1];
					mUnitIndexArray[mSpaceIndex-1] = 0;
					mSpaceIndex = mSpaceIndex-1;
					mStepCount++;
					updateSteps(mStepCount);
				} else {
					LogUtil.e("Cannot Move from left");
				}
				break;

			case UNKNOWN:
				break;
			default:
				break;
			}
			mMotionStartPos = null;
			if (Processor.isOrder(mUnitIndexArray)) {
				String content = String.format(mRes.getString(R.string.result_content),
						Processor.caculateScore(mPuzzleSize, mTimerCount, mStepCount),
						String.format("%02d:%02d:%02d", mTimerCount/(60 * 60),
								(mTimerCount / 60) % 60, mTimerCount % 60),
						mStepCount);
				Message msg = new Message();
				msg.what = MessageUtils.MSG_RESULT;
				msg.getData().putString(MessageUtils.KEY_RESULT_CONTENT,
						content);
				((MainActivity)mContext).getHandler().dispatchMessage(msg);
			}
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		PuzzleProvider.getInstance(mContext).saveArray(mUnitIndexArray);
		super.onDetachedFromWindow();
	}

	public void updateSteps(int stepCount){
		mStepCount = stepCount;
		Message msg = new Message();
		msg.what = MessageUtils.MSG_UPDATE_STEPS;
		msg.getData().putInt(MessageUtils.KEY_STEPS, mStepCount);
		((MainActivity)mContext).getHandler().dispatchMessage(msg);
	}

	/**
	 * 设置游戏等级
	 * @param newLevel
	 */
	public void setLevel(DIFFICULTY_LEVEL newLevel){
		switch (newLevel) {
		case SIMPLE:
			mPuzzleSize = 3;
			break;

		case MEDIUM:
			mPuzzleSize = 4;
			break;

		case DIFFICULT:
			mPuzzleSize = 5;
			break;

		default:
			break;
		}
		initPuzzle(true);
	}

	/**
	 * 初始化拼板
	 * @param isNew
	 */
	private void initPuzzle(boolean isNew){
		/**
		 * initialize puzzle size.
		 */
		switch (PuzzleProvider.getInstance(mContext).getGameLevel()) {
		case 0:
			mPuzzleSize = 3;
			break;

		case 1:
			mPuzzleSize = 4;
			break;

		case 2:
			mPuzzleSize = 5;
			break;

		default:
			mPuzzleSize = 3;
			break;
		}

		int puzzleHeightMax, puzzleWidthMax;
		int borderTop = (int)mRes.getDimension(R.dimen.top_border_height);
		int borderLeft = (int)mRes.getDimension(R.dimen.left_border_width);
		mGridBorder = 2*Math.abs((int)mRes.getDimension(R.dimen.puzzle_grid_border)
				-mPuzzleSize);

		puzzleHeightMax = mScreenHeight-2*borderTop;
		puzzleWidthMax = mScreenWidth - 2*borderLeft;
		mGridSize = puzzleHeightMax < puzzleWidthMax ?
				(puzzleHeightMax-mGridBorder*(mPuzzleSize+1))/mPuzzleSize : 
				(puzzleWidthMax-mGridBorder*(mPuzzleSize+1))/mPuzzleSize;

		/**
		 * 初始化拼板区域
		 */
		if (mPuzzleRect == null) {
			mPuzzleRect = new Rect();
		}

		mPuzzleRect.set(borderLeft, borderTop,
				borderLeft+mGridSize*mPuzzleSize+mGridBorder*(mPuzzleSize+1),
				borderTop+mGridSize*mPuzzleSize+mGridBorder*(mPuzzleSize+1));

		/**
		 * 初始化拼板图片块
		 */
		mUnitRectArray = new UnitRect[mPuzzleSize*mPuzzleSize];
		int left, top, rectInd;
		for (int i = 0; i < mPuzzleSize; i++) {
			left = mPuzzleRect.left + mGridBorder;
			top = mPuzzleRect.top + mGridBorder + i*(mGridSize + mGridBorder);
			for (int j = 0; j < mPuzzleSize; j++) {
				rectInd = i*mPuzzleSize+j;
				mUnitRectArray[rectInd] = new UnitRect(left, top,
						left + mGridSize,top + mGridSize);
				left = left + mGridSize + mGridBorder;
			}
		}

		/**
		 * 初始化图片画笔
		 */
		initBmpPaint();

		/**
		 * 初始化拼图序列
		 */
		if (!isNew) {
			int[] array = PuzzleProvider.getInstance(mContext).getArray();
			if ((array != null) && (array.length==mPuzzleSize*mPuzzleSize)) {
				setIndexArray(array);
			} else {
				isNew = true;
			}
		}
		if (isNew) {
			new InitPuzzleTask(this).execute(mPuzzleSize);
		}

		/**
		 * initialize steps count.
		 */
		updateSteps(0);
		resetTimer();
	}

	public void setIndexArray(int[] array){
		if (array == null) {
			return;
		}
		mUnitIndexArray = new int[array.length];
		mSpaceIndex = 0;
		int spaceCount = 0;
		for (int i = 0; i < array.length; i++) {
			mUnitIndexArray[i] = array[i];
			if (array[i]==0) {
				mSpaceIndex = i;
				spaceCount++;
			}
		}
		if (spaceCount > 1) {
			LogUtil.e("Some Error Happened.");
		}
	}

	public void lockIndexData(boolean enableLock){
		mDataLockFlag = enableLock;
	}

	/**
	 * 初始化画笔
	 */
	private void initBmpPaint(){
		/**
		 * 清理画笔
		 */
		if (mBmpPaint!=null && mBmpPaint.length>0) {
			for (int i = 0; i < mBmpPaint.length; i++) {
				if (mBmpPaint[i]!=null) {
					mBmpPaint[i].recycle();
					mBmpPaint[i] = null;
				}
			}
		}

		/**
		 * 初始化画笔
		 */
		mBmpPaint = new Bitmap[mPuzzleSize*mPuzzleSize-1];
		
	}

	/**
	 * 方向枚举
	 * @author 80070307
	 *
	 */
	enum DIRECTION{
		UNKNOWN,// 未移动
		UP,		// 向上
		DOWN,	// 向下
		LEFT,	// 向左
		RIGHT,	// 向右
	}
	/**
	 * 判断移到方向
	 * @param startPos
	 * @param endPos
	 * @return 方向标识
	 */
	private DIRECTION checkDir(Position startPos, Position endPos){
		float divX, divY;
		divX = endPos.getX() - startPos.getX();
		divY = endPos.getY() - startPos.getY();
		if (Math.abs(divX)<10 && Math.abs(divY)<10) {
			return DIRECTION.UNKNOWN;
		}
		Log.d("XUEYUAN", "check Direction. "+ divX +", "+divY);
		if (Math.abs(divX)>Math.abs(divY)) {
			return (divX < 0) ? DIRECTION.LEFT : DIRECTION.RIGHT;
		} else {
			return (divY < 0) ? DIRECTION.UP : DIRECTION.DOWN;
		}
	}

	/**
	 * Start a new game.
	 */
	public void restart() {
		// TODO Auto-generated method stub
		initPuzzle(true);
	}

	/**
	 * Reset Time Counter.
	 */
	private void resetTimer(){
		mTimerCount = 0;
		((MainActivity)mContext).startTimer();
	}

	/**
	 * Get Timer Count.
	 * @return
	 */
	public int getTimerCount(){
		return mTimerCount;
	}

	/**
	 * Update Timer.
	 */
	public void updateTimerCount(){
		mTimerCount++;
	}

	/**
	 * Get Screen Width
	 * @return
	 */
	public int getScreenWidth(){
		return mScreenWidth;
	}

	/**
	 * Get Screen Height
	 * @return
	 */
	public int getScreenHeight(){
		return mScreenHeight;
	}

	public Bitmap getScreenshot(){
		return mBitmap;
	}
}
