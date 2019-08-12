import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private final static DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final static long HOURS=3600*1000; //since the intervals are in hours
    public static Date getDate(String date)
    {
        Date result=null;
        try{
        result = dateFormat.parse(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String formatDate(Date date){
        return dateFormat.format(date);
    }
    public static Date addHours(Date oldDate,int interval){ //interval is in hours
        return new Date(oldDate.getTime()+(interval*HOURS));
    }
}
