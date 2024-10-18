package com.griefcraft.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

class DriverStub implements Driver {
    private Driver driver;

    protected DriverStub(Driver d) {
        driver = d;
    }

    public boolean acceptsURL(String u) throws SQLException {
        return driver.acceptsURL(u);
    }

    public Connection connect(String u, Properties p) throws SQLException {
        return driver.connect(u, p);
    }

    public int getMajorVersion() {
        return driver.getMajorVersion();
    }

    public int getMinorVersion() {
        return driver.getMinorVersion();
    }

    public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
        return driver.getPropertyInfo(u, p);
    }

    public boolean jdbcCompliant() {
        return driver.jdbcCompliant();
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger("LWC");
    }
}