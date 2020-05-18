package persistence;

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
        int id = DB.instance.getNextID("postbox_" + name);
        long timestamp = Instant.now().getEpochSecond();
        String sql = "INSERT INTO postbox_" + name + " (id, participant_from_id, message, timestamp) VALUES (" +
                id + ", " + participantFromID + ", '" + message + "', " + timestamp + ")";
        DB.instance.update(sql);
    }
}
