package xy.game.puzzle.logic;

import java.util.Random;

/**
 * Processor Class, generate random sequences.
 * @author 80070307
 *
 */
public class Processor {

	private static int[] mIndexArray;
	private static final int LOOP_COUNT = 160;
	/**
	 * Generator random sequences.
	 * @param length, length of sequences.
	 */
	public static int[] randomRequences(int length){
		/*
		 * initialize order sequences.
		 */
		if (mIndexArray != null && mIndexArray.length != length) {
			mIndexArray = null;
		}

		if (mIndexArray == null) {
			initArray(length);
		}

		/*
		 * modify the order.
		 */
		Random random = new Random();
		int loopInd = LOOP_COUNT;
		int randomInd, tmp;
		
		try {
			while (loopInd-- > 0) {
				randomInd = random.nextInt(length-1);
				tmp = mIndexArray[randomInd];
				mIndexArray[randomInd] = mIndexArray[randomInd+1];
				mIndexArray[randomInd+1] = tmp;
				Thread.sleep(1);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mIndexArray;
	}

	/**
	 * Initialize order index array.
	 * 
	 * @param length
	 */
	private static void initArray(int length)	{
		mIndexArray = new int[length];
		for (int i = 0; i < length; i++) {
			mIndexArray[i] = (i+1)%length;
		}
	}

	/**
	 * Check the array is order array.
	 * @param array
	 * @return
	 */
	public static boolean isOrder(int[] array){
		if (array != null && array.length >0) {			
			int index = array.length-1;
			while (index > 0) {
				index--;
				if (array[index]!=(index+1)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Calculate total score according to level, time and steps count.
	 * @param puzzleSize
	 * @param time
	 * @param stepCount
	 * @return
	 */
	public static int caculateScore(int puzzleSize, int time,
			int stepCount){
		int score = puzzleSize*puzzleSize;
		score*=(10000*Math.log10(time+1));
		score*=(10000*Math.tanh(stepCount));
		return score;
	}
}
