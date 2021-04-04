package Server;

import sun.rmi.runtime.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.*;

public class ConsoleServer {
    private static final Logger logger = Logger.getLogger("");
    private Vector<ClientHandler> clients;

    public static void writeInLogger(String logInfo) {
        logger.log(Level.ALL, logInfo);
    }

    public ConsoleServer() throws IOException {
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;
        logger.setLevel(Level.ALL);
        logger.getHandlers()[0].setLevel(Level.ALL);
        Handler fileHandler = new FileHandler("chatLog.txt");
        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        try {
            server = new ServerSocket(8189);
            logger.log(Level.ALL, "Сервер запущен!");

            while (true) {
                socket = server.accept();
                logger.log(Level.ALL,"Клиент подключился");
                clients.add(new ClientHandler(this, socket));
            }

        } catch (IOException e) {
            logger.log(Level.ALL,e.toString());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.log(Level.ALL, e.toString());
            }
            try {
                server.close();
            } catch (IOException e) {
                logger.log(Level.ALL, e.toString());
            }
        }
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }
}
