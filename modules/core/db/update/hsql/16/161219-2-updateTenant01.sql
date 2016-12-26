alter table SDBMT_TENANT add constraint FK_SDBMT_TENANT_ADMIN foreign key (ADMIN_ID) references SEC_USER(ID);
