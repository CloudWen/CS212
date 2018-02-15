import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;


public class P2LibraryBuilder{
	
	private Path path;
	private String extension;
	private ConcurrentSongLibrary clibrary;
	private WorkQueue wq;
	
	public P2LibraryBuilder(Path path, String extension, int numThreads){
		this.clibrary = new ConcurrentSongLibrary();
		this.path = path;
		this.extension = extension;
		wq = new WorkQueue(numThreads);
		traverse(path, extension);
		
		

		wq.shutdown();		
		wq.awaitTermination();
			
	}

	
	/**
	 * Recursive method go through all the directory.
	 * When find a json file, excute it and add it into worksqueue.
	 * @param path
	 * @param extension
	 */
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
//				parseSongs(path, extension);
				wq.execute(new SongWorker(path, extension, clibrary));
			}
		}
	}
	
	public ConcurrentSongLibrary getSongLibrary(){
		return this.clibrary;
	}
	
	

}
