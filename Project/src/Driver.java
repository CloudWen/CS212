import java.util.TreeMap;
import java.util.TreeSet;




public class Driver {


	public static void main(String[] args) {
		
		CommandLineParser clp = new CommandLineParser(args);
//		ConcurrentSongLibrary clibrary = new ConcurrentSongLibrary();
		
		
		P2LibraryBuilder plb = new P2LibraryBuilder(clp.getInputPath(),".json",clp.getNumThreads());

		
		if(plb.getSongLibrary() != null){
//			System.out.println(clp.getOrder()+" datastructure: before print.");
			plb.getSongLibrary().printLibrary(clp.getOrder(),  clp.getOutputPath());
		
		
//			System.out.println(plb.getSongLibrary().getSongById("TRBBBOA128F42624DA").getSimilars().get(1));
		}
//		plb.getSongLibrary().searchByQueries(clp.getSearchInputPath(), clp.getSearchOutputPath());
//		System.out.println("main finished!!");
		
		P3P1tester test = new P3P1tester(plb.getSongLibrary());
		test.searchByQueries(clp.getSearchInputPath(), clp.getSearchOutputPath());
	}
	
	
	
	
	
	
	
}
