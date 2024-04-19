/* JavaTextEditor.MM.java
 * 
 * CPS 150
 * Algorithms & Programming I
 * 
 * Final Project: Building a Java Text Editor
 * 
 * Name: Meadow Motz
 * 
 * Description: 
 * 		Supplies functions for the class Main to:
 * 			Open a text document
 *			Save a text document
 *			Display the document
 *			Display a line in the document at a given line number
 *		Also houses the buffer for the "current text document," which can be edited by class AC
 */

package JavaTextEditor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;

public class MM {
// 	**********************************************************************************************	
	// data fields
	public File filePath;
	public ArrayList<String> textDoc;
	
	// global variables
	private static PrintStream out = System.out;
	private static JFileChooser chooser = new JFileChooser();
	private static final FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");	
	private static final int err = JOptionPane.ERROR_MESSAGE;
	private static Scanner in = new Scanner(System.in);
	
	// constructor
	public MM(File input) throws FileNotFoundException { 	
		// initialize textDoc to empty
		textDoc = new ArrayList<String>();
		
		// prepare new file or load existing into buffer?
		if (input.exists()) {
			Scanner read = new Scanner(input);
		
			// copying text file by line into textDoc buffer
			while (read.hasNextLine()) textDoc.add(read.nextLine()); 
			read.close(); 
		}
				
		filePath = input; 
		out.println("\tOpened " + input.getName() + "\n");
	} // end constructor
// 	**********************************************************************************************
	// public instance methods
	
	/*
	 * saveDoc() -> void
	 * 
	 * ~ Changes	MM FILES[ID]
	 * 
	 * Using the saved filePath and fileName,
	 * 		saves the file that path with that name.
	 * If there's a file with the same name, it asks the user 
	 * 		if they wish to overwrite the existing file
	 * 		... if no overwrite, appends (i) to the file name, representing a copy
	 * 
	 * throws if file not found
	 */
	public void saveDoc() throws FileNotFoundException, NullPointerException { 
		// ArrayList<String> textDoc
		// MM FILES[ID]
		// File filePath
		
		String fileBuffer = filePath.getPath();
		
		
		// overwrite/copy
		if (filePath.exists() && !askOverwrite(filePath)) fileBuffer = addCopyTag(fileBuffer);

                
        // create new file, overwriting if user permitted
       	PrintWriter write = new PrintWriter(new File(fileBuffer));
        for (String line : textDoc) write.println(line); 
        write.close();
        
        
        JOptionPane.showMessageDialog(null, "File saved successfully.\n"
            		+ "Created " + fileBuffer);
	} // end saveDoc
	
	
	/*
	 * displayDoc() -> void
	 * 
	 * prints the current textDoc line by line
	 */
	public void displayDoc() {
		// ArrayList<String> textDoc
		// File filePath
		
		
		out.println("_" + filePath.getName() + "_\n");
		for (int cc = 0; cc<textDoc.size(); cc++) out.println(cc+1 + "\t" + textDoc.get(cc)); 
		out.println();
	} // end displayDoc
	
	
	/*
	 * displayLine() -> void
	 * 
	 * prints the contents of current textDoc at user-specified specified line
	 */
	public void displayLine() throws Exception { 
		// File filePath
		// ArrayList<String> textDoc
		
		
		if (textDoc.size() == 0 || textDoc.size() == 1) throw new 
			IllegalArgumentException("Document is too short to do this");
		
		out.println("Enter the line number that you wish to display:");
		int lineNum = -1;
		
		
		// loop until valid lineNum input
		for (boolean valid = false; !valid;) {
			out.print("\t-> ");
			if (in.hasNextInt()) {
				lineNum = in.nextInt();
				if (lineNum>0 && lineNum<=textDoc.size()) {
					valid = true; 
					continue; }
			}
			in.nextLine(); // clear scanner
			
			// when false, invalid input
			JOptionPane.showMessageDialog(null, "Invalid input."
					+ "\nPlease enter an integer (> 0)"
					+ "\n   & (<= doc size)", "[ERROR]", err);	
			out.println();
		}
		
		
		// output line contents
		out.println("_" + filePath.getName() + "_\n");
		out.println(lineNum + "\t" + textDoc.get(lineNum-1));
		out.println();
	} // end displayLine
	
	
	
	// public static methods
	
	/*
	 * openDoc() -> void
	 * 
	 * ~ Changes 	filePath
	 * 				textDoc
	 * 
	 * Opens file library for user to choose file or specify new file name.
	 * If file exists, 
	 * 		copies text from file into textDoc buffer for editing
	 * 		& saves filePath and fileName.
	 * If opening a new file (user typed a new file name),
	 * 	 	keeps an empty textDoc buffer
	 *  	& saves filePath and fileName.
	 *  
	 *  Does not create files!!
	 *  
	 * throws if file not found or "cancel"/"X" is pressed in open dialog
	 */
	public static File openDoc() throws NullPointerException, InvalidPathException, FileNotFoundException, IllegalArgumentException { 
		// File filePath
		// ArrayList<String> textDoc
		
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);

