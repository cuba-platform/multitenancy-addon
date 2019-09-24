insert into SEC_ROLE
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, NAME, LOC_NAME, DESCRIPTION, ROLE_TYPE, IS_DEFAULT_ROLE)
values ('6ebff3a8-2179-b2a0-f2f3-b0f766680a67', 3, '2016-12-16 13:11:09', 'admin', '2016-12-26 17:05:57', 'admin', null, null, 'Tenant Default Role', null, null, 0, true)^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('b81fdf04-bacc-2227-206f-10fb4ea6af82', 1, '2016-12-16 18:51:50', 'admin', '2016-12-16 18:51:50', null, null, null, 20, 'jmxcontrol$ManagedBeanInfo:read', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('9e72c1c1-9d40-3498-5994-0554bb705318', 1, '2016-12-16 13:11:09', 'admin', '2016-12-16 13:11:09', null, null, null, 10, 'serverLog', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('5ada35fd-b5a8-f4af-70af-2268eed113b1', 1, '2016-12-19 06:00:36', 'admin', '2016-12-19 06:00:36', null, null, null, 20, 'sec$Permission:read', 1, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('66ae2f2d-174e-c0b2-9ccf-891efddfd7f8', 1, '2016-12-16 13:11:09', 'admin', '2016-12-16 13:11:09', null, null, null, 10, 'appProperties', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('8293caca-30be-22b4-5c65-062da581f656', 1, '2016-12-19 13:03:34', 'admin', '2016-12-19 13:03:34', null, null, null, 30, 'jmxcontrol$ManagedBeanAttribute:description', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('94656fdb-1e96-cad6-937d-73526bda1798', 1, '2016-12-16 13:11:09', 'admin', '2016-12-16 13:11:09', null, null, null, 10, 'sys$ScheduledTask.browse', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('7a758fc8-5b84-0a08-2814-9fd48de81f4a', 1, '2016-12-19 13:03:34', 'admin', '2016-12-19 13:03:34', null, null, null, 30, 'jmxcontrol$ManagedBeanAttribute:name', 2, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('08930b1f-88a1-3681-0533-37ea0244a925', 1, '2016-12-16 13:11:09', 'admin', '2016-12-16 13:11:09', null, null, null, 10, 'screenProfiler', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('5d9e766f-6e44-5280-c1b5-ef236a248ad2', 1, '2016-12-16 13:11:09', 'admin', '2016-12-16 13:11:09', null, null, null, 10, 'jmxConsole', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('89ca10fb-495a-0816-e85c-a8ea666d2e87', 1, '2016-12-16 18:51:50', 'admin', '2016-12-16 18:51:50', null, null, null, 20, 'jmxcontrol$ManagedBeanInfo:update', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('501319ad-e0c0-21be-0991-d55b235cfef7', 1, '2016-12-19 06:00:36', 'admin', '2016-12-19 06:00:36', null, null, null, 20, 'sec$Permission:create', 1, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('69ef9e37-c51a-d7cd-0a54-447bb165b167', 1, '2016-12-19 06:00:36', 'admin', '2016-12-19 06:00:36', null, null, null, 20, 'sec$Permission:delete', 1, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('c9dcfc53-d449-19d1-20b1-6565f1fab05d', 1, '2016-12-19 17:19:53', 'admin', '2016-12-19 17:19:53', null, null, null, 30, 'sec$UserSessionEntity:tenantId', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('c894755e-74de-fdd7-b15b-e5a911cd0551', 1, '2016-12-19 04:27:01', 'admin', '2016-12-19 04:27:01', null, null, null, 20, 'sec$Tenant:create', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('569640b5-0824-ac15-3faf-7e1684e3e839', 1, '2016-12-19 04:27:01', 'admin', '2016-12-19 04:27:01', null, null, null, 20, 'sec$Tenant:delete', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('dc884f63-6b9a-42f8-7252-f6690eda501a', 1, '2016-12-19 04:27:01', 'admin', '2016-12-19 04:27:01', null, null, null, 20, 'sec$Tenant:read', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('21c54afd-5a1a-7c3e-2de5-0ae60ff4a99f', 1, '2016-12-19 20:53:54', 'admin', '2016-12-19 20:53:54', null, null, null, 10, 'sys$LockInfo.browse', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('a0146ea8-ee80-93b0-7439-3992bdc6ed58', 1, '2016-12-16 13:11:09', 'admin', '2016-12-16 13:11:09', null, null, null, 10, 'performanceStatistics', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('116b44c1-7f95-4778-4520-32412f11844e', 1, '2016-12-19 06:00:36', 'admin', '2016-12-19 06:00:36', null, null, null, 20, 'sec$Permission:update', 1, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('b3aec266-d32d-4424-905d-adfc188a8056', 1, '2016-12-19 13:03:34', 'admin', '2016-12-19 13:03:34', null, null, null, 30, 'jmxcontrol$ManagedBeanAttribute:id', 1, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('e9b0e339-1387-e6eb-c66e-b67bc0f1f48c', 1, '2017-03-20 11:11:11', 'admin', '2017-03-20 11:11:11', null, null, null, 10, 'tenant-management', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('3856bac1-2b93-1db0-23db-40eaabc444fc', 1, '2016-12-19 20:23:15', 'admin', '2016-12-19 20:23:15', null, null, null, 10, 'sec$Tenant.browse', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('4babd552-8fe0-5846-0725-d274c34cb17d', 1, '2016-12-19 20:53:54', 'admin', '2016-12-19 20:53:54', null, null, null, 10, 'entityRestore', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^

insert into SEC_PERMISSION
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, PERMISSION_TYPE, TARGET, VALUE_, ROLE_ID)
values ('517151db-9e70-774b-d966-612214a136cb', 1, '2016-12-19 04:27:01', 'admin', '2016-12-19 04:27:01', null, null, null, 20, 'sec$Tenant:update', 0, '6ebff3a8-2179-b2a0-f2f3-b0f766680a67')^
