package xy.game.puzzle.activity;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class PreviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		ImageView img = (ImageView)findViewById(R.id.iv_preview);
		switch (PuzzleProvider.getInstance(this).getGameLevel()) {
		case 0:
			img.setImageResource(R.drawable.preview_33);
			break;

		case 1:
			img.setImageResource(R.drawable.preview_44);
			break;
		case 2:
			img.setImageResource(R.drawable.preview_55);
			break;
		default:
			break;
		}

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
