-- begin CUBASDBMT_TENANT
create table CUBASDBMT_TENANT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    TENANT_ID varchar(255),
    --
    NAME varchar(255) not null,
    ACCESS_GROUP_ID uuid not null,
    ADMIN_ID uuid not null,
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