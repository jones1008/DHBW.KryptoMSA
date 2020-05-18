package persistence;

import companyNetwork.Participant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public enum DBParticipant {
    instance;
    public void dropTableParticipants() {
        System.out.println("--- dropTableParticipants");
        DB.instance.update("DROP TABLE participants");
    }
    public void createTableParticipants() {
        System.out.println("--- createTableParticipants");

        String sql = "CREATE TABLE participants (" +
                "id TINYINT NOT NULL, " +
                "name VARCHAR(50) NOT NULL, " +
                "type_id TINYINT NULL, " +
                "PRIMARY KEY (id))";
        DB.instance.update(sql);
        DB.instance.update("CREATE UNIQUE INDEX idxParticipants ON types (name)");
        DB.instance.update("ALTER TABLE participants ADD CONSTRAINT fkParticipants01 FOREIGN KEY (type_id) REFERENCES types (id) ON DELETE CASCADE");
    }
    public int insertDataTableParticipants(String name, int typeId) {
        // TODO: nextID mit ID von participant ersetzen?
        int nextID = DB.instance.getNextID("participants");
        DB.instance.update("INSERT INTO participants (id, name, type_id) VALUES (" + nextID + ", '" + name + "', " + typeId + ")");
        return nextID;
    }
    public Participant getParticipantById(int id) {
        String sql = "SELECT * FROM participants WHERE id=" + id;
        try {
            ResultSet rs = DB.instance.executeQuery(sql);
            while (rs != null && rs.next()) {
                return getParticipantFromResultSet(rs);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }
    public List<Participant> getAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        String sql = "SELECT * FROM participants";
        try {
            ResultSet rs = DB.instance.executeQuery(sql);
            while (rs != null && rs.next()) {
                participants.add(getParticipantFromResultSet(rs));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return participants;
    }
    public Participant getParticipantFromResultSet(ResultSet rs) {
        try {
            return new Participant(
                    rs.getInt("id"),
                    rs.getString("name"),
                    DBType.instance.getTypeById(rs.getInt("type_id"))
            );
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }
}
