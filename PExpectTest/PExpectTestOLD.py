import pexpect
import unittest
import threading
import time

outputBuffer = ["","",""]
childProc = []
def startClient():
    cmd = "java -jar dist/Client/BattleshipClient.jar"
    return pexpect.spawn(cmd)
def startServer():
    cmd = "java -jar dist/Server/BattleshipServer.jar"
    return pexpect.spawn(cmd)
def serverThread():
    child = startServer()
    expect(child,"\n")
    output = child.before
    output = str(output)
    outputBuffer[0]+=str(output)
    while True:
        time.sleep(1)
    
def gameThreadClient1():
    gameThread(1)
def gameThreadClient2():
    gameThread(2)
def gameThread(playerID):
    child = startClient()
    childProc.append(child)
    
    expect(child,"join")
    output = child.before
    output = str(output)
    outputBuffer[playerID]+=output
    #print(output)
    player = 0
    if("0" in output):
        player = 0
    else:
        player = 1 
    
    placeShips(child, playerID)
    print("Player " + str(player)+ " Done Placing Ships" )

    shots = ["A:1", "A:2", "A:3", "A:4", "A:5", 
        "B:1", "B:2", "B:3", "B:4", 
        "C:1", "C:2", "C:3", 
        "D:1", "D:2", "D:3", 
        "E:1", "E:2"
    ]
    if(player == 1):
        time.sleep(1)  
    fireShots(child,shots, playerID)

def sendLine(child,str):
    time.sleep(.25)
    child.sendline(str)

def expect(child,str):
    time.sleep(.25)
    child.expect(str)

def placeShips(child, playerID):
    #print("Placing Carrier")
    expect(child,"Carrier")
    
    sendLine(child,"A:1")
    expect(child,"Carrier")
    #print(child.before)
    sendLine(child,"A:5")
    #print(child.before)

    #print("Placing BShip")
    expect(child,"Battleship")
    sendLine(child,"B:1")
    expect(child,"Battleship")
    sendLine(child,"B:4")
    #print("Placing Cruiser")
    expect(child,"Cruiser")
    sendLine(child,"C:1")
    expect(child,"Cruiser")
    sendLine(child,"C:3")
    #print("Placing Sub")
    expect(child,"Submarine")
    sendLine(child,"D:1")
    expect(child,"Submarine")
    sendLine(child,"D:3")
    #print("Placing Des")
    expect(child,"Destroyer")
    sendLine(child,"E:1")
    expect(child,"Destroyer")
    sendLine(child,"E:2")

    expect(child,"starting")
    output = child.before
    output = str(output)
    outputBuffer[playerID]+=output
    #print(output)

    
def fireShots(child,shots, player):
    b = ""
    for shot in shots:
        outputBuffer[player]+= str(child.before)
        try:
            expect(child,"move?")
        except Exception:
           outputBuffer[player]+= str(child.before)
        
        output= child.before
        #print(output)
        outputBuffer[player]+= str(output)
        #print()
        sendLine(child,shot)
        
        #expect(child,"^")
        #print(child.before)
        #sendLine(child,"")


class TestBattleShip(unittest.TestCase):
    def testStartGame(self):
        server = threading.Thread(target=serverThread)
        server.start()
        time.sleep(2)
        player1 = threading.Thread(target=gameThreadClient1)
        player1.start()
        time.sleep(1)
        player2 = threading.Thread(target=gameThreadClient2)
        player2.start()
        while True:
            time.sleep(1)
            i = int(input("Print Player Buffer: "))
            if(i>=0 and i <3):
                
                print(outputBuffer[i].replace("\\r\\n", "\r\n"))
    
if __name__ == '__main__':
    unittest.main()