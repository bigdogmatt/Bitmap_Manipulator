import java.io.*; //imports java.io.* 
public class Bitmap //new class called Bitmap
{
	private int width; //a Bitmap object has a member variable width
	private int height; //a Bitmap object has a member variable height
	private byte[][] colorData; //a Bitmap object has a member variable colorData

	public static int readInt(FileInputStream image) throws IOException /*new method called readInt
																		that takes a FileInputStream
																		and returns an int*/
	{
		int first = image.read(); /*read in an int from the FileInputStream 
									and set variable 'first' equal to its value*/
		int second = image.read(); /*read in an int from the FileInputStream and 
									set variable 'second' equal to its value*/
		int third = image.read();  /*read in an int from the FileInputStream and 
									set variable 'third' equal to its value*/
		int fourth = image.read(); /*read in an int from the FileInputStream and 
									set variable 'fourth' equal to its value*/
		return first + (second<<8) + (third<<16) + (fourth<<24); /*shifts the bits in each int to
		 															the correct position and add
		 															them together. Then return the
		 															value*/
	}

	public Bitmap(FileInputStream image) throws IOException /*initiator called Bitmap 
															(same name as the class)
															that takes a FileInputStream*/
	{
		int padding = 0; /*declare new variable, padding, and set it equal to zero*/
		image.skip(18); /*skip the first 18 bytes in the bitmap header because we dont need 
		 				those values*/
		width = readInt(image); /*call readInt on the image. This read in the next 4 bytes
		 						and converts them to an int that is stored in the member
		 						variable 'width'*/
		height = readInt(image); /*call readInt on the image. This read in the next 4 bytes
									and converts them to an int that is stored in the member
									variable 'height'*/
		image.skip(28); /*skip the rest of the header*/
		if((width*3)%4 > 0) /*if statement to determine how much padding is needed in each row
		 					of the colorData*/
		{
			padding = 4 - ((width*3)%4); /*equation to find the padding. The bytes in a row
			 								always have to be divisible by 4*/
		}
		
		colorData = new byte[height][(width*3)]; /*create a new 2D array of bytes called colorData
		 											that has the number of rows equal to 'height' and
		 											number of columns equal to 'width'*/
		for(int i = 0; i < height; i++) /*for loop the runs from 0 until i is >= height and 
										increments i by 1 each time */
		{
			image.read(colorData[i]); /*read in 'width' amount of bytes from the bitmap and store
			 							them in the first row of 'colorData;*/
			image.skip(padding); /*skip the next 'padding' amount of bytes in the bitmap*/
			
		}
	}

	public void writeInt(int num, FileOutputStream image) throws IOException /*new method called writeInt 
																			that takes an int and a 
																			FileOutputStream and 
																			returns an nothing*/
	{
		int first = num & 0xFF; //ands 'num' with 11111111 (FF) to get the first byte 
		int second = (num>>8) & 0xFF; /*shifts the bits in num ands with 11111111 
										(FF) to get the second byte*/
		int third = (num>>16) & 0xFF; /*shifts the bits in num ands with 11111111 
										(FF) to get the third byte*/
		int fourth = (num>>24) & 0xFF; /*shifts the bits in num ands with 11111111 
										(FF) to get the fourth byte*/
		image.write(first); /*write the first byte to the FileOutputStream*/
		image.write(second); /*write the second byte to the FileOutputStream*/
		image.write(third); /*write the third byte to the FileOutputStream*/
		image.write(fourth); /*write the fourth byte to the FileOutputStream*/
		
		/*This method undoes the readInt method*/
	}
	public void writeShort(int num, FileOutputStream image) throws IOException /*new method called 
																				writeShort that
																				gets an in and a
																				FileOutputStream
																				and returns nothing*/
	{
		int first = num & 0xFF; //ands 'num' with 11111111 (FF) to get the first byte
		int second = (num>>8) & 0xFF; /*shifts the bits in num ands with 11111111 
										(FF) to get the second byte*/
		image.write(first); /*write the first byte to the FileOutputStream*/
		image.write(second); /*write the fourth byte to the FileOutputStream*/
		
		/*does the same thing as writeInt but with a short which is only 2 bytes instead of 4*/
	}
	
