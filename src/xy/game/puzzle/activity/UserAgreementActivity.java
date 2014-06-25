package xy.game.puzzle.activity;

import xy.game.puzzle.R;
import xy.game.puzzle.view.CustomTitleBar;
import xy.game.puzzle.view.CustomTitleBar.OnBackClicked;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.umeng.analytics.MobclickAgent;

public final class UserAgreementActivity extends SlideBaseActivity implements
		OnBackClicked {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_agreement);

		((CustomTitleBar)findViewById(R.id.title)).setListener(this);

		WebView wv = (WebView) findViewById(R.id.wv_content);
		wv.loadUrl("file:///android_asset/html/rules.html");
		Button btn = (Button) findViewById(R.id.btn_read);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		finish();
	}
}
