/* JavaTextEditor.Main.java
 * 
 * CPS 150
 * Algorithms & Programming I
 * 
 * Final Project: Building a Java Text Editor
 * 
 * Name: Meadow Motz, Adriel Colon
 * 
 * Description:
 * Using this program users can:
 * 		Open a text document
 * 		Edit a text document:
			Save an open text document
			Display the document
			Display a line in the document at a given line number
			Add a new line to the end of the document
			Insert a new line prior to a line at a given line number
			Change a line at a given line number to new text
			Delete a line at a given line number
		End the program
	This editor enables users to select "cancel" and supports multiple open documents.
	
	Bug Report:
		- If opening a new document and document is named " ", then an InvalidPathException occurs, indicating 
				an invalid file name
		- If a file name is > 4 chars,
 * 					there's no intended file extension,	
 * 					and a '.' is 4 chars from the end,
 * 				then the last 4 chars of the file name (.xxx) are replaced with ".txt"
 * 		- When terminating program, may get output "Exception while removing reference." (has no observed effect)
 */

package JavaTextEditor;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Main {
// 	**********************************************************************************************	
	// data fields
	public static ArrayList<MM> FILES; // MM removes these elements, Main adds them
	public static int ID; // only Main changes this value, default -1
	public static int fileMenuChoice;
	public static int editMenuChoice;
	private static boolean editMenuOpen;

	// global variables
	private static Scanner in = new Scanner(System.in);
	private static PrintStream out = System.out;
	private static final int err = JOptionPane.ERROR_MESSAGE;
	private static final String EDIT_MENU = 
			"Enter the number corresponding to your choice:\n"
		+ 	"--------------------------------------------------\n"
		+ 	"1. Display doc\n"
		+ 	"2. Display line #\n"
		+ 	"3. Add line at end\n"
		+ 	"4. Insert new line before #\n"
		+ 	"5. Edit line at #\n"
		+ 	"6. Delete line at #\n"
		+ 	"7. Save doc\n"
		+ 	"8. Return to file menu\n"
		+ 	"--------------------------------------------------\n"
		+ 	"\t-> ";
// 	**********************************************************************************************	
	public static void main(String[] args) {
		// ArrayList<MM> FILES
		// int ID
		// int fileMenuChoice
		// int editMenuChoice
		// boolean editMenuOpen
		fileMenuChoice = -1;
		editMenuChoice = -1; 
		ID = -1;
		editMenuOpen = false;
		FILES = new ArrayList<MM>();
		
		while (fileMenuChoice != 2) {
			try {
				
				// don't exit file menu unless "terminate"
				while (fileMenuChoice != 2) {
					// if not in edit menu
					if (!editMenuOpen) { 
						ID = -1;
						fileMenuChoice = -1;
						getFileMenu(); }
					
					// don't exit edit menu unless "return to file menu"
					while (editMenuChoice!=8 && editMenuChoice!=7 && editMenuOpen) {
						editMenuChoice = -1;
						getEditMenu(); }
					editMenuOpen = false;
					editMenuChoice = -1;
				}
			}		 
			catch (FileNotFoundException ex) { 
				JOptionPane.showMessageDialog(null, "File not found", "[ERROR]", err); 
				System.out.println(); }
			catch (NullPointerException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "[ERROR]", err); 
				System.out.println(); }
			catch (InvalidPathException ex) {
				JOptionPane.showMessageDialog(null, "Invalid file name", "[ERROR]", err); 
				System.out.println(); }
			catch (IllegalArgumentException ex) { 
				JOptionPane.showMessageDialog(null, ex.getMessage(), "[ERROR]", err); 
				System.out.println(); }
			catch (Exception ex) { // unknown error
				JOptionPane.showMessageDialog(null, ex.getMessage(), "[ERROR]", err); 
				System.out.println(); }
		} // end while loop
		
		out.println("\nClosing JavaTextEditor . . .");
	} // end main
