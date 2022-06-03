package br.com.alura.database;

import java.sql.*;

public class LocalDatabase {
    private final Connection connection;

    public LocalDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:target/" + name + ".db";
        connection = DriverManager.getConnection(url);

    }

    public void createIfNotExists(String sql){
        /* try para ignorar o erro nas proximas vezes que já existir o banco*/
        try {
            /* Criando a tablea Users*/
            connection.createStatement().execute(sql);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    private PreparedStatement prepare(String statement, String[] params) throws SQLException {
        /* preparando o sql passada no statement*/
        var preparedStatement = connection.prepareStatement(statement);

        /* Definindo os parametros de acordo com a ordem passada na função*/
        for(int i = 0; i < params.length; i++){
            preparedStatement.setString(i + 1, params[i]);
        }
        return preparedStatement;
    }

    public boolean update(String statement, String... params) throws SQLException {
        return prepare(statement, params).execute();
    }

    public ResultSet query(String query, String... params) throws SQLException {
        /* preparando o sql passada no statement*/
        /* Executando a sql*/
        return prepare(query, params).executeQuery();
    }

    public void close() throws SQLException {
        connection.close();
    }
}
