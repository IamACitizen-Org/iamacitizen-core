package com.iamacitizen.core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.iamacitizen.core.exception.SerigyDatabaseException;
import com.iamacitizen.core.util.Logger;

/**
 * Classe utilitária com tarefas relacionadas a banco de dados. Tem por objetivo abstrair a API JDBC no que se refere a
 * iniciar, efetivar e desfazer transações, fechar ResultSet, PreparedStatement e Connection.
 *
 * @author felipe
 */
class DatabaseUtils {

    public static void beginTransaction(Connection conn) throws SerigyDatabaseException {
        try {
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.error(ex.getMessage());
            throw new SerigyDatabaseException(ex.getMessage());
        }
    }

    public static void commitTransaction(Connection conn) throws SerigyDatabaseException {
        try {
            if (conn != null && !conn.isClosed() && !conn.getAutoCommit()) {
                conn.commit();
            }
        } catch (SQLException ex) {
            Logger.error(ex.getMessage());
            
            DatabaseUtils.rollback(conn);
            
            throw new SerigyDatabaseException(ex.getMessage());
        }
    }

    public static void rollback(Connection conn) throws SerigyDatabaseException {
        try {
            /* Se a conexão for válida e não foi auto commit, realiza rollback */
            if (conn != null && !conn.isClosed() && !conn.getAutoCommit()) {
                conn.rollback();
            }
        } catch (SQLException ie) {
            Logger.error(ie.getMessage());
            throw new SerigyDatabaseException(ie.getMessage());
        }
    }

    public static void cleanUp(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException ex) {
                Logger.error(ex.getMessage());
            }
        }
    }

    public static void cleanUp(PreparedStatement ps, Connection conn) {
        if (ps != null) {
            try {
                if (!ps.isClosed()) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.error(ex.getMessage());
            }
        }

        cleanUp(conn);
    }

    public static void cleanUp(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.error(ex.getMessage());
            }
        }
		
        cleanUp(ps, conn);
    }
}
