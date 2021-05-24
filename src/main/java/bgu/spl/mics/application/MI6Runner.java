package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {

        List<Thread> instances = new LinkedList<>();
        Gson gson = new Gson();
        try {
            Input file = JsonInputReader.getInputFromJson(args[0]);

            Inventory.getInstance().load(file.getInventory());
            Squad.getInstance().load(file.getSquad());
            Services runnables = file.getServices();

            JsonObject parser = new JsonParser().parse(new FileReader(args[0])).getAsJsonObject();
            JsonObject services = parser.getAsJsonObject("services");
            int intelCounter = 0;
            for (JsonElement intelligences : services.getAsJsonArray("intelligence")) {
                JsonObject temp = intelligences.getAsJsonObject();
                MissionInfo[] missionInfos = gson.fromJson(temp.getAsJsonArray("missions"), MissionInfo[].class);
                List<MissionInfo> listMissionInfos = new LinkedList<>();
                for (int i = 0; i < missionInfos.length; i++) {
                    listMissionInfos.add(missionInfos[i]);
                }
                Intelligence intelligence = new Intelligence("Intelligence" + intelCounter, listMissionInfos);
                intelCounter++;
                instances.add(new Thread(intelligence));

            }

            for (int i = 0; i < runnables.getMoneypenny(); i++)
                instances.add(new Thread(new Moneypenny(i)));
            for (int i = 0; i < runnables.getM() ; i++)
                instances.add(new Thread(new M(i)));

            TimeService timeService = new TimeService(services.get("time").getAsInt());
            instances.add(new Thread(timeService));

            instances.add(new Thread(new Q()));
            for (Thread temp: instances){
                temp.start();
            }
            for (Thread temp: instances){
                temp.join();
            }
        }
        catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
        Inventory.getInstance().printToFile(args[1]);
        Diary.getInstance().printToFile(args[2]);
    }
}
