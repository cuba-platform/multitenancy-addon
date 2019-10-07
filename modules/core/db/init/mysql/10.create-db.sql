-- begin CUBASDBMT_TENANT
create table CUBASDBMT_TENANT (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
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