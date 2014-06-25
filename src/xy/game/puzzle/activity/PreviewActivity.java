package xy.game.puzzle.activity;

import java.io.File;

import xy.game.puzzle.R;
import xy.game.puzzle.logic.PuzzleProvider;
import xy.game.puzzle.logic.RefreshByLocalFileTask;
import xy.game.puzzle.util.LogUtil;
import xy.game.puzzle.util.MessageUtils;
import xy.game.puzzle.util.ScreenUtil;
import xy.game.puzzle.util.StorageUtil;
import xy.game.puzzle.view.CustomTitleBar;
import xy.game.puzzle.view.CustomTitleBar.OnBackClicked;
import xy.game.puzzle.view.FloatMenuItem;
import xy.game.puzzle.view.PreviewSurfaceView;
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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.analytics.MobclickAgent;

public final class PreviewActivity extends BaseActivity implements
		OnClickListener, OnBackClicked {
	private boolean mPreviewModeFlag = false;
	private FloatMenuItem[] mMenuArray;
	// private ImageView mIvPreview;
	private PreviewSurfaceView mSvPreview;
	private CustomTitleBar mTvPageTitle;
	private String mFileName;
	private String mFilePath;

	private Resources mRes;
	private PuzzleProvider mProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_preview);
		mRes = getResources();
		mProvider = PuzzleProvider.getInstance(this);

		// mIvPreview = (ImageView) findViewById(R.id.iv_preview);
		mSvPreview = (PreviewSurfaceView) findViewById(R.id.preview);
		mTvPageTitle = (CustomTitleBar) findViewById(R.id.page_title);
		mTvPageTitle.setListener(this);

		Intent intent = getIntent();
		if ((intent != null) && (intent.getExtras() != null)) {
			mFilePath = intent.getExtras()
					.getString(MessageUtils.KEY_FILE_PATH);
			mPreviewModeFlag = intent.getExtras().getBoolean(
					MessageUtils.KEY_IS_PREVIEW);
			LogUtil.d("[PreviewActivity] file path = " + mFilePath
					+ "; preview=" + mPreviewModeFlag);
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

		mSvPreview.setPreviewMode(mPreviewModeFlag);
		if (mPreviewModeFlag) {
			mTvPageTitle.setText(mRes.getString(R.string.title_preview));
			mMenuArray[1].setTextAndSrc(R.string.menu_title_share);
			mMenuArray[2].setTextAndSrc(R.string.menu_title_delete);
			mMenuArray[3].setTextAndSrc(R.string.menu_title_detail);
			// RefreshByLocalFileTask task = new
			// RefreshByLocalFileTask(mIvPreview);
			RefreshByLocalFileTask task = new RefreshByLocalFileTask(
					mPreviewModeFlag, mSvPreview);
			task.execute(mFilePath);
		} else {
			mTvPageTitle.setText(mRes.getString(R.string.set_image));
			if (mProvider.checkUseDefaultBk()) {
				setDefaultPreview();
			}
		}
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

	private void setDefaultPreview() {
		mSvPreview.setImageAsBackground(true, null);
		PuzzleProvider.getInstance(this).setUseDefaultBk(true);
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
					// mIvPreview
					// .setImageResource(android.R.drawable.stat_sys_warning);
					mSvPreview.showBrokenIcon();
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
			LogUtil.e("[PreviewActivity] requestCode:" + requestCode
					+ "resultCode" + resultCode);
			switch (requestCode) {
			case MessageUtils.CODE_FROM_CAMERA:
				// RefreshByLocalFileTask task = new RefreshByLocalFileTask(
				// mIvPreview);
				RefreshByLocalFileTask task = new RefreshByLocalFileTask(
						mPreviewModeFlag, mSvPreview);
				task.execute(mFileName);
				break;

			case MessageUtils.CODE_FROM_GALLERY:
				if (!mPreviewModeFlag) {
					Uri uri = data.getData();
					ContentResolver cr = this.getContentResolver();
					try {
						Bitmap bitmap = BitmapFactory.decodeStream(cr
								.openInputStream(uri));
						// start to deal with bitmap.
						// mIvPreview.setImageBitmap(bitmap);
						mSvPreview.setImageAsBackground(false, bitmap);

						PuzzleProvider provider = PuzzleProvider
								.getInstance(PreviewActivity.this);

						// String path = StorageUtil.saveBackground(bitmap);
						String path = StorageUtil.getInstance().saveBackground(
								mSvPreview.getBackgroundBmp());
						LogUtil.e("from gallery [file path] " + path);

						if ((path != null) && (!TextUtils.isEmpty(path))) {
							provider.setUseDefaultBk(false);
							provider.setCustomBkPath(path);
						}
					} catch (Exception e) {
						// TODO: handle exception
						LogUtil.printCodeStack(e);
					}
				}
				break;

			default:
				break;
			}
		}
	}

	public boolean checkPreviewMode() {
		return mPreviewModeFlag;
	}

	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		finish();
	}
}
