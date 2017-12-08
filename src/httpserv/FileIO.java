package httpserv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {

	private BufferedReader buffReader = null;
	private FileReader fileReader = null;
	private BufferedWriter buffWriter = null;
	private FileWriter fileWriter = null;
	private String fileName = "";
	
	FileIO(String filename) {
		fileName = filename;
	}
	
	public String readFile() {
		String currentLine = null, fileContents = "";
		try {
			fileReader = new FileReader(fileName);
			buffReader = new BufferedReader(fileReader);
			
			while((currentLine = buffReader.readLine()) != null) {
				fileContents += currentLine;
			}
		} catch (IOException e) {
			System.err.println("There has been an error opening the file " + fileName);
		}
		
		closeReader();
		
		return fileContents;
	}
	
	private void closeReader() {
		try {
			buffReader.close();
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
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
	
	private void closeWriter() {
		try {
			buffWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
