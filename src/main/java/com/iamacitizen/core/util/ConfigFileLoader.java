package com.iamacitizen.core.util;

import com.iamacitizen.core.SerigyConstant;
import com.iamacitizen.core.database.AbstractDatabase;
import com.iamacitizen.core.exception.SerigyException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe responsável por carregar o arquivo de configuração do framework Serigy
 * @author felipe
 */
public class ConfigFileLoader {

    private static Properties properties;
    
    /**
     * Carrega, a partir de um arquivo serigy-config.xml, informações a serem utilizadas pelo framework.
     * @return um objeto Properties com as configurações do framework.
     * @throws SerigyException caso o arquivo de configuração não seja encontrado ou esteja inválido.
     */
    public static Properties load() throws SerigyException {
        try {
            if(properties != null) {
                return properties;
            }
            
            properties = new Properties();
            ClassLoader loader = AbstractDatabase.class.getClassLoader();
            InputStream is = loader.getResourceAsStream("/serigy-config.xml");
            
            if (is == null) {
                is = AbstractDatabase.class.getResourceAsStream("/serigy-config.xml");
                
                if (is == null) {
                    throw new SerigyException(SerigyConstant.CONFIG_FILE_NOT_FOUND);
                }
            }
            
            properties.loadFromXML(is);
            return properties;
        } catch (IOException ex) {
            throw new SerigyException(ex.getMessage());
        }
    }
}
