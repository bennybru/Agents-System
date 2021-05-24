package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class MissionReceivedEvent implements Event {
    private MissionInfo missionInfo;

    public MissionReceivedEvent(MissionInfo m) {
        missionInfo = m;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }
    public String getMissionName() {
        return missionInfo.getMissionName();
    }
    public List<String> getSerialAgentsNumbers() {
        return missionInfo.getSerialAgentsNumbers();
    }
    public String getGadget() {
        return missionInfo.getGadget();
    }
    public int getTimeIssued() {
        return missionInfo.getTimeIssued();
    }
    public int getTimeExpired() {
        return missionInfo.getTimeExpired();
    }
    public int getDuration() {
        return missionInfo.getDuration();
    }
}
