package com.example.galilinetsky.moneywatcher.Clients;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

    private Socket socket;
    public volatile String serverMessage;
    public static final String SERVERIP = "10.0.2.2"; //your computer IP address
    public static final int SERVERPORT = 3333;
    private boolean mRun = true;
    volatile String clientMsg;
    private static TCPClient clientInstance;
    PrintWriter out;
    BufferedReader in;

    public static TCPClient SingeltonClient( ){
        if (clientInstance == null){
            clientInstance = new TCPClient();
        }
        return clientInstance;
    }
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    private TCPClient() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //here you must put your computer's IP address.
                    InetAddress serverAddr = InetAddress.getByName(SERVERIP);

                    Log.e("TCP Client", "C: Connecting...");

                    //create a socket to make the connection with the server
                    socket = new Socket(serverAddr, SERVERPORT);
                }catch (Exception e){
                    Log.e("TCP", "C: Error", e);
                }
            }
        }.start();
    }



    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        clientMsg = message;
        new Thread() {
            @Override
            public void run() {
                try {
                    //send the message to the server
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    if (out != null && !out.checkError()) {
                        out.println(clientMsg);
                        out.flush();
                    }
                } catch (Exception e) {
                    Log.e("TCP", "S: Error", e);
                }
            }
        }.start();
    }


    public void receiveMessage(){
        new Thread() {
            @Override
            public void run() {
                try {
                    //receive the message which the server sends back
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String givenMsg = in.readLine();
                    if (givenMsg != null) {
                        serverMessage = givenMsg;
                    }
                    Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public void stopClient(){
        try {
            mRun = false;

            socket.close();
        }catch (Exception e){

        }
    }

}