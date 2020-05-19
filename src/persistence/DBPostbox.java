package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public enum DBPostbox {
    instance;

    public void createTablePostbox(String participantName) {
        System.out.println("--- createTablePostbox_"+participantName);
        String tableName = "postbox_" + participantName;

        String sql = "CREATE TABLE " + tableName + " (" +
                "id TINYINT NOT NULL, " +
                "participant_from_id TINYINT NOT NULL, " +
                "message VARCHAR(50) NOT NULL, " +
                "timestamp INTEGER, " +
                "PRIMARY KEY (id))";
        DB.instance.update(sql);

        sql = "ALTER TABLE " + tableName + " ADD CONSTRAINT fk" + tableName + "01 FOREIGN KEY (participant_from_id) " +
                "REFERENCES participants (id) ON DELETE CASCADE";
        DB.instance.update(sql);
    }

    public void insertDataTablePostbox(String name, int participantFromID, String message) {
        // TODO: vielleicht: check if Postbox already exists?!
        int id = DB.instance.getNextID("postbox_" + name);
        long timestamp = Instant.now().getEpochSecond();
        String sql = "INSERT INTO postbox_" + name + " (id, participant_from_id, message, timestamp) VALUES (" +
                id + ", " + participantFromID + ", '" + message + "', " + timestamp + ")";
        DB.instance.update(sql);
    }

    public void updateMessageTablePostbox(String name, String message) {
        int postboxID = getPostboxId(name);
        if (postboxID != -1) {
            message = message.substring(0, 50);
            String sql = "UPDATE postbox_" + name + " SET message='" + message + "' WHERE id=" + postboxID;
            if (DB.instance.update(sql)) {
                System.out.println("Successfully updated message in table postbox_" + name);
                return;
            }
        }
        System.out.println("Error updating message in table postbox_" + name);
    }

    public String showPostbox(String name) {
        String sql = "SELECT * FROM postbox_" + name;
        ResultSet set = DB.instance.executeQuery(sql);
        String returnString = "";
        try {
            while (set.next()) {
                returnString += set.getInt("id") + " | " +
                        set.getInt("participant_from_id") + " | " +
                        set.getString("message") + " | " +
                        set.getInt("timestamp") + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }

    private int getPostboxId(String name) {
        try {
            ResultSet rs = DB.instance.executeQuery("SELECT MAX(id) FROM postbox_" + name);
            while (rs != null && rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return -1;
    }
}
