import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LibraryBuilder  {

	private SongLibrary library;
	
	
	//Constructor.
	
	public LibraryBuilder(){
		this.library = new SongLibrary();
	}
	
	//Recursive method takes path and file extension as input.
	//If the path is a directory then call this recursive method to open it.
	//If not, and the file is a .json file then parse it into parser.
	
	public void traverse(Path path, String extension){
		if(path != null &&Files.isDirectory(path)){
			try(DirectoryStream<Path> dir = Files.newDirectoryStream(path)){
				for(Path entry:dir){
					traverse(entry, extension);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (path != null){
			if (path.toString().endsWith(extension.toLowerCase().trim())|| path.toString().endsWith(extension.toUpperCase().trim())){
				parseSongs(path, extension);
			}
		}
	}
	
	//Base case of Recursive method traverse.
	
	public void parseSongs(Path path, String extension){
		JSONParser parser = new JSONParser();
		try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))){

			JSONObject contents = (JSONObject) parser.parse(reader);
			Song song = new Song(contents);
			
			library.addSong(song);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	//Return the SongLibrary.
	
	public SongLibrary getSongLibrary(){
		return this.library;
	}
	
}
