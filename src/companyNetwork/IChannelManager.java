package companyNetwork;

public interface IChannelManager {
    String create(String name, String participant01, String participant02);
    String showAllChannels();
    String deleteChannel(String name);
    void intrudeChannel(String name, String participant);
    void sendMessage(String message, String participant01, String participant02, String algorithm, String keyfile);
}
