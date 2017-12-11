package httpserv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles all SQL interactions, using SQLite.
 * @author bwagner
 *
 */
public class SQL {

	private Connection conn = null;

	/**
	 * Connect to the SQL database.
	 * @param db Path to the database.
	 */
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

	/**
	 * Disconnects from the database.
	 */
	public void disconnect() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Execute a statement
	 * 
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

	/**
	 * Inserts data into the database.
	 * @param firstNum First number to insert.
	 * @param secondNum Second number to insert.
	 * @param result Result of adding the first and second numbers together.
	 */
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

	/**
	 * Select all data in the database and return the last 10 results in descending order.
	 * @return
	 */
	public String selectAll() {
		String sql2 = "SELECT * FROM (SELECT * FROM numbers ORDER BY id DESC LIMIT 10) ORDER BY id DESC";
		String result = "Results<br>";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql2);

			// loop through the result set
			while (rs.next()) {
				result += rs.getInt("firstNumber") + " + " + rs.getInt("secondNumber") + " = " + rs.getInt("result")
						+ "<br>";
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}
}
