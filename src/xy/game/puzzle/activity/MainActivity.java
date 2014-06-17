package xy.game.puzzle.activity;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.logic.ScreenShotAsyncTask;
import xy.game.puzzle.util.MessageUtils;
import xy.game.puzzle.util.ScreenUtil;
import xy.game.puzzle.view.PuzzleSurfaceView;
import xy.game.puzzle.view.PuzzleSurfaceView.DIFFICULTY_LEVEL;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private TextView mTvOriginalBmp;
	private TextView mTvSteps, mTvTimer;
	private TextView mTvSetLevel, mTvSetBg, mTvRestart, mTvSettings;

	private PuzzleSurfaceView mPuzzleView;

	private Resources mRes;
	private Context mContext;
	private boolean mEnableTimer;
	private String mResultContent = "";

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
				PuzzleProvider.getInstance(MainActivity.this).setGameLevel(
						level);
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

		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mPuzzleView = (PuzzleSurfaceView) findViewById(R.id.puzzle_surfaceview);

		mTvOriginalBmp = (TextView) findViewById(R.id.tv_original_background);
		mTvOriginalBmp.setOnClickListener(this);

		mTvSteps = (TextView) findViewById(R.id.tv_steps);
		mTvSteps.setText(String.format(mRes.getString(R.string.title_steps), 0));

		mTvTimer = (TextView) findViewById(R.id.tv_timer);
		mTvTimer.setText("00:00");

		mTvSetLevel = (TextView) findViewById(R.id.tv_game_level);
		mTvSetLevel.setOnClickListener(this);

		mTvSetBg = (TextView) findViewById(R.id.tv_screenshot);
		mTvSetBg.setOnClickListener(this);

		mTvRestart = (TextView) findViewById(R.id.tv_game_restart);
		mTvRestart.setOnClickListener(this);

		mTvSettings = (TextView) findViewById(R.id.tv_game_settings);
		mTvSettings.setOnClickListener(this);

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
					.setIcon(mRes.getDrawable(R.drawable.selector_game_level))
					.setSingleChoiceItems(
							mRes.getStringArray(R.array.game_level_choices),
							PuzzleProvider.getInstance(this).getGameLevel(),
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

		case R.id.tv_game_settings:
			startActivity(new Intent(MainActivity.this, AboutActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	public final Handler getHandler() {
		return mHandler;
	}

	class GameTimer extends AsyncTask<String, String, String> {
		int mTimerCount;
		TextView mTimer;

		public GameTimer(TextView tvTimer) {
			// TODO Auto-generated constructor stub
			this.mTimer = tvTimer;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mTimerCount = 0;
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

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
		case MessageUtils.CODE_PREVIEW_IMG:

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
