package com.iamacitizen.core.database;

import java.sql.Connection;

/**
 *
 * @author felipe
 */
public interface ConnectionManager {
    
    Connection getConnection();
    
}
