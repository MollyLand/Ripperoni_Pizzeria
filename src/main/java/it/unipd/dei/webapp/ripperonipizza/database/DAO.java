/*
 * Copyright 2019-20 ripperoni
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http: //www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.webapp.ripperonipizza.database;

import java.sql.SQLException;
import java.util.List;

import it.unipd.dei.webapp.ripperonipizza.resource.Resource;

/**
 * Abstract DAO(data access object) interface for a generic Resource
 * <p>
 * For SQLException handling see
 *
 * <a href=
 * "https://www.postgresql.org/docs/9.3/errcodes-appendix.html">error-code-appendix</a>
 *
 * <a href=
 * "http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt">sql-1992</a>
 *
 * @param <E> must be any subclasses of Resource
 * @see Resource
 *
 */
public interface DAO<E extends Resource> {
    /**
     * Should return the underlying table in the database
     *
     * @return table name
     */
    String tableName();

    /**
     * Should return all the records with the specific Resource
     *
     * @return resources
     * @throws SQLException if anything went wrong
     */
    List<E> listAllRecords() throws SQLException;

    /**
     * Should return all the records that matches the input parameters
     * <p>
     * The record should contain the fields that need to be checked.
     * <p>
     * Example: record with id, name, age
     *
     * If one wish to have all the records with name = 'jack', the record should
     * contain only the name field; in this case the query will be as follow:
     * <p>
     * SELECT * FROM .... WHERE name = record.name
     *
     * @param record input parameter
     * @return resources
     * @throws SQLException if anything went wrong
     */

    List<E> selectRecords(E record) throws SQLException;

    /**
     * Insert a new record into the database, some fields of the record will be
     * mandatory, in any case if any constraint is violated, an exception will be
     * raised.
     *
     * @param record new record
     * @return inserted record with updated information
     * @throws SQLException in case of SQL constraint violation
     */
    E insertRecord(E record) throws SQLException;

    /**
     * Update a record by the input parameter
     * <p>
     * Every record must some sort of primary key, the update method should update
     * only one record against the primary key
     *
     * @param record input parameter
     * @return updated record
     * @throws SQLException in case of SQL constraint violation
     */
    E updateRecord(E record) throws SQLException;

    /**
     * Delete a record by the input parameter
     * <p>
     * Every record must some sort of primary key, the delete method should delete
     * only one record against the primary key
     *
     * @param record input parameter
     * @return deleted record
     * @throws SQLException in case of SQL constraint violation
     */
    E deleteRecord(E record) throws SQLException;

    /**
     * By closing this DAO will make all the methods raise a SQLException.
     * <p>
     * If the DAO is already closed this shall be a non-op
     */
    void close();
}
