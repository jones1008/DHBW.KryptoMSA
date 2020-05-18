package persistence;

import java.time.Instant;

public enum DBMessage {
    instance;
    public void createTableMessages() {
        System.out.println("--- createTableMessages");

        String sql = "CREATE TABLE messages (" +
                "id TINYINT NOT NULL, " +
                "participant_from_id TINYINT NOT NULL, " +
                "participant_to_id TINYINT NOT NULL, " +
                "plain_message VARCHAR(50) NOT NULL, " +
                "algorithm_id TINYINT NOT NULL, " +
                "encrypted_message VARCHAR(50) NOT NULL, " +
                "keyfile VARCHAR(20) NOT NULL, timestamp INTEGER, " +
                "PRIMARY KEY (id))";
        DB.instance.update(sql);

        sql = "ALTER TABLE messages ADD CONSTRAINT fkMessages01 FOREIGN KEY (participant_from_id) " +
                "REFERENCES participants (id) ON DELETE CASCADE";
        DB.instance.update(sql);

        sql = "ALTER TABLE messages ADD CONSTRAINT fkMessages02 FOREIGN KEY (participant_to_id) " +
                "REFERENCES participants (id) ON DELETE CASCADE";
        DB.instance.update(sql);

        sql = "ALTER TABLE messages ADD CONSTRAINT fkMessages03 FOREIGN KEY (algorithm_id) " +
                "REFERENCES algorithms (id) ON DELETE CASCADE";
        DB.instance.update(sql);
    }

    public void insertDataTableMessages(int participantFromID, int participantToID, String plainMessage, int algorithmID, String encryptedMessage, String keyfile) {
        int id = DB.instance.getNextID("messages");
        long timestamp = Instant.now().getEpochSecond();
        String sql = "INSERT INTO messages (id, participant_from_id, participant_to_id, plain_message, algorithm_id, encrypted_message, keyfile, timestamp) VALUES " +
                "(" + id + ", " + participantFromID + ", " + participantToID + ", '" + plainMessage + "', " + algorithmID + ", '" + encryptedMessage + "', '" + keyfile + "', " + timestamp + ")";
        DB.instance.update(sql);
    }
}
