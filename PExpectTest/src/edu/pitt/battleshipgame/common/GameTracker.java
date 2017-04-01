package edu.pitt.battleshipgame.common;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.battleshipgame.common.board.Board;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameTracker implements GameInterface {

    public static final int MAX_PLAYERS = 2;
    private int registeredPlayers = 0;
    private int playersDonePlacing = 0;
    private ArrayList<Board> gameBoards;
    private int[] players;
    private GameState state = GameState.INIT;
    private int playerTurn = 0;
    private int winner = -1;
    Object lock;
    public static Scanner scan = new Scanner(System.in);

    public GameTracker() {
        // Exists to protect this object from direct instantiation
        lock = new Object();
        gameBoards = new ArrayList<Board>(MAX_PLAYERS);
        players = new int[MAX_PLAYERS];
        System.out.println("Server constructed.");
    }

    public void GameReset() {
        lock = new Object();
        gameBoards = new ArrayList<Board>(MAX_PLAYERS);
        players = new int[MAX_PLAYERS];
        registeredPlayers = 0;
        playersDonePlacing = 0;
        state = GameState.INIT;
        playerTurn = 0;
        winner = -1;
        System.out.println("Server Reset.");
    }

    public int registerPlayer() {
        synchronized (lock) {
            registeredPlayers++;
            //gameBoards.add(new Board("Player " + (registeredPlayers - 1) + " board"));
            gameBoards.add(new Board(registeredPlayers - 1));

        }
        return registeredPlayers - 1;
    }

    public void confirmDonePlacing() {
        synchronized (lock) {
            playersDonePlacing++;
        }
    }

    public boolean wait(int playerID) {
        switch (state) {
            case INIT: {
                System.out.println("Player " + playerID + " is waiting for other players");
                while (registeredPlayers < MAX_PLAYERS) {
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        System.err.println(e + " I can't sleep!");
                    }
                }
                state = GameState.PLACING;
                break;
            }
            case PLACING: {
                while (playersDonePlacing < MAX_PLAYERS) {
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        System.err.println(e + " I can't sleep!");
                    }
                }
                state = GameState.PLAYING;
                break;
            }
            case PLAYING: {

                if (playerTurn == playerID) {
                    return true;
                }

                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    System.err.println(e + " I can't sleep!");
                }

                return false;
            }
            default:
                break;
        }
        return true;
    }

    public ArrayList<Board> getBoards() {
        System.out.println("numBoards on get: " + gameBoards.size());
        for (Board board : gameBoards) {
            System.out.println(board.getName() + " has " + board.getShipList().size() + " ships");
        }
        return gameBoards;
    }

    public void setBoards(ArrayList<Board> boards, int playerID, boolean changeTurns) {

        gameBoards.set(playerID, boards.get(playerID));
        System.out.println("numBoards on set: " + gameBoards.size());
        for (Board board : gameBoards) {
            System.out.println(board.getName() + " has " + board.getShipList().size() + " ships");
        }
        if (changeTurns == true) {
            playerTurn = (playerTurn + 1) % registeredPlayers;
        }
    }

    public boolean isGameOver() {
        System.out.println("Checking if the game is over...");
        for (Board board : gameBoards) {
            if (board.areAllShipsSunk()) {
                winner = board.getPlayerId();
                return true;
            }
        }
        return false;
    }

    public int getWinner() {
        return winner;
    }
}
