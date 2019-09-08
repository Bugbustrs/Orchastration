import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobTracker {
    private final static int INIT_DELAY=1;
    private final static int PERIOD = 2;
    private final static int THREAD_POOL_SIZE = 1;
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
    public static void startJobTracker(){
        scheduler.scheduleAtFixedRate(trackJobs(),INIT_DELAY,PERIOD, TimeUnit.MINUTES);
        System.out.println("Tracker Has Started");
    }

    private static Runnable trackJobs(){
     Runnable runnable = new Runnable(){
         public void run() {
             track();
         }
     };
     return runnable;
    }

    public static void track(){
            Measurement.acquireWriteLock();
            System.out.println("Job Tracking is being Perfomed");
            List<Job> activeJobs=Measurement.getJobs();
            //TODO synchronize appropriately as well as how often does the thread check
            //if end time is reached remove job
            //if its a recurring job once
            //loop backwards so as to avoid skipping an index if I remove an element
            Date currentTime = new Date();
            for(int i=activeJobs.size()-1;i>=0;i--){
                Job job=activeJobs.get(i);
                if(job.isRemovable()){
                    activeJobs.remove(i);
                    System.out.println("Job is with "+job.getMeasurementDesc().get("key") +"Removed");
                }
                else if(job.isResettable(currentTime)){
                    job.reset();
                    System.out.println("Job is with "+job.getMeasurementDesc().get("key") +" is Reset");
                }
            }
            System.out.println("Current Job Size is " + activeJobs.size());
            Measurement.releaseWriteLock();
            System.out.println("Job Tracker has Finished");
        }
}
