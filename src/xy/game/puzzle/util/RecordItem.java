package xy.game.puzzle.util;

import xy.game.puzzle.logic.Processor;
import xy.game.puzzle.logic.PuzzleProvider;

/**
 * 成绩记录类
 * 
 * @author 80070307
 * 
 */

public class RecordItem {
	private int mId;

	/**
	 * player's name.
	 */
	private String mUserName;

	/**
	 * 计步器
	 */
	private int mStepCount;

	/**
	 * 计时器
	 */
	private int mTimerCount;

	/**
	 * 总得分
	 */
	private int mTotalScore;

	/**
	 * 拼图尺寸
	 */
	private int mPuzzleSize;

	private String mCompleteTime;
	private boolean mCompleteFlag;

	public RecordItem(int puzzleSize, int steps, int timercount) {
		// TODO Auto-generated constructor stub
		mStepCount = steps;
		mTimerCount = timercount;
		mPuzzleSize = puzzleSize;
		initTotalScore();
	}

	public void resetData() {
		resetData(mPuzzleSize);
	}

	public void resetData(int puzzleSize){
		mPuzzleSize = puzzleSize;
		mStepCount = 0;
		mTimerCount = 0;
		initTotalScore();
	}

	/**
	 * 初始化得分
	 * 
	 * @param puzzleSize
	 */
	private void initTotalScore() {
		mTotalScore = Processor.caculateScore(mPuzzleSize, mTimerCount,
				mStepCount);
	}

	public void updateStepCount() {
		mStepCount++;
		initTotalScore();
	}

	public int getStepCount() {
		return mStepCount;
	}

	public void updateTimerCount() {
		mTimerCount++;
		initTotalScore();
	}

	public String getTimerString() {
		return String.format("%02d:%02d:%02d", mTimerCount / (60 * 60),
				(mTimerCount / 60) % 60, mTimerCount % 60);
	}

	public void setPuzzleSize(int puzzleSize) {
		mPuzzleSize = puzzleSize;
		initTotalScore();
	}

	public int getTimerCount() {
		return mTimerCount;
	}

	public int getTotalScore() {
		return mTotalScore;
	}

	public void setUserName(String userName){
		mUserName = userName;
	}

	public String getUserName() {
		// TODO Auto-generated method stub
		return mUserName;
	}

	public void setCompleteFlag(boolean complete){
		mCompleteFlag = complete;
	}

	public int getCompleteFlag() {
		// TODO Auto-generated method stub
		return mCompleteFlag ? 1 : 0;
	}

	public void setCompleteTime(){		
		long time = System.currentTimeMillis() / 1000;
		mCompleteTime = String.format("%1$02d:%2$02d:%3$02d", time / (60 * 60),
				(time / 60) % 60, time % 60);
	}

	public String getCompleteTime() {
		// TODO Auto-generated method stub
		return mCompleteTime;
	}

	public void setId(int id){
		mId = id;
	}

	public int getId(){
		return mId;
	}

	public int getLevel(){
		return mPuzzleSize-3;
	}
}
