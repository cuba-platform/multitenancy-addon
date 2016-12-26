alter table SDBMT_CAR add constraint FK_SDBMT_CAR_CATEGORY foreign key (CATEGORY_ID) references SYS_CATEGORY(ID);
create index IDX_SDBMT_CAR_CATEGORY on SDBMT_CAR (CATEGORY_ID);
