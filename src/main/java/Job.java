import org.json.JSONObject;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Job {
    private JSONObject measurementDesc;
    private Date startTime;
    private Date endTime;
    private Date nextReset;
    private int jobInterval; //in hrs
    private int requiredNodeCount;
    private AtomicInteger currentNodeCount;
    private int instance; //TODO its embedded on the measurementDesc

    public Job (JSONObject jobDesc){
        requiredNodeCount =jobDesc.getInt("node_count");
        jobInterval=jobDesc.getInt("job_interval");//this will dictate if recurring or not
        measurementDesc=jobDesc.getJSONObject("measurement_description");
        startTime=Utils.getDate(measurementDesc.getString("start_time"));
        endTime=Utils.getDate(measurementDesc.getString("end_time")); //this field wont change
        instance=1;
        currentNodeCount = new AtomicInteger(0);
        setNextResetTime();
    }

    private void setNextResetTime(){
        //if not recurring creating a new next reset time is useless as wont use this field
        //otherwise
        if(isRecurring()) {
            this.nextReset=Utils.addHours(startTime,jobInterval);
        }
    }
    //get job with the right parameters in the measurement desc
    public JSONObject getMeasurementDesc() {
        measurementDesc.put("start_time",Utils.formatDate(startTime));
        //this can(needs to) be changed just need to be discussed as the end time might still be too far
        // although the phone will have been done with it  most likely
        measurementDesc.put("end_time",Utils.formatDate(nextReset));
        return measurementDesc;
    }

    public boolean canStart(Date currentTime){
        return currentTime.after(startTime);
    }

    public boolean nodesReached(){
        return currentNodeCount.get() >= requiredNodeCount;
    }

    public boolean isRecurring(){
        return jobInterval != 0;
    }

    public void addNodeCount(int jobinstance){
            if(currentNodeCount.get()<requiredNodeCount&&jobinstance==instance){ //results of the same job instance
                currentNodeCount.getAndIncrement();
            }
    }

    private boolean jobElapsed(){
        Date presentTime = new Date(); //either create once or all the time when checking
        return presentTime.after(endTime);
    }

    public boolean isRemovable(){
        if(jobElapsed()){
            return true;
        }
        if(!isRecurring()){ //means the job is not to be repeated and if the req nodes are reached then can be removed
            return nodesReached();
        }
        return false; //then is recurring and
    }

    public boolean isResettable(){
     if(!isRecurring()) return false; //if the job does not need to be repeated then doesnt need to be reset

         //if it is recurring and the node count is reached reset
         //if the next reset time is reached then reset the time and nodes (might need to change TODO ASK JOSIAH)
         // and wait for the next repetition of the job
     else {
         return nodesReached()|| nextReset.after(startTime);
     }
    }

    public void reset(){
        if(isRecurring()){
            currentNodeCount.set(0);
            instance++;
            startTime=nextReset;
            //this will create a new Date obj thus start and next wont be pointing to the same object
            //will use new start time(obtained from the prev reset time) and interval to create the next Reset time
            setNextResetTime();
        }
    }
}
