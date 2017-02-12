/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2017 
// PROJECT:          p1
// FILE:             GradeEstimator.java
//
// TEAM:    Team 54, null
// Authors: (Be sure to check if programming teams are allowed)
// Author1: Cory Van Beek, cvanbeek@wisc.edu, cvanbeek, Lecture 001
// Author2: Tessa McChesney, tmcchesney@wisc.edu, tmcchesney, Lecture 002
// Author3: DEVESH BRENDAN SULLIVAN, dsullivan7@wisc.edu, dsullivan7, Lecture 002
// Author4: Yang Qu, qu28@wisc.edu, qu28, Lecture 002
// Author5: Xuyan Wang, xuyan@wisc.edu, xuyan, Lecture 002
// Author6: Tinghe Wang, tzhang329@wisc.edu, tzhang329, Lecture 002
//
//////////////////////////// 80 columns wide //////////////////////////////////


import java.io.*;
import java.util.Scanner;

/**
 * Creates an object to score assignments for a class and use values to determine the overall
 * grade in the class
 * 
 * 
 * @author Cory Van Beek, Tessa McChesney, DEVESH BRENDAN SULLIVAN, Yang Qu, Xuyan Wang, Tinghe Wang
 *
 */
public class GradeEstimator {
	private String[] letterGrades;
	private double[] thresholds;
	private String[] categories;
	private double[] weights;
	private ScoreList scores;
	
	/**
	 * Creates an object used to calculate grades
	 * 
	 * @param letterGrades An array of String Letter Grades
	 * @param thresholds The minimum score to obtain each respective letter grade
	 * @param categories The different categories an assignment can be in
	 * @param weights The weights for the respective categories
	 * @param scores A list of score values
	 * @throws GradeFileFormatException
	 */
	private GradeEstimator(String[] letterGrades, double[] thresholds, 
							String[] categories, double[] weights, ScoreList scores) throws GradeFileFormatException {
		//Check if LetterGrades and thresholds are same length.
		if(letterGrades.length != thresholds.length)
			throw new GradeFileFormatException("You must have the same number of Letter Grades and minimum grade thesholds");
		
		//Check if Categories and Weights are same length.
		if(categories.length != weights.length)
			throw new GradeFileFormatException("You must have the same number of Categories and category weights");

		//Populate variables
		this.letterGrades = letterGrades;
		this.thresholds = thresholds;
		this.categories = categories;
		this.weights = weights;
		this.scores = scores;
		
	}
	
	public static void main( String[] args ) {
		try {
			System.out.println(createGradeEstimatorFromFile("grade_info.txt").getEstimateReport());
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		} catch (GradeFileFormatException e) {
			System.out.println("GradeFileFormatException: " + e.getMessage());;
		} catch (IndexOutOfBoundsException e){
			System.out.println(Config.USAGE_MESSAGE);
		}
	}
	
	/**
	 * Creates a GradeEstimator object from the data in the inputed text file
	 * 
	 * @param gradeInfo The filename containing a text file with the grade info to pull
	 * @return A GradeEstimator object with the correct info
	 * @throws FileNotFoundException
	 * @throws GradeFileFormatException
	 */
	public static GradeEstimator createGradeEstimatorFromFile( String gradeInfo ) 
		      throws FileNotFoundException, GradeFileFormatException {
		String gradeFileContents = "";
		
		File gradeFile = new File(gradeInfo);
		Scanner fileIn = new Scanner(gradeFile);
		
		//Scan all contents of the file into a String and close the file
		gradeFileContents += fileIn.next();
		while(fileIn.hasNextLine()){
			gradeFileContents += fileIn.nextLine();
			gradeFileContents += "\n";
		}
		//Close the file
		fileIn.close();
				
		//Get the letter grades from the first line
		String letterGradesLine = gradeFileContents.substring(0, gradeFileContents.indexOf("\n"));

	
		String[] gradeLetters = getGradeLetters(letterGradesLine);
		
		//Remove the first line
		gradeFileContents = gradeFileContents.substring(gradeFileContents.indexOf("\n") + 1);
		String thresholdsLine = gradeFileContents.substring(0, gradeFileContents.indexOf("\n"));
				
		double[] thresholds = getThresholds(thresholdsLine);
		
		//Remove the Second line
		gradeFileContents = gradeFileContents.substring(gradeFileContents.indexOf("\n") + 1);
		String categoriesLine = gradeFileContents.substring(0, gradeFileContents.indexOf("\n"));
		
		String[] categories = getCategories(categoriesLine);
		
		//Remove the Third line
		gradeFileContents = gradeFileContents.substring(gradeFileContents.indexOf("\n") + 1);
		String weightsLine = gradeFileContents.substring(0, gradeFileContents.indexOf("\n"));
		
		double[] weights = getWeights(weightsLine);
		
		
		ScoreList scores = new ScoreList();
		boolean moreScores = true;
		
		while(moreScores){
			try {
				//Remove a line and get the next one
				gradeFileContents = gradeFileContents.substring(gradeFileContents.indexOf("\n") + 1);
				String nextLine = gradeFileContents.substring(0, gradeFileContents.indexOf("\n"));
				
				//Get a score from the next line and add it to the end of the scores list
				scores.add(getScore(nextLine));
			//End loop when all lines are gone
			} catch (IndexOutOfBoundsException e) {
				moreScores = false;
			}
		}
			
		//Construct the GradeEstimator and return it
		return new GradeEstimator(gradeLetters, thresholds, categories, weights, scores);
	}

