package com.iamacitizen.core.database;

import com.iamacitizen.core.exception.SerigyDatabaseException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSQLErrorMapper {

    private Map<String, String> messages;
    private AbstractDatabase database;

    public AbstractSQLErrorMapper(AbstractDatabase database) {
        this.database = database;
        messages = new HashMap<>();
    }

    public void addMessage(String code, String value) {
        if (messages == null) {
            messages = new HashMap<>();
        }
        this.messages.put(code, value);
    }

    public String map(String code) {
        return messages.get(code);
    }

    public AbstractDatabase getDatabase() {
        return database;
    }

    public abstract String SQLErrorHandler(SQLException e) throws SerigyDatabaseException;
}
