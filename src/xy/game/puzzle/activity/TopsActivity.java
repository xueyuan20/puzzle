package xy.game.puzzle.activity;

import java.util.ArrayList;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.util.TopRecord;
import xy.game.puzzle.view.CustomTitleBar;
import xy.game.puzzle.view.CustomTitleBar.OnBackClicked;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class TopsActivity extends SlideBaseActivity implements OnBackClicked,
		OnClickListener {
	ListView mListView;
	TextView mLevelSimple, mLevelMedium, mLevelDifficult;
	TextView mSortType;
	PuzzleProvider mProvider;
	private Context mContext;
	private Resources mRes;
	private int mSelectLevel;
	private ArrayList<TopRecord> mTopsRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tops);
		mContext = this;
		mRes = getResources();
		mProvider = PuzzleProvider.getInstance(mContext);

		((CustomTitleBar) findViewById(R.id.title)).setListener(this);

		mListView = (ListView) findViewById(R.id.list);
		mLevelSimple = (TextView) findViewById(R.id.level_simple);
		mLevelSimple.setOnClickListener(this);
		mLevelMedium = (TextView) findViewById(R.id.level_medium);
		mLevelMedium.setOnClickListener(this);
		mLevelDifficult = (TextView) findViewById(R.id.level_difficult);
		mLevelDifficult.setOnClickListener(this);
		mSortType = (TextView) findViewById(R.id.sort_type);
		mSortType.setOnClickListener(this);

		mSelectLevel = mProvider.getGameLevel();
		initListViewByGameLevel(mSelectLevel);
	}

	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		resetSelected();
		switch (view.getId()) {
		case R.id.level_simple:
			mSelectLevel = 0;
			initListViewByGameLevel(0);
			break;

		case R.id.level_medium:
			mSelectLevel = 1;
			initListViewByGameLevel(1);
			break;

		case R.id.level_difficult:
			mSelectLevel = 2;
			initListViewByGameLevel(2);
			break;
		case R.id.sort_type:
			new AlertDialog.Builder(mContext)
					.setTitle(mRes.getString(R.string.title_choose_sort))
					.setIcon(mRes.getDrawable(R.drawable.ic_settings_level))
					.setSingleChoiceItems(
							mRes.getStringArray(R.array.sort_type_choices),
							mSelectLevel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dlg,
										int index) {
									// TODO Auto-generated method stub
									refreshListViewBySortType(index);
								}

							}).show();
			break;

		default:
			break;
		}
	}

	private void resetSelected() {
		// TODO Auto-generated method stub
		mLevelSimple.setSelected(false);
		mLevelMedium.setSelected(false);
		mLevelDifficult.setSelected(false);
	}

	private void initListViewByGameLevel(int gameLevel) {
		// TODO Auto-generated method stub
		resetSelected();
		switch (gameLevel) {
		case 0:
			mLevelSimple.setSelected(true);
			break;

		case 1:
			mLevelMedium.setSelected(true);
			break;

		case 2:
			mLevelDifficult.setSelected(true);
			break;

		default:
			break;
		}
		refreshListViewBySortType(0);
	}

	private void refreshListViewBySortType(int index) {
		// TODO Auto-generated method stub

		Cursor cursor = mProvider.queryTopByLevel(mSelectLevel);
		if (cursor != null) {
			if (mTopsRecord != null) {
				mTopsRecord.clear();
			}
			TopRecord record;
			while (cursor.moveToNext()) {
				record = new TopRecord(cursor.getString(1), cursor.getInt(3),
						cursor.getInt(4));
				Log.e("XUEYUAN", record.toString());
				mTopsRecord.add(record);
			}

			if (index == 0) {
				// 按步数排序

			} else {
				// 按时间排序
			}
			cursor.close();
		}
	}
}
