/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import groovy.sql.Sql
import java.sql.SQLException

def sql = new Sql(ds)
try {
    sql.withTransaction {
        sql.executeUpdate("update SEC_USER set CUBA_TENANT_ID = TENANT_ID where TENANT_ID is not null")
        sql.executeUpdate("update SEC_USER set DTYPE = 'sec\$User' where DTYPE = 'cubasdbmt\$TenantUser'")
    }
} catch (SQLException ex) {}

try {
    sql.withTransaction {
        sql.executeUpdate("update SEC_GROUP set CUBA_TENANT_ID = TENANT_ID where TENANT_ID is not null")
        sql.executeUpdate("update SEC_GROUP set DTYPE = 'sec\$Group' where DTYPE = 'cubasdbmt\$TenantGroup'")
    }
} catch (SQLException ex) {}
