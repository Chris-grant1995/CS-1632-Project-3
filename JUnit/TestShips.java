/**
 * Created by christophergrant on 3/25/17.
 */


import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestShips {

    public Board createBoard(){
        Board b = new Board("TestBoard");
        return b;
    }

    @Test
    public void testShipFactoryCreation(){
        Board b = createBoard();
        Assert.assertNotNull(b);
        ShipFactory.newShipFromType(Ship.ShipType.BATTLESHIP,new Coordinate("A:1"), new Coordinate("A:4"),b);
        List<Ship> ships = b.getShipList();
        Ship bship = ships.get(0);
        Assert.assertNotNull(bship);
        Assert.assertEquals(bship.getName(),"Battleship");
        Assert.assertEquals(bship.getCoordinates().get(0).toString(),"A:1" );
        //System.out.println(bship.getCoordinates().get(4));
        Assert.assertEquals(bship.getCoordinates().get(3).toString(),"A:4" );
        Assert.assertEquals(bship.getLength(),4 );
    }
    @Test
    public void testShipFactoryName(){
        Assert.assertEquals(ShipFactory.getNameFromType(Ship.ShipType.BATTLESHIP), "Battleship");
        Assert.assertEquals(ShipFactory.getNameFromType(Ship.ShipType.CARRIER), "Carrier");
        Assert.assertEquals(ShipFactory.getNameFromType(Ship.ShipType.CRUISER), "Cruiser");
        Assert.assertEquals(ShipFactory.getNameFromType(Ship.ShipType.SUBMARINE), "Submarine");
        Assert.assertEquals(ShipFactory.getNameFromType(Ship.ShipType.DESTROYER), "Destroyer");

    }
    @Test
    public void testShipPlacement(){
        int VALIDTESTS = 4;
        String[][] shipsAndCoords = {
                {"Carrier", "A:1","A:5"},//Valid Left To Right
                {"Carrier", "A:5","A:1"},//Valid Right to Left
                {"Carrier", "A:1","E:1"},//Valid Up to Down
                {"Carrier", "E:1","A:1"},//Valid Down to Up
                {"Carrier", "A:1","E:5"},//Invalid Diag
                {"Carrier", "E:1","A:5"},//Invalid Diag
                {"Carrier", "A:5","E:1"},//Invalid Diag
                {"Carrier", "E:5","A:1"},//Invalid Diag
                {"Carrier", "A:1","A:2"},//Invalid small
                {"Carrier", "E:5","D:4"},//Invalid small diag

        };

        for(int i =0; i< shipsAndCoords.length; i++){
            if(i <VALIDTESTS ){
                Assert.assertTrue(new Coordinate().checkShipPlacement(shipsAndCoords[i][0],shipsAndCoords[i][1],shipsAndCoords[i][2]));
            }
            else{
                Assert.assertFalse(new Coordinate().checkShipPlacement(shipsAndCoords[i][0],shipsAndCoords[i][1],shipsAndCoords[i][2]));
            }
        }


    }
    @Test
    public void testShipCoords(){
        String[] coords = {"A:1", "1:A", "A1", "1A","M:2"};
        Coordinate c = new Coordinate("A:1");
        //System.out.println(c.setCoordinates("1A"));
        for(int i =0; i< coords.length; i++){
            if(i<1){
                Assert.assertTrue(c.setCoordinates(coords[i]));
            }
            else{
                Assert.assertFalse(c.setCoordinates(coords[i]));
            }
        }
    }
    @Test
    public void testDuplicateShips(){
        Board b = createBoard();
        Assert.assertNotNull(b);
        ShipFactory.newShipFromType(Ship.ShipType.BATTLESHIP,new Coordinate("A:1"), new Coordinate("A:4"),b);
        try{
            ShipFactory.newShipFromType(Ship.ShipType.BATTLESHIP,new Coordinate("E:1:"), new Coordinate("E:4"),b);
        }
        catch (Exception e){

        }
        Assert.assertEquals(b.shipList.size(),1);//Should be 1 because ship 2 should not be created
    }


    @Test
    public void testMakeMove(){
        Board b = createBoard();
        ShipFactory.newShipFromType(Ship.ShipType.BATTLESHIP,new Coordinate("A:1"), new Coordinate("A:4"),b);
        Ship hitShip = b.makeMove(new Coordinate("A:1"));
        Ship bship = b.getShipList().get(0);

        Assert.assertEquals(bship.getName(),hitShip.getName());


        hitShip = b.makeMove(new Coordinate("A:5"));
        Assert.assertNull(hitShip);

    }
    @Test
    public void testCanShipFit(){
        MockBoard b = new MockBoard("MockBoard");
        MockShip s = new MockShip(new Coordinate("A:1"), new Coordinate("A:5"), b);
        MockShip s1 = new MockShip(new Coordinate("A:1"), new Coordinate("A:5"));
        s1.setType(MockShip.ShipType.BATTLESHIP);
        Assert.assertFalse(b.canShipFit(s1));
        s1.setType(MockShip.ShipType.CRUISER);
        Assert.assertTrue(b.canShipFit(s1));


    }
    @Test
    public void testToStringShowingShips(){
        Board b = createBoard();
        ShipFactory.newShipFromType(Ship.ShipType.BATTLESHIP, new Coordinate("A:1"), new Coordinate("A:4"),b);
        String s = b.toString(true);
        int count = s.length() - s.replace("S", "").length();
        Assert.assertEquals(4,count);

    }
    @Test
    public void testAllShipsSunk(){
        MockBoard b = new MockBoard("MockBoard");
        MockShip s = new MockShip(new Coordinate("A:1"), new Coordinate("A:5"), b);
        Assert.assertFalse(b.areAllShipsSunk());

        s.setHitCount(5);
        Assert.assertTrue(b.areAllShipsSunk());
    }
    @Test
    public void testGetCoordinates(){
        MockShip s = new MockShip(new Coordinate("A:1"), new Coordinate("A:5"));
        List<Coordinate> coords  = s.getCoordinates();
        String strcoords = "";
        String strcoords2 = "A:1,A:2,A:3,A:4,A:5,";
        for(Coordinate c: coords){
            strcoords+= c.toString() + ",";
        }
        Assert.assertEquals(strcoords,strcoords2);

        s = new MockShip(new Coordinate("C:1"), new Coordinate("E:1"));
        coords  = s.getCoordinates();
        strcoords = "";
        strcoords2 = "C:1,D:1,E:1,";
        for(Coordinate c: coords){
            strcoords+= c.toString() + ",";
        }
        Assert.assertEquals(strcoords,strcoords2);

    }

    @Test
    public void testNumberOfHits(){
        MockBoard b = new MockBoard("MockBoard");
        MockShip s = new MockShip(new Coordinate("A:1"), new Coordinate("A:5"), b);
        b.makeMove(new Coordinate("A:1"));
        Assert.assertEquals(s.hitCount,1);
    }

}
