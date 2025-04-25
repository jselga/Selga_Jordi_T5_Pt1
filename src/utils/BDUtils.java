/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 *
 * @author jordi
 */
public class BDUtils {

    public static void createEstructuraMysql(Connection conn) throws SQLException {

//        Taula categories
        PreparedStatement stmt;
        String query = "CREATE TABLE IF NOT EXISTS categories (\n"
                + "id INTEGER PRIMARY KEY,\n"
                + "nom TEXT NOT NULL\n"
                + ")";
        stmt = conn.prepareStatement(query);

        stmt.executeUpdate();
        stmt.close();

//        Taula vaixells
        query = " CREATE TABLE IF NOT EXISTS vaixells (\n"
                + "                        codi INTEGER PRIMARY KEY,\n"
                + "                        nom TEXT NOT NULL,\n"
                + "                        id_categoria INTEGER NOT NULL,\n"
                + "                        rating REAL NOT NULL,\n"
                + "                        club TEXT NOT NULL,\n"
                + "                        tipus_vaixell TEXT NOT NULL,\n"
                + "                        senior BOOLEAN NOT NULL,\n"
                + "                        temps_real REAL NOT NULL,\n"
                + "                        FOREIGN KEY (id_categoria) REFERENCES categories(id)\n"
                + "                    )";
        stmt = conn.prepareStatement(query);

        stmt.executeUpdate();
        stmt.close();

    }

    public static void netejarTaules(Connection conn) {
        PreparedStatement stmt;
        try {
            String query = "delete from vaixells";
            stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
            stmt = conn.prepareStatement("delete from categories");
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
