package companyNetwork;

public interface IChannelManager {
    String create(String name, String participant01, String participant02);
    String showAllChannels();
    String intrudeChannel(String name, String participant);
    String sendMessage(String message, String participant01, String participant02, String algorithm, String keyfile);
}
