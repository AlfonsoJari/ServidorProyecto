package Clases;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionUnica {

    private ConexionUnica() {
    }

    private static ConexionUnica cu = null;
    private static Connection cn = null;
    private static String url = "jdbc:postgresql://127.0.0.1:5432/javacrud";

    public static ConexionUnica getInstance() {
            if (cu == null) {
                try {
                    cu = new ConexionUnica();
                    cn = DriverManager.getConnection(url, "postgres", "postgres");
                } catch (SQLException ex) {
                    Logger.getLogger(ConexionUnica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        return cu;
    }

//    private void createConnection() {
//        try {
//            cn = DriverManager.getConnection(url, "postgres", "postgres");
//        } catch (SQLException ex) {
//            Logger.getLogger(ConexionUnica.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public Connection getConnection() {
        return cn;
    }

    public ResultSet select(String sql) throws SQLException {
        PreparedStatement st = getConnection().prepareStatement(sql);
        return st.executeQuery();
    }
}
