create table SDBMT_ORDER_ITEM (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    MENU_ITEM_ID varchar(36),
    QUANTITY integer,
    COMMENTS varchar(255),
    ORDER_ID varchar(36),
    --
    primary key (ID)
);
