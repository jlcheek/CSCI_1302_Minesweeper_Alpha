package cs1302.p1;

import java.util.Scanner;
import java.util.Random;

import java.io.File;

/**
 * This class represents a Minesweeper game.
 *
 * @author Jared Cheek <jlcheek@uga.edu>
 */
public class Minesweeper {

    public static int marks = 0;
    public static int mines;
    public static int roundsCompleted = 0;
    public static int rows;
    public static int cols;
    public static String[][] userGuessArray;
    public static int[][] boardArray;
    public boolean running = true;

    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * information provided in <code>seedFile</code>. Documentation about the 
     * format of seed files can be found in the project's <code>README.md</code>
     * file.
     *
     * @param seedFile the seed file used to construct the game
     * @see            <a href="https://github.com/mepcotterell-cs1302/cs1302-minesweeper-alpha/blob/master/README.md#seed-files">README.md#seed-files</a>
     */
    public Minesweeper(File seedFile) {
	File f = seedFile;
	try{
	    Scanner s = new Scanner(f);
	    String string = "";
	    int mineCount = 0;
	    int count = 0;
	    
	    rows = s.nextInt();
	    cols = s.nextInt();

	    userGuessArray = new String[rows][cols];
	    boardArray = new int[rows][cols];
	
	    //for loop to initialize empty mines before they are placed
	    for(int x = 0; x < rows; x++){
		for(int y = 0; y < cols; y++){
		    userGuessArray[x][y] = " ";
		    boardArray[x][y] = 0;
		}//for y

	    }//for x
	    
	    int r;
	    int c;

	    while(s.hasNextInt()){
		if(count == 0){
		    if(rows > 10 || rows < 1 || cols > 10 || cols < 1){
			System.out.println("Cannot create game with " + f.getName() + ", because it is not formatted correctly");
			System.exit(0);
		    }//if
		    else{
			count++;
		    }//else
		    
		}//if
		else if(count == 1){
		    mines = s.nextInt();
		    count++;
		}//else if
		else{
		    r = s.nextInt();
		    c = s.nextInt();
		    mineCount++;
		    boardArray[r][c] = 1;
		    }//else
	    
	    }//while
	    if(mineCount !=  mines){
		System.out.println("Cannot create game with " + f.getName() + ", because it is not formatted correctly");
		System.exit(0);
	    }//if
	    greetingsImage();
	}//try
	catch (Exception e){
	    System.out.println("Cannot create game with " + f.getName() + ", because it is not formatted correctly");
	}//catch

    } // Minesweeper


    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * <code>rows</code> and <code>cols</code> values as the game grid's number
     * of rows and columns respectively. Additionally, One quarter (rounded up) 
     * of the squares in the grid will will be assigned mines, randomly.
     *
     * @param r the number of rows in the game grid
     * @param c the number of cols in the game grid
     */
    public Minesweeper(int r, int c) {
	Scanner s = new Scanner(System.in);

	//if statement set to exit if dimensions aren't correct
	if(r < 1 || r > 10 || c < 1 || c > 10){
	    printError(2);
	    System.out.println();
	    System.exit(0);
	}//if

	userGuessArray = new String[r][c];
	boardArray = new int[r][c];
	
	//initializing the 2 2D arrays for the board
	for(int x = 0; x < r; x++){
	    for(int y = 0; y < c; y++){
		userGuessArray[x][y] = " ";
		boardArray[x][y] = 0;
	    }//y value

	}//x value

	rows = r;
	cols = c;
	mines = (int)Math.ceil(rows * cols * 0.25);
	
	Random ran = new Random();
	//randomizes placement of mines
	for(int x = 0; x < mines; x++){
	    int m = ran.nextInt(rows);
	    int n = ran.nextInt(cols);
	    if(boardArray[m][n] == 0){
		boardArray[m][n] = 1;
	    }//if
	    else{
		x--;
	    }//else

	}//for
	greetingsImage();
    }// Minesweeper
    

