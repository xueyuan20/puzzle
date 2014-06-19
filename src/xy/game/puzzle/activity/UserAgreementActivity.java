package xy.game.puzzle.activity;

import xy.game.puzzle.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class UserAgreementActivity extends SlideBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_agreement);
		WebView wv = (WebView)findViewById(R.id.wv_content);
		wv.loadUrl("file:///android_asset/html/rules.html");
		Button btn = (Button)findViewById(R.id.btn_read);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
