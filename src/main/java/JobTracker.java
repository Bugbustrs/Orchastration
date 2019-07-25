import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class JobTracker {
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startJobTracker(){
        ScheduledFuture<?> jobHandle = scheduler.scheduleAtFixedRate(trackJobs(),10,15, TimeUnit.MINUTES);
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
            List<Job> activeJobs=Measurement.getJobs();
            //TODO synchronize appropriately as well as how often does the thread check
            //if end time is reached remove job
            //if its a recurring job once
            //loop backwards so as to avoid skipping an index if I remove an element
            for(int i=activeJobs.size()-1;i>=0;i--){
                Job job=activeJobs.get(i);
                if(job.isRemovable()){
                    activeJobs.remove(i);
                    continue;
                }
                if(job.isResettable()){
                    job.reset();
                }
            }
        }
}
