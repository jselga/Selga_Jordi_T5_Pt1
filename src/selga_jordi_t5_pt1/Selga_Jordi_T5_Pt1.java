/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package selga_jordi_t5_pt1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Categoria;
import model.Vaixell;
import utils.BDUtils;
import utils.Utils;

/**
 *
 * @author jordi
 */
public class Selga_Jordi_T5_Pt1 {

    static final String url = "jdbc:mysql://localhost:3306/";
    static final String dbName = "regata";                  //Nom de la nostra BBDD
    static final String driver = "com.mysql.cj.jdbc.Driver";
    static final String userName = "root";
    static final String password = "root";
    static Connection conn = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {

            conn = DriverManager.getConnection(url + dbName, userName, password);

            if (!conn.isClosed()) {

//                BDUtils.createEstructuraMysql(conn);
                BDUtils.netejarTaules(conn);
                inicialitzar();
                llistarVaixells(true);
                llistarVaixellsXCategoria(2);
                llistarVaixellsOrdenats("saenior", true);
                try {
                    inserirVaixell(demanarVaixell());
                } catch (SQLException e) {
                    System.out.println("Error: " + e);
                }

            } else {
                System.out.println("Error");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void inicialitzar() {
        Categoria c1 = new Categoria(1, "Crussier");
        Categoria c2 = new Categoria(2, "Performance");
        Categoria c3 = new Categoria(3, "High Level");
        try {

            inserirCategoria(c1);
            inserirCategoria(c2);
            inserirCategoria(c3);
        } catch (SQLException ex) {
            Logger.getLogger(Selga_Jordi_T5_Pt1.class.getName()).log(Level.SEVERE, null, ex);
        }
        Vaixell v1 = new Vaixell(
                101,
                "Vent del Nord",
                c1,
                0.92,
                "Club Nàutic Barcelona",
                "creuer",
                true,
                85.6
        );

        Vaixell v2 = new Vaixell(
                105,
                "Mar Endins",
                c2,
                0.87,
                "Club Vela Blanes",
                "regata",
                false,
                90.3
        );

        Vaixell v3 = new Vaixell(
                103,
                "Onada Brava",
                c3,
                1.05,
                "Club Nàutic Cambrils",
                "creuer-regata",
                true,
                95.2
        );

        Vaixell v4 = new Vaixell(
                100,
                "Llampec Blau",
                c1,
                0.89,
                "Club Marítim de Tarragona",
                "regata",
                false,
                83.7
        );

        try {
            inserirVaixell(v1);
            inserirVaixell(v2);
            inserirVaixell(v3);
            inserirVaixell(v4);
        } catch (SQLException ex) {
            Logger.getLogger(Selga_Jordi_T5_Pt1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void inserirVaixell(Vaixell vaixell) throws SQLException {
        PreparedStatement stmt;
        String query = "INSERT INTO vaixells values(?,?,?,?,?,?,?,?)";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, vaixell.getCodi());
        stmt.setString(2, vaixell.getNom());
        stmt.setInt(3, vaixell.getCategoria().getId());
        stmt.setDouble(4, vaixell.getRating());
        stmt.setString(5, vaixell.getClub());
        stmt.setString(6, vaixell.getTipus());
        stmt.setBoolean(7, vaixell.isSenior());
        stmt.setDouble(8, vaixell.getTempsReal());
        int count = stmt.executeUpdate();
        stmt.close();
        System.out.println("S'ha insertat " + count + " entrada(es) a la taula vaixells");

    }

    public static void inserirCategoria(Categoria categoria) throws SQLException {
        PreparedStatement stmt;
        String query = "INSERT INTO categories values (?,?)";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, categoria.getId());
        stmt.setString(2, categoria.getNom());
        int count = stmt.executeUpdate();
        stmt.close();
        System.out.println(count + " categoria inserida a la taula categories");
    }

    public static void llistarVaixells(boolean compensat) throws SQLException {
        PreparedStatement stmt;
        String query = "SELECT * FROM vaixells";
        stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        llistarVaixellsRS(rs, compensat);
        stmt.close();

    }

    public static void llistarVaixellsRS(ResultSet rs, boolean compensat) throws SQLException {
        System.out.printf("%-5s %-26s %-12s %-6s %-25s %-15s %-6s %-8s %-6s\n",
                "Codi", "Nom", "Categoria", "Rating", "Club", "Tipus", "Senior", "T.Real", compensat ? "T.Comp" : "");
        while (rs.next()) {
            Categoria categoria = null;
            int codi = rs.getInt("codi");
            String nom = rs.getString("nom");
            int idCategoria = rs.getInt("id_categoria");
            double rating = rs.getDouble("rating");
            String club = rs.getString("club");
            String tipus = rs.getString("tipus_vaixell");
            boolean senior = rs.getBoolean("senior");
            double tReal = rs.getDouble("temps_real");
            double tComp = tReal * rating;
            categoria = obtenirCategoria(idCategoria);
            System.out.printf("%-5d %-26s %-12s %-6.2f %-25s %-15s %-6s %-8.2f %-6s\n",
                    codi, nom, categoria.getNom(), rating, club, tipus, senior ? "Sí" : "No", tReal, compensat ? String.format("%.2f", tComp) : "");

        }
    }

    public static Categoria obtenirCategoria(int idCategoria) throws SQLException {
        PreparedStatement stmt;
        Categoria categoria = new Categoria();
        String query = "SELECT * FROM categories WHERE id=? ";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, idCategoria);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {

            categoria.setNom(rs.getString("nom"));
            categoria.setId(idCategoria);
        }
        stmt.close();
        return categoria;
    }

    public static ArrayList<Categoria> obtenirCategories() throws SQLException {
        PreparedStatement stmt;
        ArrayList<Categoria> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("id"));
            categoria.setNom(rs.getString("nom"));
            categories.add(categoria);
        }
        stmt.close();
        return categories;
    }

