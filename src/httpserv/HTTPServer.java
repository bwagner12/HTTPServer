package httpserv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * This class will set up an HTTP server and handle going to the page "/test" and "exit"
 * @author bwagner
 *
 */
public class HTTPServer {

	private static HttpServer server;
	private static SQL sql;
	private static String curDir = System.getProperty("user.dir") + "\\bin\\httpserv\\";

	/**
	 * This will actually start the HTTP server
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Set up the HTTP server
		server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/test", new MyHandler());
		server.createContext("/exit", new ExitHandler());
		server.setExecutor(null); // creates a default executor
		server.start();

		// Set up the SQLite database
		sql = new SQL();
		sql.connect(curDir + "numbers.db");
		String statement = "CREATE TABLE IF NOT EXISTS numbers (\n" + "	id integer PRIMARY KEY,\n"
				+ "	firstNumber integer NOT NULL,\n" + "	secondNumber integer NOT NULL,\n"
				+ " result integer NOT NULL\n" + ");";
		// create the proper table, if it doens't exist yet
		sql.executeStatement(statement);
	}

	/**
	 * Handles page loading and when the user clicks the Add button
	 * 
	 * @author bwagner
	 *
	 */
	static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			String req = t.getRequestMethod();

			// Handle a GET request. (Opens the page by reading the file)
			if (req.equalsIgnoreCase("GET")) {
				getPage(t);
			} else if (req.equalsIgnoreCase("POST")) {
				int fnum, snum;
				InputStream stream = t.getRequestBody(); // Read the input from the user

				// These next lines format the input into s number so we can add the two
				// together
				String[] input = (new String(stream.readAllBytes())).split("&");

				stream.close(); // close the input stream

				// get the integer values from the strings and convert to integers
				fnum = Integer.parseInt(input[0].substring(5));
				snum = Integer.parseInt(input[1].substring(5));

				// Insert the values into the table
				sql.insert(fnum, snum, fnum + snum);

				getPage(t);
			}
		}
	}

	/**
	 * Will load the web page index.html and inject a string (addon) at the bottom
	 * of the page
	 * 
	 * @param t
	 * @param addon
	 * @throws IOException
	 */
	private static void getPage(HttpExchange t) throws IOException {
		FileIO file = new FileIO(curDir + "index.html");

		String response = file.readFile();
		String[] split = response.split("</form>"); // split at the closing form so that we can inject text.
		response = split[0] + sql.selectAll() + "</form>" + split[1];
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	/**
	 * Shuts down the server. There are problems with this though, as it seems
	 * fragments are still running in the background.
	 * 
	 * @author bwagner
	 *
	 */
	static class ExitHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			String response = "Server closing, goodbye";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
			server.stop(0);
			sql.disconnect();
			System.exit(0);
		}
	}
}