// 	**********************************************************************************************	
	//private static methods
	
	/*
	 * showFileMenu() -> void
	 * 
	 * prints file menu, adding more* file options as they increase:
	 * 	  	1. files*
	 * 		2. open doc
	 * 		3. terminate program
	 */
	private static void showFileMenu() {
		// ArrayList<MM> FILES
		
		out.print("Enter the number corresponding to your choice:\n"
				+ 	"--------------------------------------------------\n"
				+ 	"        1. Open doc\n" 
				+	"        2. Terminate program");
		for (int cc=0; cc<FILES.size(); cc++) {
			if (cc==0) out.print("\n(Files) ");
			else out.print("\n   |    ");
			out.print((cc+3) + ". " + FILES.get(cc).filePath.getName() );
		}
		out.print("\n--------------------------------------------------\n"
				+ 	"\t-> " );
	} // end showFileMenu
	
	
	/*
	 * getFileMenu() -> void
	 * 
	 * shows file menu, 
	 * gets valid user choice, 
	 * and executes the according function:
	 * 		1. enter edit menu for chosen file
	 * 		2. open doc
	 * 		3. terminate program
	 */
	private static void getFileMenu() throws Exception {
		// ArrayList<MM> FILES
		// int ID
		// boolean editMenuOpen
		int open = 1;
		int terminate = 2;
		
		
		// loop for valid selection
		for (boolean valid = false; !valid;) {
			showFileMenu();
			if (in.hasNextInt()) {
				fileMenuChoice = in.nextInt();
				if (fileMenuChoice>0 && fileMenuChoice<=terminate+FILES.size()) {
					valid = true; 
					in.nextLine();
					continue; }
			}
			in.nextLine(); // clear scanner
			
			// when false, invalid input
			JOptionPane.showMessageDialog(null, "Invalid input."
					+ "\nPlease enter an integer (> 0)",
					"[ERROR]", err); 
			System.out.println(); 
		}
		if (fileMenuChoice==-1) throw new Exception("FileMenu validation failed");

		
		// enter edit menu for corresponding file, open, or terminate
		if (fileMenuChoice>terminate) {
			for (int cc=0; cc<FILES.size(); cc++) {
				if (fileMenuChoice==cc+3) { 
					editMenuOpen = true; 
					ID = cc; }
			}
		}
		else if (fileMenuChoice==open)
			FILES.add(new MM(MM.openDoc()));
		else if (fileMenuChoice==terminate)
			MM.checkSaves();
		else throw new Exception("Unknown B");		
	} // end getFileMenu

	
	/*
	 * getEditMenu() -> boolean
	 * 
	 * Shows file edit menu & prompts for user choice.
	 * Then, executes the according function of the user's choice.
	 * 
	 * returns "return to file menu?"
	 * 		true = stay in edit menu
	 * 		false = return to file menu
	 */
	private static boolean getEditMenu() throws Exception {
		// int editMenuChoice
		boolean stay = true;
		
		
		// loop until valid selection
		for (boolean valid = false; !valid;) {
			out.println("\n*" + FILES.get(ID).filePath.getName() + "*");
			out.print(EDIT_MENU);
			if (in.hasNextInt()) {
				editMenuChoice = in.nextInt();
				if (editMenuChoice>0 && editMenuChoice<9) {
					valid = true; 
					in.nextLine();;
					continue; }
			}
			in.nextLine(); // clear scanner
			
			// when false, invalid input
			JOptionPane.showMessageDialog(null, "Invalid input."
					+ "\nPlease enter an integer (> 0)",
					"[ERROR]", err); 
			System.out.println(); 
		}
		if (editMenuChoice==-1) throw new Exception("EditMenu validation failed");

		
		//case switches for different choices
		switch (editMenuChoice) {
		case 1: {
			FILES.get(ID).displayDoc();
			break; }
		case 2: {
			FILES.get(ID).displayLine();
			break; }
		case 3: {
			AC.addLine();
			break; }
		case 4: {
			AC.addLinePrior();
			break; }
		case 5: {
			AC.changeLine();
			break; }
		case 6: {
			AC.deleteLine();
			break; }
		case 7: {
			FILES.get(ID).saveDoc();
			stay = false;
			FILES.remove(ID); } // remove file after successfully saved
		case 8: {
			stay = false;
			break; }
		default:	throw new Exception("Unknown A");
		} // end editMenuChoice switch

		
		return stay;
	} // end getEditMenu
// 	**********************************************************************************************	
} // end JavaTextEditor