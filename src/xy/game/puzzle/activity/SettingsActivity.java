package xy.game.puzzle.activity;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.util.StorageUtil;
import xy.game.puzzle.view.CustomEditDialog;
import xy.game.puzzle.view.CustomIconView;
import xy.game.puzzle.view.CustomTitleBar;
import xy.game.puzzle.view.CustomTitleBar.OnBackClicked;
import xy.game.puzzle.view.CustomToast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public final class SettingsActivity extends SlideBaseActivity implements
		OnClickListener, OnBackClicked {
	private Context mContext;
	private CustomIconView mTvGameLevel, mTvScoreTops, mTvShowNoHint;
	private CustomIconView mTvSetUserName, mTvClearCache;
	private CustomIconView mTvUpdate, mTvFeedback;
	private CustomIconView mTvAppdesc;
	private CustomToast mCustomToast;
	private Resources mRes;
	private PuzzleProvider mProvider;
	private FeedbackAgent mFbAgent;
	private UpdateResponse appUpdateResp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mRes = getResources();
		setContentView(R.layout.activity_settings);
		mContext = this;
		mProvider = PuzzleProvider.getInstance(mContext);

		((CustomTitleBar) findViewById(R.id.title)).setListener(this);

		mTvSetUserName = (CustomIconView) findViewById(R.id.tv_set_username);
		mTvSetUserName.setOnClickListener(this);
		if (mProvider.getUserName() != null) {
			mTvSetUserName.setValueContent(mProvider.getUserName());
		}

		mTvGameLevel = (CustomIconView) findViewById(R.id.tv_set_level);
		mTvGameLevel.setOnClickListener(this);
		initGameLevelValue(mProvider.getGameLevel());

		mTvScoreTops = (CustomIconView) findViewById(R.id.tv_top_score);
		mTvScoreTops.setOnClickListener(this);

		mTvShowNoHint = (CustomIconView) findViewById(R.id.tv_set_numhint);
		mTvShowNoHint.setOnClickListener(this);
		initShowNoHint(mProvider.checkWetherUseHint());

		mTvClearCache = (CustomIconView) findViewById(R.id.tv_clearcache);
		mTvClearCache.setOnClickListener(this);

		mTvFeedback = (CustomIconView) findViewById(R.id.tv_feedback);
		mTvFeedback.setOnClickListener(this);
		mTvAppdesc = (CustomIconView) findViewById(R.id.tv_app_desc);
		mTvAppdesc.setOnClickListener(this);

		mTvUpdate = (CustomIconView) findViewById(R.id.tv_update);
		mTvUpdate.setOnClickListener(this);

		mCustomToast = (CustomToast) findViewById(R.id.customToast);
		mCustomToast.setOnClickListener(this);

		mFbAgent = new FeedbackAgent(mContext);
		/**
		 * 若调用该接口，反馈模块将在你程序启动后于后台检查是否有新的来自开发者的回复。
		 * 若有，我们将在通知栏提醒用户，若无，则不会打扰用户。你也可以选择不调用该接口，这样我们会在用户进入反馈界面后，再去检查是否存在新的回复。
		 * 如果你希望改变默认通知方式， 可以使用接口
		 * 
		 * agent.getDefaultConversation().sync(listener);
		 */
		mFbAgent.sync();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_set_username:
			// 修改用户名
			CustomEditDialog dlg = new CustomEditDialog(mContext);
			dlg.setDialogTitle(mRes.getString(R.string.title_set_username));
			String userName = mProvider.getUserName() == null ? mRes
					.getString(R.string.default_username) : mProvider
					.getUserName();
			dlg.setContent(userName);
			dlg.setListener(new CustomEditDialog.OnDialogClicked() {

				@Override
				public void onClickOk(String input) {
					// TODO Auto-generated method stub
					mProvider.setUserName(input);
					mTvSetUserName.setValueContent(input);
				}

				@Override
				public void onClickCancle() {
					// TODO Auto-generated method stub
					// Not deal with anything.
				}
			});
			dlg.show();
			break;
		case R.id.tv_set_level:
			new AlertDialog.Builder(mContext)
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
									mProvider.setGameLevel(index);
									initGameLevelValue(index);
									dlg.dismiss();
								}
							}).show();
			break;
		case R.id.tv_top_score:
			// not realize.
			startActivity(new Intent(mContext, TopsActivity.class));
			break;
		case R.id.tv_set_numhint:
			boolean showHint = !mProvider.checkWetherUseHint();
			initShowNoHint(showHint);
			mProvider.changeHintType(showHint);
			break;
		case R.id.tv_feedback:
			mFbAgent.startFeedbackActivity();
			break;

		case R.id.tv_app_desc:
			startActivity(new Intent(mContext, UserAgreementActivity.class));
			break;
		// 检查更新
		case R.id.tv_update:
			mCustomToast.setText(mRes.getString(R.string.update_checking));
			mCustomToast.setVisibility(View.VISIBLE);
			/**
			 * set false if you want to handle the update result by yourself.
			 * and add a UmengUpdateListener() like below
			 **/
			UmengUpdateAgent.setUpdateAutoPopup(true);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int updateState,
						UpdateResponse arg1) {
					// TODO Auto-generated method stub
					appUpdateResp = arg1;

					switch (updateState) {
					case UpdateStatus.Yes: // has update
						mCustomToast.setVisibility(View.GONE);
						UmengUpdateAgent.showUpdateDialog(mContext,
								appUpdateResp);
						break;
					case UpdateStatus.No: // has no update
						mCustomToast.setText(mRes
								.getString(R.string.update_no_new));
						break;
					case UpdateStatus.NoneWifi: // none wifi
						mCustomToast.setText("no wifi");
						break;
					case UpdateStatus.Timeout: // time out
						mCustomToast.setText("time out");
						break;
					}
				}
			});

			/**
			 * Listen to the update events.
			 */
			UmengUpdateAgent.update(mContext);
			break;

		case R.id.tv_clearcache:
			new AlertDialog.Builder(mContext)
					.setTitle(mRes.getString(R.string.title_clear_cache))
					.setMessage(mRes.getString(R.string.clear_cache_hint))
					.setNegativeButton(mRes.getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									StorageUtil.getInstance().clearCache();
								}
							})
					.setPositiveButton(mRes.getString(R.string.cancle), null)
					.setIcon(
							getResources().getDrawable(
									android.R.drawable.alert_light_frame))
					.show();
			break;

		case R.id.customToast:
			mCustomToast.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private void initGameLevelValue(int levelInd) {
		switch (levelInd) {
		case 0:
			mTvGameLevel.setValueContent(mRes
					.getString(R.string.game_level_values_simple));
			break;

		case 1:
			mTvGameLevel.setValueContent(mRes
					.getString(R.string.game_level_values_medium));
			break;

		case 2:
			mTvGameLevel.setValueContent(mRes
					.getString(R.string.game_level_values_difficult));
			break;

		default:
			mTvGameLevel.setValueContent("");
			break;
		}
	}

	private void initShowNoHint(boolean checkWetherUseHint) {
		// TODO Auto-generated method stub

		if (checkWetherUseHint) {
			mTvShowNoHint
					.setValueBkImg(android.R.drawable.checkbox_on_background);
		} else {
			mTvShowNoHint
					.setValueBkImg(android.R.drawable.checkbox_off_background);
		}
	}

	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		finish();
	}

	
}

