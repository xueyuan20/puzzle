package xy.game.puzzle.view;

import xy.game.puzzle.R;
import xy.game.puzzle.util.ScreenUtil;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Custom defined float menu item view.
 * 
 * @since 2014-6-13
 * @author 80070307
 * 
 */
public final class FloatMenuItem extends RelativeLayout {

	protected int mBackgroundId = -1;
	protected int mImageId = -1; // ImageView控件ID
	protected int mTextId = -1; // TextView控件ID
	protected String mText; // TextView控件text内容
	protected int mImageSrcId = -1;// ImageView控件Image图片资源ID

	private TextView mTextView;
	private ImageView mImageView;
	private Resources mRes;
	private Context mContext;

	public FloatMenuItem(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);

		mContext = context;
		mRes = mContext.getResources();

		mTextView = new TextView(mContext);
		/**
		 * Get attributes from xml file.
		 */
		mImageSrcId = attrs
				.getAttributeResourceValue(null, CustomViewID.SRC, 0);
		if (mImageSrcId > 0) {
			mImageView = new ImageView(mContext);
			mImageId = attrs.getAttributeResourceValue(null,
					CustomViewID.IMAGEVIEW_ID, 0);
			initImageView(mImageView);

			mImageView.setImageResource(mImageSrcId);

		}

		mTextId = attrs.getAttributeResourceValue(null,
				CustomViewID.TEXTVIEW_ID, 0);
		initTextView(mTextView);

		int tvResId = attrs.getAttributeResourceValue(null, CustomViewID.TEXT,
				0);
		if (tvResId > 0) {
			mText = mRes.getString(tvResId);
			if (mText == null || mText.isEmpty()) {
				mTextView.setText("Test");
			} else {
				mTextView.setText(mText);
			}
		}

		mBackgroundId = attrs.getAttributeResourceValue(null,
				CustomViewID.BACKGROUND, 0);
		if (mBackgroundId > 0) {
			this.setBackgroundResource(mBackgroundId);
		}
		this.addView(mTextView);
		this.addView(mImageView);

	}

	private void initTextView(TextView tv) {
		tv.setTextColor(mRes.getColor(R.color.content_text_color));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mRes.getDimensionPixelSize(R.dimen.menu_text_size));

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.BELOW, mImageId);
		tv.setLayoutParams(params);

		tv.setId(mTextId);
		tv.setTextColor(Color.WHITE);
	}

	private void initImageView(ImageView img) {
		img.setId(mImageId);
		img.setPadding(3, 3, 3, 3);
		img.setScaleType(ScaleType.FIT_CENTER);
		img.setAdjustViewBounds(true);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ScreenUtil.dip2px(mContext,
						mRes.getDimension(R.dimen.menu_img_size)),
				ScreenUtil.dip2px(mContext,
						mRes.getDimension(R.dimen.menu_img_size)));
		// RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// params.addRule(RelativeLayout.ABOVE, mTextId);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(
				RelativeLayout.ALIGN_PARENT_TOP,
				ScreenUtil.dip2px(mContext,
						mRes.getDimension(R.dimen.menu_top_border)));
		img.setLayoutParams(params);
		img.setImageResource(mImageSrcId);
	}

	public void setTextAndSrc(int textId) {
		if (textId > 0) {
			mText = mRes.getString(textId);
		}
		switch (textId) {
		case R.string.menu_title_back:
			mImageSrcId = R.drawable.selector_back;
			break;

		case R.string.menu_title_camera:
			mImageSrcId = R.drawable.ic_menu_camera;
			break;

		case R.string.menu_title_gallery:
			mImageSrcId = R.drawable.ic_menu_gallery;
			break;

		case R.string.menu_title_default:
			mImageSrcId = R.drawable.ic_menu_default;
			break;

		case R.string.menu_title_share:
			mImageSrcId = R.drawable.ic_menu_share_holo;
			break;

		case R.string.menu_title_delete:
			mImageSrcId = R.drawable.ic_menu_delete;
			break;

		case R.string.menu_title_detail:
			mImageSrcId = R.drawable.ic_menu_more;
			break;

		default:
			break;
		}

		if (mTextView != null) {
			mTextView.setText(mText);
		}

		if (mImageSrcId > 0) {
			mImageView.setImageResource(mImageSrcId);
		}
	}

	public int getId() {
		return mImageSrcId;
	}
}
