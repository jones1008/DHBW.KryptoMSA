package companyNetwork;

import persistence.HSQLDB;

import java.util.List;

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

        if (HSQLDB.instance.channelExists(name)) {
            addChannelToMap(participant1, participant2, name);
            return "channel " + name + " already exists";
        }

        if (HSQLDB.instance.channelExists(participant1, participant2)) {
            addChannelToMap(participant1, participant2, name);
            return "communication channel between " + participant01 + " and " + participant02 + " already exists";
        }

        IChannel channel = new Channel(name, participant1, participant2);
        CompanyNetwork.instance.addChannelToMap(channel);
        HSQLDB.instance.insertDataTableChannel(channel);

        return "channel " + name + " from " + participant01 + " to " + participant02 + " successfully created";
    }

    public String showAllChannels() {
        // TODO für jones1008: Infos von der Map holen
        List<Channel> channels = HSQLDB.instance.getAllChannels();
        if (channels != null) {
            String ret = "";
            for (Channel channel : channels) {
                ret += channel.getName() + " | branch_" + channel.getParticipant01().getName() + " and branch_" + channel.getParticipant02().getName() + "\n";
            }
            return ret;
        } else {
            return "No channels found.";
        }
    }

    public String dropChannel(String name) {
        if (HSQLDB.instance.channelExists(name)) {
            return HSQLDB.instance.deleteChannel(name);
        }
        return "unknown channel " + name;
    }

    public void intrudeChannel(String name, String participant) {

    }

    public void sendMessage(String message, String participant01, String participant02, String algorithm, String keyfile) {

    }

    private void addChannelToMap(Subscriber participant1, Subscriber participant2, String name) {
        if (!CompanyNetwork.instance.isChannelRegistered(name, participant1, participant2))
        {
            IChannel channel = new Channel(name, participant1, participant2);
            CompanyNetwork.instance.addChannelToMap(channel);
        }
    }
}
