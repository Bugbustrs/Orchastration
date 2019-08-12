import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//this class will hold jobs
public class Measurement {
    private static List<Job> activeJobs;
    private static ReentrantReadWriteLock readWriteLock;

    public static void init() {
        activeJobs = new ArrayList<Job>();
        readWriteLock = new ReentrantReadWriteLock();
    }

    public static boolean addMeasurement(JSONObject jobRequest) {
        acquireWriteLock();
        if (jobRequest == null) return false;
        //TODO there is need for error checking for valid jobRequest structure before addition
        System.out.println(jobRequest.toString());
        JSONObject jobDesc = jobRequest.getJSONObject("job_description");
        Job jobTobeScheduled = new Job(jobDesc);
        activeJobs.add(jobTobeScheduled);
        releaseWriteLock();
        System.out.println("job added:" + jobTobeScheduled);
        return true;
    }

    public static JSONArray getActiveJobs(){
        acquireReadLock();
        JSONArray sentJobs = new JSONArray();
        Date currentTime = new Date();
        for (Job job : activeJobs) {
            if (job.canStart(currentTime) && !job.isRemovable() && !job.isResettable(currentTime)) {
                sentJobs.put(job.getMeasurementDesc());
            }
        }
        releaseReadLock();
        return sentJobs;
    }

    public static JSONArray getAllJobs(){
        acquireReadLock();
        JSONArray sentJobs= new JSONArray();
        for(Job job:activeJobs){
            sentJobs.put(job.getMeasurementDesc());
        }
        releaseReadLock();
        return sentJobs;
    }

    public static boolean recordSuccessfulJob(JSONObject jobDesc) {
        //assuming the JsonObj has key field mapping which measurement failed
        String key = jobDesc.getString("task_key");
        //int instance = jobDesc.getInt("instance");
        for (Job job : activeJobs) {
            String currKey = (String) job.getMeasurementDesc().get("key");
            if (currKey.equals(key) && jobDesc.getBoolean("success")){
                System.out.println("Job with key : "+currKey+"has been incrimented by one");
                job.addNodeCount();
                if(job.nodesReached()) System.out.println("\nJobs with Key "+key+" has Reached its Req Node count\n");
                return true;
            }
        }
        //false means the object is already removed since the count is reached
        return false;
    }

    public static void acquireReadLock() {
        readWriteLock.readLock().lock();
    }

    public static void releaseReadLock() {
        readWriteLock.readLock().unlock();
    }

    public static void acquireWriteLock() {
        readWriteLock.writeLock().lock();
    }

    public static void releaseWriteLock() {
        readWriteLock.writeLock().unlock();
    }

    public static List<Job> getJobs() {
        return activeJobs;
    }
}