package companyNetwork;

import persistence.HSQLDB;

import java.util.HashMap;
import java.util.Map;

public enum CompanyNetwork {
    instance;

    private Map<Integer, Subscriber> participantMap = new HashMap<>();
    private Map<String, IChannel> channelMap = new HashMap<>();

    public void addSubscriberToMap(Subscriber subscriber) {
        this.participantMap.put(subscriber.getId(), subscriber);
    }

    public void addChannelToMap(IChannel channel) {
        this.channelMap.put(channel.getName(), channel);
    }

    public Subscriber getSubscriberForName(String name) {
        for (Subscriber subscriber : participantMap.values()) {
            if (subscriber.getName().equals(name)) {
                return subscriber;
            }
        }
        return null;
    }

    public boolean isChannelRegistered(String name, Subscriber participant1, Subscriber participant2) {
        if (channelMap.containsKey(name)) {
            return true;
        }

        for (IChannel channel : channelMap.values()) {
            if ((channel.getParticipant01().equals(participant1)
                    || channel.getParticipant01().equals(participant2))
                    && (channel.getParticipant02().equals(participant1)
                    || channel.getParticipant02().equals(participant2))) {
                return true;
            }
        }
        return false;
    }

    public String registerParticipant(String name, String type) {
        if (HSQLDB.instance.participantExists(name)) {
            if (getSubscriberForName(name) == null) {
                int participantID = HSQLDB.instance.getParticipantId(name);
                createParticipant(name, type, participantID);
            }
            return "participant " + name + " already exists, using existing postbox_" + name;
        }

        int typeID = HSQLDB.instance.getTypeID(type);
        if (typeID == 0) {
            return "Invalid type";
        }
        int participantID = HSQLDB.instance.insertDataTableParticipants(name, typeID);
        HSQLDB.instance.createTablePostbox(name);

        Participant participant = createParticipant(name, type, participantID);

        return "participant " + participant.getName() + " with type " + participant.getType() + " registered and postbox_" + participant.getName() + " created";
    }

    private Participant createParticipant(String name, String type, int participantID) {
        Participant participant;

        switch (type) {
            case "normal":
                participant = new Participant(participantID, name, ParticipantType.NORMAL);
                break;
            case "intruder":
                participant = new Participant(participantID, name, ParticipantType.INTRUDER);
                break;
            default:
                return null;
        }

        addSubscriberToMap(participant);
        return participant;
    }
}
