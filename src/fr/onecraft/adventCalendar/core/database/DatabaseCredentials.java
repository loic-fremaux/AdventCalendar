package fr.onecraft.adventCalendar.core.database;

public class DatabaseCredentials {
    private final String host;
    private final String pass;
    private final int port;
    private final String user;
    private final String dataBaseName;

    public DatabaseCredentials(String host, String pass, int port, String user, String dbName) {
        this.host = host;
        this.pass = pass;
        this.port = port;
        this.user = user;
        this.dataBaseName = dbName;
    }

    String toURI() {
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dataBaseName + "?useSSL=false";
    }

    String getUser() {
        return this.user;
    }

    String getPass() {
        return this.pass;
    }
}