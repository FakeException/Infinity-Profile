package me.darkboy.profile.infinityprofile.mysql;

import org.bukkit.entity.Player;

import java.sql.*;

public class MySQLDatabase {
// in pratica nell'evento dell'checkuser vedi che c'è la variabile log? praticamente nel join lo devi mettere a zero in caso il player non esista... ecco mi serve metterlo a 0
    public static void checkUser(Player p){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://82.165.125.77:3306/PlayerProfile", "root", "123567wersdF$");
            Statement stmt = con.createStatement();
            String query= "SELECT * FROM playertime WHERE username='" + p.getName() + "'";
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()){
                String query1 = "INSERT INTO playertime (username, time) VALUES (?, ?)";
                PreparedStatement pstmt = con.prepareStatement(query1);
                pstmt.setString(1, p.getName());
                pstmt.setString(2, ""); //prova a compilarlo così
                pstmt.execute();
                con.close();
                System.out.println("Utente creato correttamente"); //builda ora e vediamo che errore da or
            }
        }catch(Exception e){
            p.kickPlayer("Cannot create you user, please retry later");
        }
    }

//proviamo a creare manualmente l'utente...
    public static void Update(Player p, String time) { // e te pareva... ma n on si può settare l'array in sql?
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://82.165.125.77:3306/PlayerProfile", "root", "123567wersdF$");
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM playertime WHERE username='" + p.getName() + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {// devi mettere try catch si lo so...
                String updateUser = "UPDATE playertime SET time=? WHERE username='" + p.getName() + "'";
                PreparedStatement pst = con.prepareStatement(updateUser);
                pst.setString(1, time); // allora facciamo in un'altro modo... dici che il long si converte in stringa? si puo
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
