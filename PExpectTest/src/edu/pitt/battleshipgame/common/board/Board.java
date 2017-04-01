package edu.pitt.battleshipgame.common.board;

import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;

import edu.pitt.battleshipgame.common.ships.Ship;
import java.util.ArrayList;

public class Board implements Serializable {
    public static final int BOARD_DIM = 10;
    // We could track a Ship-Bool pair but it is just as easy to have
    // two arrays. The Ships array will keep track of the ships on the
    // game board. The moves array will be initialized to false and will
    // change to true when that move is made.
    //private ArrayList < ArrayList < Ship > > theShips;
    private Ship [][] theShips;
    private boolean [][] moves;
    // Keep a list of all ships on this board for quick searching.
    LinkedList<Ship> shipList;
    private String name;
    private ArrayList<Coordinate> shotsFired;
    private int playerId;

    //public Board (String _name) {
    public Board(int Id){
        String _name = "Player " + Id + " board";
        System.out.println(_name+" created");
        theShips = new Ship[BOARD_DIM][BOARD_DIM];
        moves = new boolean[BOARD_DIM][BOARD_DIM];
        shipList = new LinkedList<Ship>();
        name = _name;
        playerId = Id;
        shotsFired = new ArrayList<>();
    }
    public int getPlayerId(){
     return playerId;
    }    
    
    public void addCoordinate(Coordinate e){
        shotsFired.add(e);
    }
    
    public ArrayList<Coordinate> getShots(){
        return shotsFired;
    }
    
    public boolean isCoordinateFiredAlready(Coordinate e){
        if(shotsFired.contains(e)){
            return true;
        }
        else{
            return false;
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void addShip(Ship ship) {
        if (!canShipFit(ship)) {
            throw new IllegalArgumentException("This board already has the maximum amount of: " + ship.getName());
        }
        for (Coordinate coord : ship.getCoordinates()){
            theShips[coord.getRow()][coord.getCol()] = ship;
        }
        shipList.add(ship);
    }
    
    public Ship makeMove(Coordinate move) {
       // moves[move.getCol()][move.getRow()] = true;
        //Ship ship = theShips[move.getCol()][move.getRow()];
        moves[move.getRow()][move.getCol()] = true;
        Ship ship = theShips[move.getRow()][move.getCol()];
        if(ship != null) {
            ship.registerHit();
        }
        return ship;
    }
    
    public boolean canShipFit(Ship ship) {
        int shipCount = 0;
        for (Ship s : shipList) {
            if (s.getType() == ship.getType()) {
                shipCount++;
            }
        }
        if (shipCount >= ship.maxAllowed()) {
            return false;
        } else {
            return true;
        }
    }
    
    public List<Ship> getShipList() {
        return shipList;
    }
    
    public boolean areAllShipsSunk() {
        //System.out.println("SHIPLIST SIZE " + shipList.size());
        for (Ship s : shipList) {
            if (! s.isSunk()) {
                //System.out.println("return false");
                return false;
            }
        }
        //System.out.println("return true");
        return true;
    }
    
    public String toString() {
        return toString(false);
    }
    
    public String toString(boolean showShips) {
        StringBuilder sb = new StringBuilder();
        // Buld an intermediate representation of the board as a character array
        char [][] boardRepresentation = new char[BOARD_DIM+1][BOARD_DIM+3];
        boardRepresentation[0][0] = '+';
        
        
        for (int row = 1; row < BOARD_DIM+1; row++) {
            // The first column will be filled with the row labels
            if(row == 10){
                 boardRepresentation[row][0] = '1';
                boardRepresentation[row][1] = '0';
            }  
            else{
                boardRepresentation[row][0] = ' ';
                boardRepresentation[row][1] = Integer.toString(row).charAt(0);
            }
            
            boardRepresentation[row][2] = ' ';
        }
        boardRepresentation[0][1] = ' ';
        boardRepresentation[0][2] = ' ';
        for (int col = 3; col < BOARD_DIM+3; col++) {
            boardRepresentation[0][col] = Coordinate.reverseColumnLookup(col-3);
          
 
        }
        
        
        for (int row = 0; row < BOARD_DIM; row++) {
            for (int col = 0; col < BOARD_DIM; col++) {
                if (moves[row][col]) {
                    if (theShips[row][col] != null) {
                        boardRepresentation[row+1][col+3] = 'X';
                    } else {
                        boardRepresentation[row+1][col+3] = 'O';
                    }
                }
                else{
                    boardRepresentation[row+1][col+3] = ' ';
                }
                if (showShips && theShips[row][col] != null) {
                    boardRepresentation[row+1][col+3] = theShips[row][col].getId();
                }
            }
        }
        for (char [] row : boardRepresentation) {
            sb.append(row);
            sb.append('\n');
        }
        return sb.toString();
    }
}