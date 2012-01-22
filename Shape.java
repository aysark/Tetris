// The "Shape" class.
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

/** Tetris Shape Class
  * @author Aysar Khalid and Or-el Mousaffi
  * @version 1.0 - January 7th, 2007
  */
public class Shape
{
    final int[] [] [] SHAPES = {
	    {{0, 1, 0, 0},  // Straight Line Shape 0
		{0, 1, 0, 0},
		{0, 1, 0, 0},
		{0, 1, 0, 0}},

	    {{0, 0, 2, 0},  // Z-Shape
		{0, 2, 2, 0},
		{0, 2, 0, 0},
		{0, 0, 0, 0}},

	    {{0, 3, 0, 0},  // Second Z-Shape 4
		{0, 3, 3, 0},
		{0, 0, 3, 0},
		{0, 0, 0, 0}},

	    {{0, 0, 4, 0},  //Horizontally Fliped L-Shape 6
		{0, 0, 4, 0},
		{0, 4, 4, 0},
		{0, 0, 0, 0}},

	    {{0, 5, 0, 0},  //L-Shape 10
		{0, 5, 0, 0},
		{0, 5, 5, 0},
		{0, 0, 0, 0}},

	    {{0, 6, 0, 0},  //Cut F-shape 14
		{0, 6, 6, 0},
		{0, 6, 0, 0},
		{0, 0, 0, 0}},

	    {{0, 0, 0, 0},  //Box shape 18
		{0, 7, 7, 0},
		{0, 7, 7, 0},
		{0, 0, 0, 0}},

	    {{0, 0, 0, 0},  // Dot shape 19
		{0, 8, 0, 0},
		{0, 0, 0, 0},
		{0, 0, 0, 0}},
	};

    // Shape variables to keep track of a shape's data (characteristics)
    private int type; // Keeps track of the index for the type of a shape
    private int nextShapeType;
    private int row;
    private int column;
    private int[] [] shape;
    private Image[] blocks;


    public Shape (int row, int column)
    {
	// Pick a random shape from all the shape types
	this.type = (int) (Math.random () * (SHAPES.length));
	this.row = row;
	this.column = column;

	//Generate the randomized shape
	shape = SHAPES [type];
    } // Constructor


    /** Checks if a current shape move is valid
    *@param grid the game field which is to be played on
    *@param dRow how much rows to be moved of the shape
    *@param dColumn how much columns to be moved of the shape
    *@return True if the move can be valid, false otherwise.
    */
    public boolean move (int[] [] grid, int dRow, int dColumn)
    {
	row += dRow;
	column += dColumn;
	for (int row = 0 ; row < shape.length ; row++)
	{
	    for (int col = 0 ; col < shape [0].length ; col++)
	    {
		if (((shape [row] [col] > 0) && (shape [row] [col] < 10)) &&
			((grid [this.row + row] [this.column + col] > 0) && (grid [this.row + row] [this.column + col] < 10)))
		{
		    this.row -= dRow;
		    this.column -= dColumn;
		    return false;
		}
	    }
	}
	return true;
    }


    /** Changes the position of a shape, this method is used
	to display the upcoming/next shape
    *@param newRow the row to be displayed at
    *@param newColumn the column to be displayed at
    */
    public void changePosition (int newRow, int newColumn)
    {
	row = newRow;
	column = newColumn;
    }


