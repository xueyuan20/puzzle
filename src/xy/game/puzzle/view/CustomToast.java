package xy.game.puzzle.view;

import xy.game.puzzle.R;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomToast extends RelativeLayout {
	private TextView mCustomTextView;
	private Resources mRes;
	private Context mContext;

	public CustomToast(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		mContext = context;
		mRes = getResources();

		setBackgroundColor(mRes.getColor(R.color.grydk));

		mCustomTextView = new TextView(mContext);
		initTextView(mCustomTextView);
		addView(mCustomTextView);
	}

	private void initTextView(TextView tv) {
		tv.setTextColor(mRes.getColor(R.color.content_text_color));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				mRes.getDimensionPixelSize(R.dimen.content_text_size));
		tv.setBackground(mRes.getDrawable(R.drawable.white_button_style));

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv.setLayoutParams(params);

		mCustomTextView.setText("");
	}

	public void setText(String message) {
		mCustomTextView.setText(message);
	}

	public CharSequence getText() {
		return mCustomTextView.getText();
	}

	@Override
	@CapturedViewProperty
	public int getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		setVisibility(View.GONE);
		return true;
	}
}
