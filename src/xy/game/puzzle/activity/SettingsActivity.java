package xy.game.puzzle.activity;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.StorageUtil;
import xy.game.puzzle.view.CustomToast;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public final class SettingsActivity extends SlideBaseActivity implements
		OnClickListener {
	private Context mContext;
	private TextView mTvGameLevel, mTvScoreTops, mTvShowNoHint, mTvClearCache;
	private TextView mTvUpdate, mTvFeedback, mTvAppdesc;
	private CustomToast mCustomToast;
	private Resources mRes;
	private PuzzleProvider mProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mRes = getResources();
		setContentView(R.layout.activity_settings);
		mContext = this;
		mProvider = PuzzleProvider.getInstance(mContext);
		mTvGameLevel = (TextView) findViewById(R.id.tv_set_level);
		mTvGameLevel.setOnClickListener(this);
		mTvScoreTops = (TextView) findViewById(R.id.tv_top_score);
		mTvScoreTops.setOnClickListener(this);
		mTvShowNoHint = (TextView) findViewById(R.id.tv_set_numhint);
		mTvShowNoHint.setOnClickListener(this);
		mTvClearCache = (TextView) findViewById(R.id.tv_clearcache);
		mTvClearCache.setOnClickListener(this);

		mTvFeedback = (TextView) findViewById(R.id.tv_feedback);
		mTvFeedback.setOnClickListener(this);
		mTvAppdesc = (TextView) findViewById(R.id.tv_app_desc);
		mTvAppdesc.setOnClickListener(this);

		mTvUpdate = (TextView) findViewById(R.id.tv_update);
		mTvUpdate.setOnClickListener(this);
		mTvUpdate.setText(mRes.getString(R.string.update));

		mCustomToast = (CustomToast) findViewById(R.id.customToast);
		mCustomToast.setOnClickListener(this);


	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
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
									dlg.dismiss();
								}
							}).show();
			break;
		case R.id.tv_top_score:
			// not realize.
			break;
		case R.id.tv_set_numhint:
			boolean showHint = !mProvider.checkWetherUseHint();
			LogUtil.e("Change whether to show hint: " + showHint);
			mProvider.changeHintType(showHint);
			break;
		case R.id.tv_feedback:
			break;

		case R.id.tv_app_desc:
			startActivity(new Intent(mContext, UserAgreementActivity.class));
			break;
		// ¼ì²é¸üÐÂ
		case R.id.tv_update:
			mCustomToast.setText(mRes.getString(R.string.update_checking));
			mCustomToast.setVisibility(View.VISIBLE);
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
}
