/**
 * Project 5 -- Maze
 * This is the Maze class for project 5.  It is passed a string obtained by MapCreator.readMapCreator()
 * at initialization and parses the string to create a maze.  The maze consists of a 2-D array
 * of MazeRoom objects.  The MazeRoom class a public class defined within the Maze class (simply
 * because only Maze.java and Rat.java can be turned in: otherwise it would be in its own file) which
 * contains a constructor, accessors, and mutators.  It allows each element of the 2-D array to know
 * about its own status with regard to wall presence and whether it is the start or end room.
 * The maze class itself contains its constructor, and accessors for width, height, and individual
 * elements of the array.  (MazeRoom is public so that this last may return a MazeRoom that Rat can look at).
 *
 * @author Bess L. Walker
 *
 * @recitation 01 01 (H) Markova
 *
 * @date October 6, 2002
 */
public class Maze
{
	public static final char NORTH = '0';
	public static final char EAST = '1';
	public static final char SOUTH = '2';
	public static final char WEST = '3';
	public static final char START = 'S';
	public static final char END = 'E';

	/**
	 * MazeRoom
	 * Normally this would be in a separate file, but turn-in constraints force my hand.
	 */
	public class MazeRoom
	{
		private boolean northWall;
		private boolean eastWall;
		private boolean southWall;
		private boolean westWall;
		private boolean start;
		private boolean end;

		/**
		 * default constructor
		 */
		public MazeRoom()
		{
			northWall = false;
			eastWall = false;
			southWall = false;
			westWall = false;
			start = false;
			end = false;
		}

		/**
		 * north wall accessor
		 *
		 * @return the value of northWall
		 */
		public boolean northWall()
		{
			return northWall;
		}

		/**
		 * east wall acessor
		 *
		 * @return the value of eastWall
		 */
		public boolean eastWall()
		{
			return eastWall;
		}

		/**
		 * south wall acessor
		 *
		 * @return the value of southWall
		 */
		public boolean southWall()
		{
			return southWall;
		}

		/**
		 * west wall acessor
		 *
		 * @return the value of westWall
		 */
		public boolean westWall()
		{
			return westWall;
		}

		/**
		 * start acessor
		 *
		 * @return the value of start
		 */
		public boolean start()
		{
			return start;
		}

		/**
		 * end acessor
		 *
		 * @return the value of end
		 */
		public boolean end()
		{
			return end;
		}

		/**
		 * north wall mutator
		 *
		 * @param boolean value of the north wall
		 */
		public void setNorth(boolean inValue)
		{
			northWall = inValue;
		}

		/**
		 * east wall mutator
		 *
		 * @param boolean value of the east wall
		 */
		public void setEast(boolean inValue)
		{
			eastWall = inValue;
		}

		/**
		 * south wall mutator
		 *
		 * @param boolean value of the south wall
		 */
		public void setSouth(boolean inValue)
		{
			southWall = inValue;
		}

		/**
		 * west wall mutator
		 *
		 * @param boolean value of the west wall
		 */
		public void setWest(boolean inValue)
		{
			westWall = inValue;
		}

		/**
		 * start mutator
		 *
		 * @param boolean value of start (is this room the starting room?)
		 */
		public void setStart(boolean inValue)
		{
			start = inValue;
		}

		/**
		 * end mutator
		 *
		 * @param boolean value of end (is this room the ending room?)
		 */
		public void setEnd(boolean inValue)
		{
			end = inValue;
		}
	}

	private MazeRoom[][] room;  //size and elements given in Maze constructor

	/**
	 * constructor which takes a string of maze data found by MapCreator.readMapFile() and turns it into a maze
	 *
	 * @param string of maze data found by MapCreator.readMapFile()
	 */
	public Maze(String mazeData)
	{
		final int END_OF_HEIGHT = 6;  //"HEIGHT=" ends at mazeData[6]
		int position;
		int height;
		int width;
		int numRow = 0;
		int numCol = 0;
		int i;
		int j;

		mazeData = mazeData.toUpperCase();  //just in case it is not in upper case as the specs show

		for(position = END_OF_HEIGHT + 1; position <= mazeData.length() - 1; position++)  //it should never reach mazeData.length() -1, but you can't be too careful
		{
			if(mazeData.charAt(position) == ' ')
			{break;}
		}
		height = Integer.parseInt(mazeData.substring(END_OF_HEIGHT + 1, position));  //parse the part of the string which represents the height integer into an integer

		final int END_OF_WIDTH = position + 6;  //same idea as END_OF_HEIGHT above, but it can't be defined until you know where the height definition ends; normally I would never declare things in the middle of a function

		for(position = END_OF_WIDTH + 1; position <= mazeData.length() - 1; position++)
		{
			if(mazeData.charAt(position) == ' ')
			{break;}
		}
		width = Integer.parseInt(mazeData.substring(END_OF_WIDTH + 1, position));  //as above

		room = new MazeRoom[height][width];
		for(i = 0; i <= room.length - 1; i++)
		{
			for(j = 0; j <= room[i].length - 1; j++)
			{
				room[i][j] = new MazeRoom();
			}
		}

		for(position = position + 1; position <= mazeData.length() - 1; position++)
		{
			switch(mazeData.charAt(position))
			{
				case ' ':
					numCol++;
					if(numCol % (room[numRow].length) == 0)
					{
						numCol = 0;
						numRow++;
					}
					break;
				case '0':
					room[numRow][numCol].setNorth(true);
					break;
				case '1':
					room[numRow][numCol].setEast(true);
					break;
				case '2':
					room[numRow][numCol].setSouth(true);
					break;
				case '3':
					room[numRow][numCol].setWest(true);
					break;
				case 'S':
					room[numRow][numCol].setStart(true);
					break;
				case 'E':
					room[numRow][numCol].setEnd(true);
					break;
				case 'X':
					break;
				default:
					System.out.println("uh-oh" + mazeData.charAt(position));
					break;
			}
		}
	}

	/**
	 * accessor for height
	 *
	 * @return height of maze
	 */
	public int getHeight()
	{
		return room.length;
	}

	/**
	 * accessor for width
	 *
	 * @return width of maze
	 */
	public int getWidth()
	{
		return room[0].length;
	}

	/**
	 * accessor for an element of the maze
	 *
	 * @param height-index, width-index
	 * @return room at that position
	 */
	public MazeRoom element(int h, int w)
	{
		return room[h][w];
	}
}