package org.endeavourhealth.common.security.usermanagermodel.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import com.zaxxer.hikari.pool.HikariProxyConnection;
import org.endeavourhealth.common.config.ConfigManager;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {


    public static enum Db {
        Dsm,
        Um,
        Dds;
    }
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);
    private static Map<String, EntityManagerFactory> entityManagerFactoryMap = new ConcurrentHashMap<>();
    private static Map<UUID, String> publisherServiceToConfigMap = new ConcurrentHashMap<>();
    private static Map<String, Integer> connectionMaxPoolSize = new ConcurrentHashMap<>();

    public static EntityManager getEntityManager(Db dbName) throws Exception {
        return getEntityManager(dbName, null);
    }

    public static EntityManager getEntityManager(Db dbName, String explicitConfigName) throws Exception {

        String cacheKey = "" + dbName + "/" + explicitConfigName;
        EntityManagerFactory factory = entityManagerFactoryMap.get(cacheKey);

        if (factory == null
                || !factory.isOpen()) {

            synchronized (entityManagerFactoryMap) {

                //once in the sync block, repeat the check
                factory = entityManagerFactoryMap.get(cacheKey);
                if (factory == null
                        || !factory.isOpen()) {

                    factory = createEntityManager(dbName, explicitConfigName);
                    entityManagerFactoryMap.put(cacheKey, factory);
                }
            }
        }

        return factory.createEntityManager();
    }

    /*public static void shutdown(Db dbName) {

        EntityManagerFactory factory = entityManagerFactoryMap.get(dbName);

        if (factory != null) {
            factory.close();
            entityManagerFactoryMap.remove(dbName);
        }
    }*/

    private static synchronized EntityManagerFactory createEntityManager(Db dbName, String explicitConfigName) throws Exception {

        //adding this line to force compile-time checking for this class. Spent far too long investigating
        //why this wasn't being found when it turned out to be that it had been removed from POM.xml,
        //so adding this to ensure it's picked up during compile-time rather than run-time
        org.hibernate.hikaricp.internal.HikariCPConnectionProvider p = null;

        JsonNode json = findDatabaseConfigJson(dbName, explicitConfigName);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false"); //always turn this off (https://stackoverflow.com/questions/10075081/hibernate-slow-to-acquire-postgres-connection)

        Iterator<String> fieldNames = json.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode child = json.get(fieldName);

            if (fieldName.equals("url")) {
                String url = child.asText();
                properties.put("hibernate.hikari.dataSource.url", url);

            } else if (fieldName.equals("username")) {
                String user = child.asText();
                properties.put("hibernate.hikari.dataSource.user", user);

            } else if (fieldName.equals("password")) {
                String pass = child.asText();
                properties.put("hibernate.hikari.dataSource.password", pass);

            } else if (fieldName.equals("class")) {
                String cls = child.asText();
                properties.put("hibernate.hikari.dataSourceClassName", cls);

            } else if (fieldName.equals("dialect")) {
                String dialect = child.asText();
                properties.put("hibernate.dialect", dialect);

            } else if (fieldName.equals("connection_properties")) {
                populateConnectionProperties(child, properties, dbName, explicitConfigName);

            } else {
                //ignore it, as it's nothing to do with the DB connection
            }
        }

        String hibernatePersistenceUnit = getPersistenceUnitName(dbName);
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(hibernatePersistenceUnit, properties);

        return factory;
    }

    private static void populateConnectionProperties(JsonNode connectionPropertiesRoot, Map<String, Object> properties, Db dbName, String explicitConfigName) {

        if (!connectionPropertiesRoot.isObject()) {
            throw new IllegalArgumentException("connection_properties should be an object");
        }

        Iterator<String> fieldNames = connectionPropertiesRoot.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode child = connectionPropertiesRoot.get(fieldName);

            //if not one of the generally-used fields above, then just interpret as a
            //properly named property, and just set in according to its type
            if (child.isTextual()) {
                String value = child.asText();
                properties.put(fieldName, value);

            } else if (child.isInt()) {
                int value = child.asInt();
                properties.put(fieldName, new Integer(value));

            } else if (child.isBigInteger()) {
                long value = child.asLong();
                properties.put(fieldName, new Long(value));

            } else if (child.isBoolean()) {
                boolean value = child.asBoolean();
                properties.put(fieldName, new Boolean(value));

            } else {
                throw new IllegalArgumentException("Unsupported JSON element type for database " + dbName + " " + explicitConfigName + ": " + child.getNodeType());
            }
        }

    }

    /*private static synchronized EntityManagerFactory createEntityManager(Db dbName, String explicitConfigName) throws Exception {

        //adding this line to force compile-time checking for this class. Spent far too long investigating
        //why this wasn't being found when it turned out to be that it had been removed from POM.xml,
        //so adding this to ensure it's picked up during compile-time rather than run-time
        org.hibernate.hikaricp.internal.HikariCPConnectionProvider p = null;

        JsonNode json = findDatabaseConfigJson(dbName, explicitConfigName);

        String url = json.get("url").asText();
        String user = json.get("username").asText();
        String pass = json.get("password").asText();

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        properties.put("hibernate.hikari.dataSource.url", url);
        properties.put("hibernate.hikari.dataSource.user", user);
        properties.put("hibernate.hikari.dataSource.password", pass);

        if (json.has("class")) {
            properties.put("hibernate.hikari.dataSourceClassName", json.get("class").asText());
        }

        if (json.has("dialect")) {
            properties.put("hibernate.dialect", json.get("dialect").asText());
        }

        String hibernatePersistenceUnit = getPersistenceUnitName(dbName);
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(hibernatePersistenceUnit, properties);

        return factory;
    }*/

    private static JsonNode findDatabaseConfigJson(Db dbName, String configName) throws Exception {

        JsonNode json = null;
        if (dbName == Db.Dsm) {
            //support new-style DB config records where app ID isn't specified
            json = ConfigManager.getConfigurationAsJson("db_data_sharing_manager");
            if (json == null) {
                //if no new-style record, try getting it the old way
                json = ConfigManager.getConfigurationAsJson(configName, "data-sharing-manager");
            }

        } else if (dbName == Db.Um) {
            //support new-style DB config records where app ID isn't specified
            json = ConfigManager.getConfigurationAsJson("db_user_manager");
            if (json == null) {
                //if no new-style record, try getting it the old way
                json = ConfigManager.getConfigurationAsJson(configName, "user-manager");
            }

        } else if (dbName == Db.Dds) {
            //support new-style DB config records where app ID isn't specified
            json = ConfigManager.getConfigurationAsJson("db_admin");
            if (json == null) {
                //if no new-style record, try getting it the old way
                json = ConfigManager.getConfigurationAsJson("admin", "db_common");
            }

        } else {
            throw new RuntimeException("Unknown database " + dbName);
        }

        if (json == null) {
            throw new Exception("No config JSON for " + dbName + " and config " + configName);
        }

        return json;
    }

    private static String getPersistenceUnitName(Db dbName) {
        if (dbName == Db.Dsm) {
            return "DataSharingManager";
        } else if (dbName == Db.Um) {
            return "UserManager";
        } else if (dbName == Db.Dds) {
            return "DDS";
        } else {
            throw new RuntimeException("Unknown database " + dbName);
        }
    }


    public static EntityManager getDsmEntityManager() throws Exception {
        return getEntityManager(Db.Dsm, "database");
    }

    public static EntityManager getUmEntityManager() throws Exception {
        return getEntityManager(Db.Um, "database");
    }

    public static EntityManager getDdsEntityManager() throws Exception {
        return getEntityManager(Db.Dds, "database");
    }

    private static int getConnectionPoolMaxSize(Db dbName, String explicitConfigName) throws Exception {
        String cacheKey = "" + dbName + "/" + explicitConfigName;
        Integer maxPoolSize = connectionMaxPoolSize.get(cacheKey);

        if (maxPoolSize == null) {

            synchronized (connectionMaxPoolSize) {

                EntityManager entityManager = getEntityManager(dbName, explicitConfigName);
                try {
                    maxPoolSize = getConnectionPoolMaxSize(entityManager);
                } finally {
                    entityManager.close();
                }

                connectionMaxPoolSize.put(cacheKey, maxPoolSize);
            }
        }

        return maxPoolSize.intValue();
    }

    /**
     * returns the HikariCP max pool size for an EntityManager. Note this function uses
     * reflection to access private variables, so it potentially ask risk of breaking
     * if HikariCP changes, but allows us to base things such as thread pool size on
     * DB connection pool sizes, reducing the amount of config needed
     */
    private static Integer getConnectionPoolMaxSize(EntityManager entityManager) throws Exception {
        SessionImpl session = (SessionImpl) entityManager.getDelegate();
        Connection connection = session.connection();

        HikariProxyConnection hikariProxyConnection = (HikariProxyConnection)connection;

        Field field = hikariProxyConnection.getClass().getSuperclass().getDeclaredField("poolEntry");
        field.setAccessible(true);
        Object poolEntry = field.get(hikariProxyConnection);

        field = poolEntry.getClass().getDeclaredField("hikariPool");
        field.setAccessible(true);
        HikariPool hikariPool = (HikariPool)field.get(poolEntry);

        field = hikariPool.getClass().getSuperclass().getDeclaredField("config");
        field.setAccessible(true);
        HikariConfig hikariConfig = (HikariConfig)field.get(hikariPool);

        return new Integer(hikariConfig.getMaximumPoolSize());
    }

    public static void shutdown() {
        LOG.debug("Shutting down User Manager connection factories");
        for(String factoryName : entityManagerFactoryMap.keySet()) {
            LOG.debug("Closing " + factoryName);
            EntityManagerFactory factory = entityManagerFactoryMap.get(factoryName);
            factory.close();
        }
        LOG.debug("User Manager connection factory shutdown complete");
    }
}
