import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//this class will hold jobs
public class Measurement{
   private static List<Job> activeJobs=new ArrayList<Job>();
   private final static ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();

   public static boolean addMeasurement(JSONObject jobRequest){
            acquireWriteLock();
            if(jobRequest==null) return false;
            //TODO there is need for error checking for valid jobRequest structure before addition
            JSONObject jobDesc = jobRequest.getJSONObject("job_description");
            Job jobTobeScheduled = new Job(jobDesc);
            activeJobs.add(jobTobeScheduled);
            releaseWriteLock();
            return true;
        }
    public static JSONArray getActiveJobs() {
            acquireReadLock();
            JSONArray sentJobs=new JSONArray();
            Date currentTime=new Date();
            for(Job job:activeJobs) {
                if(job.canStart(currentTime)&&!job.isResettable()&&!job.isRemovable()){
                    sentJobs.put(job.getMeasurementDesc());
                }
            }
            releaseReadLock();
            return sentJobs;
    }

    public static boolean recordSuccessfulJob(JSONObject jobDesc){
         //assuming the JsonObj has key field mapping which measurement failed
        //TODO mellar needs to provide me with this info KEY AND INSTANCE
         String key=jobDesc.getString("key");
         int instance=jobDesc.getInt("instance");
         for(Job job:activeJobs){
             String currKey=(String)job.getMeasurementDesc().get("key");
             if(currKey.equals(key)){
                 job.addNodeCount(instance);
                 return true;
             }
         }
         //false means the object is already removed since the count is reached
         return false;
    }

    public static void acquireReadLock(){
       readWriteLock.readLock().lock();
    }

    public static void releaseReadLock(){
       readWriteLock.readLock().unlock();
    }

    public static void acquireWriteLock(){
        readWriteLock.writeLock().lock();
    }

    public static void releaseWriteLock(){
        readWriteLock.writeLock().unlock();
    }

    public static List<Job> getJobs(){
        return activeJobs;
    }
}