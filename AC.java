/* JavaTextEditor.AC.java
 * 
 * CPS 150
 * Algorithms & Programming I
 * 
 * Final Project: Building a Java Text Editor
 * 
 * Name: Adriel Colon
 * 
 * This class supplies the main class with the functions of:
 * 		Adding a line of text to the end of a text file document
 * 		Adding or inserting a line of text from a line number of the users choice in a text file document
 * 		Changing a line of text from a line number of the users choice in a text file document
 * 		Deleting a line of text completely from a line number of the users choice in a text file document
 */

package JavaTextEditor;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class AC {

	// data fields
	private static ArrayList<String> doc = Main.FILES.get(Main.ID).textDoc;

	// global variables
	private static Scanner in = new Scanner(System.in);
	private static final int err = JOptionPane.ERROR_MESSAGE;

	/*
	 * addLine() -> ArrayList<String>
	 * 
	 * This method prompts the user to add a line of text which will then be added
	 * to the end of the text file document
	 */
	public static void addLine() {
		// system prompts the user for text
		System.out.println("Enter line of text:");

		// variable for user input
		String addedText = in.nextLine();
		System.out.println();

		// append user input
		doc.add(addedText);

	} // end addLineEnd

	/*
	 * addLine) ->
	 * 
	 * Uses the integer parameter for the selection of a line number and the users
	 * selected text of choice will be inserted where that line number is at
	 */
	public static void addLinePrior() throws Exception {
		if (doc.size() == 0 || doc.size() == 1) {
			throw new IllegalArgumentException ("Document is too short to do this");
		}
		
		// system prompt for user input
		System.out.println("Enter line number: ");
	

		// line number variable
		int num = -1;

		// loop for valid selection
		for (boolean valid = false; !valid;) {
			System.out.print("\t-> ");
			if (in.hasNextInt()) {
				num = in.nextInt();
				if (num > 0 && num <= doc.size() - 1) {
					valid = true;
					in.nextLine();
					continue;
				}
			} 
			in.nextLine(); // clear scanner

			// when false, invalid input
			JOptionPane.showMessageDialog(null,
					"Invalid input." 
					+ "\nPlease enter an integer (> 0)"
					+ "\n   & (<= doc size)",
					"[ERROR]", err);
			System.out.println();
		}

		if (num == -1) {
			throw new Exception("Unknown Error");
		}

		// system prompt for the user
		System.out.println("Enter your text of choice to add: ");

		String addedText = in.nextLine(); // takes user input for what value they would like to add
		System.out.println();
		
		// insert user input
		doc.add(num-1, addedText); 

	} // end addLinePrior

	/*
	 * changeLine() ->
	 * 
	 * This method will prompt the user for a line number and then change the text
	 * to what the user chooses to change it to in the text file document
	 */
	public static void changeLine() throws Exception {
		if (doc.size() == 0) {
			throw new IllegalArgumentException ("Document is too short to do this");
		}
		
		// system prompt for line number
		System.out.println("Enter a line number");
	

		// line number variable
		int num = -1;

		// loop for valid selection
		for (boolean valid = false; !valid;) {
			System.out.print("\t-> ");
			if (in.hasNextInt()) {
				num = in.nextInt();
				if (num > 0 && num <= doc.size()) {
					valid = true;
					in.nextLine();
					continue;
				}
			} 
			in.nextLine(); // clear scanner

			// when false, invalid input
			JOptionPane.showMessageDialog(null, "Invalid input." 
			+ "\nPlease enter an integer (> 0)"
			+ "\n   & (<= doc size)", "[ERROR]", err); 
			System.out.println();
		}
		if (num == -1) {
			throw new Exception("Unknown Error");
		}

		// system prompt for whatever the user would like to insert
		System.out.println("Enter line of text:");

		// variable for user input
		String addedText = in.nextLine(); 
		System.out.println();

		// change line to user input
		doc.set(num - 1, addedText);

	} // end changeLine

	/*
	 * deleteLine() ->
	 * 
	 * Gets a line number from the user and erases that line of text from the text
	 * file document
	 */
	public static void deleteLine() throws Exception {
		if (doc.size() == 0) {
			throw new IllegalArgumentException ("Document is too short to do this");
		}
		
		// system prompt for a line number to delete
		System.out.println("Enter line number to delete:");
	

		// line number variable
		int num = -1;

		// loop for valid selection
		for (boolean valid = false; !valid;) {
			System.out.print("\t-> ");
			if (in.hasNextInt()) {
				num = in.nextInt();
				if (num > 0 && num <= doc.size()) {
					valid = true;
					continue;
				}
			} 
			in.nextLine(); // clear scanner

			// when false, invalid input
			JOptionPane.showMessageDialog(null, "Invalid input." 
					+ "\nPlease enter an integer (> 0)"
					+ "\n   & (<= doc size)", "[ERROR]", err); 
			System.out.println();
		}

		if (num == -1) {
			throw new Exception("Unknown Error");
		}

		// delete selected line
		doc.remove(num - 1);

	} // end deleteLine
}// end AC
