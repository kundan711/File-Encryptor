import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class fileEncryptor extends JFrame implements ActionListener {
	JTextArea result = new JTextArea(20,54);
	JLabel errors = new JLabel();
	JScrollPane scroller = new JScrollPane();
	JButton openButton = new JButton("Get File");
	JButton encryptButton = new JButton("Encrypt");
	JButton decryptButton = new JButton("Decrypt");
	JPanel buttonPanel = new JPanel();
	JLabel phraseLabel = new JLabel("Secret Phrase:");
	JTextField phraseTF = new JTextField(40);
	JPanel phrasePanel = new JPanel();
	JPanel southPanel = new JPanel();
	
	public fileEncryptor() {
		setLayout(new java.awt.BorderLayout());
		setSize(700,430);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		scroller.getViewport().add(result);
		result.setLineWrap(true);
		result.setWrapStyleWord(true);
		add(scroller, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new java.awt.GridLayout(3,1));
		southPanel.add(buttonPanel);
		buttonPanel.add(openButton); openButton.addActionListener(this);
		buttonPanel.add(encryptButton); encryptButton.addActionListener(this);
		buttonPanel.add(decryptButton); decryptButton.addActionListener(this);
		southPanel.add(phrasePanel);
		phrasePanel.add(phraseLabel);
		phrasePanel.add(phraseTF);
		southPanel.add(errors);
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == openButton) {
			getFile();
		}
		else if (evt.getSource() == encryptButton) {
			encrypt();
		}
		else if (evt.getSource() == decryptButton) {
			decrypt();
		}
	}
	
	public static void main(String[] args) {
		fileEncryptor display = new fileEncryptor();
		display.setVisible(true);
	}
	
	//display a file dialog
	String getFileName() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile().getPath();
		else
			return null;
	}
	
	//read the file and display it in the text area
	void getFile() {
		String fileName = getFileName();
		if (fileName == null)
			return;
		result.setText("");
		try{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = in.readLine()) != null) {
				result.append(line + "\n");
			}
			in.close();
		} catch(IOException ioe) {
			errors.setText("ERROR: " + ioe);
		}
	}
	
