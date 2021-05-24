package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class AgentsAvailableEvent implements Event<Object[]> {
    private MissionInfo missionInfo;

    public AgentsAvailableEvent(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public List<String> getAgentsSerials() {
        return missionInfo.getSerialAgentsNumbers();
    }

    public int getDuration() {
        return missionInfo.getDuration();
    }
}