	/**
	 * Generates a string report on the grades for the class based on this GradeEstimator data
	 * 
	 * @return The report string
	 * @throws GradeFileFormatException 
	 */
	public String getEstimateReport() throws GradeFileFormatException {
		String result = "Grade estimate is based on " + scores.size() + " scores.\n";

		//The double value to keep track of the grade
		double totalGradePercent = 0;
		
		for(int i = 0; i < categories.length; i ++) {
			//Set Variables for each category
			double totalScores = 0, weightedAverageScore = 0, averageScore = 0;
			int numScores = 0;
			String name = categories[i];
			double weight = weights[i];
			
			//Create a category iterator for the category
			ScoreIterator categoryIterator = new ScoreIterator(scores, name);
			
			//Give 100% if there are no assignments in a category
			if(!categoryIterator.hasNext()){
				averageScore = 100;
			}
			else {				
				//Loop through each Score in the category and add its percent to the total score
				while(categoryIterator.hasNext()){
					Score nextScore = categoryIterator.next();		
					totalScores += nextScore.getPercent();
					numScores ++;
				}
				
				//Calculate the average score
				averageScore = totalScores / (double) numScores;
			}
			
			//Set the value for the weighted average score and add it to the total score
			weightedAverageScore = (averageScore * weight) / 100;
			totalGradePercent += weightedAverageScore;
			
			//Print Statements
			result += String.format("%7.2f%3s%5.2f%3s%2.0f%4s%-9s%1s", weightedAverageScore, "% = ", 
										averageScore, "% * ", weight, "% for ", name, "\n");
					
		}
		
		result += "---------------------------------------------\n";
		result += String.format("%7.2f%-2s", totalGradePercent, "% weighted percent\nLetter Grade Estimate: ");
		
		String letterGrade = "";
		for(int i = 0; i < letterGrades.length; i++)
		{
			if(totalGradePercent > thresholds[i]){
				letterGrade = letterGrades[i];
				break;
			}	
		}
		
		if(letterGrade.equals(""))
			result += String.format("%34s%5.2f","Unable to find a letter grade for ", totalGradePercent);
		
		result += letterGrade;
		
		return result;
	}
	
	/**
	 * Generates a Score object from a string containing a name, number of points, and points possible
	 * all separated by spaces
	 * 
	 * @param s The string to get the Score from
	 * @return The score object
	 * @throws GradeFileFormatException
	 */
	private static Score getScore(String s) throws GradeFileFormatException {
		String name = "";
		double points, maxPoints;
		
		String[] values = new String[3];
		
		//Run this loop three times to get a string for name, points and max points
		for(int i = 0; i < 3; i++){
			//remove whitespace
			s = s.trim();
			
			//Get the next value and shorten the string s
			values[i] = s.substring(0, s.indexOf(" "));
			s = s.substring(s.indexOf(" ") + 1);		
		}
		
		//Assign the values from the value array
		name = values[0];
		try{
			points = Double.parseDouble(values[1]);
			maxPoints = Double.parseDouble(values[2]);
		} catch(NumberFormatException e){
			throw new GradeFileFormatException("The lines containing scores must have a number for their second and third values");
		}
		
		//Create a Score object from the values and return it
		Score lineScore = new Score(name, points, maxPoints);
		return lineScore;
	}
	
