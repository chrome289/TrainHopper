package in.trainhopper.trainhopper;

import java.util.ArrayList;

class Leg {
    int train_id, day_def;
    String train_name, train_class;
    String station_id_start, station_id_end, station_name_start, station_name_end;
    int arrival_start, departure_start, arrival_end, departure_end, duration;

    public Leg(Leg leg) {
        this.day_def = leg.day_def;
        this.train_id = leg.train_id;
        this.train_name = leg.train_name;
        this.train_class = leg.train_class;
        this.station_id_start = leg.station_id_start;
        this.station_id_end = leg.station_id_end;
        this.station_name_start = leg.station_name_start;
        this.station_name_end = leg.station_name_end;
        this.arrival_start = leg.arrival_start;
        this.departure_start = leg.departure_start;
        this.arrival_end = leg.arrival_end;
        this.departure_end = leg.departure_end;
        this.duration = leg.duration;
    }

    public Leg() {
    }
}

public class ResultContainer {

    public ResultContainer(ResultContainer resultContainer) {
        this.layover = resultContainer.layover;
        this.layover_def = resultContainer.layover_def;
        for (int x = 0; x < resultContainer.legs.size(); x++)
            this.legs.add(new Leg(resultContainer.legs.get(x)));
        this.total_duration = resultContainer.total_duration;
        this.wait_time = resultContainer.wait_time;
    }

    ArrayList<Leg> legs = new ArrayList<>();
    int total_duration, wait_time, layover, layover_def;

    public ResultContainer() {

    }
}
