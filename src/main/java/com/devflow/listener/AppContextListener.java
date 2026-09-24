package com.devflow.listener;

import com.devflow.config.DBConnection;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("DevFlow Application Initialized successfully on Servlet Container.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("DevFlow Application shutting down. Releasing database pool...");
        DBConnection.shutdown();
    }
}