	private static double[] getWeights(String s) throws GradeFileFormatException {
		//This value will be an index for the weights Array
		int numWeight = 0;
		
		//Can hold as many thresholds as grade letters
		double[] weight = new double[26];
		
		boolean hasMore = true;
		while(hasMore){
			//Remove Whitespace
			s = s.trim();
			
			try {
				//Get the next part of the string and try to convert it to a double
				String parse = s.substring(0, s.indexOf(" "));
				weight[numWeight] = Double.parseDouble(parse);
				numWeight ++;
				
				//remove that part of the string so loop can continue
				s = s.substring(s.indexOf(" ") + 1);
				
			//Catch if the next part isn't a number, and exit the loop
			} catch(NumberFormatException e){
				hasMore = false;
			}
		}
		
		if(weight[0] == 0)
			throw new GradeFileFormatException("The second line must contain values for grade thresholds");
		
		//Create and populate an array of the correct length, then return that array
		double[] t = new double[numWeight];
		for(int i = 0; i < numWeight; i++){
			t[i] = weight[i];
		}
		
		int count = 0;
		for (int i = 0; i< numWeight; i++)
			count += weight[i];
		if (count != 100){
			throw new GradeFileFormatException("The weights should add up to 100");
		}
		
		return t;
	}

	
	/**
	 * This creates an array of strings from the words inputed in the param string s, which were separated by spaces
	 * 
	 * @param s The string to turn into an array of strings
	 * @return The array of string values
	 * @throws GradeFileFormatException
	 */
	private static String[] getCategories(String s) throws GradeFileFormatException {
		//This value will be an index for the cat Array
		int numCats = 0;
		
		//Can hold as many categories as letters of the alphabet
		String[] cat = new String[26];
		
		//This array will keep track of which letters were already used.
		String[] check = new String[26];
		
		boolean hasMore = true;
		while(hasMore){
			//Remove Whitespace
			s = s.trim();
			
			try {
				//Get the next part of the string and try to convert it to a double
				String parse = s.substring(0, s.indexOf(" "));
				String firstLetter = parse.substring(0,1);
				
				//Check if rest of line is comments and if it is end the loop
				if(firstLetter.equals("#")){
					hasMore = false;
					break;
				}			
				
				//Check to see if first letter is already taken
				for(int i = 0; i < numCats; i++){
					if(check[i].equals(firstLetter))
						throw new GradeFileFormatException("Your categories must all start with different letters.");
				}
				
				//Add the values to the arrays and increment the index
				cat[numCats] = parse;
				check[numCats] = firstLetter;
				numCats ++;
				
				//remove that part of the string so loop can continue
				s = s.substring(s.indexOf(" ") + 1);
				
			//Catch if there are no values left in the string and end the loop
			} catch (IndexOutOfBoundsException e) {
				hasMore = false;
			}			
		}
		
		if(cat[0] == null)
			throw new GradeFileFormatException("The Third line must contain values for categories");
		
		//Create and populate an array of the correct length, then return that array
		String[] c = new String[numCats];
		for(int i = 0; i < numCats; i++){
			c[i] = cat[i];
		}
		
		return c;
	}
	
	/**
	 * This method returns number values from a string as a double array
	 * 
	 * @param s The line from the grade file to be converted to a double array
	 * @return An array containing double values for the thresholds of letter grades
	 * @throws GradeFileFormatException 
	 */
	private static double[] getThresholds(String s) throws GradeFileFormatException {
		//This value will be an index for the thresh Array
		int numThresholds = 0;
		
		//Can hold as many thresholds as grade letters
		double[] thresh = new double[26];
		
		boolean hasMore = true;
		while(hasMore){
			//Remove Whitespace
			s = s.trim();
			
			try {
				//Get the next part of the string and try to convert it to a double
				String parse = s.substring(0, s.indexOf(" "));
				thresh[numThresholds] = Double.parseDouble(parse);
				numThresholds ++;
				
				//remove that part of the string so loop can continue
				s = s.substring(s.indexOf(" ") + 1);
				
			//Catch if the next part isn't a number, and exit the loop
			} catch(NumberFormatException e){
				hasMore = false;
			}
		}
		
		if(thresh[0] == 0)
			throw new GradeFileFormatException("The second line must contain values for grade thresholds");
		
		//Create and populate an array of the correct length, then return that array
		double[] t = new double[numThresholds];
		for(int i = 0; i < numThresholds; i++){
			t[i] = thresh[i];
		}
		
		return t;
	}
	
	
	
	/**
	 * Generates and array of letter grades from a String equal to the first
	 * line of the grade info file.
	 * 
	 * @param s The String for the first line of the input file
	 * @return A String Array with the Grade Letter Values
	 * @throws GradeFileFormatException
	 */
	private static String[] getGradeLetters(String s) throws GradeFileFormatException {
		int numLetters = 0;
		
		//26 is the number of possible grades because you couldn't have more than the letters of the alphabet
		String[] gradeLetters = new String[26];
		
		boolean moreToLine = true;	
		while(moreToLine){
			//remove whitespace at beginning and end of line.
			s = s.trim();
			
			try{
				String nextLetter = s.substring(0, 1);
				if(nextLetter.equals("#")) {
					//Stop if the rest of the line is a comment
					moreToLine = false;
				}
				else {
					//Remove nextLetter from line so it isn't pulled again
					s = s.substring(1);
					
					//Add nextLetter to the array
					gradeLetters[numLetters] = nextLetter;
					numLetters ++;
				}
			} catch (IndexOutOfBoundsException e){
				//stop loop if ran out of items in line
				moreToLine = false;
			}			
		}
		
		//If there is nothing throw the exception
		if(gradeLetters[0] == null)
			throw new GradeFileFormatException();
		
		//Creates an array with the exact length of the number of letters and add in the data
		String[] grades = new String[numLetters];
		for(int i = 0; i < numLetters; i++){
			grades[i] = gradeLetters[i];
		}
		
		//Return the Array
		return grades;
	}
	
}
