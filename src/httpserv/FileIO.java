package httpserv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class handles file input/output
 * @author bwagner
 *
 */
public class FileIO {

	private BufferedReader buffReader = null;
	private FileReader fileReader = null;
	private BufferedWriter buffWriter = null;
	private FileWriter fileWriter = null;
	private String fileName = "";

	/**
	 * Constructor that sets up fileIO with the file name given as an argument.
	 * @param filename Name of the file to process IO on
	 */
	FileIO(String filename) {
		fileName = filename;
	}

	/**
	 * Reads an entire file into a String and returns this as a result.
	 * @return File read into a String
	 */
	public String readFile() {
		String currentLine = null, fileContents = "";
		try {
			fileReader = new FileReader(fileName);
			buffReader = new BufferedReader(fileReader);

			// While we still have lines to read...
			while ((currentLine = buffReader.readLine()) != null) {
				fileContents += currentLine;
			}
		} catch (IOException e) {
			System.err.println("There has been an error opening the file " + fileName);
		}

		closeReader();

		return fileContents;
	}

	/**
	 * Closes the buffer and FileReader objects.
	 */
	private void closeReader() {
		try {
			buffReader.close();
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes all of the data passed into the String into the file.
	 * @param data
	 */
	public void writeFile(String data) {
		try {
			fileWriter = new FileWriter(fileName);
			buffWriter = new BufferedWriter(fileWriter);

			buffWriter.write(data);
		} catch (IOException e) {
			System.err.println("There has been an error writing to the file " + fileName);
		}

		closeWriter();
	}

	/**
	 * Closes the buffer and FileWriter objects.
	 */
	private void closeWriter() {
		try {
			buffWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
