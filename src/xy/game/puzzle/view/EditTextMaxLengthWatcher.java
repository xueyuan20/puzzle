package xy.game.puzzle.view;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 
 * <P>
 * [功能] 监听输入内容是否超出最大长度，并设置光标位置
 * </P>
 * <P>
 * [说明]
 * </P>
 * <P>
 * [备注]
 * </P>
 * <P>
 * [环境] android 2.1
 * </P>
 * 
 * @author Lazy
 * @version ver 1.0
 * @2011-5-7 下午07:48:27
 */
public class EditTextMaxLengthWatcher implements TextWatcher {

	// 最大长度
	private int mMaxLen;

	// 监听改变的文本框
	private EditText mEditText;

	/**
	 * 构造函数
	 */
	public EditTextMaxLengthWatcher(int maxLen, EditText editText) {
		this.mMaxLen = maxLen;
		this.mEditText = editText;
	}

	@Override
	public void onTextChanged(CharSequence ss, int start, int before, int count) {
		Editable editable = mEditText.getText();
		int len = editable.length();
		// 大于最大长度
		if (len > mMaxLen) {
			int selEndIndex = Selection.getSelectionEnd(editable);
			String str = editable.toString();
			// 截取新字符串
			String newStr = str.substring(0, mMaxLen);
			mEditText.setText(newStr);
			editable = mEditText.getText();
			// 新字符串长度
			int newLen = editable.length();
			// 旧光标位置超过字符串长度
			if (selEndIndex > newLen) {
				selEndIndex = editable.length();
			}
			// 设置新的光标所在位置
			Selection.setSelection(editable, selEndIndex);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

}
