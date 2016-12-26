-- begin SDBMT_TENANTalter table SDBMT_TENANT add constraint FK_SDBMT_TENANT_ACCESS_GROUP foreign key (ACCESS_GROUP_ID) references SEC_GROUP(ID)^
alter table SDBMT_TENANT add constraint FK_SDBMT_TENANT_ADMIN foreign key (ADMIN_ID) references SEC_USER(ID)^
create unique index IDX_SDBMT_TENANT_UNIQ_TENANT_ID on SDBMT_TENANT (TENANT_ID) ^
create unique index IDX_SDBMT_TENANT_UNIQ_ACCESS_GROUP_ID on SDBMT_TENANT (ACCESS_GROUP_ID) ^
-- end SDBMT_TENANT
-- begin SDBMT_CAR
alter table SDBMT_CAR add constraint FK_SDBMT_CAR_CATEGORY foreign key (CATEGORY_ID) references SYS_CATEGORY(ID)^
create index IDX_SDBMT_CAR_CATEGORY on SDBMT_CAR (CATEGORY_ID)^
-- end SDBMT_CAR
-- begin SDBMT_CATEGORY
alter table SDBMT_CATEGORY add constraint FK_SDBMT_CATEGORY_ID foreign key (ID) references SYS_CATEGORY(ID)^
-- end SDBMT_CATEGORY
-- begin SDBMT_ORDER_ITEM
alter table SDBMT_ORDER_ITEM add constraint FK_SDBMT_ORDER_ITEM_MENU_ITEM foreign key (MENU_ITEM_ID) references SDBMT_MENU_ITEM(ID)^
alter table SDBMT_ORDER_ITEM add constraint FK_SDBMT_ORDER_ITEM_ORDER foreign key (ORDER_ID) references SDBMT_ORDER(ID)^
create index IDX_SDBMT_ORDER_ITEM_ORDER on SDBMT_ORDER_ITEM (ORDER_ID)^
create index IDX_SDBMT_ORDER_ITEM_MENU_ITEM on SDBMT_ORDER_ITEM (MENU_ITEM_ID)^
-- end SDBMT_ORDER_ITEM
-- begin SDBMT_MENU_ITEM
alter table SDBMT_MENU_ITEM add constraint FK_SDBMT_MENU_ITEM_PHOTO foreign key (PHOTO_ID) references SYS_FILE(ID)^
create index IDX_SDBMT_MENU_ITEM_PHOTO on SDBMT_MENU_ITEM (PHOTO_ID)^
-- end SDBMT_MENU_ITEM
