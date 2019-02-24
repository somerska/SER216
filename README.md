This was a school project I completed in Spring 2019.
This project is a java8 implementation of a classic game, Connect 4.
In case you're not aware of the game play please review https://en.wikipedia.org/wiki/Connect_Four


This project has some flaws in both design and implementation but it works.
If I had more time some areas I would focus on are:
* the inheritence structure between Player and Connect4ComputerPlayer is poor.
I would instead implement an interface (or possibly an abstract class) IPlayer
that Connect4ComputerPlayer and Player inherit from.  
* the server code is poor quality, in particular the run method of HandleASession is far too
long and could easily be refactored to remove duplicate code.
* naming could be better in some classes

And I'm sure there are a few other things I could improve if I had the time.

INSTRUCTIONS TO PLAY:
-----

To test this project you'll need Java 8.

Open 3 terminals or cmd.exe and navigate to out/production/SER216

In terminal 1 Launch the server:
java core/Connect4Server

In terminal 2 launch the first game client, 
follow the prompt for UI version and Opponent type (computer or player):
java core/Connect4
C
P

In terminal 3 launch the second game client,
follow the prompt for UI version and Opponent type (computer or player):
java core/Connect4
C
P

Play Connect4.