	public void writeFile(FileOutputStream image) throws IOException /*new method that takes a 
	 																	FileOutputStream and
	 																	returns nothing*/
	{
		int padding = 0; /*declare an new int variable called padding and set equal to zero*/
		if((width*3)%4 > 0) /*if statement to determine how much padding is needed in each row
							of the colorData*/
		{
			padding = 4 - ((width*3)%4); /*equation to find the padding. The bytes in a row
											always have to be divisible by 4*/
		}
		/*writing the header to the FileOutputStream in the correct order*/
		image.write('B'); //writes 'B' to the FileOutputStream
		image.write('M'); //writes 'B' to the FileOutputStream
		
		int size = 54 + height * (width * 3 + padding) + 2; //computes the size of the bitmap
		writeInt(size, image); //writes size to the FileOutputStream
		
		writeInt(0, image); //writes 0 to the FileOutputStream
		writeInt(54, image); //writes 54 to the FileOutputStream
		writeInt(40, image); //writes 40 to the FileOutputStream
		writeInt(width, image); //writes the member variable 'width' to the FileOutputStream
		writeInt(height, image); //writes the member variable 'height' to the FileOutputStream
		writeShort(1, image); //writes 1 to the FileOutputStream
		writeShort(24, image); //writes 24 to the FileOutputStream
		writeInt(0, image); //writes 0 to the FileOutputStream
		writeInt(height * (width * 3 + padding), image); /*writes writes the colorData size
															to the FileOutputStream*/
		writeInt(72, image); //writes 72 to the FileOutputStream
		writeInt(72, image); //writes 72 to the FileOutputStream
		writeInt(0, image); //writes 0 to the FileOutputStream
		writeInt(0, image); //writes 0 to the FileOutputStream
		
		for(int i = 0; i < height; i++) /*for loop the runs from 0 until i is >= height and 
										increments i by 1 each time */
		{
			image.write(colorData[i]); /*write each row of the color data to the FileOutputStream*/
			for(int j = 0; j < padding; j++) /*for loop runs from 0 until j >= padding and 
			 									increments j by 1 each time*/
			{
				image.write(0); //writes a 0 to the FileOutputStream				
			}
			//this for loop added the padding back into the bitmap
		}
		image.write(0); //writes 0 to the FileOutputStream
		image.write(0); //writes 0 to the FileOutputStream
		/*writes 2 zeros at the end because the total size of the bitmap must be divisible by 4
		and since the header is 54 and the color data is always divisible by 4, we have to add 2
		to the end*/
	}
	
	public int fix(int value) //new method called 'fix' that takes an int and returns an int
	{
		if(value < 0) //if value is neg
			return value + 256; //add 256 to value and return it
		else //otherwise
			return value; //return value
		//this method deals with the signed vs unsigned issue for the color data
	}

	public void invert() { //Invert the colors of the image

		for(int row=0; row<height; row++) { //Loop through the rows

			for(int column=0; column<width; column++) { //Loop through the columns

				for(int color=0; color<3; color++) { //Loop through the colors; color is less than 3 because there are 3 colors and the it starts at 0
					colorData[row][column*3 + color] = (byte) (255 - fix(colorData[row][column*3 + color])); 
				}	//add color to the column and subtract the value from 255 to get the opposite color
			}
		}
	}
	public void greyScale() { //Make the image grayscale
		for(int row=0; row<height; row++) {  //Loop through the rows

			for(int column=0; column<width; column++) {  //Loop through the columns
				int blue = fix(colorData[row][column*3]);     //put the fixed value for the first byte in the pixel into variable blue
				int green = fix(colorData[row][column*3 + 1]);//put the fixed value for the second byte in the pixel into variable green
				int red = fix(colorData[row][column*3 + 2]);  //put the fixed value for the third byte in the pixel into variable red

				byte grey = (byte) Math.round(.3*red + .59*green + .11*blue); //use the formula to find the grey value and round
				colorData[row][column*3] = grey;     //make the first byte equal to grey
				colorData[row][column*3 + 1] = grey; //make the second byte equal to grey
				colorData[row][column*3 + 2] = grey; //make the third byte equal to grey			
				//since all three bytes have the same value, the pixel will be grey
			}
		}
	}
	public void blur() { //blur image
		byte[][] array=new byte[height][width*3]; //create new array 
		for(int row=0; row<height; row++) { //loop through the rows

			for(int column=0; column<width; column++) { //loop through the columns
				int blue = 0;  //Initialize variable blue
				int green = 0; //Initialize variable green
				int red = 0;   //Initialize variable red

				double pixels = 0; //Initialize variable pixels

				for(int y = Math.max(row - 2, 0); y <=Math.min( row + 2, height-1) ; y++) { //use max and min so the rows 2 above and  
														  //below are counted but there will not be an error when it gets to the edge
					for(int x=Math.max(column-2, 0); x<=Math.min(column+2, width-1); x++) {//use max and min so the columns 2 above and  
														  //below are counted but there will not be an error when it gets to the edge

						pixels++; //increment pixels
						blue += fix(colorData[y][x*3]);   //add the blue value for the 5 columns 
						green += fix(colorData[y][x*3+1]);//add the green value for the 5 columns
						red += fix(colorData[y][x*3+2]);  //add the red value for the 5 columns
					}
				}
				byte blueBlur = (byte)Math.round(blue/pixels);    //average blue and round
				byte greenBlur = (byte)Math.round(green/pixels);  //average green and round
				byte redBlur = (byte)Math.round(red/pixels);      //average red and round


				array[row][column*3] = blueBlur;       //set first byte to the averaged blue value
				array[row][column*3 + 1] =  greenBlur; //set second byte to the averaged green value
				array[row][column*3 + 2] = redBlur;    //set third byte to the averaged red value
			}
		}
		colorData = array; //array is now colorData
	}
	
