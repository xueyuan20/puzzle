package xy.game.puzzle.view;

import xy.game.puzzle.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CustomEditDialog extends Dialog implements View.OnClickListener {
	private EditText mInput;
	private Button mButtonOk, mButtonCancle;
	private OnDialogClicked mListener;
	String mTitle, mContent;

	public CustomEditDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_edit);
		setTitle(mTitle);

		mInput = (EditText) findViewById(R.id.input);
		mInput.setText(mContent);

		mButtonOk = (Button) findViewById(R.id.ok);
		mButtonOk.setOnClickListener(this);
		mButtonCancle = (Button) findViewById(R.id.cancle);
		mButtonCancle.setOnClickListener(this);
	}

	public void setDialogTitle(String title) {
		// TODO Auto-generated method stub
		mTitle = title;
	}

	public void setContent(String content) {
		mContent = content;
	}

	public void setListener(OnDialogClicked listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.ok:
			if (mListener != null) {
				mListener.onClickOk(mInput.getText().toString());
			}
			dismiss();
			break;

		case R.id.cancle:
			if (mListener != null) {
				mListener.onClickCancle();
			}
			dismiss();
			break;

		default:
			break;
		}
	}

	public interface OnDialogClicked {
		public void onClickOk(String input);

		public void onClickCancle();
	}
}
