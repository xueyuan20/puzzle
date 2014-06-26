package xy.game.puzzle.view;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 
 * <P>
 * [����] �������������Ƿ񳬳���󳤶ȣ������ù��λ��
 * </P>
 * <P>
 * [˵��]
 * </P>
 * <P>
 * [��ע]
 * </P>
 * <P>
 * [����] android 2.1
 * </P>
 * 
 * @author Lazy
 * @version ver 1.0
 * @2011-5-7 ����07:48:27
 */
public class EditTextMaxLengthWatcher implements TextWatcher {

	// ��󳤶�
	private int mMaxLen;

	// �����ı���ı���
	private EditText mEditText;

	/**
	 * ���캯��
	 */
	public EditTextMaxLengthWatcher(int maxLen, EditText editText) {
		this.mMaxLen = maxLen;
		this.mEditText = editText;
	}

	@Override
	public void onTextChanged(CharSequence ss, int start, int before, int count) {
		Editable editable = mEditText.getText();
		int len = editable.length();
		// ������󳤶�
		if (len > mMaxLen) {
			int selEndIndex = Selection.getSelectionEnd(editable);
			String str = editable.toString();
			// ��ȡ���ַ���
			String newStr = str.substring(0, mMaxLen);
			mEditText.setText(newStr);
			editable = mEditText.getText();
			// ���ַ�������
			int newLen = editable.length();
			// �ɹ��λ�ó����ַ�������
			if (selEndIndex > newLen) {
				selEndIndex = editable.length();
			}
			// �����µĹ������λ��
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
