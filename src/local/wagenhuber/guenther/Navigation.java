package local.wagenhuber.guenther;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class Navigation {

    public static void main(String[] args) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            /**Datei dbconnect.properties für Verbindung zur Datenbank::
             * #MYSQL
             * driver=com.mysql.jdbc.Driver
             * url=jdbc:mysql://<hostname>/<dbname>
             * user=<username>
             * password=<password>
             */
            //DB-Parameter einlesen
            FileInputStream in = new FileInputStream("dbconnect.properties");
            //The Properties class represents a persistent set of properties. The Properties can be saved to a stream or loaded from a stream. Each key and its corresponding value in the property list is a string.
            Properties prop = new Properties();
            prop.load(in);
            in.close();

            String url = prop.getProperty("url");
            String user = prop.getProperty("user", "");
            String password = prop.getProperty("password", "");

            //Verbindung zur DB herstellen:
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("select buecher_id, titel from buecher order by buecher_id");

            if (rs.last()) {
                System.out.println("Anzahl Zeilen: " + rs.getRow());
            } else {
                System.out.println("Ergebnismenge leer");
                return;
            }
            rs.beforeFirst();

            System.out.println(" IHre Eingabe bitte: " + "next, previous, first, last oder quit");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String line;

            while ((line = br.readLine()) != null) {
                if (line.equals("next")) {
                    if (rs.next())
                        System.out.println(rs.getRow() + " " + rs.getString(1) + " " + rs.getString(2));
                } else if (line.equals("previous")) {
                    if (rs.previous())
                        System.out.println(rs.getRow() + " " + rs.getString(1) + " " + rs.getString(2));
                } else if (line.equals("first")) {
                    if (rs.first())
                        System.out.println(rs.getRow() + " " + rs.getString(1) + " " + rs.getString(2));
                } else if (line.equals("last")) {
                    if (rs.last())
                        System.out.println(rs.getRow() + " " + rs.getString(1) + " " + rs.getString(2));
                } else if (line.equals("quit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

    }
}


