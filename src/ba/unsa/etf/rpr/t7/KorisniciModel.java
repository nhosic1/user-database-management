package ba.unsa.etf.rpr.t7;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class KorisniciModel {
    private ObservableList<Korisnik> korisnici = FXCollections.observableArrayList();
    private SimpleObjectProperty<Korisnik> trenutniKorisnik = new SimpleObjectProperty<>();
    private Connection conn;
    private PreparedStatement dajKorisnike, izmijeniKorisnika, obrisiKorisnikaId, dajNoviId, dodajKorisnika;

    public KorisniciModel() {
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:korisnici.db");
        } catch (SQLException e){
            e.printStackTrace();
        }
        try{
           dajKorisnike = conn.prepareStatement("SELECT * FROM korisnik");
        } catch (SQLException e){
            regenerisiBazu();
            try{
                dajKorisnike = conn.prepareStatement("SELECT * FROM korisnik");
            } catch (SQLException e1){
                e1.printStackTrace();
            }
        }
        try{
            izmijeniKorisnika = conn.prepareStatement("UPDATE korisnik SET ime=?, prezime=?, email=?, username=?, password=? WHERE id=?");
            obrisiKorisnikaId = conn.prepareStatement("DELETE FROM korisnik WHERE id=?");
            dajNoviId = conn.prepareStatement("SELECT MAX(id)+1 FROM korisnik");
            dodajKorisnika = conn.prepareStatement("INSERT INTO korisnik VALUES(?,?,?,?,?,?)");
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
    private void regenerisiBazu() {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("korisnici.db.sql"));
            String sqlUpit = "";
            while (ulaz.hasNext()) {
                sqlUpit += ulaz.nextLine();
                if ( sqlUpit.length() > 1 && sqlUpit.charAt( sqlUpit.length()-1 ) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ne postoji SQL datoteka… nastavljam sa praznom bazom");
        }
    }

    public void napuni() {
        // Ako je lista već bila napunjena, praznimo je
        // Na taj način se metoda napuni() može pozivati više puta u testovima
        /*korisnici.clear();

        korisnici.add(new Korisnik("Vedran", "Ljubović", "vljubovic@etf.unsa.ba", "vedranlj", "test"));
        korisnici.add(new Korisnik("Amra", "Delić", "adelic@etf.unsa.ba", "amrad", "test"));
        korisnici.add(new Korisnik("Tarik", "Sijerčić", "tsijercic1@etf.unsa.ba", "tariks", "test"));
        korisnici.add(new Korisnik("Rijad", "Fejzić", "rfejzic1@etf.unsa.ba", "rijadf", "test"));
        trenutniKorisnik.set(null);*/
        //korisnici.clear();
        try {
            ResultSet rs = dajKorisnike.executeQuery();
            while (rs.next()) {
                Korisnik k = new Korisnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                korisnici.add(k);
                if (trenutniKorisnik == null) trenutniKorisnik = new SimpleObjectProperty<>(k);
            }
        } catch(SQLException e) {
            System.out.println("Neuspješno čitanje iz baze: " + e.getMessage());
        }
        if (trenutniKorisnik == null) trenutniKorisnik = new SimpleObjectProperty<>();
    }

    public void vratiNaDefault(){
        // Dodali smo metodu vratiNaDefault koja trenutno ne radi ništa, a kada prebacite Model na DAO onda
        // implementirajte ovu metodu
        // Razlog za ovo je da polazni testovi ne bi padali nakon što dodate bazu
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM korisnik");
            regenerisiBazu();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void diskonektuj() {
        try {
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(ObservableList<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public Korisnik getTrenutniKorisnik() {
        return trenutniKorisnik.get();
    }

    public SimpleObjectProperty<Korisnik> trenutniKorisnikProperty() {
        return trenutniKorisnik;
    }

    public void setTrenutniKorisnik(Korisnik trenutniKorisnik) {
        if(getTrenutniKorisnik() != null) izmijeniKorisnika(getTrenutniKorisnik());
        this.trenutniKorisnik.set(trenutniKorisnik);
    }

    public void setTrenutniKorisnik(int i) {
        this.trenutniKorisnik.set(korisnici.get(i));
    }

    public void izmijeniKorisnika(Korisnik k){
        try{
            izmijeniKorisnika.setString(1, k.getIme());
            izmijeniKorisnika.setString(2, k.getPrezime());
            izmijeniKorisnika.setString(3, k.getEmail());
            izmijeniKorisnika.setString(4, k.getUsername());
            izmijeniKorisnika.setString(5, k.getPassword());
            izmijeniKorisnika.setInt(6, k.getId());
            izmijeniKorisnika.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void obrisiKorisnika(int id){
        try {
            obrisiKorisnikaId.setInt(1, id);
            obrisiKorisnikaId.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void dodajKorisnika(Korisnik k){
        try{
            dodajKorisnika.setInt(1, dajNoviIdKorisnika());
            dodajKorisnika.setString(2, k.getIme());
            dodajKorisnika.setString(3, k.getPrezime());
            dodajKorisnika.setString(4, k.getEmail());
            dodajKorisnika.setString(5, k.getUsername());
            dodajKorisnika.setString(6, k.getPassword());
            dodajKorisnika.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public int dajNoviIdKorisnika(){
        int id = 1;
        try{
            ResultSet result = dajNoviId.executeQuery();
            if (result.next()) {
                id = result.getInt(1);  //ako tabela nije prazna
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }
    public void zapisiDatoteku(File f){
        PrintWriter izlaz;
        if (f != null) {
            try {
                izlaz = new PrintWriter(new FileWriter(f.getAbsolutePath()));

            } catch (IOException e) {
                System.out.println("Datoteka se ne može otvoriti");
                return;
            }

            try {
                for (Korisnik k : korisnici) {
                    izlaz.println(k.getUsername() + ":" + k.getPassword() + ":" + k.getId() + ":" + k.getId() + ":" + k.getIme() + " " + k.getPrezime() + "::");
                }
            } catch (Exception e) {
                System.out.println("Problem pri pisanju podataka.");
                System.out.println("Greška: " + e);
            } finally {
                izlaz.close();
            }
        }
    }
    public Connection getConn(){
        return conn;
    }
}