	public void mirror() { //vertically mirror the image
		for(int row=0; row<height/2; row++) { //loop through half of the rows so that the image doesn't overlap
			byte[]temp=colorData[height-1-row]; //store the mirrored value of row in temp 
			colorData[height-1-row]=colorData[row]; //make the mirrored value of row equal to row
			colorData[row]=temp;//make row equal to the mirrored value
		}
	}
	public void shrink() {//Shrink the image to half its size in both height and width directions
		byte[][] array=new byte[height/2][width/2*3]; //create new array that is half the size
		for(int row=0; row<height/2; row++) { //loop through the rows in array

			for(int column=0; column<width/2; column++) { //loop through the columns in array

				for(int color=0; color<3; color++) { //loop through colors
					int sum = fix(colorData[row*2][column*2*3+color]); //sum equals the value of the current byte plus color
					sum+=fix(colorData[row*2+1][column*2*3+color]);    //add byte 1 row from original byte to sum
					sum+=fix(colorData[row*2][(column*2+1)*3+color]);  //add byte 1 column from original byte to sum
					sum+=fix(colorData[row*2+1][(column*2+1)*3+color]);//add byte 1 row and column from original byte to sum

					array[row][column*3+color]=(byte) Math.round(sum/4.0);//average the sum and round
				}
			}
		}
		height /=2; //height is cut in half
		width /=2;  //width is cut in half
		colorData = array;//array is now colorData
	}
	public void doubleSize() {  //Double the size of the image in both height and width directions
		byte[][] array=new byte[height*2][width*2*3];//height and width are doubled in the new array

		for(int row=0; row<height*2; row++) {  //Loop through the rows; height is doubled to match array

			for(int column=0; column<width*2; column++) {  //Loop through the columns; width is doubled to match array
				array[row][column*3]=colorData[row/2][column/2*3]; //Blue colorData is put into array
				array[row][column*3+1]=colorData[row/2][column/2*3+1]; //Green colorData is put into array
				array[row][column*3+2]=colorData[row/2][column/2*3+2]; //Red colorData is put into array
			}   //row and column are divided by 2 to access the right byte since colorData is half the size
		}
		height *=2;//height doubles
		width *=2;//width doubles
		colorData = array; //array is now colorData
	}
	public void rotate() { //Will rotate the image 90 degrees to the left 
		byte[][] array=new byte[width][height *3];//create new array
		for(int row=0; row<height; row++) {   //Loop through the rows

			for(int column=0; column<width; column++) {  //Loop through the columns
				array[column][(height-1-row)*3]=colorData[row][column*3]; //Blue colorData is put into array 
				array[column][(height-1-row)*3+1]=colorData[row][column*3+1]; //Green colorData is put into array
				array[column][(height-1-row)*3+2]=colorData[row][column*3+2]; //Red colorData is put into array
			}   //the second set of braces is height-1 because the row starts at 0 not 1, therefore the height needs to be 
				//subtracted by one, otherwise the number will be out of bounds. Row is subtracted from height-1 so that the
				//image rotates to the left instead of the right 
		}
		colorData = array; //array is now colorData
		int temp = height; //store height in temp to use later
		height = width; //height becomes width
		width = temp;//width becomes height
	}
}
