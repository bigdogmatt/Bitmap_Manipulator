import java.io.*; //imports java.io.*
import java.util.Scanner; //imports scanner to use in main

public class Manipulator {
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in); //create a new scanner object called 'in'
		System.out.print("What image file would you like to edit: "); /*prompt user for the 
																	name of a file to be edited*/
		String preEditImage = in.next(); //gets the users input and stores it in a new variable
		FileInputStream image = new FileInputStream(preEditImage); /*create a new FileInputStream 
																	and pass preEditImage to it*/
		Bitmap bitmap = new Bitmap(image); /*create a new Bitmap object and pass the
		 									FileInputStream to it*/
		
		boolean quit = false; //create new boolean variable and set it to false
		
		while(quit == false) //loop while the boolean is equal to false
		{
			System.out.print("What command would you like to perform "
					+ "(i, g, b, v, s, d, r, or q): "); /*prompt the user for a command to 
					 									perform on the image*/
			char command = in.next().charAt(0); /*get users input and put it in a variable*/
			
			if(command == 'q') //if the command is q
			{
				quit = true; /*set boolean variable to true, which terminates the loop*/
			}
			else if(command == 'i') //if the command is i
			{
				bitmap.invert(); //call invert on the bitmap
			}
			else if(command == 'g') //if the command is g
			{
				bitmap.greyScale(); //call greyScale on the bitmap
			}
			else if(command == 'b') //if the command is b
			{
				bitmap.blur(); //call blur on the bitmap
			}
			else if(command == 'v') //if the command is v
			{
				bitmap.mirror(); //call mirror on the bitmap
			}
			else if(command == 's') //if the command is s
			{
				bitmap.shrink(); //call shrink on the bitmap
			}
			else if(command == 'd') //if the command is d
			{
				bitmap.doubleSize(); //call doubleSize on the bitmap
			}
			else if(command == 'r') //if the command is r
			{
				bitmap.rotate(); //call rotate on the bitmap
			}
			else //otherwise,
			{
				System.out.println("Sorry, that is not a valid command. Please try again."); 
				/*Print a statement telling the user that their command is not valid 
				and prompt for another*/
			}
		}
		image.close(); //closes the FileInputStream
		System.out.print("What do you want to name your new image file: "); /*when the loop is over
		 																	prompt the user for a 
		 																	new name for the edited
		 																	file*/
		String editedImage = in.next(); //store the users input in a new variable
		FileOutputStream output = new FileOutputStream(editedImage); /*create a new FileOutputStream
		 																and and pass the new file 
		 																name to it*/
		bitmap.writeFile(output); //call writeFile on the bitmap and pass the FileOutputStream to it		
		output.close(); //close the FileOutputStream
		in.close(); //close the scanner
		
	}
}
