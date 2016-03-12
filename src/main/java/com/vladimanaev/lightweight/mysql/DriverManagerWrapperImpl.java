/*******************************************************************************
 * Copyright 2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.vladimanaev.lightweight.mysql;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Vladi
 * Date: 3/12/2016
 * Time: 8:37 PM
 * Copyright VMSR
 */
public class DriverManagerWrapperImpl implements DriverManagerWrapper {

    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    private BasicDataSource basicDataSource;

    private final String url;
    private final String user;
    private final String password;

    public static DriverManagerWrapperImpl withoutConnectionPool(String url, String user, String password) {
        return new DriverManagerWrapperImpl(url, user, password);
    }

    public static DriverManagerWrapperImpl withConnectionPool(String url, String user, String password, int size) {
        return new DriverManagerWrapperImpl(url, user, password, size);
    }

    public DriverManagerWrapperImpl(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public DriverManagerWrapperImpl(String url, String user, String password, int size) {
        this(url, user, password);

        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(DRIVER_NAME);
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(user);
        basicDataSource.setPassword(password);
        basicDataSource.setInitialSize(size);
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            if(isUsingConnectionPool()) {
                return getConnectionFromPool();
            }

            return getNewConnection(url, user, password);

        } catch(ClassNotFoundException e) {
            throw new SQLException(String.format("Missing '%s'", DRIVER_NAME), e);
        }
    }

    private Connection getConnectionFromPool() throws SQLException {
        return basicDataSource.getConnection();
    }

    private boolean isUsingConnectionPool() {
        return basicDataSource != null;
    }

    private Connection getNewConnection(String url, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_NAME);
        return DriverManager.getConnection(url, user, password);
    }
}
