package xy.game.puzzle.activity;

import java.io.File;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.logic.RefreshByLocalFileTask;
import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.MessageUtils;
import xy.game.puzzle.util.ScreenUtil;
import xy.game.puzzle.view.FloatMenuItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PreviewActivity extends Activity implements OnClickListener {
	private boolean mPreviewModeFlag = false;
	private FloatMenuItem[] mMenuArray;
	private ImageView mIvPreview;
	private TextView mTvPageTitle;
	private String mFileName;
	private String mFilePath;

	private Resources mRes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_preview);
		mRes = getResources();

		mIvPreview = (ImageView) findViewById(R.id.iv_preview);
		mTvPageTitle = (TextView) findViewById(R.id.page_title);

		Intent intent = getIntent();
		LogUtil.e("Get Intent + " + intent);
		if ((intent != null) && (intent.getExtras() != null)) {
			mPreviewModeFlag = true;
			mFilePath = intent.getExtras()
					.getString(MessageUtils.KEY_FILE_PATH);
			RefreshByLocalFileTask task = new RefreshByLocalFileTask(mIvPreview);
			task.execute(mFilePath);
		}

		mMenuArray = new FloatMenuItem[4];
		mMenuArray[0] = (FloatMenuItem) findViewById(R.id.floatMenuItem01);
		mMenuArray[0].setOnClickListener(this);
		mMenuArray[1] = (FloatMenuItem) findViewById(R.id.floatMenuItem02);
		mMenuArray[1].setOnClickListener(this);
		mMenuArray[2] = (FloatMenuItem) findViewById(R.id.floatMenuItem03);
		mMenuArray[2].setOnClickListener(this);
		mMenuArray[3] = (FloatMenuItem) findViewById(R.id.floatMenuItem04);
		mMenuArray[3].setOnClickListener(this);

		if (mPreviewModeFlag) {
			mTvPageTitle.setText(mRes.getString(R.string.preview));
			mMenuArray[1].setTextAndSrc(R.string.menu_title_share);
			mMenuArray[2].setTextAndSrc(R.string.menu_title_delete);
			mMenuArray[3].setTextAndSrc(R.string.menu_title_detail);
		} else {
			mTvPageTitle.setText(mRes.getString(R.string.set_image));
			setDefaultPreview();
		}
	}

	private void setDefaultPreview() {
		switch (PuzzleProvider.getInstance(this).getGameLevel()) {
		case 0:
			mIvPreview.setImageResource(R.drawable.preview_33);
			break;

		case 1:
			mIvPreview.setImageResource(R.drawable.preview_44);
			break;
		case 2:
			mIvPreview.setImageResource(R.drawable.preview_55);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {

		case R.drawable.selector_back:
			finish();
			break;
		/**
		 * 背景图片设置模式
		 */
		case R.drawable.ic_menu_camera:
			long dateTaken = System.currentTimeMillis();
			String name = String.valueOf(dateTaken) + ".jpg";
			String path = Environment.getExternalStorageDirectory().getPath()
					+ "/" + getResources().getString(R.string.app_name);
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String fileName = path + "/" + name;
			mFileName = fileName;
			ContentValues values = new ContentValues();
			values.put(Images.Media.TITLE, fileName);
			values.put("_data", fileName);
			values.put(Images.Media.PICASA_ID, fileName);
			values.put(Images.Media.DISPLAY_NAME, fileName);
			values.put(Images.Media.DESCRIPTION, fileName);
			values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, fileName);
			Uri photoUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

			Intent inttPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			inttPhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(inttPhoto, MessageUtils.CODE_FROM_CAMERA);
			break;
		case R.drawable.ic_menu_gallery:
			Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(i, MessageUtils.CODE_FROM_GALLERY);
			break;
		case R.drawable.ic_menu_default:
			setDefaultPreview();
			break;
		/**
		 * 图片预览模式
		 */
		case R.drawable.ic_menu_share_holo:
			ScreenUtil.shareMsg(PreviewActivity.this, getTitle().toString(),
					"test", "testContent", mFilePath);

			break;
		case R.drawable.ic_menu_delete:
			File delFile = new File(mFilePath);
			if (delFile.exists()) {
				if (delFile.delete()) {
					mIvPreview
							.setImageResource(android.R.drawable.stat_sys_warning);
				}
			}
			break;
		case R.drawable.ic_menu_more:
			File viewFile = new File(mFilePath);
			if (viewFile.exists()) {

				new AlertDialog.Builder(PreviewActivity.this)
						.setTitle("bitmap info")
						.setMessage(
								"名称：" + viewFile.getName() + "\r\n路径："
										+ viewFile.getPath() + "\r\n大小："
										+ viewFile.getTotalSpace()
										+ "\r\n修改时间：" + viewFile.lastModified()
										+ "" + "")
						.setNegativeButton(mRes.getString(R.string.ok), null)
						.show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case MessageUtils.CODE_FROM_CAMERA:
				RefreshByLocalFileTask task = new RefreshByLocalFileTask(
						mIvPreview);
				task.execute(mFileName);
				break;

			case MessageUtils.CODE_FROM_GALLERY:
				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(uri));
					// start to deal with bitmap.
					mIvPreview.setImageBitmap(bitmap);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;

			default:
				break;
			}
		}
	}
}
