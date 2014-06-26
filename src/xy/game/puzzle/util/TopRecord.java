package xy.game.puzzle.util;

public class TopRecord {

	/**
	 * �Ʋ���
	 */
	private int mStepCount;

	/**
	 * ��ʱ��
	 */
	private int mTimerCount;

	/**
	 * �û���
	 */
	private String mUserName;

	public TopRecord(String userName, int step, int time) {
		// TODO Auto-generated constructor stub
		mUserName = userName;
		mStepCount = step;
		mTimerCount = time;
	}

	public String getUserName() {
		return mUserName;
	}

	public int getStepCount() {
		return mStepCount;
	}

	public int getTimerCount() {
		return mTimerCount;
	}

	public String toString() {
		return mUserName + " [steps]" + String.valueOf(mStepCount) + "; [time]"
				+ String.valueOf(mTimerCount);
	}
}
