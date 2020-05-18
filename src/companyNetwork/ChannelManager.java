package companyNetwork;

import persistence.HSQLDB;

import java.util.List;
import java.util.Map;

public enum ChannelManager implements IChannelManager {
    instance;
    public String create(String name, String participant01, String participant02) {
        if (participant01.equals(participant02)) {
            return participant01 + " and " + participant02 + " are identifical - cannot create channel on itself";
        }

        Participant participant1 = CompanyNetwork.instance.getParticipantFromMapByName(participant01);
        Participant participant2 = CompanyNetwork.instance.getParticipantFromMapByName(participant02);

        if (participant1 == null || participant2 == null) {
            return participant01 + " or " + participant02 + " do not exists";
        }

        if (CompanyNetwork.instance.isChannelRegistered(name)) {
            return "channel " + name + " already exists";
        }

        if (CompanyNetwork.instance.isChannelRegistered(participant1, participant2)) {
            return "communication channel between " + participant01 + " and " + participant02 + " already exists";
        }

        IChannel channel = new Channel(name, participant1, participant2);
        CompanyNetwork.instance.addChannelToMap(channel);
        HSQLDB.instance.insertDataTableChannel(channel);

        return "channel " + name + " from " + participant01 + " to " + participant02 + " successfully created";
    }

    public String showAllChannels() {
        Map<String, IChannel> channelMap = CompanyNetwork.instance.getChannelMap();
        if (!channelMap.isEmpty()) {
            String ret = "";
            for (IChannel channel : channelMap.values()) {
                ret += channel.getName() + " | branch_" + channel.getParticipant01().getName() + " and branch_" + channel.getParticipant02().getName() + "\n";
            }
            return ret;
        } else {
            return "No channels found.";
        }
    }
    public String deleteChannel(String name) {
        Map<String, IChannel> channelMap = CompanyNetwork.instance.getChannelMap();
        if (channelMap.get(name) != null) {
            if (HSQLDB.instance.deleteChannel(name)) {
                channelMap.remove(name);
                return "channel " + name + " deleted";
            }
            return "Error deleting channel " + name;
        }
        return "unknown channel " + name;
    }

    public void intrudeChannel(String name, String participant) {

    }

    public void sendMessage(String message, String participant01, String participant02, String algorithm, String keyfile) {

    }
}
