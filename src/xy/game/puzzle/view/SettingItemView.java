package xy.game.puzzle.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {
	private ImageView mLeftIcon, mRightIcon;
	private TextView mTextView;
	private Resources mRes;
	private Context mContext;

	public SettingItemView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		mContext = context;
		mRes = getResources();

		if (attrs.getAttributeResourceValue(null, CustomViewID.SRC, 0) > 0) {
			mLeftIcon = new ImageView(mContext);
			initIcon(mLeftIcon, true);
			addView(mLeftIcon);
		}

		// custom_dir_img_src
		if (attrs.getAttributeResourceValue(null, CustomViewID.DIR_IMG_SRC, 0) > 0) {
			mRightIcon = new ImageView(mContext);
			initIcon(mRightIcon, false);
			addView(mRightIcon);
		}
		addView(mTextView);
	}

	private void initIcon(ImageView mLeftIcon2, boolean isAlignToLeft) {
		// TODO Auto-generated method stub

	}

}
