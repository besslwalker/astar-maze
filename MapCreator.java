
import java.io.*;

public class MapCreator
{
    
    public static String readMapFile(String fileName)
    {
	String returnString = new String();
	try
	{
	    BufferedReader inputStream =
		new BufferedReader(new FileReader(fileName));

	    
	    String tempString = new String();
	    while(true)
	    {
		tempString = inputStream.readLine();
		if(tempString == null)
		    break;
		    
		returnString = returnString + tempString;
		
	    }
	}
	catch(FileNotFoundException e)
	{
	    System.out.println("File: " + fileName + " could not be opened");
	    
	}
	catch(IOException e)
	{
	    System.out.println("error in the file: " + fileName);
	}

	return returnString;
	
    }

    

}
    
