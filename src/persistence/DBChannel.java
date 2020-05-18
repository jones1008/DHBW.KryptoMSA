package persistence;

import companyNetwork.Channel;
import companyNetwork.CompanyNetwork;
import companyNetwork.IChannel;
import companyNetwork.Participant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public enum DBChannel {
    instance;
    public void createTableChannel() {
        System.out.println("--- createTableChannel");

        String sql = "CREATE TABLE channel (" +
                "name VARCHAR(25) NOT NULL, " +
                "participant_01 TINYINT NOT NULL, " +
                "participant_02 TINYINT NOT NULL, " +
                "PRIMARY KEY (name))";
        DB.instance.update(sql);

        sql = "ALTER TABLE channel ADD CONSTRAINT fkChannel01 FOREIGN KEY (participant_01) " +
                "REFERENCES participants (id) ON DELETE CASCADE";
        DB.instance.update(sql);

        sql = "ALTER TABLE channel ADD CONSTRAINT fkChannel02 FOREIGN KEY (participant_02) " +
                "REFERENCES participants (id) ON DELETE CASCADE";
        DB.instance.update(sql);
    }
    public boolean insertDataTableChannel(IChannel channel) {
        String sql = "INSERT INTO channel (name, participant_01, participant_02) " +
                "VALUES ('" + channel.getName() + "', " + channel.getParticipant01().getId() + ", " + channel.getParticipant02().getId() + ")";
        return DB.instance.update(sql);
    }
    public boolean deleteChannel(String name) {
        String sql = "DELETE FROM channel WHERE name='" + name + "'";
        return DB.instance.update(sql);
    }
    public List<Channel> getAllChannels() {
        List<Channel> channels = new ArrayList<>();
        try {
            String sql = "SELECT * FROM channel";
            ResultSet rs = DB.instance.executeQuery(sql);
            while (rs != null && rs.next()) {
                // get participants from Map if they exist
                Participant participant01 = CompanyNetwork.instance.getParticipantFromMapById(rs.getInt("participant_01"));
                if(participant01 == null)  participant01 = DBParticipant.instance.getParticipantById(rs.getInt("participant_01"));
                Participant participant02 = CompanyNetwork.instance.getParticipantFromMapById(rs.getInt("participant_02"));
                if(participant02 == null)  participant02 = DBParticipant.instance.getParticipantById(rs.getInt("participant_02"));
                Channel channel = new Channel(rs.getString("name"), participant01, participant02);
                channels.add(channel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channels;
    }
}
