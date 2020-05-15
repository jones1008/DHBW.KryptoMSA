package persistence;

import configuration.Configuration;
import companyNetwork.Subscriber;

import java.sql.*;

public enum HSQLDB {
    instance;

    private Connection connection;

    // general sql functions
    public void setupConnection() {
        System.out.println("--- setupConnection");

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = Configuration.instance.driverName + Configuration.instance.databaseFile;
            connection = DriverManager.getConnection(databaseURL, Configuration.instance.username, Configuration.instance.password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private synchronized void update(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);

            if (result == -1) {
                System.out.println("error executing " + sqlStatement);
            }

            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }
    private int getNextID(String table) {
        int nextID = 0;

        try {
            String sqlStatement = "SELECT max(id) FROM " + table;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);

            while (resultSet.next()) {
                nextID = resultSet.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return nextID;
    }
    private boolean hasResult(String sql) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return false;
    }
    public void shutdown() {
        System.out.println("--- shutdown");

        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    // types
    public void dropTableTypes() {
        System.out.println("--- dropTableTypes");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE types");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }
    public void createTableTypes() {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE types ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxTypes ON types (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }
    public void insertDataTableTypes(String name) {
        if (getTypeID(name) != 0) {
            System.out.println("Type with name " + name + " already exists");
            return;
        }
        int nextID = getNextID("types") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO types (").append("id").append(",").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append("'").append(name).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }
    public int getTypeID(String type) {
        int id = 0;

        try
        {
            String sqlStatement = "SELECT id FROM types WHERE name='" + type + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);

            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return id;
    }

    // participants
    public void dropTableParticipants() {
        System.out.println("--- dropTableParticipants");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE participants");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }
    public void createTableParticipants() {
        System.out.println("--- createTableParticipants");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE participants ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("type_id TINYINT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxParticipants ON types (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE participants ADD CONSTRAINT fkParticipants01 ");
        sqlStringBuilder03.append("FOREIGN KEY (type_id) ");
        sqlStringBuilder03.append("REFERENCES types (id) "); // jones1008: references-Tabelle war falsch
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());

        update(sqlStringBuilder03.toString());
    }
    public int insertDataTableParticipants(String name, int typeID) {
        int nextID = getNextID("participants") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO participants (").append("id").append(",").append("name").append(",").append("type_id").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append("'").append(name).append("'").append(",").append(typeID);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
        return nextID;
    }
    public boolean participantExists(String name) {
        String sql = "SELECT * FROM participants WHERE name='" + name + "'";
        return hasResult(sql);
    }
    public int getParticipantId(String name) {
        int id = 0;

        try
        {
            String sqlStatement = "SELECT id FROM participants WHERE name='" + name + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);

            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return id;
    }

    // algorithm
    public void createTableAlgorithms() {
        System.out.println("--- createTableAlgorithms");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE algorithms ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxAlgorithms ON algorithms (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }
    public void insertDataTableAlgorithms(String name) {
        if (getAlgorithmID(name) != 0) {
            System.out.println("Algorithm with name " + name + " already exists");
            return;
        }
        int nextID = getNextID("algorithms") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO algorithms (").append("id").append(",").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append("'").append(name).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }
    public int getAlgorithmID(String algorithm) {
        int id = 0;

        try
        {
            String sqlStatement = "SELECT id FROM algorithms WHERE name='" + algorithm + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);

            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return id;
    }

    // channel
    public void createTableChannel() {
        System.out.println("--- createTableChannel");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE channel ( ");
        sqlStringBuilder01.append("name VARCHAR(25) NOT NULL").append(",");
        sqlStringBuilder01.append("participant_01 TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_02 TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (name)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE channel ADD CONSTRAINT fkChannel01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_01) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE channel ADD CONSTRAINT fkChannel02 ");
        sqlStringBuilder03.append("FOREIGN KEY (participant_02) ");
        sqlStringBuilder03.append("REFERENCES participants (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());
        update(sqlStringBuilder03.toString());
    }
    public void insertDataTableChannel(String name, Subscriber participant01, Subscriber participant02) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO channel (").append("name").append(",").append("participant_01").append(",").append("participant_02").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("('").append(name).append("',").append("'").append(participant01.getId()).append("'").append(",").append("'").append(participant02.getId()).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }
    public void deleteChannel(String name) {
        String sql = "DELETE FROM channel WHERE name='" + name + "'";
        update(sql);
    }
    public boolean channelExists(String name) {
        String sql = "SELECT * FROM channel WHERE name='"+name+"'";
        return hasResult(sql);
    }
    public boolean channelExists(Subscriber participant01, Subscriber participant02) {
        String sql = "SELECT * FROM channel WHERE participant_01 IN ("+participant01.getId()+", "+participant02.getId()+")" +
                     "AND participant_02 IN ("+participant01.getId()+", "+participant02.getId()+")";
        return hasResult(sql);
    }

    // messages
    public void createTableMessages() {
        System.out.println("--- createTableMessages");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE messages ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_from_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_to_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("plain_message VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("algorithm_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("encrypted_message VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("keyfile VARCHAR(20) NOT NULL").append(",");
        sqlStringBuilder01.append("timestamp INTEGER").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE messages ADD CONSTRAINT fkMessages01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_from_id) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE messages ADD CONSTRAINT fkMessages02 ");
        sqlStringBuilder03.append("FOREIGN KEY (participant_to_id) ");
        sqlStringBuilder03.append("REFERENCES participants (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());
        update(sqlStringBuilder03.toString());

        StringBuilder sqlStringBuilder04 = new StringBuilder();
        sqlStringBuilder04.append("ALTER TABLE messages ADD CONSTRAINT fkMessages03 ");
        sqlStringBuilder04.append("FOREIGN KEY (algorithm_id) ");
        sqlStringBuilder04.append("REFERENCES algorithms (id) ");
        sqlStringBuilder04.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder04.toString());
        update(sqlStringBuilder04.toString());
    }

    // postbox
    public void createTablePostbox(String participantName) {
        System.out.println("--- createTablePostbox_"+participantName);
        String tableName = "postbox_" + participantName;

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE ").append(tableName).append(" (");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_from_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("message VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("timestamp INTEGER").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE ").append(tableName);
        sqlStringBuilder02.append(" ADD CONSTRAINT fk").append(tableName).append("01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_from_id) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }
}