package persistence;

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

        sql = "ALTER TABLE ADD CONSTRAINT fk" + tableName + "01 FOREIGN KEY (participant_from_id) " +
                "REFERENCES participants (id) ON DELETE CASCADE";
        DB.instance.update(sql);
    }
}