    public static void llistarCategories() {
        ArrayList<Categoria> categories = null;
        try {
            categories = obtenirCategories();
        } catch (Exception e) {
            System.out.println("Error al obtenir les categories + e.getMessage()");
        }
        if (categories == null || categories.isEmpty()) {
            System.out.println("No hi ha categories disponibles.");
            return;
        }
        System.out.printf("%-5s %-12s\n", "ID", "Nom");
        for (Categoria categoria : categories) {
            System.out.printf("%-5d %-12s\n", categoria.getId(), categoria.getNom());
        }
    }

    public static void llistarVaixellsXCategoria(int idcategoria) throws SQLException {
        // En aquest mètode no es reutilitza codi perque el llistat és diferent
        PreparedStatement stmt;
        Categoria categoria = obtenirCategoria(idcategoria);
        System.out.println("\nCategoria: " + categoria.getNom());

        String query = "SELECT * FROM vaixells WHERE id_categoria=?";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, idcategoria);
        ResultSet rs = stmt.executeQuery();
        System.out.printf("%-5s %-26s %-6s %-25s %-15s %-6s %-8s %-6s\n",
                "Codi", "Nom", "Rating", "Club", "Tipus", "Senior", "T.Real", "T.Comp");
        while (rs.next()) {

            int codi = rs.getInt("codi");
            String nom = rs.getString("nom");

            double rating = rs.getDouble("rating");
            String club = rs.getString("club");
            String tipus = rs.getString("tipus_vaixell");
            boolean senior = rs.getBoolean("senior");
            double tReal = rs.getDouble("temps_real");
            double tComp = tReal * rating;

            System.out.printf("%-5d %-26s %-6.2f %-25s %-15s %-6s %-8.2f %-6s\n",
                    codi, nom, rating, club, tipus, senior ? "Sí" : "No", tReal, String.format("%.2f", tComp));

        }

    }

    public static void llistarVaixellsOrdenats(String ordre, boolean compensat) throws SQLException {
        PreparedStatement stmt;
        String query = "SELECT * FROM vaixells ORDER BY ";
        if (ordre.equalsIgnoreCase("codi")) {
            query += "codi";
        } else if (ordre.equalsIgnoreCase("tempsc")) {
            query += "temps_real * rating";
        } else if (ordre.equalsIgnoreCase("nom")) {
            query += "nom";
        } else if (ordre.equalsIgnoreCase("rating")) {
            query += "rating";
        } else if (ordre.equalsIgnoreCase("club")) {
            query += "club";
        } else if (ordre.equalsIgnoreCase("tipus")) {
            query += "tipus_vaixell";
        } else if (ordre.equalsIgnoreCase("senior")) {
            query += "senior";
        } else if (ordre.equalsIgnoreCase("tempsr")) {
            query += "temps_real";

        } else {
            System.out.println("Ordre no vàlid. Opcions: codi, nom,tempsc"
                    + ", rating, club, tipus, senior, tempsr");
            return;
        }
        stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        llistarVaixellsRS(rs, compensat);
        stmt.close();

    }

    public static Vaixell demanarVaixell() throws SQLException {
        // Demanem les dades del vaixell
        // i les guardem en un objecte Vaixell
        int codi = Utils.validaInt("Codi: ", "Codi incorrecte");
        String nom = Utils.validaString("Nom: ", "Nom incorrecte");
        llistarCategories();
        int idCategoria = Utils.validaInt("ID Categoria: ", "ID Categoria incorrecte");
        Categoria categoria = obtenirCategoria(idCategoria);
        double rating = Utils.validaDouble("Rating: ", "Rating incorrecte");
        String club = Utils.validaString("Club: ", "Club incorrecte");
        String tipus = Utils.validaString("Tipus: ", "Tipus incorrecte");
        boolean senior = Utils.validaSN("Senior (s/n): ", "Valor incorrecte");
        double tempsReal = Utils.validaDouble("Temps real: ", "Temps real incorrecte");
        // Creem l'objecte Vaixell

        Vaixell vaixell = new Vaixell(codi, nom, categoria, rating, club, tipus, senior, tempsReal);

        return vaixell;
    }

}