/***********************************************************************/
	
	void encrypt() {
		//Creates a array to store the matrix
		String[] matrix = new String[81];
		//Calls the method to create the matriz
		matrix = CreateAMatrix();
		
		//Retrieves that data that has to be encrypted
		String data = result.getText();
		//Calls the recursion method to encrypt data
		result.setText(encryption(matrix, data));
	}
	
	//Method to create a matrix
	String[] CreateAMatrix() {
		//String that contains all the character needed in the array.
		String letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 '()*+,-./:;<=>[]^?";
		String phrase = phraseTF.getText(); //Retrieves the secret phrase from user
		String[] matrix = new String[81]; //Creates an array to store the matrix
		String A1;
		int c = 0; //keeps track of number of characters stored in the array
		int d = 0; //Used as index to store the characters that are not in the secret phrase
		boolean repetition = false; //Checks if a character is repeated
		int x = 0;
		int index = 0; //index of array
		
		//Starts the loop to store characters from the secret phrase
		for (x = 0; x < phrase.length(); x++) {
			A1 = phrase.substring(x,x+1); //Retrieves individual characters from the secret phrase
			int j = 0;
			repetition = false;
			while (j < x ) {
				//Checks if the next character is already stored in the array
				if (A1.equals(matrix[j])) {
					repetition = true; //Boolean becomes true if the character is already stored in the array
					index--; //Reduces the index by 1 if the character is repeated
					break;
				}
				j++;
			}
			//Stores the character in the matrix array if it is not repeated
			if (repetition == false) { 
				matrix[index] = A1;
				//Keeps count of the character stored in the array
				c++; 
				d++;
			} 
			index++; //Increase the index value by 1 after each loop
		}
		
		//Starts the loop to store the remaining characters that are not in the secret phrase
		for (int i = 0; i < 81; i++) {
			repetition = false; //boolean to check is the character is already stored
			char character = letter.charAt(i); //Retrieves individual character from the string with all required characters
			String ch = Character.toString(character); //Changes character to string
			//Loop to check if the next character is already stored in the matrix
			for(int j = 0; j < c; j++) {
				if(ch.equals(matrix[j])) {
					repetition = true; //Changes the boolean to true if the character is already stored
					break;
				}
			}
			//Stores the character in to the matrix if the character is not stored yet
			if (repetition == false) {
				matrix[d] = ch;
				d++; //Increases the index of array by 1 as a character is stored in the previous index
			}
		}
		return matrix;
	}
	
	//Calls the recursion method for encryption
	String encryption(String mat[], String input) {
		String info = "";
		String Efirst, Esecond;
		
		int j = 0;
		int k = 0;
		
		//base case: Return if the user input has less than 2 characters left
		if (input.length() < 2) {
			info = info + input;
			return info;
		}
		
		//Store the first two characters
		String first = input.substring(0,1);
		String second = input.substring(1,2);
		
		//Compare the first character with the matrix to find the index number
		for(int i = 0; i < 81; i++) {
			if (first.equals(mat[i])) {
				j = i; //Store the index number of the first character
				break;
			}
		}
		//Compare the second character with the matrix to find the index number
		for(int i = 0; i < 81; i++) {
			if (second.equals(mat[i])) {
				k = i; //Store the index number of second character
				break;
			}
		}
		
		//Find the row number and column number for both the characters
		int jrow = j / 9;
		int jcolumn = j % 9;
		int krow = k / 9;
		int kcolumn = k % 9;
		
		//If the two characters are not in same row and same column then swap the column number
		if ((jrow != krow) && (jcolumn != kcolumn)) {
			int l = jcolumn;
			jcolumn = kcolumn;
			kcolumn = l;
		} 
		//If the characters are in same row but different column, then right shift the column number by 1  
		else if((jrow == krow) && (jcolumn != kcolumn)) {
			jcolumn = (j + 1) % 9 ;
			kcolumn = (k + 1) % 9;
		}
		//If the characters are in different row but same column, then down shift the column number by 1  
		else if((jrow != krow) && (jcolumn == kcolumn)) {
			jrow = (j + 9) / 9;
			krow = (k + 9) / 9 ;
			//If the characters are in the last row, then get the top row number
			if(jrow >8) {
				jrow = jrow - 9;
			}
			if(krow > 8) {
				krow = krow - 9;
			}
		}
		//Retrieve encrypted first and second character according to the new row and columns 
		Efirst = mat[(9 * jrow) + jcolumn];
		Esecond = mat[(9 * krow) + kcolumn];
		
		//Store the new encrypted character in info
		info = info + Efirst + Esecond;
		
		//Delete the first two encrypted characters from the user input
		input = input.substring(2,input.length());
		//Call recursion method for encryption and return info
		return info + encryption(mat, input);
	}
	
	void decrypt() {
		
		//Creates a array to store the matrix
		String[] matrix = new String[81];
		//Calls the method to create the matrix
		matrix = CreateAMatrix();
		
		//Retrieves that data that has to be decrypted
		String data = result.getText();
		//Calls the recursion method to decrypt data
		result.setText(decryption(matrix, data));
		
	}
	
	//Calls the recursion method for decryption
	String decryption(String mat[], String input) {
		String info = "";
		String Efirst, Esecond;
		int j = 0;
		int k = 0;
		
		//base case: Return if the user input has less than 2 characters left
		if (input.length() < 2) {
			info = info + input;
			return info;
		}

		//Stores the first two characters
		String first = input.substring(0,1);
		String second = input.substring(1,2);
		
		//Compare the first character with the matrix to find the index number
		for(int i = 0; i < 81; i++) {
			if (first.equals(mat[i])) {
				j = i; //Store the index number of the first character
				break;
			}
		}
		//Compare the second character with the matrix to find the index number
		for(int i = 0; i < 81; i++) {
			if (second.equals(mat[i])) {
				k = i;  //Store the index number of the second character
				break;
			}
		}
		
		//Find the row number and column number for both the characters
		int jrow = j / 9;
		int jcolumn = j % 9;
		int krow = k / 9;
		int kcolumn = k % 9;
		
		//If the two characters are not in same row and same column then swap the column number
		if ((jrow != krow) && (jcolumn != kcolumn)) {
			int l = jcolumn;
			jcolumn = kcolumn;
			kcolumn = l;
		} 
		//If the characters are in same row but different column, then left shift the column number by 1  
		else if((jrow == krow) && (jcolumn != kcolumn)) {
			jcolumn = (jcolumn - 1) % 9 ;
			kcolumn = (kcolumn - 1) % 9;
			//If the character is in the first column, then get the last column number
			if (jcolumn < 0) {
				jcolumn = jcolumn + 9;
			}
			if (kcolumn < 0) {
				kcolumn = kcolumn + 9;
			}
		}
		//If the characters are in different row but same column, then up shift the column number by 1  
		else if((jrow != krow) && (jcolumn == kcolumn)) {
			
			//If the character is in the first column, then get the last row number
			j = (j - 9);
			if (j < 0) {
				j = 80 + j;
			}
			k = (k - 9)  ;
			if (k < 0) {
				k = 80 + k;
			}
			//Retrieve the new row number
			jrow = j / 9;
			krow = k / 9;
		}
		//Retrieve decrypted first and second character according to the new row and columns 
		Efirst = mat[(9 * jrow) + jcolumn];
		Esecond = mat[(9 * krow) + kcolumn];
		
		//Store the new decrypted character in info
		info = info + Efirst + Esecond;
		
		//Delete the first two decrypted characters from the user input
		input = input.substring(2,input.length());
		//Call recursion method for decryption and return info
		return info + decryption(mat, input);
	}

}

