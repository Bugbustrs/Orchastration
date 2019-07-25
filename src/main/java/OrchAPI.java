import org.json.JSONObject;

import java.util.concurrent.Callable;

public class OrchAPI implements Callable {

    final String MEASUREMENT_REQUEST_TYPE="Measurement";
    final String MEASUREMENT_CHECK_IN_TYPE="checkIn";

    private JSONObject request;

    public OrchAPI(JSONObject request) {
        this.request = request;
    }

    public Object call() {
       String requestType = (String)request.get("request_type");
       Object response=null; //dont know if ts a string or a json object is needed as of yet?
       if(requestType.equals(MEASUREMENT_CHECK_IN_TYPE)){
           //send the client a list of available jobs;
           response=Measurement.getActiveJobs();
       }
       else if(requestType.equals(MEASUREMENT_REQUEST_TYPE)){
            //the request contains Measurement Description so should be added to the list of jobs
            Measurement.addMeasurement(request);
            return null; //or generate success response?;
       }
       else{
           //TODO recording successful jobs
           throw new IllegalArgumentException();
       }
       return response;
    }
}
