import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SearchServlet extends BaseServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String responseHtml = "<html>"+
								"<head><title>Song Finder</title></head>"+
								"<body>"+
								"<p>Welcome to song finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like.+</p>"+
								"<form action=\"SongsServlet\">"+
								"Search Type:"+
								"<select name =\"type\">"+
								"<option value=\"artist\">Artist</option>"+
								"<option value=\"song title\">Song Title</option>"+
								"<option value=\"tag\">Tag</option>"+
								"</select>"+
								"Query:"+
								"<input type=\"text\" name=\"query\">"+
								"<br><br>"+
								"<input type=\"submit\">"+
								"</form>"+
								"</body>"+
								"</html>";
		
		PrintWriter writer = prepareResponse(response);
		writer.println(responseHtml);		
	}

	
	
}
