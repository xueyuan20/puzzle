package xy.game.puzzle.util;


public class Position {
	protected float pos_x; // X����
	protected float pos_y; // Y����

	public Position(float x, float y){
		pos_x = x;
		pos_y = y;
	}

	public float getX(){
		return pos_x;
	}

	public float getY(){
		return pos_y;
	}
}
