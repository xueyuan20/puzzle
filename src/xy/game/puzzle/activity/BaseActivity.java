package xy.game.puzzle.activity;

import android.app.Activity;

/**
 * session��ͳ��
 * 
 * ��ȷ�������´��룬���ܹ���֤��ȡ��ȷ�������û�����Ծ�û�������������ʹ��ʱ���Ȼ������ݡ�
 * 
 * ��ÿ��Activity��onResume�����е��� MobclickAgent.onResume(Context), 
 * onPause�����е���MobclickAgent.onPause(Context)
 * 
 * eg.
 * 
 * public void onResume() {
 * 		super.onResume();
 * 		MobclickAgent.onResume(this);
 * }
 * public void onPause() {
 * 		super.onPause();
 * 		MobclickAgent.onPause(this);
 * }
 * 
 * ȷ�������е�Activity�ж����� MobclickAgent.onResume()��MobclickAgent.onPause()������
 * ���������ý���������Ӧ�ó�������̣߳�Ҳ����Ӱ��Ӧ�ó�������ܡ�
 * 
 * ע���������Activity֮���м̳л��߿��ƹ�ϵ,�벻Ҫͬʱ�ڸ�����Activity���ظ����onPause��onResume����
 * �����������ظ�ͳ��(eg.ʹ��TabHost��TabActivity��ActivityGroupʱ)��
 * 
 * ���û�����ʹ��֮��������30��ʱ��������Ϊ�������Ķ�����session
 * (����)�������û��ص�home��������������򣬾���һ��ʱ����ٷ���֮ǰ��Ӧ�á���ͨ���ӿ�
 * MobclickAgent.setSessionContinueMillis(long interval) ������������(������λΪ����)��
 * 
 * @author 80070307
 * 
 */
public class BaseActivity extends Activity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickAgent.onPause(this);
	}
}
