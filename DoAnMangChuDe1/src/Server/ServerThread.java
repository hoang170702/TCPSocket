/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huyho
 */
public class ServerThread implements Runnable {

    private Socket client;
    DataInputStream din;
    DataOutputStream dout;
    private String key = "DoAnMang";
    private Connection con;
    private Statement stmt;

    public ServerThread(Socket client) throws IOException {
        this.client = client;
        din = new DataInputStream(client.getInputStream());
        dout = new DataOutputStream(client.getOutputStream());
        new Thread(this).start();
    }

    public static String database;
    public static String username;
    public static String password;

    public void ConnectDB(String str) throws IOException {
        try {
            String[] ReadStr = str.split("!");
            database = ReadStr[0];
            username = ReadStr[1];
            password = ReadStr[2];
            System.out.println(database + username + password);
            ConnectionDatabase conn = new ConnectionDatabase();
            con = conn.getConnect();           
            if (con != null) {
                dout.writeUTF("ok");
            } else {
                dout.writeUTF("failed");
            }
        } catch (Exception ex) {
            dout.writeUTF("failed");
        }

    }

    public void SendData(String str) throws IOException {
        try {
            stmt = con.createStatement();
            String[] STR = str.split("!");
            String ID = STR[0];
            String Name = STR[1];
            String DToan = STR[2];
            String DVan = STR[3];
            String DAnh = STR[4];
            System.out.println("insert into SV (ID,Name,DiemToan,DiemVan,DiemAnh) values('" + ID + "','" + Name + "','" + DToan + "','" + DVan + "','" + DAnh + "')");
            String EncryptID = algorithmDES.DesEncrypt(ID, key);
            String EncryptName = algorithmDES.DesEncrypt(Name, key);
            String EncryptDToan = algorithmDES.DesEncrypt(DToan, key);
            String EncryptDVan = algorithmDES.DesEncrypt(DVan, key);
            String EncryptDAnh = algorithmDES.DesEncrypt(DAnh, key);
            String sqlInsert = "insert into SV (ID,Name,DiemToan,DiemVan,DiemAnh) values('" + EncryptID + "','" + EncryptName + "','" + EncryptDToan + "','" + EncryptDVan + "','" + EncryptDAnh + "')";
            try {
                if (stmt.executeUpdate(sqlInsert) > 0) {
                    dout.writeUTF("ok");
                } else {
                    dout.writeUTF("failed");
                }
            } catch (Exception ex) {
                dout.writeUTF("failed");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getData(String sql) {
        StringBuilder str = new StringBuilder();
        try {
            stmt = con.createStatement();
            ResultSet rslS = stmt.executeQuery(sql);
            while (rslS.next()) {
                //lay du lieu tu sql server ve
                String ID = rslS.getString("ID");
                String Name = rslS.getString("Name");
                String DToan = rslS.getString("DiemToan");
                String DVan = rslS.getString("DiemVan");
                String DAnh = rslS.getString("DiemAnh");
                // thuc hien giai des
                String DecryptID = algorithmDES.DesDecrypt(ID, key);
                String DecryptName = algorithmDES.DesDecrypt(Name, key);
                String DecryptDToan = algorithmDES.DesDecrypt(DToan, key);
                String DecryptDvan = algorithmDES.DesDecrypt(DVan, key);
                String DecryptDAnh = algorithmDES.DesDecrypt(DAnh, key);
                //tinh diem trung binh
                float DiemToanReal = Float.valueOf(DecryptDToan);
                float DiemVanReal = Float.valueOf(DecryptDvan);
                float DiemAnhReal = Float.valueOf(DecryptDAnh);
                float DTB = ((DiemToanReal + DiemVanReal + DiemAnhReal) / 3);
                // thuc hien chen chuoi
                String sendStudent = DecryptID + "/" + DecryptName + "/" + DecryptDToan + "/" + DecryptDvan + "/" + DecryptDAnh + "/" + DTB + "!";
                str.append(sendStudent);
            }
        } catch (Exception e) {
        }
        return str.toString();
    }

    public static final int connect = 1;
    public static final int add = 2;
    public static final int show = 3;

    public int flag(String str) {
        if (str.equals("1")) {
            return connect;
        }
        if (str.equals("2")) {
            return add;
        }
        if (str.equals("3")) {
            return show;
        }
        return -1;
    }

    @Override
    public void run() {
        Scanner sc = null;
        try {
            while (true) {
                String readSTR = din.readUTF();
                sc = null;
                String lenh = "";
                String getSTR = "";
                try {
                    sc = new Scanner(readSTR);
                    sc.useDelimiter("@");
                    lenh = sc.next();
                    getSTR = sc.next();
                } catch (Exception e) {
                }

                switch (flag(lenh)) {
                    case connect:
                        System.out.println("lenh :" + lenh);
                        ConnectDB(getSTR);
                        break;
                    case add:
                        System.out.println("lenh :" + lenh);
                        SendData(getSTR);
                        break;
                    case show:
                        String sqlGetData = "select * from SV";
                        String result = getData(sqlGetData);
                        if (result != null) {
                            dout.writeUTF(result);
                        } else {
                            dout.writeUTF("failed");
                        }
                        break;
                }
            }
        } catch (Exception e) {
            
        }
    }
}
