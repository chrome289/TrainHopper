package in.trainhopper.trainhopper;

import java.util.ArrayList;

/**
 * Created by Siddharth on 25-Jul-16.
 */
class Leg{
    int train_id, day_def;
    String train_name, train_class;
    String station_id_start,station_id_end, station_name_start, station_name_end;
    int arrival_start, departure_start, arrival_end, departure_end;
}
public class ResultContainer {
    ArrayList<Leg> legs = new ArrayList<>();
    int total_duration, wait_time;
}
