package xy.game.puzzle.view;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.ScreenUtil;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class PreviewSurfaceView extends SurfaceView implements Callback,
		Runnable {
	private final String BG_RES_ID = "backgroundResId";
	private final String LEVEL = "puzzleLevel";
	private final String USE_DEFAULT = "userDefaultBmp";

	private Canvas mCanvas;
	private Paint mPaint;
	private SurfaceHolder mHolder;

	private Canvas mClipCanvas;
	private Bitmap mClipBitmap;
	/**
	 * ∆¥∞Â«¯”Ú
	 */
	private Rect mPuzzleRect;
	private Resources mRes;
	private Context mContext;
	private boolean mUpdateEnable;

	private static int mScreenHeight;
	private static int mScreenWidth;

	private int mBackgroudResId = -1;

	/**
	 * Grid count.
	 */
	private int mPuzzleSize = 3;

	/**
	 * Bitmap showed by SurfaceView.
	 */
	private Bitmap mBackgroundBmp;

	/**
	 * Use default bitmap.
	 */
	private boolean mUseDefaultBmp;

	/**
	 * Is preview mode or not.
	 */
	private boolean mIsPreviewMode;

	public PreviewSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initSurfaceView(context);
	}

	public PreviewSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initSurfaceView(context);
		mPuzzleSize += (attrs.getAttributeResourceValue(null, LEVEL, 0) % 3);
		mBackgroudResId = attrs.getAttributeResourceValue(null, BG_RES_ID, 0);

		mUseDefaultBmp = attrs.getAttributeResourceValue(null, USE_DEFAULT, 1) > 0;
	}

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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (mUpdateEnable) {
			drawViews();
			long start = System.currentTimeMillis();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 100) {
					Thread.sleep(100 - (end - start));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	private void logic() {
		// TODO Auto-generated method stub

	}

	private void drawViews() {
		// TODO Auto-generated method stub
		try {
			mCanvas = mHolder.lockCanvas();

			if (mCanvas != null) {
				mCanvas.drawColor(Color.DKGRAY);
				if (!mIsPreviewMode) {
					if ((mBackgroundBmp == null)
							|| (mBackgroundBmp.isRecycled())) {
						useDefaultImageBitmap();
					}
					mPaint.setColor(Color.WHITE);
					mPaint.setAlpha(0xFF);
					if (mBackgroundBmp.getHeight() != mScreenHeight) {
						mCanvas.drawBitmap(
								mBackgroundBmp,
								(mScreenWidth - mBackgroundBmp.getWidth()) / 2,
								(mPuzzleRect.top + mPuzzleRect.bottom - mBackgroundBmp
										.getHeight()) / 2, mPaint);
					} else {
						mCanvas.drawBitmap(mBackgroundBmp, 0, 0, mPaint);
					}

					// ªÊ÷∆∆¥∞Â±≥æ∞≤√ºÙøÚ
					mPaint.setColor(Color.DKGRAY);
					mPaint.setAlpha(0x88);
					mPaint.setStyle(Style.FILL);
					mCanvas.drawRect(0, 0, getWidth(), mPuzzleRect.top, mPaint);
					mCanvas.drawRect(0, mPuzzleRect.top, mPuzzleRect.left,
							getHeight(), mPaint);
					mCanvas.drawRect(mPuzzleRect.right, mPuzzleRect.top,
							getWidth(), getHeight(), mPaint);
					mCanvas.drawRect(mPuzzleRect.left, mPuzzleRect.bottom,
							mPuzzleRect.right, getHeight(), mPaint);
					int div = mPuzzleRect.width() / mPuzzleSize;
					int left = mPuzzleRect.left;
					int top = mPuzzleRect.top;
					for (int i = 0; i < mPuzzleSize + 1; i++) {
						mCanvas.drawLine(left, top + i * div, left
								+ mPuzzleRect.width(), top + i * div, mPaint);
						mCanvas.drawLine(left + i * div, top, left + i * div,
								top + mPuzzleRect.width(), mPaint);
					}
				} else {
					if ((mBackgroundBmp != null)
							&& (!mBackgroundBmp.isRecycled())) {
						mCanvas.drawBitmap(mBackgroundBmp, 0, 0, mPaint);
					} else {
						mPaint.setTextSize(30);
						mPaint.setColor(Color.WHITE);
						mCanvas.drawText("Not Found", getWidth() / 2,
								getHeight() / 2, mPaint);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.printCodeStack(e);
		} finally {
			mPaint.reset();
			if (mCanvas != null) {
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		mScreenHeight = getHeight();
		mScreenWidth = getWidth();
		LogUtil.e("width = " + mScreenWidth + "; height = " + mScreenHeight);

		int borderTop = (int) mRes.getDimension(R.dimen.top_border_height);
		int borderLeft = (int) mRes.getDimension(R.dimen.left_border_width);

		int puzzleHeightMax = mScreenHeight - 2 * borderTop;
		int puzzleWidthMax = mScreenWidth - 2 * borderLeft;

		int puzzleWidth = (puzzleHeightMax > puzzleWidthMax) ? puzzleWidthMax
				: puzzleHeightMax;
		mPuzzleRect = new Rect(borderLeft, borderTop, borderLeft + puzzleWidth,
				borderTop + puzzleWidth);

		mClipBitmap = Bitmap.createBitmap(mPuzzleRect.width(),
				mPuzzleRect.height(), Config.ARGB_8888);
		mClipCanvas = new Canvas(mClipBitmap);

		// ≥ı ºªØ
		mUpdateEnable = true;
		initParams();
		(new Thread(this)).start();
	}

	private void initParams() {
		// TODO Auto-generated method stub
		switch (PuzzleProvider.getInstance(mContext).getGameLevel()) {
		case 0:
			mPuzzleSize = 3;
			mBackgroudResId = R.drawable.preview_33;
			break;

		case 1:
			mPuzzleSize = 4;
			mBackgroudResId = R.drawable.preview_44;
			break;

		case 2:
			mPuzzleSize = 5;
			mBackgroudResId = R.drawable.preview_55;
			break;

		default:
			mPuzzleSize = 3;
			mBackgroudResId = R.drawable.preview_33;
			break;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		mUpdateEnable = false;
		if (mBackgroundBmp != null && !mBackgroundBmp.isRecycled()) {
			mBackgroundBmp.recycle();
			mBackgroundBmp = null;
		}
		if (mClipBitmap != null && !mClipBitmap.isRecycled()) {
			mClipBitmap.recycle();
			mClipBitmap = null;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	public void setBitmap(Bitmap bmp) {
		if (mIsPreviewMode) {
			mBackgroundBmp = bmp;
		} else {
			if (mUseDefaultBmp) {
				initParams();
				useDefaultImageBitmap();
			} else {
				if (!setImageBitmap(bmp)) {
					mUseDefaultBmp = false;
				}
			}
		}
	}

	public void setImageAsBackground(boolean userDefault, Bitmap bmp) {
		mUseDefaultBmp = userDefault;
		setBitmap(bmp);
	}

	public void setBackgroundByPath(String customBkPath) {
		// TODO Auto-generated method stub
		setImageAsBackground(false,
				ScreenUtil.getBitmapByWidth(customBkPath, mScreenWidth, 0));
	}

	private void useDefaultImageBitmap() {
		LogUtil.e("Screen Width = " + mScreenWidth);
		setImageBitmap(ScreenUtil.getBitmap(mRes, mBackgroudResId,
				mScreenWidth, 0));
	}

	private boolean setImageBitmap(Bitmap bmp) {
		if (bmp == null || bmp.isRecycled()) {
			return false;
		}
		if (mBackgroundBmp != null && !mBackgroundBmp.isRecycled()) {
			mBackgroundBmp.recycle();
			mBackgroundBmp = null;
		}
		mBackgroundBmp = bmp;
		return true;
	}

	public void setPreviewMode(boolean isPreview) {
		mIsPreviewMode = isPreview;
	}

	public void showBrokenIcon() {
		// TODO Auto-generated method stub
		if (mBackgroundBmp != null && !mBackgroundBmp.isRecycled()) {
			mBackgroundBmp.recycle();
			mBackgroundBmp = null;
		}
	}

	public Bitmap getBackgroundBmp() {
		Bitmap bmp = null;
		if (mBackgroundBmp != null && !mBackgroundBmp.isRecycled()) {
			bmp = Bitmap.createBitmap(mBackgroundBmp, mPuzzleRect.left,
					mPuzzleRect.top, mPuzzleRect.right, mPuzzleRect.bottom);
		}
		return bmp;
	}

}
