/**
 * Arena class
 *
 * originally written by Elizabeth Sklar and Pablo Funes, summer 1998
 *
 */


import java.awt.*;
import java.lang.*;
import java.applet.*;

public class HumanPlayer extends Player {


    public HumanPlayer( String n, Color c, Arena a, int x, int y, byte number ) {
	name = n;
	color = c;
	arena = a;
	x_max = x;
	y_max = y;
	score = 0;
	player_no = number;

    } // end of HumanPlayer constructor
    

    // human player's d is controlled by interrupt-driven keyboard
    // handler; doesn't have to implement whereDoIGo
    
    
    public void go( int x,int y ) {
	super.go(x,y); // do everything the original GO does, plus custom inits
	didsomething = false;
    } // end of go()



} /* end of HumanPlayer class */
