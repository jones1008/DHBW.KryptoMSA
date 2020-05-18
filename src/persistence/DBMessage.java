package persistence;

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
}
