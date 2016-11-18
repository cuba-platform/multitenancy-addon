-- begin SDBMT_COMPANY
create table SDBMT_COMPANY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    TEST varchar(255),
    TENANT_ID varchar(255) not null,
    --
    primary key (ID)
)^
-- end SDBMT_COMPANY
-- begin SDBMT_TENANT
create table SDBMT_TENANT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    TENANT_ID varchar(255) not null,
    ACCESS_GROUP_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end SDBMT_TENANT
-- begin SEC_GROUP
alter table SEC_GROUP add column TENANT_ID varchar(255) ^
alter table SEC_GROUP add column DTYPE varchar(100) ^
update SEC_GROUP set DTYPE = 'MT' where DTYPE is null ^
-- end SEC_GROUP
