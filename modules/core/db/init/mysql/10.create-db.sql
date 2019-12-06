-- begin CUBASDBMT_TENANT
create table CUBASDBMT_TENANT (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETE_TS_NN datetime(3) not null default '1000-01-01 00:00:00.000',
    DELETED_BY varchar(50),
    TENANT_ID varchar(255),
    --
    NAME varchar(255) not null,
    ACCESS_GROUP_ID varchar(32) not null,
    ADMIN_ID varchar(32) not null,
    --
    primary key (ID)
)^
-- end CUBASDBMT_TENANT
-- begin SEC_USER
alter table SEC_USER add column TENANT_ID varchar(255)^
alter table SEC_USER add column DTYPE varchar(100)^
-- end SEC_USER
-- begin SEC_GROUP
alter table SEC_GROUP add column TENANT_ID varchar(255)^
alter table SEC_GROUP add column DTYPE varchar(100)^
-- end SEC_GROUP