    /**
     * Starts the game and execute the game loop.
     *
     * @param b boolean variable that decides if the printGrid should print fog of war feature
     */
    public void run(boolean b) {
	if(b){
	    printGrid(false);
	}
	Scanner s = new Scanner(System.in);
	String input = "";
	int r = 11;
	int c = 11;
	
	try{
	    //running while loop to continue until the game ends or system exits
	    while(running){
		System.out.println();
		System.out.print("minesweeper-alpha$ ");
		input = s.next();
		
		if(input.equals("quit") || input.equals("q")){
		    System.out.println();
		    System.out.println("ლ(ಠ_ಠლ)");
		    System.out.println("Y U NO PLAY MORE?");
		    System.out.println("Bye!");
		    System.out.println();
		    System.exit(0);
		}//quit-if
		else if(input.equals("help") || input.equals("h")){
		    roundsCompleted++;
		    printGrid(false);
		    helpMenu();
		}//help-else if
		else if(input.equals("nofog")){
		    roundsCompleted++;
		    printGrid(true);
		}//no-fog else if
		else{
		    r = s.nextInt();
		    if(r >= rows){
			printError(1);
			run(false);
		    }//if
		    c = s.nextInt();
		    if(c >= cols){
			printError(1);
			run(false);
		    }//if
		    if(r < 0 || r > rows || c < 0 || c > cols){
			printError(1);
		    }//if
		    else{
			if(input.equals("reveal") || input.equals("r")){
			    if(marks > 0){
				marks--;
			    }
			    userGuessArray[r][c] = reveal(r, c)+ "";
			    roundsCompleted++;
			    printGrid(false);
			}//reveal-else if
			else if(input.equals("mark") || input.equals("m")){
			    mark(r, c);
			    marks++;
			    roundsCompleted++;
			    int count = 0;
			    if(marks == mines){
				for(int x = 0; x < rows; x++){
				    for(int y = 0; y < cols; y++){
					if(boardArray[x][y] == 1 && userGuessArray[x][y].equals("F")){
					    count++;
					    if(mines == count){
						playerWins();
						System.exit(0);
					    }//if
					}//if
				    }//for y
				}//for x
			    }//if
			    printGrid(false);
			}//mark-else if
			else if(input.equals("guess") || input.equals("g")){
			    guess(r, c);
			    roundsCompleted++;
			    printGrid(false);
			}//guess-if
			else{
			    printError(1);
			    printGrid(false);
			}//else
			
		    }//else
		    
		}//else
		
	    }//running-while

	}//try
	catch(Exception e){
	    printError(1);
	    run(false);
	}//catch
	
    } // run


    /**
     * The entry point into the program. This main method does implement some
     * logic for handling command line arguments. If two integers are provided
     * as arguments, then a Minesweeper game is created and started with a 
     * grid size corresponding to the integers provided and with 10% (rounded
     * up) of the squares containing mines, placed randomly. If a single word 
     * string is provided as an argument then it is treated as a seed file and 
     * a Minesweeper game is created and started using the information contained
     * in the seed file. If none of the above applies, then a usage statement
     * is displayed and the program exits gracefully. 
     *
     * @param args the shell arguments provided to the program
     */
    public static void main(String[] args) {
	/*
	  The following switch statement has been designed in such a way that if
	  errors occur within the first two cases, the default case still gets
	  executed. This was accomplished by special placement of the break
	  statements.
	*/
	
	Minesweeper game = null;

	switch (args.length) {

        // random game
	case 2: 

	    int rows, cols;

	    // try to parse the arguments and create a game
	    try {
		rows = Integer.parseInt(args[0]);
		cols = Integer.parseInt(args[1]);
		game = new Minesweeper(rows, cols);
		break;
	    } catch (NumberFormatException nfe) { 
		// line intentionally left blank
	    } // try

	// seed file game
	case 1: 

	    String filename = args[0];
	    File file = new File(filename);

	    if (file.isFile()) {
		game = new Minesweeper(file);
		break;
	    } // if
    
        // Display usage statement
	default:

	    System.out.println("Usage: java Minesweeper [FILE]");
	    System.out.println("Usage: java Minesweeper [ROWS] [COLS]");
	    System.exit(0);

	} // switch

	// if all is good, then run the game
	game.run(true); //added boolean parameter for try-catch so when catches error the program doesn't quit and when run() is called again it doesn't execute printGrid();

    } // main
    
    /**
     * This method prints a couple different variations of error messages based on the
     * number that is being passed through the parameters. If a 1 is passed through, then
     * the first error message is printed. If any other number is passed through then the
     * second error message is printed.
     *
     * @param n is an integer that distinguishes between two different error messages
     *
     **/
    public static void printError(int n){
	if(n == 1){
	    System.out.println();
	    System.out.println("ಠ_ಠ says, \"Command not recognized!\"");
	}
	else{
	    System.out.println();
	    System.out.println("ಠ_ಠ says, \"Cannot create a mine field with that many rows and/or columns!\"");
	}
    }

    /**
     * This is a combination of print statements that create the graphics
     * for the grid based on the size <code>rows</code> and <code>cols</code>.
     * Incorporated is a boolean variable that determines whether the fog of 
     * war feature is to be used or not.
     *
     * @param fogOfWar is a boolean that tells the nested for loop to show the mines if true
     *
     **/
    public static void printGrid(boolean fogOfWar){
	System.out.println();
	System.out.println("Rounds Completed: " + roundsCompleted);
	System.out.println();
	String rowString = "";

	//for-loop to create the grid by rows
	for(int x = 0; x < rows; x++){
	    rowString += " " + x + " |";
	    for(int y = 0; y < cols; y++){
		if(fogOfWar && boardArray[x][y] == 1){
		    rowString += "<" + userGuessArray[x][y] + ">|";
		}//if
		else{
		    rowString += " " + userGuessArray[x][y] + " |";
		}//else
	    }//row-loop
	    System.out.println(rowString);
	    rowString = "";
	}//column-loop
	
	System.out.print("  ");
	//for-loop to create the bottom scale
	for(int x = 0; x < cols; x++){
	    System.out.print("   " + x);
	}
	System.out.println();
    }//printGrid

