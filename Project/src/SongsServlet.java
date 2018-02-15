import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


public class SongsServlet extends BaseServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	
		String type = request.getParameter("type");
		String query = request.getParameter("query");
		ConcurrentSongLibrary csl =  (ConcurrentSongLibrary) request.getServletContext().getAttribute("songlibrary");
		TreeSet<JSONObject> result;
		if(type.equals("artist")){
			result = csl.searchByArtist(query);
		}
		else if(type.equals("tag")){
			result = csl.searchByTag(query);
		}
		else{
			result = csl.searchByTitle(query);
		}
		
		String responseHtmlHead = "<html>"+
				"<head><title>Song Finder</title></head>"+
				"<body>"+
				"<p>Welcome to song finder! Search for an artist, song title, or tag and we will give you a list of similar songs you might like.+</p>"+
				"<form action=\"SongsServlet\" method=\"get\">"+
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
				"<p>Here are songs you might like!</p>"+
				"<table border=\"3\" style=\"width:100%\">"+
				  "<tr>"+
				    "<td><b>Artist</b></td>"+
				    "<td><b>Song Title</b></td>"+ 
				  "</tr>"+
				"</body>"+
				"</html>";
		
		String responseHtmlContent = "";
		if (result != null){
			for(JSONObject obj: result){
				String artist = (String) obj.get("artist");
				String title = (String) obj.get("title");
				
				responseHtmlContent = responseHtmlContent+"<tr>"+
						"<td>"+artist+"</td>"+
						"<td>"+title+"</td>"+
					"</tr>";
				
			}
		}
		else{
			responseHtmlContent = "";
		}
		
		String reponseHtml = responseHtmlHead + responseHtmlContent;
		PrintWriter writer = prepareResponse(response);
		writer.println(reponseHtml);
	}
}
