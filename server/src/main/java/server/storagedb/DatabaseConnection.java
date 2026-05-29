package server.storagedb;

import server.lifecycle.Shutdownable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection implements Shutdownable {

    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());


    private final BlockingQueue<Connection> connectionPool;
    private final static int maxSize = 10;
    private static DatabaseConnection instance;
    private final DatabaseCredentials credentials;


    private DatabaseConnection(DatabaseCredentials credentials) {
        this.connectionPool = new ArrayBlockingQueue<>(maxSize);
        this.credentials = credentials;
    }



    public static void init(DatabaseCredentials credentials) {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection(credentials);
                }
            }
        }
    }


    public static DatabaseConnection getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Database connection not initialized. Missing database credentials.");
        }
        return instance;
    }


    public void initPoolConnection(){
        try {
            for (int i = 0; i < maxSize; i++){
                var connection = DriverManager.getConnection(
                        credentials.url(),
                        credentials.user(),
                        credentials.password());
                connectionPool.put(connection);
            }
        } catch (SQLException | InterruptedException e){
            logger.severe(e.getMessage());
        }
    }


    public Connection getConnection() throws InterruptedException {
        return connectionPool.take();
    }


    public void giveBackTheConnection(Connection connection) {
        if (connection != null) {
            try {
                connectionPool.put(connection);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("Thread interrupted while returning connection to pool.");
            }
        }
    }


    private void closeConnection() throws SQLException {
        int closed = 0;
        for(Connection c : connectionPool){
            if(c != null && !c.isClosed()){
                c.close();
                closed++;
            }
        }
        logger.info("PostgreSQL database connections closed gracefully:"+ closed);
    }


    @Override
    public void shutdownHook(){
        try {
            closeConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}