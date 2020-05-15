package companyNetwork;

import persistence.HSQLDB;

import java.util.HashMap;
import java.util.Map;

public enum CompanyNetwork {
    instance;

    private Map<Integer, Subscriber> participantMap = new HashMap<>();
    private Map<String, IChannel> channelMap = new HashMap<>();

    public void addParticipantToMap(Participant participant) {
        this.participantMap.put(participant.getId(), participant);
    }

    public void addChannelToMap(IChannel channel) {
        this.channelMap.put(channel.getId(), channel);
    }

    public String registerParticipant(String name, String type) {
        if (HSQLDB.instance.participantExists(name)) {
            return "participant " + name + " already exists, using existing postbox_" + name;
        }

        int typeID = HSQLDB.instance.getTypeID(type);
        if (typeID == 0) {
            return "Invalid type";
        }

        int participantID = HSQLDB.instance.insertDataTableParticipants(name, typeID);
        HSQLDB.instance.createTablePostbox(name);
        Participant participant;

        switch (type) {
            case "normal":
                participant = new Participant(participantID, name, ParticipantType.NORMAL);
                break;
            case "intruder":
                participant = new Participant(participantID, name, ParticipantType.INTRUDER);
                break;
            default:
                return "";
        }

        addParticipantToMap(participant);
        return "participant " + participant.getName() + " with type " + participant.getType() + " registered and postbox_" + participant.getName() + " created";
    }
}
