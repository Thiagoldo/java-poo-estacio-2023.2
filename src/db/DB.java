package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;

public class DB {
    private static Connection connection = null;

    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = loadProperties();
                String dburl = props.getProperty("dburl");
                connection = DriverManager.getConnection(dburl, props);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void closeStatement(PreparedStatement st) {
        try {
            st.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void closeResultset(ResultSet rs) {
        try {
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createDatabBase() {
        if (!isDataBaseCreated()){
            getConnection();
            try (Reader reader = new BufferedReader(new FileReader("src\\db\\begin.sql"))) {
                ScriptRunner sr = new ScriptRunner(connection);
                sr.runScript(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isDataBaseCreated(){
        File file = new File("db\\AssetsControler.db");
        return file.exists();
    }
}
