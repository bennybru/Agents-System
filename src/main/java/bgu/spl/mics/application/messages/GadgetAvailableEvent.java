package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class GadgetAvailableEvent implements Event<Integer> {
    private MissionInfo missionInfo;

    public GadgetAvailableEvent(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public String getGadget() {
        return missionInfo.getGadget();
    }
}
