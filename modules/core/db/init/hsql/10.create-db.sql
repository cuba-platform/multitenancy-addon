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
-- begin SDBMT_TENANTcreate table SDBMT_TENANT (
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
    ADMIN_ID varchar(36) not null,
    --
    primary key (ID)
)^-- end SDBMT_TENANT
-- begin SEC_GROUP
alter table SEC_GROUP add column TENANT_ID varchar(255) ^
alter table SEC_GROUP add column DTYPE varchar(100) ^
update SEC_GROUP set DTYPE = 'MT' where DTYPE is null ^
-- end SEC_GROUP
-- begin SDBMT_CAR
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
)^
-- end SDBMT_CAR
-- begin SDBMT_CATEGORY
create table SDBMT_CATEGORY (
    ID varchar(36) not null,
    --
    TENANT_ID varchar(255),
    --
    primary key (ID)
)^
-- end SDBMT_CATEGORY
-- begin SYS_CATEGORY_ATTR
alter table SYS_CATEGORY_ATTR add column TENANT_ID varchar(255) ^
alter table SYS_CATEGORY_ATTR add column DTYPE varchar(100) ^
update SYS_CATEGORY_ATTR set DTYPE = 'SDBMT' where DTYPE is null ^
-- end SYS_CATEGORY_ATTR
-- begin SEC_USER
alter table SEC_USER add column TENANT_ID varchar(255) ^
alter table SEC_USER add column DTYPE varchar(100) ^
update SEC_USER set DTYPE = 'SDBMT' where DTYPE is null ^
-- end SEC_USER
-- begin SEC_ROLE
alter table SEC_ROLE add column TENANT_ID varchar(255) ^
alter table SEC_ROLE add column DTYPE varchar(100) ^
update SEC_ROLE set DTYPE = 'sec$Role' where DTYPE is null ^
-- end SEC_ROLE
-- begin SYS_FILE
alter table SYS_FILE add column TENANT_ID varchar(255) ^
alter table SYS_FILE add column DTYPE varchar(100) ^
update SYS_FILE set DTYPE = 'SDBMT' where DTYPE is null ^
-- end SYS_FILE
-- begin SYS_SENDING_MESSAGE
alter table SYS_SENDING_MESSAGE add column TENANT_ID varchar(255) ^
alter table SYS_SENDING_MESSAGE add column DTYPE varchar(100) ^
update SYS_SENDING_MESSAGE set DTYPE = 'SDBMT' where DTYPE is null ^
-- end SYS_SENDING_MESSAGE
-- begin SYS_SENDING_ATTACHMENT
alter table SYS_SENDING_ATTACHMENT add column TENANT_ID varchar(255) ^
alter table SYS_SENDING_ATTACHMENT add column DTYPE varchar(100) ^
update SYS_SENDING_ATTACHMENT set DTYPE = 'SDBMT' where DTYPE is null ^
-- end SYS_SENDING_ATTACHMENT
-- begin SEC_USER_ROLE
alter table SEC_USER_ROLE add column TENANT_ID varchar(255) ^
alter table SEC_USER_ROLE add column DTYPE varchar(100) ^
update SEC_USER_ROLE set DTYPE = 'SDBMT' where DTYPE is null ^
-- end SEC_USER_ROLE
-- begin SEC_FILTER
alter table SEC_FILTER add column TENANT_ID varchar(255) ^
alter table SEC_FILTER add column DTYPE varchar(100) ^
update SEC_FILTER set DTYPE = 'sdbmt$SdbmtFilter' where DTYPE is null ^
-- end SEC_FILTER
-- begin SYS_ATTR_VALUE
alter table SYS_ATTR_VALUE add column TENANT_ID varchar(255) ^
alter table SYS_ATTR_VALUE add column DTYPE varchar(100) ^
update SYS_ATTR_VALUE set DTYPE = 'SDBMT' where DTYPE is null ^
-- end SYS_ATTR_VALUE
-- begin SDBMT_MENU_ITEM
create table SDBMT_MENU_ITEM (
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
    PRICE double precision,
    HOT boolean,
    TENANT_ID varchar(255),
    PHOTO_ID varchar(36),
    --
    primary key (ID)
)^
-- end SDBMT_MENU_ITEM
-- begin SDBMT_ORDER
create table SDBMT_ORDER (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DATE_TIME timestamp,
    TOTAL double precision,
    TENANT_ID varchar(255),
    --
    primary key (ID)
)^
-- end SDBMT_ORDER
-- begin SDBMT_ORDER_ITEM
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
    TENANT_ID varchar(255),
    --
    primary key (ID)
)^
-- end SDBMT_ORDER_ITEM
