package httpserv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
	
	private Connection conn = null;
	
	public void connect(String db) {		
		try {
            // db parameters
            String url = "jdbc:sqlite:" + db;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public void disconnect() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Execute a statement
	 * @param statement
	 */
	public void executeStatement(String statement) {
		try {
			Statement stat = conn.createStatement();
			stat.execute(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void insert(int firstNum, int secondNum, int result) {
		String stat = "INSERT INTO numbers(firstNumber, secondNumber, result) VALUES(?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(stat);
			
			// insert the values
			pstmt.setInt(1, firstNum);
			pstmt.setInt(2, secondNum);
			pstmt.setInt(3, result);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	 public String selectAll(){
		 	String sql2 = "SELECT * FROM (SELECT * FROM numbers ORDER BY id DESC LIMIT 10) ORDER BY id DESC";
	        String sql = "SELECT firstNumber, secondNumber, result FROM numbers";
	        String result = "Results<br>";
	        
	        try {
	             Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql2);
	            
	            // loop through the result set
	            while (rs.next()) {
	                result += rs.getInt("firstNumber") + " + " +
	                                   rs.getInt("secondNumber") + " = " +
	                                   rs.getInt("result") + "<br>";
	            }
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        
	        return result;
	 }
}
