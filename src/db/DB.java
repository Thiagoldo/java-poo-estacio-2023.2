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

import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;

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

    public static Integer homeQueryBensAtivos() {
        String sqlBensAtivos = "SELECT * FROM ViewBensAtivos";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sqlBensAtivos);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int bensAtivos = rs.getInt(1);
                return bensAtivos;
            }
        } catch (SQLException e) {
            Alerts.showAlert("Error", "SQL Error", e.getMessage(), AlertType.ERROR);
        }
        finally {
            DB.closeResultset(rs);
            DB.closeStatement(stmt);
        }

        return null;
    }

    public static String homeQueryUltimoBemCadastrado() {
        String sqlUltimoBemCadastrado = "SELECT * FROM ViewUltimoBemCadastrado";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sqlUltimoBemCadastrado);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String ultimoBemCadastrado = rs.getString(1);
                return ultimoBemCadastrado;
            }
        } catch (SQLException e) {
            Alerts.showAlert("Error", "SQL Error", e.getMessage(), AlertType.ERROR);
        }
        finally {
            DB.closeResultset(rs);
            DB.closeStatement(stmt);
        }

        return null;
    }

    public static Double homeQueryValorTotalBensAtivos() {
        String sqlValorTotalBensAtivos = "SELECT * FROM ViewValorTotalBensAtivos";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sqlValorTotalBensAtivos);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Double valorTotalBensAtivos = rs.getDouble(1);
                return valorTotalBensAtivos;
            }
        } catch (SQLException e) {
            Alerts.showAlert("Error", "SQL Error", e.getMessage(), AlertType.ERROR);
        }
        finally {
            DB.closeResultset(rs);
            DB.closeStatement(stmt);
        }

        return null;
    }
}
