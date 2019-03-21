create table CUBASDBMT_TENANT (
    ID varchar2(32),
    VERSION number(10) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar2(50 char),
    UPDATE_TS timestamp,
    UPDATED_BY varchar2(50 char),
    DELETE_TS timestamp,
    DELETED_BY varchar2(50 char),
    TENANT_ID varchar2(255 char),
    --
    NAME varchar2(255 char) not null,
    ACCESS_GROUP_ID varchar2(32) not null,
    ADMIN_ID varchar2(32) not null,
    --
    primary key (ID)
)^
