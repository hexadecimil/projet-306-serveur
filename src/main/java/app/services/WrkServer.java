package app.services;

import app.ctrl.IControllerForWrkServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * @author waeberla
 * @version 1.0
 * @created 18-nov.-2024 16:36:24
 */
public class WrkServer extends Thread {

    private volatile Client client;
    private IControllerForWrkServer controller;
    private volatile boolean running;
    private volatile ServerSocket serverSocket;

    public WrkServer(IControllerForWrkServer controller) {
        this.controller = controller;
        this.client = null;
    }

    public void demarrerServeur(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);
            this.start();
            //wrk.recevoirMessageInfo("Serveur démarré");
            controller.serveurDemmarer(true);
            running = true;
        } catch (IOException ex) {

        } catch (Exception exc) {
        }
    }

    public void arreterServeur() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                running = false;
                controller.serveurDemmarer(false);
            }
        } catch (IOException ex) {
            //wrk.recevoirMessageInfo("Erreur dans l'arrêt du serveur.");
        }
    }

    public void attendre(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException ex) {

        }
    }

    public boolean isRunning() {
        return this.running;
    }

    public void recevoirMessage(String msg) {
        String[] args = msg.split(",");
        System.out.println(msg);
        System.out.println(args[0]);
        switch (args[0]) {
            case "move":
                if (client.isConnected()) {
                    double move;
                    double turn;
                    try {
                        move = Double.parseDouble(args[1]);
                        turn = Double.parseDouble(args[2]);
                    } catch (NumberFormatException ex) {
                        move = 0.0;
                        turn = 0.0;
                    }
                    controller.move(move, turn);
                }
                break;
            case "button":
                if (client.isConnected()) {

                }
                break;
            case "connect":
                System.out.println(args[1]);
                String tag = args[1];
                if (tag != null && !tag.isEmpty()) {
                    if (controller.connexionClient(tag)) {
                        client.setConnected(true);
                    }
                }
                break;
            default:
                System.out.println("default");
                break;
        }
    }

    public void removeClient() {
        this.client = null;
        controller.deconnexionClient();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                synchronized (serverSocket) {
                    if (client == null) {
                        Socket socketClient = serverSocket.accept();
                        Client client1 = new Client(socketClient.getInetAddress().getHostAddress(), socketClient, this);
                        this.client = client1;
                        client.start();
                        System.out.println("Client connecté (" + socketClient.getInetAddress().getHostAddress() + ")");
                        attendre(10);
                    }
                }
            } catch (SocketTimeoutException ex) {
                // rien car timeout
            } catch (IOException exc) {
                //wrk.recevoirMessageInfo("Erreur dans la connexion d'un client.");
            }
        }
        if (client != null) {
            client.setRunning(false);
            try {
                client.join();
                client = null;
            } catch (InterruptedException ex) {

            }
        }

        System.gc();
        //wrk.recevoirMessageInfo("Serveur fermé");
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Client getClient() {
        return client;
    }
}