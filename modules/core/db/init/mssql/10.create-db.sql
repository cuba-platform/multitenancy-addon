-- begin CUBASDBMT_TENANT
create table CUBASDBMT_TENANT (
    ID uniqueidentifier,
    VERSION integer not null,
    CREATE_TS datetime2,
    CREATED_BY varchar(50),
    UPDATE_TS datetime2,
    UPDATED_BY varchar(50),
    DELETE_TS datetime2,
    DELETED_BY varchar(50),
    TENANT_ID varchar(255),
    --
    NAME varchar(255) not null,
    ACCESS_GROUP_ID uniqueidentifier not null,
    ADMIN_ID uniqueidentifier not null,
    --
    primary key nonclustered (ID)
)^
-- end CUBASDBMT_TENANT
-- begin SEC_USER
alter table SEC_USER add TENANT_ID varchar(255) ^
alter table SEC_USER add DTYPE varchar(100) ^
update SEC_USER set DTYPE = 'cubasdbmt$TenantUser' where DTYPE is null ^
-- end SEC_USER
-- begin SEC_GROUP
alter table SEC_GROUP add TENANT_ID varchar(255) ^
alter table SEC_GROUP add DTYPE varchar(100) ^
update SEC_GROUP set DTYPE = 'cubasdbmt$TenantGroup' where DTYPE is null ^
-- end SEC_GROUP
