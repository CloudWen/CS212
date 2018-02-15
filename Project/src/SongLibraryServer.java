import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;


public class SongLibraryServer {
	
	public static final int DEFAULT_PORT = 8080;
	public static final String LIBRARY_INPUT = "input/lastfm_subset";
	
	public static void main(String[] args) throws Exception{
		
		
		
		Server server = new Server(DEFAULT_PORT);
		
		ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);     
		server.setHandler(servhandler);
		
		servhandler.addEventListener(new ServletContextListener(){
			
			@Override
			public void contextDestroyed(ServletContextEvent sce){
				
			}
			
			@Override
			public void contextInitialized(ServletContextEvent sce){
				Path path = Paths.get(LIBRARY_INPUT);
				P2LibraryBuilder plb = new P2LibraryBuilder(path,".json",1);
				ConcurrentSongLibrary csl = plb.getSongLibrary();
				if (csl == null){
					csl = new ConcurrentSongLibrary();
				}
				sce.getServletContext().setAttribute("songlibrary", csl);
			}
		});
		
		servhandler.addServlet(SearchServlet.class, "/search");
		servhandler.addServlet(SongsServlet.class, "/SongsServlet");
		
		server.setHandler(servhandler);
		server.start();
		server.join();
		
	}

}
