package app.services;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author waeberla
 * @version 1.0
 * @created 18-nov.-2024 16:36:20
 */
public class Client extends Thread {

    private volatile BufferedReader in;
    private volatile BufferedWriter out;
    private volatile boolean running;
    private volatile Socket socket;
    private volatile WrkServer wrkServer;
    private volatile boolean isConnected;

    /**
     * @param name
     * @param socket
     * @param wrkServer
     */
    public Client(String name, Socket socket, WrkServer wrkServer) {
        super(name);
        this.socket = socket;
        this.wrkServer = wrkServer;
    }

    /**
     * @param ms
     */
    public void attendre(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param msg
     */
    public void ecrire(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        running = true;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (running) {
                System.out.println("while");
                String msg = in.readLine();

                if (msg != null) {
                    System.out.println(msg);
                    wrkServer.recevoirMessage(msg);
                } else {
                    running = false;
                    System.out.println("close");
                }

                attendre(10);
            }
            socket.close();
            wrkServer.removeClient();
        } catch (IOException ex) {

        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getIp() {
        System.out.println(this.socket.getInetAddress().getHostAddress());
        return this.socket.getInetAddress().getHostAddress();
    }
}