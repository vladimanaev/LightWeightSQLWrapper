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

    public static DriverManagerWrapperImpl createDefaultConnectionPool(String url, String user, String password) {
        return new DriverManagerWrapperImpl(url, user, password, 10, 0);
    }

    public static DriverManagerWrapperImpl createConnectionPool(String url, String user, String password, int totalMax, int initialSize) {
        return new DriverManagerWrapperImpl(url, user, password, totalMax, initialSize);
    }

    public DriverManagerWrapperImpl(String url, String user, String password, int totalMax, int initialSize) {
        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(DRIVER_NAME);
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(user);
        basicDataSource.setPassword(password);
        basicDataSource.setInitialSize(initialSize);
        basicDataSource.setMaxTotal(totalMax);
        basicDataSource.setDefaultAutoCommit(false);
    }

    public DriverManagerWrapperImpl(BasicDataSource basicDataSource) {
        this.basicDataSource = basicDataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return basicDataSource.getConnection();
    }
}
