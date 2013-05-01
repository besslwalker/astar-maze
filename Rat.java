/**
 * Project 5 -- Rat
 * This is the Rat class for Project 5.  The rat is passed a Maze at initialization.
 * When findPath is called, the Rat returns a string of instructions to solve the maze.
 * R = turn right, L = turn left, F = move forward.  R and L do not include an F:
 * |^ | R = |> |, not | >|.
 * The maze is not solved by a creature exploring the maze, but by a nice algorithm
 * I found on the web (see findPath JavaDoc block).  Thus calling the class "Rat"
 * is really more of an affectation than anything else.
 * This class requires the Maze class.
 *
 * @author Bess L. Walker
 *
 * @recitation 01 01 (H) Markova
 *
 * @date October 6, 2002
 */
public class Rat
{
	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	private static final int RIGHT = 1;
	private static final int LEFT = -1;
	private static final int AROUND = 2;
	private static final int FORWARD = 1;

	private Maze theMaze;
	private int[][] solutionGrid;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private int positionX;
	private int positionY;
	private int orientation;

	/*
	 * constructor
	 *
	 * @param a Maze
	 */
	public Rat(Maze inMaze)
	{
		theMaze = inMaze;
		solutionGrid = new int[theMaze.getHeight()][theMaze.getWidth()];
		orientation = NORTH;
	}

	/**
	 * finds the path through the maze -- and a bloody good path it is, too
	 *
	 * the basic algorithm for this solver was found at
	 * http://www.ii.uni.wroc.pl/~wzychla/maze_en.html
	 * by Wiktor Zychla
	 * ALL code is original by Bess L. Walker
	 *
	 * @return a string of instructions (r,l,f) for solving the maze
	 */
	public String findPath()
	{
		String path = "";

		constructGrid();

		positionX = endX;
		positionY = endY;

		do
		{
			path += getMove();
		}while(!startFound());  //since this is constructed from end->start  (but end and start in here are the opposite of the end and start of the maze, so it works out correctly)

		return path;
	}

	private void constructGrid()
	{
		int i;
		int j;
		int value = -1;

		//initialize elements to -1
		for(i = 0; i <= solutionGrid.length - 1; i++)
		{
			for(j = 0; j <= solutionGrid[i].length - 1; j++)
			{
				solutionGrid[i][j] = value;
			}
		}

		value++;

		//find start point of search (since solution is constructed backwards, this is the END of the maze
		for(i = 0; i <= solutionGrid.length - 1; i++)
		{
			for(j = 0; j <= solutionGrid[i].length - 1; j++)
			{
				if(theMaze.element(i, j).end())
				{
					startX = i;
					startY = j;
					solutionGrid[i][j] = value;
				}
			}
		}

		//find end point of search (since solution is constructed backwards, this is the START of the maze
		for(i = 0; i <= solutionGrid.length - 1; i++)
		{
			for(j = 0; j <= solutionGrid[i].length - 1; j++)
			{
				if(theMaze.element(i, j).start())
				{
					endX = i;
					endY = j;
				}
			}
		}

		//construct the solution grid
		do
		{
			for(i = 0; i <= (solutionGrid.length - 1); i++)
			{
				for(j = 0; j <= (solutionGrid[i].length - 1); j++)
				{
					if(solutionGrid[i][j] == value)
					{
						setReachable(i, j, value + 1);
					}
				}
			}
			value++;
		}while(!endFound(value));
	}

	private void setReachable(int inX, int inY, int inValue)
	{
		if(!theMaze.element(inX, inY).northWall())
		{setIfClear(inX - 1, inY, inValue);}
		if(!theMaze.element(inX, inY).eastWall())
		{setIfClear(inX, inY + 1, inValue);}
		if(!theMaze.element(inX, inY).southWall())
		{setIfClear(inX + 1, inY, inValue);}
		if(!theMaze.element(inX, inY).westWall())
		{setIfClear(inX, inY - 1, inValue);}
	}

	private void setIfClear(int inX, int inY, int inValue)
	{
		if(solutionGrid[inX][inY] == -1)
		{solutionGrid[inX][inY] = inValue;}
	}

	private boolean endFound(int inValue)
	{
		int i;
		int j;

		for(i = 0; i <= solutionGrid.length - 1; i++)
		{
			for(j = 0; j <= solutionGrid[i].length - 1; j++)
			{
				if(solutionGrid[i][j] == inValue)
				{
					if(i == endX && j == endY)
					{return true;}
				}
			}
		}
		return false;  //if you haven't already returned true
	}

	private boolean startFound()
	{
		boolean isFound;

		if(positionX == startX && positionY == startY)
		{isFound = true;}
		else
		{isFound = false;}

		return isFound;
	}

	private String getMove()
	{
		int backTrackValue = solutionGrid[positionX][positionY];
		int directionOfNext = findNext(backTrackValue);
		String movement = "";

		//it would be great to do this with a switch statement, but those require literals in the cases (not just constants (final variables), but actual literals)
		if(directionOfNext == orientation)  //straight ahead
		{
			move(FORWARD);
			movement = "F";
		}
		if(directionOfNext == (orientation + 1) || directionOfNext == (orientation - 3))  //to the right; usually o + 1, but if o==WEST, to the right == 0
		{
			turn(RIGHT);
			move(FORWARD);
			movement = "RF";
		}
		if(directionOfNext == (orientation - 1) || directionOfNext == (orientation + 3))  //to the left
		{
			turn(LEFT);
			move(FORWARD);
			movement = "LF";
		}
		if(directionOfNext == (orientation + 2) || directionOfNext == (orientation - 2))  //behind
		{
			turn(AROUND);
			move(FORWARD);
			movement = "RRF";  //could just as easily be "LLF"
		}

		return movement;
	}

	private int findNext(int currentValue)
	{
		int direction = 4;  //if direction STAYS 4, there is an error somewhere

		if(!theMaze.element(positionX, positionY).northWall())
		{
			if(solutionGrid[positionX - 1][positionY] == (currentValue - 1))
			{direction = NORTH;}
		}
		if(!theMaze.element(positionX, positionY).eastWall())
		{
			if(solutionGrid[positionX][positionY + 1] == (currentValue - 1))
			{direction = EAST;}
		}
		if(!theMaze.element(positionX, positionY).southWall())
		{
			if(solutionGrid[positionX + 1][positionY] == (currentValue - 1))
			{direction = SOUTH;}
		}
		if(!theMaze.element(positionX, positionY).westWall())
		{
			if(solutionGrid[positionX][positionY - 1] == (currentValue - 1))
			{direction = WEST;}
		}

		return direction;
	}

	private void turn(int turnValue)
	{
		orientation += turnValue;

		while(orientation > 3) //thus if orientation == 4, after this it will be 0: both values correspond to north
		{
			orientation -= 4;
		}
		while(orientation < 0)
		{
			orientation += 4;
		}
	}

	private void move(int moveValue)
	{
		switch(orientation)
		{
			case NORTH:
				positionX -= moveValue;
				break;
			case EAST:
				positionY += moveValue;
				break;
			case SOUTH:
				positionX += moveValue;
				break;
			case WEST:
				positionY -= moveValue;
				break;
			default:
				System.out.println("Problem in move() caused by incorrect orientation value.");
				break;
		}
	}

	private void showGridLinear()
	{
		int i, j;

		for(i = 0; i <= solutionGrid.length - 1; i++)
		{
			for(j = 0; j <= solutionGrid[i].length - 1; j++)
			{
				System.out.println(i + " " + j + " " + solutionGrid[i][j]);
			}
		}
	}
}