This school project is a java8 implementation of a classic game, Connect 4.
In case you're not aware of the game play please review https://en.wikipedia.org/wiki/Connect_Four


If I had more time some areas I would focus on are:
* the inheritence structure between Player and Connect4ComputerPlayer is poor.
I would instead implement an interface that Connect4ComputerPlayer and Player implement, because they have the same behavior.  
* the server code is poor quality, in particular the run method of HandleASession is far too
long and could easily be refactored to remove duplicate code.
* naming could be better in some classes
* aplogies for the unecessary commenting in some areas, this was a requirement for the class (comment every method even getters and setters)


And I'm sure there are a few other things I could improve if I had the time.

INSTRUCTIONS TO PLAY:
-----

To test this project you'll need Java 8.

Open 3 terminals or cmd.exe and navigate to out/production/SER216

In terminal 1 Launch the server:

java core/Connect4Server <enter>

In terminal 2 launch the first game client, 
follow the prompt for UI version and Opponent type (computer or player):

java core/Connect4 <enter>
 
C <enter>

P <enter>

In terminal 3 launch the second game client,
follow the prompt for UI version and Opponent type (computer or player):

java core/Connect4 <enter>

C <enter>

P <enter>

Play Connect 4, enjoy :)
