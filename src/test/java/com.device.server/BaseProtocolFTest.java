package com.device.server;

import jakarta.annotation.PostConstruct;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public abstract class BaseProtocolFTest {

    private static final String SPEED_UP_POSTGRES =
            "ALTER SYSTEM SET fsync = 'off'; " +
                    "ALTER SYSTEM SET full_page_writes = 'off'; " +
                    "ALTER SYSTEM SET synchronous_commit = 'off'; " +
                    "ALTER SYSTEM SET work_mem = '32MB'; " +
                    "SELECT pg_reload_conf(); ";

    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse("postgres:12").asCompatibleSubstituteFor("postgres");
    private static PostgreSQLContainer<?> testPostgres;
    private static boolean SCHEMA_INITIALIZED = false;

    @Configuration
    public static class TestDataSourceConfig {

        @PostConstruct
        public void init() {
            if (SCHEMA_INITIALIZED) {
                return;
            }

            testPostgres = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME);
            testPostgres.start();

            final PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setUrl(testPostgres.getJdbcUrl());
            ds.setUser(testPostgres.getUsername());
            ds.setPassword(testPostgres.getPassword());
            try (final Connection conn = ds.getConnection();
                 final PreparedStatement stmt = conn.prepareStatement(SPEED_UP_POSTGRES)) {
                stmt.execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            SCHEMA_INITIALIZED = true;
        }

        @Bean
        @Primary
        public DataSource dataSource() {
            if (!testPostgres.isRunning()) {
                throw new BeanInstantiationException(DataSource.class, "Test docker container is not running!");
            }

            final PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setUrl(testPostgres.getJdbcUrl());
            ds.setUser(testPostgres.getUsername());
            ds.setPassword(testPostgres.getPassword());
            return ds;
        }
    }
}
