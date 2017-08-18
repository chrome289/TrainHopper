package in.trainhopper.trainhopper;

import android.content.Context;
import android.util.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class JsonParser {
    static ArrayList<ResultContainer> resultContainerArrayList = new ArrayList<>();
    static long resultQueryID;
    static int resultCurrentPage;

    int parseResponse1(Context context, String response) throws IOException {

        JsonReader jsonReader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(response.getBytes()), "UTF-8"));
        int result = readJsonArray1(context, jsonReader);
        jsonReader.close();
        return result;
    }

    private int readJsonArray1(Context context, JsonReader jsonReader) {

        try {
            jsonReader.beginArray();
            jsonReader.beginObject();
            jsonReader.nextName();
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                ResultContainer resultContainer = new ResultContainer();
                jsonReader.beginObject();
                jsonReader.nextName();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject();
                    Leg temp = new Leg();
                    while (jsonReader.hasNext()) {
                        String name = jsonReader.nextName();
                        // Log.v(MainActivity.TAG, name);
                        if (name.equals("train_id"))
                            temp.train_id = jsonReader.nextInt();
                        if (name.equals("train_name"))
                            temp.train_name = jsonReader.nextString();
                        if (name.equals("train_class"))
                            temp.train_class = jsonReader.nextString().replace("'", "").replace("[", "").replace("]", "").trim();

                        if (name.equals("day_def"))
                            temp.day_def = jsonReader.nextInt();
                        if (name.equals("duration"))
                            temp.duration = jsonReader.nextInt();

                        if (name.equals("id_start")) {
                            temp.station_id_start = jsonReader.nextString();
                            String []tempArr=context.getResources().getStringArray(R.array.station_name);
                            String tempName="";
                            for(String str:tempArr){
                                if(str.contains("( "+temp.station_id_start+" )"))
                                    tempName=str;
                            }
                            temp.station_name_start = tempName.replace("( "+temp.station_id_start+" )","").trim();
                        }
                        if (name.equals("id_end")) {
                            temp.station_id_end = jsonReader.nextString();
                            String[] tempArr = context.getResources().getStringArray(R.array.station_name);
                            String tempName = "";
                            for (String str : tempArr) {
                                if (str.contains("( " + temp.station_id_end + " )"))
                                    tempName = str;
                            }
                            temp.station_name_end = tempName.replace("( " + temp.station_id_end + " )", "").trim();
                        }

                        if (name.equals("arrival_start"))
                            temp.arrival_start = jsonReader.nextInt();
                        if (name.equals("arrival_end"))
                            temp.arrival_end = jsonReader.nextInt();
                        if (name.equals("departure_start"))
                            temp.departure_start = jsonReader.nextInt();
                        if (name.equals("departure_end"))
                            temp.departure_end = jsonReader.nextInt();

                    }
                    resultContainer.legs.add(temp);
                    jsonReader.endObject();
                }
                jsonReader.endArray();
                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    if (name.equals("total_duration"))
                        resultContainer.total_duration = jsonReader.nextInt();
                    if (name.equals("wait_time"))
                        resultContainer.wait_time = jsonReader.nextInt();
                    if (name.equals("layover"))
                        resultContainer.layover = jsonReader.nextInt();
                    if (name.equals("layoverDef"))
                        resultContainer.layover_def = jsonReader.nextInt();
                }
                jsonReader.endObject();
                //Log.v("nero",resultContainer.layover+"%"+resultContainer.layover_def);
                resultContainerArrayList.add(resultContainer);
            }
            jsonReader.endArray();
                jsonReader.nextName();
                resultQueryID = jsonReader.nextLong();
                resultCurrentPage = 0;
            //Log.v("nero","query id  "+resultQueryID);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    int parseResponse2(Context context, String response) throws IOException {

        JsonReader jsonReader = new JsonReader(new InputStreamReader(new ByteArrayInputStream(response.getBytes()), "UTF-8"));
        int result = readJsonArray2(context, jsonReader);
        jsonReader.close();
        return result;
    }

    private int readJsonArray2(Context context, JsonReader jsonReader) {

        try {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                ResultContainer resultContainer = new ResultContainer();
                jsonReader.beginObject();
                jsonReader.nextName();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject();
                    Leg temp = new Leg();
                    while (jsonReader.hasNext()) {
                        String name = jsonReader.nextName();
                        // Log.v(MainActivity.TAG, name);
                        if (name.equals("train_id"))
                            temp.train_id = jsonReader.nextInt();
                        if (name.equals("train_name"))
                            temp.train_name = jsonReader.nextString();
                        if (name.equals("train_class"))
                            temp.train_class = jsonReader.nextString().replace("'", "").replace("[", "").replace("]", "").trim();

                        if (name.equals("day_def"))
                            temp.day_def = jsonReader.nextInt();
                        if (name.equals("duration"))
                            temp.duration = jsonReader.nextInt();

                        if (name.equals("id_start")) {
                            temp.station_id_start = jsonReader.nextString();
                            String []tempArr=context.getResources().getStringArray(R.array.station_name);
                            String tempName="";
                            for(String str:tempArr){
                                if(str.contains("( "+temp.station_id_start+" )"))
                                    tempName=str;
                            }
                            temp.station_name_start = tempName.replace("( "+temp.station_id_start+" )","").trim();
                        }
                        if (name.equals("id_end")) {
                            temp.station_id_end = jsonReader.nextString();
                            String[] tempArr = context.getResources().getStringArray(R.array.station_name);
                            String tempName = "";
                            for (String str : tempArr) {
                                if (str.contains("( " + temp.station_id_end + " )"))
                                    tempName = str;
                            }
                            temp.station_name_end = tempName.replace("( " + temp.station_id_end + " )", "").trim();
                        }

                        if (name.equals("arrival_start"))
                            temp.arrival_start = jsonReader.nextInt();
                        if (name.equals("arrival_end"))
                            temp.arrival_end = jsonReader.nextInt();
                        if (name.equals("departure_start"))
                            temp.departure_start = jsonReader.nextInt();
                        if (name.equals("departure_end"))
                            temp.departure_end = jsonReader.nextInt();

                    }
                    resultContainer.legs.add(temp);
                    jsonReader.endObject();
                }
                jsonReader.endArray();
                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    if (name.equals("total_duration"))
                        resultContainer.total_duration = jsonReader.nextInt();
                    if (name.equals("wait_time"))
                        resultContainer.wait_time = jsonReader.nextInt();
                    if (name.equals("layover"))
                        resultContainer.layover = jsonReader.nextInt();
                    if (name.equals("layoverDef"))
                        resultContainer.layover_def = jsonReader.nextInt();
                }
                jsonReader.endObject();
               // Log.v("nero",resultContainer.layover+"%"+resultContainer.layover_def);
                resultContainerArrayList.add(resultContainer);
            }
            jsonReader.endArray();
            //Log.v("nero","query id  "+resultQueryID);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}
