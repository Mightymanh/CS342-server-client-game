import java.io.Serializable;
import java.util.ArrayList;

public class GameDetail implements Serializable {
    private static final long serialVersionUID  = 1L;


    public int guessRemain;
    public ArrayList<Integer> position;

    public char guessLetter;

    public String category;

    public int wordLength;

    public int roundWins;
    
    public int roundStatus;

    public int gameStatus;

    public int restart;

}
