package persistence;

import companyNetwork.ParticipantType;

import java.sql.ResultSet;
import java.sql.SQLException;

public enum DBType {
    instance;
    public void dropTableTypes() {
        System.out.println("--- dropTableTypes");
        DB.instance.update("DROP TABLE types");
    }
    public void createTableTypes() {
        System.out.println("--- createTableTypes");
        DB.instance.update("CREATE TABLE types (id TINYINT NOT NULL, name VARCHAR(10) NOT NULL, PRIMARY KEY (id))");
        DB.instance.update("CREATE UNIQUE INDEX idxTypes ON types (name)");
    }
    public void insertDataTableTypes(String name) {
        if (getTypeID(name) != 0) {
            System.out.println("Type with name " + name + " already exists");
            return;
        }
        int nextID = DB.instance.getNextID("types");
        DB.instance.update("INSERT INTO types (id, name) VALUES (" + nextID + ", '" + name + "')");
    }
    public int getTypeID(String type) {
        int id = 0;

        try
        {
            String sql = "SELECT id FROM types WHERE name='" + type.toLowerCase() + "'";
            ResultSet rs = DB.instance.executeQuery(sql);
            while (rs != null && rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return id;
    }
    public ParticipantType getTypeById(int id) {
        try {
            String sql = "SELECT name FROM types WHERE id=" + id;
            ResultSet rs = DB.instance.executeQuery(sql);
            while (rs != null && rs.next()) {
                return ParticipantType.valueOf(rs.getString("name").toUpperCase());
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }
}
