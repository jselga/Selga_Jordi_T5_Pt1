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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Categoria;
import model.Vaixell;
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
//                BDUtils.netejarTaules(conn);
//                inicialitzar();
                try {
//                    llistarVaixells(true);
//                    llistarVaixellsXCategoria(2);
//                    llistarVaixellsOrdenats("senior", true);
                    ////                    eliminarVaixell(101);
//                    modificarVaixell(100);
//                    llistarVaixells(true);


//                    inserirVaixell(demanarVaixell());
mostrarMenu();
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

    public static void mostrarMenu() throws SQLException {
        Scanner sc = new Scanner(System.in);
        int opcio;

        do {
            System.out.println("\n===== MENÚ DE GESTIÓ DE VAIXELLS =====");
            System.out.println("1. Llistar vaixells");
            System.out.println("2. Llistar vaixells per categoria");
            System.out.println("3. Llistar vaixells ordenats");
            System.out.println("4. Afegir nou vaixell");
            System.out.println("5. Modificar vaixell");
            System.out.println("6. Eliminar vaixell");
            System.out.println("0. Sortir");
            System.out.print("Escull una opció: ");
            opcio = Integer.parseInt(sc.nextLine());

            switch (opcio) {
                case 1 ->
                    llistarVaixells(true);
                case 2 -> {
                    llistarCategories();
                    int idCat = Utils.validaInt("Introdueix ID de categoria: ", "ID incorrecte");
                    llistarVaixellsXCategoria(idCat);
                }
                case 3 -> {
                    System.out.print("Camp d'ordenació (codi, nom, tempsc, rating, club, tipus, senior, tempsr): ");
                    String ordre = sc.nextLine();
                    llistarVaixellsOrdenats(ordre, true);
                }
                case 4 -> {
                    Vaixell nou = demanarVaixell();
                    inserirVaixell(nou);
                }
                case 5 -> {
                    int codiMod = Utils.validaInt("Codi del vaixell a modificar: ", "Codi incorrecte");
                    modificarVaixell(codiMod);
                }
                case 6 -> {
                    int codiDel = Utils.validaInt("Codi del vaixell a eliminar: ", "Codi incorrecte");
                    eliminarVaixell(codiDel);
                }
                case 0 ->
                    System.out.println("Sortint del programa...");
                default ->
                    System.out.println("Opció no vàlida.");
            }

        } while (opcio != 0);
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

    public static void mostrarVaixell(Vaixell v) {
        System.out.println("===== FITXA VAIXELL =====");
        System.out.println("Codi:        " + v.getCodi());
        System.out.println("Nom:         " + v.getNom());
        System.out.println("Categoria:   " + v.getCategoria().getNom());
        System.out.printf("Rating:      %.2f%n", v.getRating());
        System.out.println("Club:        " + v.getClub());
        System.out.println("Tipus:       " + v.getTipus());
        System.out.println("Senior:      " + (v.isSenior() ? "Sí" : "No"));
        System.out.printf("Temps real:  %.2f%n", v.getTempsReal());
        System.out.println("=========================");
    }

    public static void modificarVaixell(int codi) throws SQLException {
        Scanner sc = new Scanner(System.in);
        Vaixell vaixell = obtenirVaixell(codi);

        if (vaixell == null) {
            System.out.println("No existeix cap vaixell amb aquest codi.");
            return;
        }

        // Mostrar dades actuals
        mostrarVaixell(vaixell);

        System.out.println("\n--- Modificació de dades ---");

        System.out.print("Nom actual (" + vaixell.getNom() + "): ");
        String nom = sc.nextLine();
        if (!nom.isBlank()) {
            vaixell.setNom(nom);
        }

        System.out.print("Rating actual (" + vaixell.getRating() + "): ");
        String ratingStr = sc.nextLine();
        if (!ratingStr.isBlank()) {
            vaixell.setRating(Double.parseDouble(ratingStr));
        }

        System.out.print("Club actual (" + vaixell.getClub() + "): ");
        String club = sc.nextLine();
        if (!club.isBlank()) {
            vaixell.setClub(club);
        }

        System.out.print("Tipus actual (" + vaixell.getTipus() + ") [regata/creuer/creuer-regata]: ");
        String tipus = sc.nextLine();
        if (!tipus.isBlank()) {
            vaixell.setTipus(tipus);
        }

        System.out.print("És sènior? actual (" + (vaixell.isSenior() ? "Sí" : "No") + ") [sí/no]: ");
        String seniorStr = sc.nextLine();
        if (!seniorStr.isBlank()) {
            vaixell.setSenior(seniorStr.equalsIgnoreCase("sí"));
        }

        System.out.print("Temps real actual (" + vaixell.getTempsReal() + "): ");
        String tRealStr = sc.nextLine();
        if (!tRealStr.isBlank()) {
            vaixell.setTempsReal(Double.parseDouble(tRealStr));
        }
        Categoria categoria = obtenirCategoria(vaixell.getCategoria().getId());
        System.out.print("Categoria actual (" + categoria.getId() + "): " + categoria.getNom() + ")");
        System.out.println("\nCategories disponibles:");
        llistarCategories();
        String catIdStr = sc.nextLine();
        if (!catIdStr.isBlank()) {
            int novaCategoriaId = Integer.parseInt(catIdStr);
            Categoria novaCategoria = obtenirCategoria(novaCategoriaId);
            if (novaCategoria != null) {
                vaixell.setCategoria(novaCategoria);
            } else {
                System.out.println("Categoria inexistent, no s'ha modificat.");
            }
        }

        System.out.println("\nDades actualitzades:");
        mostrarVaixell(vaixell);
        if (Utils.validaSN("Actualitzar a la BBDD? (S/N)", "Resposta incorrecte")) {
            actualitzarVaixell(vaixell);
        } else {
            System.out.println("No s'han actualitzat els canvis a la BBDD");
        }

    }

    public static void actualitzarVaixell(Vaixell vaixell) throws SQLException {
        String query = """
        UPDATE vaixells SET
            nom = ?,
            id_categoria = ?,
            rating = ?,
            club = ?,
            tipus_vaixell = ?,
            senior = ?,
            temps_real = ?
        WHERE codi = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, vaixell.getNom());
            stmt.setInt(2, vaixell.getCategoria().getId());
            stmt.setDouble(3, vaixell.getRating());
            stmt.setString(4, vaixell.getClub());
            stmt.setString(5, vaixell.getTipus());
            stmt.setBoolean(6, vaixell.isSenior());
            stmt.setDouble(7, vaixell.getTempsReal());
            stmt.setInt(8, vaixell.getCodi());

            int filesAfectades = stmt.executeUpdate();
            if (filesAfectades > 0) {
                System.out.println("Vaixell actualitzat correctament.");
            } else {
                System.out.println("No s'ha pogut actualitzar el vaixell (potser no existeix).");
            }
        }
    }

    public static void eliminarVaixell(Vaixell vaixell) throws SQLException {
        PreparedStatement stmt;
        String query = "DELETE from vaixells WHERE codi=?";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, vaixell.getCodi());
        int count = stmt.executeUpdate();
        System.out.printf("S'ha eliminat  %d vaixell(s)\n", count);
        stmt.close();
    }

    public static void eliminarVaixell(int codi) throws SQLException {
        Vaixell vaixell = obtenirVaixell(codi);
        if (vaixell == null) {
            System.out.printf("No existeix cap vaixell amb el codi %d\n", codi);
        } else {
            mostrarVaixell(vaixell);
            if (Utils.validaSN("Estàs segur que vols eliminar aquest vaixell?", "Resposta no correcta")) {
                eliminarVaixell(vaixell);
            }
        }
    }

    public static Vaixell obtenirVaixell(int codi) throws SQLException {
        PreparedStatement stmt;
        String query = "SELECT * FROM vaixells WHERE codi=?";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, codi);
        ResultSet rs = stmt.executeQuery();
        Vaixell vaixell = null;
        if (rs.next()) {
            vaixell = new Vaixell();
            vaixell.setCodi(rs.getInt("codi"));
            vaixell.setNom(rs.getString("nom"));
            vaixell.setRating(rs.getDouble("rating"));
            vaixell.setClub(rs.getString("club"));
            vaixell.setTipus(rs.getString("tipus_vaixell"));
            vaixell.setSenior(rs.getBoolean("senior"));
            vaixell.setTempsReal(rs.getDouble("temps_real"));
            vaixell.setCategoria(obtenirCategoria(rs.getInt("id_categoria")));

        }
        return vaixell;
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
