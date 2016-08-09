package in.trainhopper.trainhopper;

import android.util.JsonReader;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Siddharth on 08-Aug-16.
 */
public class JsonParser {
    static ArrayList<ResultContainer> resultContainerArrayList = new ArrayList<>();

    public static ArrayList<ResultContainer> parseResponse(String response) throws IOException {

        JsonReader jsonReader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(response.getBytes()), "UTF-8"));
        readJsonArray(jsonReader);
        jsonReader.close();
        return resultContainerArrayList;
    }

    private static void readJsonArray(JsonReader jsonReader) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            ResultContainer resultContainer = new ResultContainer();
            jsonReader.beginObject();
            jsonReader.nextName();
            jsonReader.beginArray();
            while(jsonReader.hasNext()) {
                jsonReader.beginObject();
                Leg temp=new Leg();
                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    Log.v(MainActivity.TAG, name);
                    if (name.equals("train_id"))
                        temp.train_id = jsonReader.nextInt();
                    if (name.equals("train_name"))
                        temp.train_name = jsonReader.nextString();
                    if (name.equals("train_class"))
                        temp.train_class = jsonReader.nextString().replace("'","").replace("[","").replace("]","").trim();

                    if (name.equals("day_def"))
                        temp.day_def = jsonReader.nextInt();

                    if (name.equals("station_id_start"))
                        temp.station_id_start = jsonReader.nextString();
                    if (name.equals("station_id_end"))
                        temp.station_id_end = jsonReader.nextString();

                    if (name.equals("arrival_start"))
                        temp.arrival_start = jsonReader.nextInt();
                    if (name.equals("arrival_end"))
                        temp.arrival_end = jsonReader.nextInt();
                    if (name.equals("departure_start"))
                        temp.departure_start = jsonReader.nextInt();
                    if (name.equals("departure_end"))
                        temp.departure_end = jsonReader.nextInt();

                    if (name.equals("station_name_start"))
                        temp.station_name_start = jsonReader.nextString();
                    if (name.equals("station_name_end"))
                        temp.station_name_end = jsonReader.nextString();
                }
                resultContainer.legs.add(temp);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            while (jsonReader.hasNext()){
                String name = jsonReader.nextName();
                if (name.equals("total_duration"))
                    resultContainer.total_duration = jsonReader.nextInt();
                if (name.equals("wait_time"))
                    resultContainer.wait_time = jsonReader.nextInt();
            }
            jsonReader.endObject();
            resultContainerArrayList.add(resultContainer);
        }
        jsonReader.endArray();
    }
}