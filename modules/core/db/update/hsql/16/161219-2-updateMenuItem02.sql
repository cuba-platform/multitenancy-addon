alter table SDBMT_MENU_ITEM add constraint FK_SDBMT_MENU_ITEM_PHOTO foreign key (PHOTO_ID) references SYS_FILE(ID);
create index IDX_SDBMT_MENU_ITEM_PHOTO on SDBMT_MENU_ITEM (PHOTO_ID);