		// get file (only txt files)
		chooser.showOpenDialog(null);
		File input = chooser.getSelectedFile();
		if (input==null) {
			Main.fileMenuChoice = -1;
			throw new NullPointerException("[!]:\t\tCancel was pressed.\n"); }
				// return to menu if cancel
		input = toTxt(input);
		
		// check if doc already open
		for (MM el : Main.FILES)
			if (el.filePath.equals(input)) throw new IllegalArgumentException("[ERROR]:\tFile is already open");
		
		return input;
	} // end openDoc
	
	
	/*
	 * checkSave() -> void
	 *
	 * checks if there's any open documents.
	 * if so, it asks if the user would like to save them.
	 * if yes, it asks the user if they'd like to save each file.
	 * for each, if yes, it saves the file and closes the document
	 */
	public static void checkSaves() throws FileNotFoundException, NullPointerException {
		// ArrayList<MM> FILES
		int saveChoice = -1;
		final int YES = JOptionPane.YES_OPTION;
		final int CANCEL = JOptionPane.CANCEL_OPTION;
		final int NO = JOptionPane.NO_OPTION;
		
		if (Main.FILES.size()>0) 
			saveChoice = JOptionPane.showConfirmDialog(null,
								"You have unsaved documents.\nWould you like to save them?", // message
								"[!] Choose an option", // title
								JOptionPane.YES_NO_CANCEL_OPTION, // option type
								JOptionPane.QUESTION_MESSAGE); // message type
		
		
		if (saveChoice==CANCEL) {
			Main.fileMenuChoice = -1;
			throw new NullPointerException("[!]:\t\tCancel was pressed.\n"); }
		if (saveChoice==YES) {
			while (Main.FILES.size()>0) {
				saveChoice = JOptionPane.showConfirmDialog(null,
									Main.FILES.get(0).filePath.getPath(),
									"Save?",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
				if (saveChoice==CANCEL) {
					Main.fileMenuChoice = -1;
					throw new NullPointerException("[!]:\t\tCancel was pressed.\n"); }
				if (saveChoice==YES) {
					Main.FILES.get(0).saveDoc();
					Main.FILES.remove(0); }
				if (saveChoice==NO) Main.FILES.remove(0);
			}
		}
	} // end checkSaves
// 	**********************************************************************************************
	// private static methods

	/*
	 * toTxt(File) -> File
	 * 
	 * checks for file name length, and if too short to 
	 * 		contain a file extension, it adds ".txt"
	 * otherwise, it removes the current file extension if there is one
	 * 		and adds/replaces it with ".txt"
	 */
	private static File toTxt(File input) {
		String path = input.getPath();
		if (input.getName().length()<4) path = ".txt";
		else {			
			// remove current extension (possibly not an extension!!)
			if (path.charAt(path.length()-4)=='.') path = path.substring(0, path.length()-4);
			path+=".txt"; }
		return new File(path);
	} // end toTxt
	
	
	/*
	 * askOverwrite() -> boolean
	 * 
	 * Asks user if they wish to overwrite an existing file.
	 * Returns their choice, but returns to menu if cancel
	 */
	private static boolean askOverwrite(File path) throws NullPointerException {
		// File filePath
		
		boolean yn = false; // true = yes; false = no
		
		
		// get user choice
		int choice = JOptionPane.showConfirmDialog(null,
				"The destination already has a file named \"" 
				+ path.getName() + "\"\n"		// message
				+ "Would you like to replace it?", 
				"[!] Choose an option", // title
				JOptionPane.YES_NO_CANCEL_OPTION, // option type
				JOptionPane.QUESTION_MESSAGE); // message type
		
		
		if (choice==0) yn = true;
		else if (choice==1) yn = false;
		else if (choice==2) {
			Main.editMenuChoice = -1;
			Main.fileMenuChoice = -1;
			throw new NullPointerException("[!]:\t\tCancel was pressed.\n"); }
		
		
		return yn;
	} // end askOverwrite
	
	
	/*
	 * addCopyTag(String) -> String
	 * 
	 * extracts file name, 
	 * 	checks if there's any copies of it, 
	 * 	makes a new copy tag,
	 * 	and inserts it into the file path passed to it
	 */
	private static String addCopyTag(String path) {
		String parent = new File(path).getParent();
		String name = new File(path).getName();
		
		// remove extension
		name = name.substring(0, name.length()-4);
		
		// determine copy number
		int copyNum = 1;
		for (int cc = 1; new File(name + " (" + cc + ").txt").exists(); cc++)
			copyNum++;

		// insert tag
		name += " (" + copyNum + ").txt";
		
		
		return (parent + "\\" + name);
	} // end addCopyTag
// 	**********************************************************************************************
} // end class MM
