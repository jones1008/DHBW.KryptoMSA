package companyNetwork;

import com.google.common.eventbus.Subscribe;
import persistence.HSQLDB;

public class ChannelManager implements IChannelManager {
    public String create(String name, String participant01, String participant02) {
        if (participant01.equals(participant02)) {
            return participant01 + " and " + participant02 + " are identifical - cannot create channel on itself";
        }

        Participant participant1 = (Participant) CompanyNetwork.instance.getSubscriberForName(participant01);
        Participant participant2 = (Participant) CompanyNetwork.instance.getSubscriberForName(participant02);

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

        IChannel channel = new Channel(participant1, participant2, name);
        CompanyNetwork.instance.addChannelToMap(channel);
        HSQLDB.instance.insertDataTableChannel(name, participant1, participant2);

        return "channel " + name + " from " + participant01 + " to " + participant02 + " successfully created";
    }

    public void show() {

    }

    public void dropChannel(String name) {

    }

    public void intrudeChannel(String name, String participant) {

    }

    public void sendMessage(String message, String participant01, String participant02, String algorithm, String keyfile) {

    }

    private void addChannelToMap(Subscriber participant1, Subscriber participant2, String name) {
        if (!CompanyNetwork.instance.isChannelRegistered(name, participant1, participant2))
        {
            IChannel channel = new Channel(participant1, participant2, name);
            CompanyNetwork.instance.addChannelToMap(channel);
        }
    }
}