    /** Rotates the current shape 90 degrees rightwards
    *@param grid the 2D array game field which is to be played on
    */
    public void rotateRightwards (int[] [] grid)
    {
	int[] [] shapeCopy = new int [4] [4];
	int[] currentLine = new int [4];

	int column = 3;
	int index = 0;
	int currentRow = 0;

	// go thru the current shape copying it over to the shapeCopy array
	for (int row = 0 ; row < shape.length ; row++)
	{
	    for (int col = 0 ; col < shape [0].length ; col++)
	    {
		shapeCopy [row] [col] = shape [row] [col];
	    }
	}

	if (shape != SHAPES [7]) // if the shape isn't the dot shape
	{
	    while (column >= 0)
	    {
		for (int col = 0 ; col < shapeCopy [0].length ; col++)
		{
		    if (index <= 3)
			currentLine [index] = shapeCopy [currentRow] [col];
		    index++;
		}

		index = 0;
		for (int row = 0 ; row < shape.length ; row++)
		{
		    shape [row] [column] = currentLine [index];
		    index++;
		}
		currentRow++;
		column--;
		index = 0;
	    }
	}
	for (int row = 0 ; row < shape.length ; row++)
	{
	    for (int col = 0 ; col < shape [0].length ; col++)
	    {
		if (((shape [row] [col] > 0) && (shape [row] [col] < 10)) &&
			((grid [this.row + row] [this.column + col] > 0) && (grid [this.row + row] [this.column + col] < 10)))
		{
		    rotateLeftwards (grid);
		    return;
		}
	    }
	}
    }


    /** Rotates the current shape 90 degrees leftwards
    *@param grid the 2D array game field which is to be played on
    */
    public void rotateLeftwards (int[] [] grid)
    {
	int[] [] shapeCopy = new int [4] [4];
	int[] currentLine = new int [4];

	int column = 0;
	int index = 0;
	int currentRow = 0;

	for (int row = 0 ; row < shape.length ; row++)
	{
	    for (int col = 0 ; col < shape [0].length ; col++)
	    {
		shapeCopy [row] [col] = shape [row] [col];
	    }
	}

	if (shape != SHAPES [7]) // if the shape isn't the dot shape
	{
	    while (column <= 3)
	    {
		for (int col = 0 ; col < shapeCopy [0].length ; col++)
		{
		    if (index <= 3)
			currentLine [index] = shapeCopy [currentRow] [col];
		    index++;
		}

		index = 0;
		for (int row = shape.length - 1 ; row >= 0 ; row--)
		{
		    shape [row] [column] = currentLine [index];
		    index++;
		}
		currentRow++;
		column++;
		index = 0;
	    }
	}
	for (int row = 0 ; row < shape.length ; row++)
	{
	    for (int col = 0 ; col < shape [0].length ; col++)
	    {
		if (((shape [row] [col] > 0) && (shape [row] [col] < 10)) &&
			((grid [this.row + row] [this.column + col] > 0) && (grid [this.row + row] [this.column + col] < 10)))
		{
		    rotateRightwards (grid);
		    return;
		}
	    }
	}
    }


    /** Adds the shape's array on to the main game grid.
	This is called once the shape can't move downwards anymore
    *@param grid the 2Darray game field which is to be played on
    */
    public void addToGrid (int[] [] grid)
    {
	for (int row = 0 ; row < shape.length ; row++)
	{
	    for (int col = 0 ; col < shape [0].length ; col++)
	    {
		if (shape [row] [col] > 0)
		{
		    grid [this.row + row] [this.column + col] = shape [row] [col];
		}
	    }

	}
    }


    /** Checks if a shape is on the given row and column
    *@param grid the 2Darray game field which is to be played on
    *@param currentRow the row of the place where to check
    *@param currentCol the column of the place where to check
    *@return True if the shape is on that specified spot, false otherwise
    */
    public boolean containsShape (int[] [] grid, int currentRow, int currentCol)
    {
	for (int i = 0 ; i < shape.length ; i++)
	{
	    for (int j = 0 ; j < shape [0].length ; j++)
	    {
		if (shape [i] [j] > 0 && i + row == currentRow && j + column == currentCol)
		{
		    return true;
		}
	    }
	}
	return false;
    }


    /** Draws the shapes
    *@param g The graphics
    *@param blocks the array that holds the locations of the shape images
    */
    public void draw (Graphics g, Image[] blocks)
    {
	for (int row = 0 ; row < shape.length ; row++)
	{
	    for (int col = 0 ; col < shape [0].length ; col++)
	    {
		if ((shape [row] [col] > 0))
		{
		    int imageNo = shape [row] [col];
		    g.drawImage (blocks [imageNo],
			    (this.column + col + 1) * 20 + 1,
			    76 + (this.row + row) * 20, 20, 20, null);
		}
	    }
	}
    }
} // ShapeRotate class


