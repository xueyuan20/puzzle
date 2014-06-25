package xy.game.puzzle.view;

import xy.game.puzzle.R;
import xy.game.puzzle.util.ScreenUtil;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomTitleBar extends RelativeLayout {
	private ImageView mBackButton;
	private TextView mTitleTextView;
	private Context mContext;
	private Resources mRes;
	private OnBackClicked mBackListener;

	public CustomTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		mRes = getResources();

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
		mBackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mBackListener!=null) {
					mBackListener.onBack();
				}
			}
		});

		addView(mTitleTextView);
		addView(mBackButton);

		mBackButton.setBackgroundResource(R.drawable.selector_back);
		int titleStrId = attrs.getAttributeResourceValue(null,
				CustomViewID.TEXT, 0);
		if (titleStrId > 0) {
			mTitleTextView.setText(mRes.getString(titleStrId));
		}

		int backgroundSrcId = attrs.getAttributeResourceValue(null,
				CustomViewID.BACKGROUND, 0);
		if (backgroundSrcId > 0) {
			setBackgroundResource(backgroundSrcId);
		} else {
			setBackgroundResource(R.drawable.ic_title_background);
		}
		mTitleTextView.setTextColor(Color.WHITE);
		mTitleTextView.setTextSize(ScreenUtil.px2dip(mContext,
				mRes.getDimension(R.dimen.title_text_size)));
	}

	public void setText(CharSequence text) {
		mTitleTextView.setText(text);
	}

	public void setListener(OnBackClicked listener){
		mBackListener = listener;
	}
	
	public interface OnBackClicked{
		public void onBack();
	}
}
