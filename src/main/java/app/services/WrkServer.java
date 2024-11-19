package app.services;

import app.ctrl.IControllerForWrkServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
            //wrk.recevoirMessageInfo("Serveur démarré");
        } catch (IOException ex) {

        } catch (Exception exc) {
        }
    }

    public void arreterServeur() {
        try {
            serverSocket.close();
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
        switch (args[0]) {
            case "turn":
                if (client.isConnected()) {
                    Double valeur;
                    try {
                        valeur = Double.parseDouble(args[1]);
                    } catch (NumberFormatException ex) {
                        valeur = 0.0;
                    }

                    controller.sendCommand("turn", valeur);
                }
                break;
            case "move_forward":
                if (client.isConnected()) {

                }
                break;
            case "button":
                if (client.isConnected()) {

                }
                break;
            case "connect":
                String tag = args[1];
                if (tag != null && !tag.isEmpty()) {
                    if (controller.connexionClient(tag)) {
                        client.setConnected(true);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void removeClient() {
        this.client = client;
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