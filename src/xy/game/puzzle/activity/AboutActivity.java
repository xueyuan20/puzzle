package xy.game.puzzle.activity;

import xy.game.puzzle.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends Activity implements OnClickListener {
	private Context mContext;
	private TextView mTvFeedback, mTvAppdesc, mTvUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mContext = this;
		mTvFeedback = (TextView)findViewById(R.id.tv_feedback);
		mTvFeedback.setOnClickListener(this);
		mTvAppdesc = (TextView)findViewById(R.id.tv_app_desc);
		mTvAppdesc.setOnClickListener(this);
		mTvUpdate = (TextView)findViewById(R.id.tv_update);
		mTvUpdate.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_feedback:
			
			break;

		case R.id.tv_app_desc:
			startActivity(new Intent(mContext, UserAgreementActivity.class));
			break;
		// ������
		case R.id.tv_update:
			Toast.makeText(mContext, "���ڼ����£����Ժ�", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}
}
