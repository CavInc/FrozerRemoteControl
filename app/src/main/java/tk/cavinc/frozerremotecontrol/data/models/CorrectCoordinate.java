package tk.cavinc.frozerremotecontrol.data.models;

/**
 * Created by cav on 27.08.19.
 */

public class CorrectCoordinate {
    private boolean mCorrect = false;
    private int x;
    private int y;

    public CorrectCoordinate(boolean correct, int x, int y) {
        mCorrect = correct;
        this.x = x;
        this.y = y;
    }

    public boolean isCorrect() {
        return mCorrect;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
