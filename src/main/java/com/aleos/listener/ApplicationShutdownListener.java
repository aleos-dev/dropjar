package com.aleos.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * ApplicationShutdownListener is a component responsible for handling
 * application shutdown events. It implements the ApplicationListener
 * interface and listens for ContextClosedEvent.
 *
 * When a ContextClosedEvent is received, it performs necessary
 * cleanup tasks such as unregistering JDBC drivers.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        log.info("Application is shutting down. Performing cleanup tasks...");

        unregisterJdbcDrivers();
    }

    private void unregisterJdbcDrivers() {

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log.debug("Successfully deregistered JDBC driver: {}", driver);
            } catch (SQLException e) {
                log.debug("Error unregistering JDBC driver: {}", driver, e);
            }
        }
    }
}
