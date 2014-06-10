package xy.game.puzzle.view;

import xy.game.puzzle.MainActivity;
import xy.game.puzzle.R;
import xy.game.puzzle.logic.InitPuzzleTask;
import xy.game.puzzle.logic.Processor;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.MessageUtils;
import xy.game.puzzle.util.Position;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
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

	/**
	 * �Ѷȼ���
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
	 * �����ͻ���
	 */
	private Canvas mCanvas;
	private Paint mPaint;

	/**
	 * ҳ��ˢ�¿��ر�ʶ
	 */
	private boolean mUpdateEnable = true;

	/**
	 * ��Ļ��Ⱥ͸߶�
	 */
	private int mScreenWidth, mScreenHeight;

	/**
	 * Сƴ��ߴ�ͼ��
	 */
	private int mGridSize,mGridBorder;

	/**
	 * ƴ������
	 */
	private Rect mPuzzleRect;

	/**
	 * ƴ����
	 */
	private int mPuzzleSize = 3;

	/**
	 * ��������
	 */
	private Bitmap[] mBmpPaint;

	/**
	 * ��¼����״̬����
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
	 * ���췽��
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
	 * ��ʼ���ؼ�
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
	 * ˢ��ҳ��
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
	 */
	private void drawViews() {
		// TODO Auto-generated method stub
		try {
			mCanvas = mHolder.lockCanvas();
			if (mCanvas != null) {
				// ���Ʊ���
				mCanvas.drawColor(Color.argb(0xFF, 0xF8, 0xF3, 0xD1));// �װ�ɫ����
				// ����ƴ��
				mPaint.setColor(Color.DKGRAY);
				mPaint.setAlpha(0x55);
				mPaint.setStyle(Style.FILL);
				mCanvas.drawRect(mPuzzleRect, mPaint);

				// ����ͼƬ����
				mPaint.setColor(Color.argb(0x55, 0xF8, 0xF3, 0xD1));
				mPaint.setStyle(Style.FILL);
				for (int i = 0; i < mUnitRectArray.length; i++) {
					mCanvas.drawRect(mUnitRectArray[i].getRect(), mPaint);
				}

				// ����number
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
						}
					}

					// ����ͼ��
					if (mBmpPaint!=null && mBmpPaint.length>0) {
						LogUtil.e("Unsurportted!");
					}
				}

			}
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
		// ��ʼ��ƴ��
		initPuzzle(false);
		mUpdateEnable = true;
		(new Thread(this)).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mUpdateEnable = false;
	}

	/**
	 * �����û�����
	 */
	private Position mMotionStartPos;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mMotionStartPos = new Position(event.getX(), event.getY());
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
				((MainActivity)mContext).getHandler().sendEmptyMessage(MessageUtils.MSG_RESULT);
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
	 * ������Ϸ�ȼ�
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
	 * ��ʼ��ƴ��
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
		 * ��ʼ��ƴ������
		 */
		if (mPuzzleRect == null) {
			mPuzzleRect = new Rect();
		}

		mPuzzleRect.set(borderLeft, borderTop,
				borderLeft+mGridSize*mPuzzleSize+mGridBorder*(mPuzzleSize+1),
				borderTop+mGridSize*mPuzzleSize+mGridBorder*(mPuzzleSize+1));

		/**
		 * ��ʼ��ƴ��ͼƬ��
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
		 * ��ʼ��ͼƬ����
		 */
		initBmpPaint();

		/**
		 * ��ʼ��ƴͼ����
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
	 * ��ʼ������
	 */
	private void initBmpPaint(){
		/**
		 * ������
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
		 * ��ʼ������
		 */
		mBmpPaint = new Bitmap[mPuzzleSize*mPuzzleSize-1];
		
	}

	/**
	 * ����ö��
	 * @author 80070307
	 *
	 */
	enum DIRECTION{
		UNKNOWN,// δ�ƶ�
		UP,		// ����
		DOWN,	// ����
		LEFT,	// ����
		RIGHT,	// ����
	}
	/**
	 * �ж��Ƶ�����
	 * @param startPos
	 * @param endPos
	 * @return �����ʶ
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

	private void resetTimer(){
		mTimerCount = 0;
		((MainActivity)mContext).startTimer();
	}

	public int getTimerCount(){
		return mTimerCount;
	}

	public void updateTimerCount(){
		mTimerCount++;
	}
}
