package me.darkboy.profile.infinityprofile.MySQL;

import org.bukkit.entity.Player;

import java.sql.*;

public class DataManager {

    private static String Username = "root";
    private static String Password = "123567wersdF$";
    private static String link = "jdbc:mysql://82.165.125.77:3306/ranks?useSSL=false";

    public static String getRank(Player p) {
        Connection con;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(link, Username, Password);
            Statement stmt = con.createStatement();
            String sql = "SELECT rank FROM groups WHERE username='" + p.getName() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                return rs.getString(3);
            }
            con.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "§4Rank Error";
    }
}
