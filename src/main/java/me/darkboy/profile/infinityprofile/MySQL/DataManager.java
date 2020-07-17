package me.darkboy.profile.infinityprofile.MySQL;

import org.bukkit.entity.Player;

import java.sql.*;

public class DataManager {

    private static String Username = "";
    private static String Password = "";
    private static String link = "";

    public static String getRank(Player p) {
        String rank = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(link, Username, Password);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM groups WHERE username='" + p.getName() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                rank = rs.getString(3);
                con.close();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return rank;
    }
}
