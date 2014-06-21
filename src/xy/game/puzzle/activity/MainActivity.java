package xy.game.puzzle.activity;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.logic.ScreenShotAsyncTask;
import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.MessageUtils;
import xy.game.puzzle.util.ScreenUtil;
import xy.game.puzzle.view.PuzzleSurfaceView;
import xy.game.puzzle.view.PuzzleSurfaceView.DIFFICULTY_LEVEL;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public final class MainActivity extends BaseActivity implements OnClickListener {
	private ImageView mTvMore, mTvHint;
	private TextView mTvSteps, mTvTimer;
	private TextView mTvOperatorHint;
	private ImageView mTvSetLevel, mTvSetBackground, mTvRestart, mTvScreenshot;

	private PuzzleSurfaceView mPuzzleView;

	private Resources mRes;
	private Context mContext;
	private boolean mEnableTimer;
	private String mResultContent = "";
	private PuzzleProvider mProvider;

	/**
	 * Ë«»÷ÍË³öº¯Êý
	 */
	private static long mBackTime = 0;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MessageUtils.MSG_UPDATE_STEPS:
				mTvSteps.setText(String.format(mRes
						.getString(R.string.title_steps),
						msg.getData().getInt(MessageUtils.KEY_STEPS)));
				break;

			case MessageUtils.MSG_RESULT:
				mResultContent = msg.getData().getString(
						MessageUtils.KEY_RESULT_CONTENT);
				new AlertDialog.Builder(MainActivity.this)
						.setTitle(mRes.getString(R.string.result_title))
						.setMessage(mResultContent)
						.setNegativeButton(
								mRes.getString(R.string.menu_title_share),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										ScreenUtil.shareMsg(
												mContext,
												getTitle().toString(),
												mRes.getString(R.string.menu_title_share),
												String.format(
														mRes.getString(R.string.share_content),
														mRes.getString(R.string.app_name),
														mResultContent), null);
									}
								})
						.setPositiveButton(mRes.getString(R.string.ok), null)
						.setIcon(
								getResources().getDrawable(
										R.drawable.ic_launcher)).show();
				break;

			case MessageUtils.MSG_CHANGE_LEVEL:
				int level = msg.getData().getInt(MessageUtils.KEY_GAME_LEVEL);
				mProvider.setGameLevel(level);
				switch (level) {
				case 0:
					mPuzzleView.setLevel(DIFFICULTY_LEVEL.SIMPLE);
					break;

				case 1:
					mPuzzleView.setLevel(DIFFICULTY_LEVEL.MEDIUM);
					break;

				case 2:
					mPuzzleView.setLevel(DIFFICULTY_LEVEL.DIFFICULT);
					break;

				default:
					break;
				}
				break;

			case MessageUtils.MSG_START_TIMER:
				sendEmptyMessageDelayed(MessageUtils.MSG_START_TIMER, 1000);
			case MessageUtils.MSG_INIT_TIMER:
				if (mEnableTimer) {
					int timer = mPuzzleView.getTimerCount();
					mTvTimer.setText(String.format("%02d:%02d:%02d", timer
							/ (60 * 60), (timer / 60) % 60, timer % 60));
					mPuzzleView.updateTimerCount();
				}
				break;

			case MessageUtils.MSG_HIDE_HINT:
				mTvOperatorHint.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		mRes = getResources();
		mContext = this;
		mProvider = PuzzleProvider.getInstance(this);

		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mPuzzleView = (PuzzleSurfaceView) findViewById(R.id.puzzle_surfaceview);
		mPuzzleView.setShowHint(mProvider.checkUseDefaultBk());

		mTvSteps = (TextView) findViewById(R.id.tv_steps);
		mTvSteps.setText(String.format(mRes.getString(R.string.title_steps), 0));

		mTvTimer = (TextView) findViewById(R.id.tv_timer);
		mTvTimer.setText("00:00");

		mTvOperatorHint = (TextView) findViewById(R.id.tv_operate_hint);
		mTvOperatorHint.setVisibility(View.GONE);

		// mTvSetLevel = (TextView) findViewById(R.id.tv_game_level);
		mTvSetLevel = (ImageView) findViewById(R.id.tv_game_level);
		mTvSetLevel.setOnClickListener(this);

		mTvSetBackground = (ImageView) findViewById(R.id.tv_original_background);
		mTvSetBackground.setOnClickListener(this);

		mTvRestart = (ImageView) findViewById(R.id.tv_game_restart);
		mTvRestart.setOnClickListener(this);

		mTvScreenshot = (ImageView) findViewById(R.id.tv_screenshot);
		mTvScreenshot.setOnClickListener(this);

		mTvMore = (ImageView) findViewById(R.id.tv_game_more);
		mTvMore.setOnClickListener(this);

		mTvHint = (ImageView) findViewById(R.id.tv_game_hint);
		mTvHint.setImageResource(mProvider.checkWetherUseHint() ? R.drawable.ic_menu_hint_yes
				: R.drawable.ic_menu_hint_no);
		mTvHint.setOnClickListener(this);

		mHandler.sendEmptyMessage(MessageUtils.MSG_START_TIMER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_original_background:
			startActivity(new Intent(MainActivity.this, PreviewActivity.class));
			break;

		case R.id.tv_game_level:
			new AlertDialog.Builder(MainActivity.this)
					.setTitle(mRes.getString(R.string.title_game_level))
					.setIcon(mRes.getDrawable(R.drawable.ic_settings_level))
					.setSingleChoiceItems(
							mRes.getStringArray(R.array.game_level_choices),
							mProvider.getGameLevel(),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dlg,
										int index) {
									// TODO Auto-generated method stub
									Message msg = new Message();
									msg.what = MessageUtils.MSG_CHANGE_LEVEL;
									msg.getData().putInt(
											MessageUtils.KEY_GAME_LEVEL, index);
									mHandler.dispatchMessage(msg);
									dlg.dismiss();
								}
							}).show();

			break;

		case R.id.tv_screenshot:
			ScreenShotAsyncTask task = new ScreenShotAsyncTask(
					MainActivity.this, mPuzzleView);
			task.execute("");
			break;

		case R.id.tv_game_restart:
			mPuzzleView.restart();
			break;

		case R.id.tv_game_more:
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			break;

		case R.id.tv_game_hint:
			boolean showHint = !mProvider.checkWetherUseHint();
			LogUtil.e("Change whether to show hint: " + showHint);
			mProvider.changeHintType(showHint);
			mTvHint.setImageResource(showHint ? R.drawable.ic_menu_hint_yes
					: R.drawable.ic_menu_hint_no);
			mPuzzleView.setShowHint(showHint);
			break;
		default:
			break;
		}
	}

	/**
	 * ²Ëµ¥¡¢·µ»Ø¼üÏìÓ¦
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long time = System.currentTimeMillis();
			if ((time - mBackTime > 1000) || (time - mBackTime < 50)) {
				mBackTime = time;
				mTvOperatorHint.setVisibility(View.VISIBLE);
				mHandler.sendEmptyMessageDelayed(MessageUtils.MSG_HIDE_HINT,
						1000);
			} else {
				mPuzzleView.savePuzzle();
				finish();
				System.exit(0);
			}
		}
		return false;
	}

	public final Handler getHandler() {
		return mHandler;
	}

	public void startTimer() {
		mEnableTimer = true;
	}

	public void stopTimer() {
		mEnableTimer = false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 0:

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
