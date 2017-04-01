package edu.pitt.battleshipgame.common;

import edu.pitt.battleshipgame.common.board.*;
import java.util.ArrayList;

public interface GameInterface {
    int registerPlayer();
    void confirmDonePlacing();
    boolean wait(int playerID);
    ArrayList<Board> getBoards();
    void setBoards(ArrayList<Board> boards, int playerID, boolean change);
    boolean isGameOver();
    int getWinner();
    void GameReset();
}