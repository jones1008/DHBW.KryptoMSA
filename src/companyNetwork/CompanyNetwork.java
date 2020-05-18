package companyNetwork;

import persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CompanyNetwork {
    instance;

    private Map<Integer, Participant> participantMap = new HashMap<>();
    private Map<String, IChannel> channelMap = new HashMap<>();

    // participant
    public void addParticipantToMap(Participant participant) {
        this.participantMap.put(participant.getId(), participant);
    }
    public Participant getParticipantFromMapById(int id) {
        return participantMap.get(id);
    }
    public Participant getParticipantFromMapByName(String name) {
        for (Participant participant : participantMap.values()) {
            if (participant.getName().equals(name)) {
                return participant;
            }
        }
        return null;
    }
    private Participant createParticipant(String name, String type, int participantID) {
        Participant participant;

        switch (type.toLowerCase()) {
            case "normal":
                participant = new Participant(participantID, name, ParticipantType.NORMAL);
                break;
            case "intruder":
                participant = new Participant(participantID, name, ParticipantType.INTRUDER);
                break;
            default:
                return null;
        }

        addParticipantToMap(participant);
        return participant;
    }
    public String registerParticipant(String name, String type) {
        if (getParticipantFromMapByName(name) != null) {
            return "participant " + name + " already exists, using existing postbox_" + name;
        }

        int typeID = DBType.instance.getTypeID(type);
        if (typeID == 0) {
            return "Invalid type";
        }
        int participantID = DBParticipant.instance.insertDataTableParticipants(name, typeID);
        DBPostbox.instance.createTablePostbox(name);

        Participant participant = createParticipant(name, type, participantID);

        return "participant " + participant.getName() + " with type " + participant.getType() + " registered and postbox_" + participant.getName() + " created";
    }

    // channel
    public void addChannelToMap(IChannel channel) {
        this.channelMap.put(channel.getName(), channel);
    }
    public boolean isChannelRegistered(String name) {
        return channelMap.containsKey(name);
    }
    public boolean isChannelRegistered(Subscriber participant1, Subscriber participant2) {
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
    public IChannel getChannel(Subscriber participant1, Subscriber participant2) {
        for (IChannel channel : channelMap.values()) {
            if ((channel.getParticipant01().equals(participant1)
                    || channel.getParticipant01().equals(participant2))
                    && (channel.getParticipant02().equals(participant1)
                    || channel.getParticipant02().equals(participant2))) {
                return channel;
            }
        }
        return null;
    }
    public IChannel getChannel(String name) {
        return channelMap.get(name);
    }
    public Map<String, IChannel> getChannelMap() {
        return channelMap;
    }
    public String deleteChannel(String name) {
        if (isChannelRegistered(name)) {
            if (DBChannel.instance.deleteChannel(name)) {
                channelMap.remove(name);
                return "channel " + name + " deleted";
            }
            return "Error deleting channel " + name;
        }
        return "unknown channel " + name;
    }

    // general
    public void initMaps() {
        List<Participant> participants = DBParticipant.instance.getAllParticipants();
        for (Participant participant : participants) {
            addParticipantToMap(participant);
        }
        List<Channel> channels = DBChannel.instance.getAllChannels();
        for (Channel channel : channels) {
            addChannelToMap(channel);
        }
    }
}
