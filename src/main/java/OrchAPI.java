import org.json.JSONObject;

import java.util.concurrent.Callable;

public class OrchAPI {

    final String MEASUREMENT_REQUEST_TYPE="SCHEDULE_MEASUREMENT";
    final String MEASUREMENT_CHECK_IN_TYPE="checkIn";
    final String MEASUREMENT_SUCCESSFUL="MeasurementSucceed";

    public OrchAPI() {

    }

    public Object returnResponse(JSONObject request){
       String requestType = (String)request.get("request_type");
       Object response=null; //dont know if ts a string or a json object is needed as of yet?
       if(requestType.equalsIgnoreCase(MEASUREMENT_CHECK_IN_TYPE)){
           //send the client a list of available jobs;
           response=Measurement.getActiveJobs();
       }
       else if(requestType.equals(MEASUREMENT_REQUEST_TYPE)){
            //the request contains Measurement Description so should be added to the list of jobs
            Measurement.addMeasurement(request);
            return null; //or generate success response?;
       }

       else if(requestType.equals(MEASUREMENT_SUCCESSFUL)) {
           return Measurement.recordSuccessfulJob(request);
       }
       else{
           throw new IllegalArgumentException();
       }
       return response;
    }
}
