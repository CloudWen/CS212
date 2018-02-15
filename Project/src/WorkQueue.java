import java.util.LinkedList;

public class WorkQueue
{
    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList queue;

	private volatile boolean isShutdown = false;
    
	/**
	 * Constructor that read in the nThreads as parameter to create a ThreadPool with nThreads Threads.
	 * And call threads.start() for every thread.
	 * @param nThreads
	 */
    public WorkQueue(int nThreads)
    {
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new PoolWorker[nThreads];

        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    
    /**
     * Add a runnable task into queue.
     * @param r
     */
    public void execute(Runnable r) {
        
    	if(!this.isShutdown){
    		synchronized(queue) {
        		queue.addLast(r);
        		queue.notify();
        	}
        }
    }
    
    /**
     * After this method, queue stop receiving task.
     */
    public void shutdown(){
    	isShutdown = true;
    	synchronized(queue){
    		queue.notifyAll();
    	}
    }
    
    /**
     * Block until all the Threads finished.
     */
    public void awaitTermination(){
    	if (isShutdown){
    		for(int i=0; i<nThreads; i++){
    			try {
					threads[i].join();

	    			
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
   
    private class PoolWorker extends Thread {
    	
    	
        public void run() {
            Runnable r;

            while (true) {
                synchronized(queue) {
		            
                    while (queue.isEmpty() && !isShutdown) {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }
                    
                    if(queue.isEmpty()&&isShutdown){
                    	break;
                    }

                    r = (Runnable) queue.removeFirst();
                }

                try {
                    r.run();
                }
                catch (RuntimeException e) {
                    // You might want to log something here
                }
            }
        }
        
     

    }
}
