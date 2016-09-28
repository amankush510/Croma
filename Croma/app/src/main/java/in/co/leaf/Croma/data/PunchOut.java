package in.co.leaf.Croma.data;

/**
 * Created by rakeshk on 29/09/16.
 */

public class PunchOut {

    public String uid;
    public String user_name;
    public String dateTime;
    public long timeStamp;
    public String IEMI;
    public String locationName;

    public PunchOut(String uid, String user_name, String dateTime, long timeStamp, String IEMI, String locationName) {
        this.uid = uid;
        this.user_name = user_name;
        this.dateTime = dateTime;
        this.timeStamp = timeStamp;
        this.IEMI = IEMI;
        this.locationName = locationName;
    }
}
