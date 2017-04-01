import pexpect
import unittest
import threading
import time



def startClient():
    cmd = "java -jar dist/Client/BattleshipClient.jar"
    return pexpect.spawn(cmd)
def startServer():
    cmd = "java -jar dist/Server/BattleshipServer.jar"
    return pexpect.spawn(cmd)
def serverThread():
    child = startServer()
    child.expect("\n")
    output = child.before
    output = str(output)
    while playingGame:
        time.sleep(1)
        #print(playingGame)
    print("Server Thread Done")
    
def gameThread():
    child = startClient()
    child.expect("join")
    output = child.before
    output = str(output)
    #print(child.before)
    #print(child.after)
    player = 0
    if("0" in output):
        player = 0
    else:
        player = 1 
    print("Player " + str(player) + " joined")
    
    placeShips(child)
    print("Player " + str(player)+ " Done Placing Ships" )

    shots = ["A:1", "A:2", "A:3", "A:4", "A:5", 
        "B:1", "B:2", "B:3", "B:4", 
        "C:1", "C:2", "C:3", 
        "D:1", "D:2", "D:3", 
        "E:1", "E:2"
    ]
    fireShots(child,shots, player)
    
    print("Client Thread " + str(player) + " Done")
    global playingGame
    playingGame = bool(False)

def placeShips(child):
    #print("Placing Carrier")
    child.expect("Carrier")
    child.sendline("A:1")
    child.expect("Carrier")
    child.sendline("A:5")

    #print("Placing BShip")
    child.expect("Battleship")
    child.sendline("B:1")
    child.expect("Battleship")
    child.sendline("B:4")
    #print("Placing Cruiser")
    child.expect("Cruiser")
    child.sendline("C:1")
    child.expect("Cruiser")
    child.sendline("C:3")
    #print("Placing Sub")
    child.expect("Submarine")
    child.sendline("D:1")
    child.expect("Submarine")
    child.sendline("D:3")
    #print("Placing Des")
    child.expect("Destroyer")
    child.sendline("E:1")
    child.expect("Destroyer")
    child.sendline("E:2")

    child.expect("9")
    output = child.before
    output = str(output)
    #print(output)

    
def fireShots(child,shots, player):
    

    # child.expect(".]")
    # output = child.before
    # output = str(output)
    # #print(output)

    # child.expect(".]")
    # output = child.before
    # output = str(output)
    # #print(output)

    # child.sendline("A:1")
    # child.expect(".Where")
    # output = child.before
    # output = str(output)
    # print(output)
    # if("Hit" in output):
    #     print("Player " + str(player) + " hit on A:1")
    # else:
    #     print("Player " + str(player) + " miss on A:1")


    for shot in shots:
        time.sleep(1)
        child.expect("move?|winner ")
        if("Over" in str(child.before)):
            print("The game is over, player " + str(player) + " lost")
            break
        
        #print(output)

        child.sendline(shot)
        # if(shot == "D:3"):
        #     child.expect(".Sub")
        #     output = child.before
        #     output = str(output)
        #     print(output)
        try:
            child.expect(".t|s")
            output = child.before
            output = str(output)
            #print(output)

        except Exception:
            print(child.before)
        

        output = child.before
        output = str(output)
        #print(output)
        if("H" in output):
            print("Player " + str(player) + " hit on " + shot)
        elif("You" in output):
            print("Player " + str(player) + " sunk a ship")
        else:
            print("Player " + str(player) + " miss on " + shot)



def testStartGame():
    server = threading.Thread(target=serverThread)
    server.start()
    time.sleep(1)
    player1 = threading.Thread(target=gameThread)
    player1.start()
    time.sleep(1)
    player2 = threading.Thread(target=gameThread)
    player2.start()
    while playingGame:
        time.sleep(1)
        #print(playingGame)
    print("Main Thread Done")
playingGame = True   
testStartGame()