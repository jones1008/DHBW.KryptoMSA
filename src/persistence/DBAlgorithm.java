package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

public enum DBAlgorithm {
    instance;
    public void createTableAlgorithms() {
        System.out.println("--- createTableAlgorithms");
        DB.instance.update("CREATE TABLE algorithms (id TINYINT NOT NULL, name VARCHAR(10) NOT NULL, PRIMARY KEY (id))");
        DB.instance.update("CREATE UNIQUE INDEX idxAlgorithms ON algorithms (name)");
    }
    public void insertDataTableAlgorithms(String name) {
        if (getAlgorithmID(name) != 0) {
            System.out.println("Algorithm with name " + name + " already exists");
            return;
        }
        int nextID = DB.instance.getNextID("algorithms");
        DB.instance.update("INSERT INTO algorithms (id, name) VALUES (" + nextID + ", '" + name + "')");
    }
    public int getAlgorithmID(String algorithm) {
        int id = 0;

        try
        {
            String sql = "SELECT id FROM algorithms WHERE name='" + algorithm + "'";
            ResultSet rs = DB.instance.executeQuery(sql);
            while (rs != null && rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return id;
    }
}
