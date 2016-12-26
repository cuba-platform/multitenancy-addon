create table SDBMT_CAR (
    ID varchar(36) not null,
    CATEGORY_ID varchar(36),
    VERSION integer not null,
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    NAME varchar(255),
    VIN varchar(255),
    --
    primary key (ID)
);
