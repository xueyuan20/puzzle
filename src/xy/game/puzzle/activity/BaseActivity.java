package xy.game.puzzle.activity;

import android.app.Activity;

/**
 * session的统计
 * 
 * 正确集成如下代码，才能够保证获取正确的新增用户、活跃用户、启动次数、使用时长等基本数据。
 * 
 * 在每个Activity的onResume方法中调用 MobclickAgent.onResume(Context), 
 * onPause方法中调用MobclickAgent.onPause(Context)
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
 * 确保在所有的Activity中都调用 MobclickAgent.onResume()和MobclickAgent.onPause()方法，
 * 这两个调用将不会阻塞应用程序的主线程，也不会影响应用程序的性能。
 * 
 * 注意如果您的Activity之间有继承或者控制关系,请不要同时在父和子Activity中重复添加onPause和onResume方法
 * ，否则会造成重复统计(eg.使用TabHost、TabActivity、ActivityGroup时)。
 * 
 * 当用户两次使用之间间隔超过30秒时，将被认为是两个的独立的session
 * (启动)，例如用户回到home，或进入其他程序，经过一段时间后再返回之前的应用。可通过接口
 * MobclickAgent.setSessionContinueMillis(long interval) 来设置这个间隔(参数单位为毫秒)。
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
