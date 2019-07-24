
# Orchestration Server

**Format of the JSON The server should receive**
1)  Scheduling a task to be run on the nodes
```
{
	request_type:string
	job_description:{
            measurement_description:{
                    type:string  
                    key:string  
                    start_time:string  
                    end_time:string  
                    interval_sec:int  
                    count:long  
                    priority:long  
                    parameters:{
                    //depends on the measurement
                    }
            }	
	node_count:int
	job_interval:int 		    
	}	 
	user_id:string			
} 
```
2) Querying tasks from Mobiperf
```
{
	request_type:string
}
```

# Fields in the JSON 

## request_type
i) If mobile phone is requesting tasks from the server
```
request_type : "CHECKIN"  
```
ii) if Web client is scheduling a measurement 
```
request_type : "SCHEDULE_MEASUREMENT"
```
## type
i) Different measurements have different types as shown below
 #### Ping
```
type:"ping"
```
#### DNS Lookup
```
type:"dns_lookup"
```
 #### HTTP
```
type:"http"
```
 #### TCP SpeedTest
```
type:"tcp_speed_test"
```

 #### Trace Route
```
type:"traceroute"
```
## key
Key which is the job ID has to be unique so as to easily map results to the experiment
```
key:"some unique value"
``` 

## start_time and end_time
The times have to be the format **" yyyy-MM-dd'T'HH:mm:ss.SSS'Z' "** 

Example 
```
start_time:"2019-07-08T13:34:02.202Z"
end_time:"2019-07-08T13:38:07.109Z"
```
## count
Maximum number of times that a measurement should be performed. A count of 0 means to continue the measurement indefinitely (until end_time).

I think in our case we should default to 1 and increment it from there(if the user requests so) but definitely not zero as it will drain batteries if the difference between and start and end time is very large
```
count:1 or more
```

## interval_sec
Minimum number of seconds to elapse between consecutive measurements taken with this description.
```
interval_sec : 1 or more 
```
## priority
Larger values indicate Higher priority

Example
```
priority:1000
```
## parameters
####  Ping
 ```
 {
	parameters:
	 {
			target:"www.vula.uct.ac.za"   
     } 
 }
```

####  DNS Look Up
 ```
 {
	parameters:
	 {
			target:"www.google.com" 
			server : "null" //put a null in their for now		  
     } 
 }
```


####  HTTP
 ```
 {
	parameters:
	 {
			target:"www.google.com" 
     } 
 }
```

#### tcp_speed_test
 ```
 {
	parameters:
	 {
			direction:"down" 
     } 
 }