    /**
     * This method takes in two points to plot an "F" where the user knows
     * a mine has been placed. This has the most important effect on the
     * game because in order to win you must place the exact number of marks
     * as there are mines and in all the correct places. 
     *
     * @param r is the row of a given grid point
     * @param c is the column of a given grid point
     *
     **/
    public static void mark(int r, int c){
	userGuessArray[r][c] = "F";
    }//mark
    
    /**
     * This method takes in two points to plot a "?" where the user thinks
     * a mine is. This has no effect on the game but is just a place marker
     * to help the user for strategy.
     *
     * @param r is the row of a given grid point
     * @param c is the column of a given grid point
     *
     **/
    public static void guess(int r, int c){
	userGuessArray[r][c] = "?";
    }//guess
    
    /**
     * This is the command that takes in the specified point of row and col
     * to find and report the number of mines adjacent to that specified point.
     * 
     * @param r is the row of a given grid point
     * @param c is the column of a given grid point
     * @return returns the number of mines adjacent to the given point
     *
     **/
    public static int reveal(int r, int c){
	int count = 0;
	
	//nested for-loop to iterate adjacent points to see if there are mines
	//r 5 5
	for(int y = c - 1; y < c + 2; y++){
	    for(int x = r - 1; x < r + 2; x++){
		//if the original point contains a bomb
		if(x == r && y == c && boardArray[r][c] == 1){
		    gameOver();
		    System.exit(0);
		}//if
		//To make sure the points do not go out of bounds
		if((y >= 0) && (y <= cols) && (x >= 0) && (x <= rows)){
		    //if any of the points adjacent contain a mine
		    if(boardArray[x][y] == 1){
			count++;
		    }//if
		    
		}//if
	 	
	    }//for y

	}//for x
	    return count;
    }//reveal

    /**
     * This is the combination of print statements that greets the user once the
     * program has been configured properly and is ready to start the game.
     *
     **/
    public static void greetingsImage(){
	System.out.println("        _");
	System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __");
	System.out.println(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|");
	System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
	System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
	System.out.println("                             ALPHA EDITION |_| v2017.f");
	System.out.println();
    }//greetingsImage

    /**
     * This is the combination of print statements that print out a neat little image 
     * telling the user that he or she has hit a mine and that the game is now over.
     * The game then ends gracefully.
     *
     **/
    public static void gameOver(){
	System.out.println();
	System.out.println(" Oh no... You revealed a mine!");
	System.out.println("  __ _  __ _ _ __ ___   ___    _____   ____ _ __");
	System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / /_ \\ '__|");
	System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V / __/ |");
	System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/\\___|_|");
	System.out.println(" |___/                                               ");
	System.out.println();
    }//gameOver image
    
    /**
     * This is the combination of print statements used when the user
     * uses the "help/h" command in search of help for a list of 
     * commands to play the game.
     *
     **/
    public static void helpMenu(){
	System.out.println();
	System.out.println("Commands Available...");
	System.out.println("- Reveal: r/reveal row col");
	System.out.println("-   Mark: m/mark   row col");
	System.out.println("-  Guess: g/guess  row col");
	System.out.println("-   Help: h/help");
	System.out.println("-   Quit: q/quit");
	System.out.println();
    }//helpMenu

    /**
     * This is the combination of print statements used to notify the user
     * that he or she has won the game also congratulating them and showing
     * them their score for the entire game.
     *
     **/
    public static void playerWins(){
	System.out.println();
	System.out.println(" ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"");
	System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
	System.out.println(" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"");
	System.out.println(" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
	System.out.println(" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"");
	System.out.println(" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
	System.out.println(" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"");
	System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
	System.out.println(" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░");
	System.out.println(" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
	System.out.println(" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
	System.out.println(" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌");
	System.out.println(" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
	System.out.println(" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
	System.out.println(" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
	System.out.println(" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
	System.out.println(" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!");
	System.out.println(" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!");
	System.out.println(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " + getScore(rows, cols, mines, roundsCompleted));
    }//playerWins
    /**
     * This is where the score is computed at the end of the game once
     * the user has successfully placed the correct number of flags on 
     * on the mines and has won the game.
     *
     * @param r the number of rows in the game grid
     * @param c the number of cols in the game grid
     * @param mines the number of mines in the game grid
     * @param rounds the number of rounds it took the user to win
     * @return returns the score of the game
     */
    public static int getScore(int r, int c, int mines, int rounds){
	int score = (r * c) - mines - rounds;
	return score;
    }//getScore

} // Minesweeper
