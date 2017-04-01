package edu.pitt.battleshipgame.client;

import java.util.ArrayList;
import java.util.Scanner;

import edu.pitt.battleshipgame.common.board.*;
import edu.pitt.battleshipgame.common.ships.*;
import edu.pitt.battleshipgame.common.GameInterface;
import edu.pitt.battleshipgame.common.GameTracker;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Client {

    public static GameInterface gi;
    public static int myPlayerID;
    public static ArrayList<Board> gameBoards;
    public static Scanner scan = new Scanner(System.in);
    private static boolean[][] placements;
    private static final String shipPlacementRules = "Ship must only be placed vertically or horizontally";
    private static final String illegalCoordinates = "Coordinates must have the same size as the ships";

    public static void main(String[] args) throws IOException {
//        gi = new ClientWrapper();
//        placements = new boolean[10][10];
//        myPlayerID = gi.registerPlayer();
//        System.out.println("You have registered as Player " + myPlayerID);
//        System.out.println("Please wait for other players to join");
//        gi.wait(myPlayerID);
//        System.out.println("Both Players have joined, starting the game.");
//        gameBoards = gi.getBoards();
//        placeShips(gameBoards.get(myPlayerID));
//        System.out.println("Your board:");
//        System.out.println(gameBoards.get(myPlayerID).toString(true));
//        System.out.println("Waiting for ship placement to finish. Please Wait.");
//        gi.confirmDonePlacing();
//        gi.setBoards(gameBoards, myPlayerID, false);
//        gi.wait(myPlayerID);
//        gameBoards = gi.getBoards();
//
//        // gameLoop(gameBoards.get(myPlayerID));
//        gameLoop(gameBoards.get(myPlayerID), getOpponentBoard(myPlayerID));
        Client c = new Client();
        c.GameStart();
    }

    public void GameStart() throws IOException {
        boolean cGame = true;
        while (cGame) {
            gi = new ClientWrapper();
            placements = new boolean[10][10];
            myPlayerID = gi.registerPlayer();
            System.out.println("You have registered as Player " + myPlayerID);
            System.out.println("Please wait for other players to join");
            gi.wait(myPlayerID);
            System.out.println("Both Players have joined, starting the game.");
            gameBoards = gi.getBoards();
            placeShips(gameBoards.get(myPlayerID));
//            System.out.println("Your board:");
//            System.out.println(gameBoards.get(myPlayerID).toString(true));
            System.out.println("Waiting for ship placement to finish. Please Wait.");
            gi.confirmDonePlacing();
            gi.setBoards(gameBoards, myPlayerID, false);
            gi.wait(myPlayerID);
            gameBoards = gi.getBoards();

            // gameLoop(gameBoards.get(myPlayerID));
            gameLoop(gameBoards.get(myPlayerID), getOpponentBoard(myPlayerID));
            cGame = continueGame();

        }
    }

    public boolean continueGame() {
        boolean cGame = true;
        while (true) {
            System.out.println("Do you want to play another game? (Y/N)");
            String response = scan.nextLine();
            if (response.equals("N") || response.equals("n")) {
                cGame = false;
                break;
            } else if (response.equals("Y") || response.equals("y")) {
                if (gi.getBoards().size() > 1) {
                    gi.GameReset();
                }
                System.out.println("New Game Starting!");
                break;
            } else {
                System.out.println("You enter an invalid value.");
            }
        }
        return cGame;
    }

    public static void placeShips(Board board) throws IllegalArgumentException {
        for (Ship.ShipType type : Ship.ShipType.values()) {
            System.out.println("Your Board:");
            System.out.println(board.toString(true));
            Coordinate start, end;

            if (type != Ship.ShipType.NONE) {
                while (true) {
                    System.out.println("Please enter a start coordinate to place your " + ShipFactory.getNameFromType(type) + " (" + ShipFactory.getLengthFromType(type) + " spaces)");
                    while (true) {
                        try {
                            start = new Coordinate(scan.nextLine());
                            break;

                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid Coordinate: Try Again");
                            System.out.println("Please enter a start coordinate to place your " + ShipFactory.getNameFromType(type) + " (" + ShipFactory.getLengthFromType(type) + " spaces)");
                        } catch (ArrayIndexOutOfBoundsException ae) {
                            System.out.println("Invalid Coordinate: Try Again");
                            System.out.println("Please enter a start coordinate to place your " + ShipFactory.getNameFromType(type) + " (" + ShipFactory.getLengthFromType(type) + " spaces)");
                        }

                    }//end start while loop
                    System.out.println("Please enter an end coordinate to place your " + ShipFactory.getNameFromType(type) + " (" + ShipFactory.getLengthFromType(type) + " spaces)");
                    while (true) {
                        try {
                            end = new Coordinate(scan.nextLine());
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid Coordinate: Try Again");
                            System.out.println("Please enter an end coordinate to place your " + ShipFactory.getNameFromType(type) + " (" + ShipFactory.getLengthFromType(type) + " spaces)");
                        } catch (ArrayIndexOutOfBoundsException ae) {
                            System.out.println("Invalid Coordinate: Try Again");
                            System.out.println("Please enter a end coordinate to place your " + ShipFactory.getNameFromType(type) + " (" + ShipFactory.getLengthFromType(type) + " spaces)");
                        }
                    } //end end while loop 

                    //if the rows AND the columns both dont match, then the ship must be placed in a diagonal fashion
                    if (start.getRow() != end.getRow() && start.getCol() != end.getCol()) {
                        System.out.println(shipPlacementRules);
                    } //if the ship is vertical 
                    else if (start.getCol() == end.getCol()) {

                        int start_val = Math.min(start.getRow(), end.getRow());
                        int end_val = Math.max(start.getRow(), end.getRow());
                        if (start_val != start.getRow()) {
                            Coordinate temp = start;
                            start = end;
                            end = temp;
                        }

                        if (Math.abs(start_val - end_val) + 1 != ShipFactory.getLengthFromType(type)) {
                            System.out.println(illegalCoordinates);
                        } else {
                            boolean isValidPlacement = isValidPlacement(start, end);
                            if (isValidPlacement) {
                                for (int i = start_val; i <= end_val; i++) {
                                    placements[i][start.getCol()] = true;
                                }
                                break;
                            } else {
                                System.out.println("Ships cannot be overlapped. Try again.");
                            }
                        }
                    } //if the ship is horizontal 
                    else {
                        int start_val = Math.min(start.getCol(), end.getCol());
                        int end_val = Math.max(start.getCol(), end.getCol());

                        if (start_val != start.getCol()) {
                            Coordinate temp = start;
                            start = end;
                            end = temp;
                        }

                        if (Math.abs(start_val - end_val) + 1 != ShipFactory.getLengthFromType(type)) {
                            System.out.println(illegalCoordinates);
                        } else {
                            boolean isValidPlacement = isValidPlacement(start, end);
                            if (isValidPlacement) {
                                for (int i = start_val; i <= end_val; i++) {
                                    placements[start.getRow()][i] = true;
                                }

                                break;
                            } else {
                                System.out.println("Ships cannot be overlapped. Try again.");
                            }
                        }
                    }

                }
                // We don't need to track a reference to the ship since it will be
                // on the board.
                ShipFactory.newShipFromType(type, start, end, board);
            }
        }
    }

    public static boolean isValidPlacement(Coordinate s, Coordinate e) {
        int true_coord_end_row = Math.max(s.getRow(), e.getRow());
        int true_coord_start_row = Math.min(s.getRow(), e.getRow());
        int true_coord_end_col = Math.max(s.getCol(), e.getCol());
        int true_coord_start_col = Math.min(s.getCol(), e.getCol());

        //vertical
        if (true_coord_start_col == true_coord_end_col) {
            for (int i = true_coord_start_row; i <= true_coord_end_row; i++) {
                if (placements[i][true_coord_start_col] == true) {
                    return false;
                }
            }

        } //horizontal
        else {
            for (int i = true_coord_start_col; i <= true_coord_end_col; i++) {
                if (placements[true_coord_start_row][i] == true) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void gameLoop(Board myBoard, Board opBoard) throws IOException {
        System.out.println("The game is starting!");
        boolean isOver = false;
        int numCoords = 0;
        boolean isFired = false;
        Coordinate move;
        String trash;

        doWhileLoop:
        {
            do {
                System.out.println("\nCurrent board:");
                System.out.println(opBoard.toString());

                // Wait for our turn
                boolean isDone = false;
                while (isDone == false) {
                    isDone = gi.wait(myPlayerID);
                    if (System.in.available() > 0) {
                        System.out.println("It is not your turn yet. Please wait.");
                        trash = scan.nextLine();
                    }
                    if (gi.isGameOver()) {
                        break doWhileLoop;
                    }
                }

                isFired = false;
                // Get the updated boards
                //gameBoards = gi.getBoards();
                System.out.println("Where would you like to place your move?");

                while (true) {
                    try {
                        isFired = false;
                        move = new Coordinate(scan.nextLine());
                        //boolean isFired = gameBoards.get((myPlayerID + 1) % GameTracker.MAX_PLAYERS).isShotFiredAlready(move);
                        for (int i = 0; i < numCoords; i++) {
                            Coordinate currCoord = opBoard.getShots().get(i);
                            if (currCoord.isCoordEqual(move)) {
                                isFired = true;
                            }
                        }
                        //boolean isFired = board.isCoordinateFiredAlready(move);//coordChecker.contains(move);
                        opBoard.addCoordinate(move);
                        numCoords++;
                        if (isFired == false) {
                            Ship ship = opBoard.makeMove(move);
                            if (ship == null) {
                                System.out.println("Miss");
                            } else if (ship.isSunk()) {
                                System.out.println("You sunk " + ship.getName());
                            } else {
                                System.out.println("Hit");
                            }
                            break;
                        } else {
                            System.out.println("COORDINATE ALREADY FIRED AT");
                            System.out.println("Where would you like to place your move?");
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid Coordinate: Try Again");
                        System.out.println("Where would you like to place your move?");
                    }

                }

                // Send the updated boards.
                //gi.setBoards(gameBoards,myPlayerID, true);
                gi.setBoards(gameBoards, opBoard.getPlayerId(), isDone);
                //isOver = gi.isGameOver();
                //} while(isOver == false);
            } while (!gi.isGameOver());
        }
        System.out.println("The Game is Over!");
        if (gi.getWinner() > -1) {
            System.out.println("The winner is Player" + gi.getWinner());
        } else {
            System.out.println("The game ended before a winner was crowned");
        }
    }

    private static Board getOpponentBoard(int myPlayerID) {
        Board nullboard = null;
        for (Board board : gameBoards) {
            if (!board.getName().equals("Player " + myPlayerID + " board")) {
                return board;
            }
        }
        return nullboard;
    }
}
