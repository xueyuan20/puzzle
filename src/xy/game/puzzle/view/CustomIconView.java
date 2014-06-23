package xy.game.puzzle.view;

import xy.game.puzzle.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * CustomIconView, include Content TextView and Value TextView.
 * 
 * @author 80070307
 * @since 2014-6-23
 * 
 */
public class CustomIconView extends RelativeLayout {

	private TextView mContentTextView, mValueTextView;
	private Resources mRes;
	private Context mContext;

	public CustomIconView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		mContext = context;
		mRes = getResources();
		initView(attrs);
	}

	public CustomIconView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		mRes = getResources();
		initView(attrs, defStyle);
	}

	private void initView(AttributeSet attrs) {
		// TODO Auto-generated method stub
		initView(attrs, 0);
	}

	private void initView(AttributeSet attrs, int defStyle) {
		// TODO Auto-generated method stub
		setPadding(3, 0, 3, 0);

		mContentTextView = new TextView(mContext, null, defStyle);
		mValueTextView = new TextView(mContext, null, defStyle);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(CENTER_VERTICAL);
		params.addRule(ALIGN_PARENT_LEFT);
		mContentTextView.setLayoutParams(params);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(CENTER_VERTICAL);
		params1.addRule(ALIGN_PARENT_RIGHT);
		
		mValueTextView.setLayoutParams(params1);

		int contentTextStrId = attrs.getAttributeResourceValue(null,
				CustomViewID.TEXT, 0);
		if (contentTextStrId > 0) {
			mContentTextView.setText(mRes.getString(contentTextStrId));
			mContentTextView.setTextColor(mRes.getColor(R.color.half_dark));
		}
		int valueTextStrId = attrs.getAttributeResourceValue(null,
				CustomViewID.DESC_TEXT, 0);
		if (valueTextStrId > 0) {
			setValueContent(mRes.getString(contentTextStrId));
		}
		int valueBkImgSrcId = attrs.getAttributeResourceValue(null,
				CustomViewID.DESC_IMG_SRC, 0);
		if (valueBkImgSrcId > 0) {
			setValueBkImg(valueBkImgSrcId);
		}

		addView(mContentTextView);
		addView(mValueTextView);
		mContentTextView.setTextColor(Color.DKGRAY);
		mValueTextView.setTextColor(Color.LTGRAY);
		mValueTextView.setTextSize(12);
	}

	public void setValueBkImg(int resourceId) {
		if (mValueTextView != null) {
			mValueTextView.setBackgroundResource(resourceId);
		}
	}

	public void setValueContent(String valueText) {
		if (mValueTextView != null) {
			mValueTextView.setText(valueText);
		}
	}
}
