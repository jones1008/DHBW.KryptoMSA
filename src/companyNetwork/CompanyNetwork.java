package companyNetwork;

import java.util.Map;

public enum CompanyNetwork {
    instance;
    private Map<Integer, Subscriber> participantMap;
    private Map<String, IChannel> channelMap;
}
