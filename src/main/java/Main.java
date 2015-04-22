import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class Main extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

	if(req.getRequestURI().endsWith("appletTestClass")) {
		sendApplet(req, resp);
	} if (req.getRequestURI().endsWith("appletTest")) {
      showApplet(req,resp);
    } else {
      showHome(req,resp);
    }
  }
  
  private void showApplet(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
	  	resp.getWriter().println("<html><body>");
	    resp.getWriter().println("<p>Hello from the Java Server!!!</p>");
	    resp.getWriter().println("<applet code=\"appletTestClass\" width=\"300\" height =\"300\"></body></html>");
  }
  
  private void sendApplet(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
	  InputStream is = Main.class.getClassLoader().getResourceAsStream("appletTest.class");
	  byte[] bytes = new byte[is.available()];
	  while(is.available() > 0) {
		  is.read(bytes);
		  resp.getOutputStream().write(bytes);
		  bytes = new byte[is.available()];
	  }
	  is.close();
  }

  private void showHome(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.getWriter().print("Hello from Java!");
  }

  @SuppressWarnings("unused")
private void showDatabase(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Connection connection = null;
    try {
      connection = getConnection();

      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      String out = "Hello!\n";
      while (rs.next()) {
          out += "Read from DB: " + rs.getTimestamp("tick") + "\n";
      }

      resp.getWriter().print(out);
    } catch (Exception e) {
      resp.getWriter().print("There was an error: " + e.getMessage());
    } finally {
      if (connection != null) try{connection.close();} catch(SQLException e){}
    }
  }

  private Connection getConnection() throws URISyntaxException, SQLException {
    URI dbUri = new URI(System.getenv("DATABASE_URL"));

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    int port = dbUri.getPort();

    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

    return DriverManager.getConnection(dbUrl, username, password);
  }

  public static void main(String[] args) throws Exception {
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new Main()),"/*");
    server.start();
    server.join();
  }
}
