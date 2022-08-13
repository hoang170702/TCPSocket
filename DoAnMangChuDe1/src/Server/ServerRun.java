/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author huyho
 */
public class ServerRun {

    public static void main(String[] args) {
        try {
            // server thuc hien mo cong ket noi
            ServerSocket svSocket = new ServerSocket(1234);
            System.out.println("Server is open!!!");
            //server chap nhan ket noi
            Socket client = null;
            int i = 0;
            while ((client = svSocket.accept()) != null) {
                System.out.println("Da Chap nhan ket noi lan : "+ (++i));
                new ServerThread(client);          
            }
            svSocket.close();
        } catch (IOException ex) {
            Scanner sc = new Scanner(System.in);
            sc.nextLine();
        }

    }

}
