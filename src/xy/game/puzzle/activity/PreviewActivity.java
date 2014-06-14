package xy.game.puzzle.activity;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.view.FloatMenuItem;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PreviewActivity extends Activity implements OnClickListener {
	private boolean mPreviewModeFlag = false;
	private FloatMenuItem[] mMenuArray;
	private ImageView mIvPreview;
	private TextView mTvPageTitle;

	private Resources mRes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		mRes = getResources();

		mIvPreview = (ImageView) findViewById(R.id.iv_preview);
		mTvPageTitle = (TextView) findViewById(R.id.page_title);

		mMenuArray = new FloatMenuItem[4];
		mMenuArray[0] = (FloatMenuItem) findViewById(R.id.floatMenuItem01);
		mMenuArray[0].setOnClickListener(this);
		mMenuArray[1] = (FloatMenuItem) findViewById(R.id.floatMenuItem02);
		mMenuArray[1].setOnClickListener(this);
		mMenuArray[2] = (FloatMenuItem) findViewById(R.id.floatMenuItem03);
		mMenuArray[2].setOnClickListener(this);
		mMenuArray[3] = (FloatMenuItem) findViewById(R.id.floatMenuItem04);
		mMenuArray[3].setOnClickListener(this);

		if (mPreviewModeFlag) {
			mTvPageTitle.setText(mRes.getString(R.string.preview));
			mMenuArray[1].setTextAndSrc(R.string.menu_title_share);
			mMenuArray[2].setTextAndSrc(R.string.menu_title_delete);
			mMenuArray[3].setTextAndSrc(R.string.menu_title_detail);
		} else {
			mTvPageTitle.setText(mRes.getString(R.string.set_image));
			switch (PuzzleProvider.getInstance(this).getGameLevel()) {
			case 0:
				mIvPreview.setImageResource(R.drawable.preview_33);
				break;

			case 1:
				mIvPreview.setImageResource(R.drawable.preview_44);
				break;
			case 2:
				mIvPreview.setImageResource(R.drawable.preview_55);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {

		case R.drawable.selector_back:
			finish();
			break;
		/**
		 * ±≥æ∞Õº∆¨…Ë÷√ƒ£ Ω
		 */
		case R.drawable.ic_menu_camera:
			break;
		case R.drawable.ic_menu_gallery:
			break;
		case R.drawable.ic_menu_default:
			break;
		/**
		 * Õº∆¨‘§¿¿ƒ£ Ω
		 */
		case R.drawable.ic_menu_share_holo:
			break;
		case R.drawable.ic_menu_delete:
			break;
		case R.drawable.ic_menu_more:
			break;
		default:
			break;
		}
	}
}
