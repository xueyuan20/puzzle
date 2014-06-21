package xy.game.puzzle.view;

import xy.game.puzzle.R;
import xy.game.puzzle.util.ScreenUtil;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomIconTextView extends RelativeLayout {

	private ImageView mLeftIcon, mRightIcon;
	private TextView mTextView;
	private Resources mRes;
	private Context mContext;

	public CustomIconTextView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		mContext = context;
		mRes = getResources();
		initViews(attrs, 0);
	}

	public CustomIconTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		mRes = getResources();
		initViews(attrs, defStyle);
	}


	private void initViews(AttributeSet attrs, int defStyle) {
		// TODO Auto-generated method stub

		mTextView = new TextView(mContext, attrs, defStyle);
		int resTextResId = attrs.getAttributeResourceValue(null,
				CustomViewID.TEXT, 0);
		if (resTextResId > 0) {
			mTextView.setText(mRes.getString(resTextResId));
		}
		initTextView(mTextView);
		addView(mTextView);

		int resLeftIconId = attrs.getAttributeResourceValue(null,
				CustomViewID.SRC, 0);
		if (resLeftIconId > 0) {
			mLeftIcon = new ImageView(mContext);
			mLeftIcon.setImageResource(resLeftIconId);
			initIcon(mLeftIcon, true);
			addView(mLeftIcon);
		}

		// custom_dir_img_src
		mRightIcon = new ImageView(mContext);
		int resRightIconId = attrs.getAttributeResourceValue(null,
				CustomViewID.DIR_IMG_SRC, 0);
		if (resRightIconId > 0) {
			mRightIcon.setImageResource(resRightIconId);
		}
		initIcon(mRightIcon, false);
		addView(mRightIcon);
	}	

	private void initTextView(TextView tv) {
		// TODO Auto-generated method stub
		tv.setPadding(
				ScreenUtil.dip2px(mContext,
						mRes.getDimension(R.dimen.menu_img_size)) + 3, 3, 3, 3);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(CENTER_VERTICAL);
		tv.setLayoutParams(params);
		tv.setBackgroundColor(mRes.getColor(R.color.clear));
	}

	private void initIcon(ImageView img, boolean isAlignToLeft) {
		// TODO Auto-generated method stub
		img.setPadding(3, 3, 3, 3);
		img.setScaleType(ScaleType.FIT_CENTER);
		img.setAdjustViewBounds(true);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ScreenUtil.dip2px(mContext,
						mRes.getDimension(R.dimen.menu_img_size)),
				ScreenUtil.dip2px(mContext,
						mRes.getDimension(R.dimen.menu_img_size)));
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		if (isAlignToLeft) {
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		} else {
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
		img.setLayoutParams(params);
	}

}
