// The "Items" class.
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;

/** Tetris Item Class
  * @author Aysar Khalid and Or-el Mousaffi
  * @version 1.0 - January 7th, 2007
  */
public class Items
{
    final int[] ITEMS = {10, 11, 12, 13, 14, 15};

    // Shape variables to keep track of a shape's data (characteristics)
    private int type; // Keeps track of the index for the type of a shape
    private int row;
    private int column;
    private int item;
    private int itemCount;

    private Image[] blocks;

    public Items ()
    {
	// Generate a random spot and random shape
	int randomRow = (int) (Math.random () * 22) + 5;
	int randomCol = (int) (Math.random () * 13) + 1;

	this.type = (int) (Math.random () * (ITEMS.length));
	this.row = randomRow;
	this.column = randomCol;

	//Generate the randomized shape
	if (frequency ())
	{
	    item = ITEMS [type];
	}
    } // Constructor


    /** Adds the shape into the grid/game field to be drawn
    *@param grid The grid or game field that will be played on
    */
    public void addToGrid (int[] [] grid)
    {
	if (grid [this.row] [this.column] == 0)
	{
	    grid [this.row] [this.column] = item;
	}
    }


    /** Determines the frequency rate of if the item should be displayed or not
	randomly.
      * @return True if the item should be displayed, flase otherwise.
    */
    public boolean frequency ()
    {
	if (item == 10) // Increase Points
	{
	    int randomizer = (int) (Math.random () * 10);

	    if (randomizer == 5)
	    {
		return true;
	    }
	    return false;
	}
	else if (item == 11) // Clear Blocks
	{
	    int randomizer = (int) (Math.random () * 100);

	    if (randomizer == 50)
	    {
		return true;
	    }
	    return false;
	}
	else if (item == 12) // Decrease Points
	{
	    int randomizer = (int) (Math.random () * 10);

	    if (randomizer == 5)
	    {
		return true;
	    }
	    return false;
	}
	else if (item == 13) // Decrease Speed
	{
	    int randomizer = (int) (Math.random () * 50);

	    if (randomizer == 25)
	    {
		return true;
	    }
	    return false;
	}
	else if (item == 14) // Change Shape
	{
	    int randomizer = (int) (Math.random () * 75);

	    if (randomizer == 35)
	    {
		return true;
	    }
	    return false;
	}
	else // Increase Speed
	{
	    int randomizer = (int) (Math.random () * 50);

	    if (randomizer == 25)
	    {
		return true;
	    }
	    return false;
	}
    }


    /** Searches the whole game field checking if an item has been placed
      * @param grid The grid or game field that will be played on
      * @return True if it has been placed, false otherwise
    */
    public boolean isItemPlaced (int[] [] grid)
    {
	for (int row = 0 ; row < grid.length ; row++)
	{
	    for (int col = 0 ; col < grid [row].length ; col++)
	    {
		if (grid [row] [col] > 9)
		{
		    return true;
		}
	    }
	}
	return false;
    }


    /** Gives the current item for other frames
    *@return current Item number
    */
    public int findCurrentItem ()
    {
	return item;
    }


    /** Gives the current item's row position
    *@return Current item's row position
    */
    public int findRow ()
    {
	return this.row;
    }


    /** Gives the current item's column position
    *@return Current item's column position
    */
    public int findColumn ()
    {
	return this.column;
    }


    /** Draws the items
    *@param g The graphics
    *@param blocks the array that holds the locations of the item images
    */
    public void draw (Graphics g, Image[] blocks)
    {
	int imageNo = item;
	g.drawImage (blocks [imageNo],
		(this.column) * 20 + 16,
		76 + (this.row) * 20, 20, 20, null);
    }
} // Items class


