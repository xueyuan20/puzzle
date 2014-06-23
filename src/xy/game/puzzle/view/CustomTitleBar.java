package xy.game.puzzle.view;

import xy.game.puzzle.R;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomTitleBar extends RelativeLayout {
	private ImageView mBackButton;
	private TextView mTitleTextView;
	private Context mContext;
	private Resources mRes;

	public CustomTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		mRes = getResources();
		int backgroundSrcId = attrs.getAttributeResourceValue(null, CustomViewID.BACKGROUND, 0);
		if (backgroundSrcId>0) {
			setBackgroundResource(backgroundSrcId);
		} else {
			setBackgroundResource(R.drawable.ic_title_background);
		}

		mTitleTextView = new TextView(mContext);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(CENTER_IN_PARENT);
		mTitleTextView.setLayoutParams(params2);

		mBackButton = new ImageView(mContext);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(ALIGN_PARENT_LEFT);
		params1.addRule(CENTER_VERTICAL);
		mBackButton.setLayoutParams(params1);

		addView(mTitleTextView);
		addView(mBackButton);

		mBackButton.setBackgroundResource(R.drawable.selector_back);
		int titleStrId = attrs.getAttributeResourceValue(null,
				CustomViewID.TEXT, 0);
		if (titleStrId > 0) {
			mTitleTextView.setText(mRes.getString(titleStrId));
		}
	}

	public void setText(CharSequence text){
		mTitleTextView.setText(text);
	}
}
