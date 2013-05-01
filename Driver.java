public class Driver
{
    public static void main(String args[])
    {

	if(args.length == 0)
	{
	    System.out.println("A filename must be supplied at the command line");
	    System.exit(1);
	}
	    
	String s = MapCreator.readMapFile(args[0]);
	Maze m = new Maze(s);

	Rat sammyJankis = new Rat(m);
	
	String p = sammyJankis.findPath();
	System.out.println("Solution Path");
	System.out.println(p);
	
	
    }
}
	
