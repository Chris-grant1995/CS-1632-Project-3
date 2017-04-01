package edu.pitt.battleshipgame.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.util.ArrayList;

import edu.pitt.battleshipgame.common.Serializer;
import edu.pitt.battleshipgame.common.board.*;
import edu.pitt.battleshipgame.common.*;

public class ClientWrapper implements GameInterface {
    ServerInterface serverInterface = null;
    int myPlayerID;

    private static ServerInterface getServer() {
        URL url = null;
        try {
            url = new URL("http://localhost:9999/battleship?wsdl");
        } catch (MalformedURLException e) {
            System.err.println(e);
        }
        QName qname = new QName("http://server.battleshipgame.pitt.edu/", "ServerWrapperService");
        Service service = Service.create(url, qname);
        return service.getPort(ServerInterface.class);
    }
    
    public ClientWrapper() {
        serverInterface = getServer();
    }
    
    @Override
    public int registerPlayer() {
        return serverInterface.registerPlayer();
    }
    
    @Override
    public void confirmDonePlacing() {
        serverInterface.confirmDonePlacing();
    }
    
    @Override
    public boolean wait(int playerID) {
        return serverInterface.wait(playerID);
        //return false;
    }
    
    @Override
    public void setBoards(ArrayList<Board> boards, int playerID, boolean change) {
        serverInterface.setBoards(Serializer.toByteArray(boards), playerID, change);
    }
    
    /**
     * Client side wrapper around the 
     * @return 
     */
    @Override
    public ArrayList<Board> getBoards() {
        return (ArrayList<Board>) Serializer.fromByteArray(serverInterface.getBoards());
    }
    
    public boolean isGameOver() {
        return serverInterface.isGameOver();
    }
    
    public int getWinner(){
        return serverInterface.getWinner();
    }
    
    public void GameReset(){
        serverInterface.GameReset();
    }
}