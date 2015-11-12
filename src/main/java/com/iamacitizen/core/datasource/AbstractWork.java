package com.iamacitizen.core.datasource;

import java.sql.Connection;

/**
 *
 * @author felipe
 */
public interface AbstractWork {

    void execute(Connection conn);
    
}
