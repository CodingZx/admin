
-- ----------------------------
-- Table structure for a_admin
-- ----------------------------
DROP TABLE IF EXISTS "public"."a_admin";
CREATE TABLE "public"."a_admin" (
  "id" uuid NOT NULL,
  "user_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "real_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL,
  "status" bool NOT NULL,
  "role_id" uuid NOT NULL
)
;
COMMENT ON COLUMN "public"."a_admin"."user_name" IS '登录账号';
COMMENT ON COLUMN "public"."a_admin"."real_name" IS '真实姓名';
COMMENT ON COLUMN "public"."a_admin"."password" IS '密码';
COMMENT ON COLUMN "public"."a_admin"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."a_admin"."status" IS '状态 true:正常 false:被冻结';
COMMENT ON COLUMN "public"."a_admin"."role_id" IS '角色ID';

-- ----------------------------
-- Records of a_admin
-- ----------------------------
INSERT INTO "public"."a_admin" VALUES ('a82da747-ebab-4d1f-8783-b49296c07698', 'admin', '后台管理员', '978042f7cf4008453d62369bb6266f227d1df47e63d1200e', '2019-06-09 16:43:46.021786', 't', 'c8f60307-1b1b-4685-b432-c4da7785c8db');

-- ----------------------------
-- Table structure for a_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."a_menu";
CREATE TABLE "public"."a_menu" (
  "id" uuid NOT NULL,
  "menu_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "icon" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_by" int4 NOT NULL,
  "create_time" timestamp(0) NOT NULL,
  "menu_type" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "parent_id" uuid,
  "permission" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying
)
;
COMMENT ON COLUMN "public"."a_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "public"."a_menu"."menu_url" IS '菜单URL';
COMMENT ON COLUMN "public"."a_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."a_menu"."sort_by" IS '排序值';
COMMENT ON COLUMN "public"."a_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."a_menu"."menu_type" IS '菜单类型';
COMMENT ON COLUMN "public"."a_menu"."parent_id" IS '父级菜单ID';
COMMENT ON COLUMN "public"."a_menu"."permission" IS '对应权限';

-- ----------------------------
-- Records of a_menu
-- ----------------------------
INSERT INTO "public"."a_menu" VALUES ('1637688e-cba8-4c84-be86-b7d032f06b11', '后台账号', '/admin/admin-list', '', 99, '2019-06-09 14:01:40', 'LINK', '3b270ebc-c2ee-41ba-91fb-3a7eb76a7f93', 'sys:admin:list');
INSERT INTO "public"."a_menu" VALUES ('3c06c819-c688-4a27-81ea-a78cb97250cc', '新增', '', '', 99, '2019-06-09 14:12:41', 'BUTTON', '1637688e-cba8-4c84-be86-b7d032f06b11', 'sys:admin:add');
INSERT INTO "public"."a_menu" VALUES ('bfad1b0b-5d0a-4a83-849d-08dad3f3ca35', '删除', '', '', 97, '2019-06-09 14:14:12', 'BUTTON', '1637688e-cba8-4c84-be86-b7d032f06b11', 'sys:admin:remove');
INSERT INTO "public"."a_menu" VALUES ('f7cdbab9-9885-4df9-8e4d-7bb9e07d0b97', '启用/禁用', '', '', 96, '2019-06-09 14:14:58', 'BUTTON', '1637688e-cba8-4c84-be86-b7d032f06b11', 'sys:admin:status');
INSERT INTO "public"."a_menu" VALUES ('e9d4bae2-3c77-40aa-a155-9e43428c5319', '新增', '', '', 99, '2019-06-09 14:21:04', 'BUTTON', 'b5c46a29-19b2-4d4d-b5ae-291abf43622e', 'sys:role:add');
INSERT INTO "public"."a_menu" VALUES ('b5c46a29-19b2-4d4d-b5ae-291abf43622e', '角色管理', '/role/role-list', '', 98, '2019-06-09 14:16:05', 'LINK', '3b270ebc-c2ee-41ba-91fb-3a7eb76a7f93', 'sys:role:list');
INSERT INTO "public"."a_menu" VALUES ('1b8a0dc9-ba1a-4c86-bb25-f825f0e9d4ac', '删除', '', '', 97, '2019-06-09 14:22:22', 'BUTTON', 'b5c46a29-19b2-4d4d-b5ae-291abf43622e', 'sys:role:remove');
INSERT INTO "public"."a_menu" VALUES ('a17b464d-97ce-4f42-ad73-e34fdf948db5', '修改', '', '', 98, '2019-06-09 14:23:10', 'BUTTON', 'b5c46a29-19b2-4d4d-b5ae-291abf43622e', 'sys:role:edit');
INSERT INTO "public"."a_menu" VALUES ('b4a2fd6f-5bd0-4145-81f5-e2ebc51b1cda', '菜单管理', '/menu/menu-list', '', 97, '2019-06-09 16:40:17', 'LINK', '3b270ebc-c2ee-41ba-91fb-3a7eb76a7f93', 'sys:menu:list');
INSERT INTO "public"."a_menu" VALUES ('ea446020-4443-44fe-8bd0-5b0710d7252c', '新增', '', '', 99, '2019-06-09 16:40:46', 'BUTTON', 'b4a2fd6f-5bd0-4145-81f5-e2ebc51b1cda', 'sys:menu:add');
INSERT INTO "public"."a_menu" VALUES ('60c20b7e-1483-46b9-aa90-47f270deec99', '修改', '', '', 98, '2019-06-09 16:41:15', 'BUTTON', 'b4a2fd6f-5bd0-4145-81f5-e2ebc51b1cda', 'sys:menu:edit');
INSERT INTO "public"."a_menu" VALUES ('1dd10ca7-4435-47e5-92d0-a3cb5e8f2645', '编辑', '', '', 98, '2019-06-09 14:13:43', 'DIC', '1637688e-cba8-4c84-be86-b7d032f06b11', 'sys:admin:edit');
INSERT INTO "public"."a_menu" VALUES ('3e8f763c-364f-4111-847f-8c3711024858', '删除', '', '', 97, '2019-06-09 16:42:12', 'BUTTON', 'b4a2fd6f-5bd0-4145-81f5-e2ebc51b1cda', 'sys:menu:remove');
INSERT INTO "public"."a_menu" VALUES ('3b270ebc-c2ee-41ba-91fb-3a7eb76a7f93', '系统管理', '', 'layui-icon-set-fill', 99, '2019-06-09 14:00:04', 'DIC', NULL, '');

-- ----------------------------
-- Table structure for a_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."a_role";
CREATE TABLE "public"."a_role" (
  "id" uuid NOT NULL,
  "role_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL,
  "menus" jsonb NOT NULL
)
;
COMMENT ON COLUMN "public"."a_role"."role_name" IS '角色名';
COMMENT ON COLUMN "public"."a_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."a_role"."menus" IS 'json :  {"menus":["id1","id2"]}';

-- ----------------------------
-- Records of a_role
-- ----------------------------
INSERT INTO "public"."a_role" VALUES ('c8f60307-1b1b-4685-b432-c4da7785c8db', '后台管理员', '2019-06-08 12:07:16.92915', '{"menus": ["3b270ebc-c2ee-41ba-91fb-3a7eb76a7f93", "1637688e-cba8-4c84-be86-b7d032f06b11", "3c06c819-c688-4a27-81ea-a78cb97250cc", "bfad1b0b-5d0a-4a83-849d-08dad3f3ca35", "f7cdbab9-9885-4df9-8e4d-7bb9e07d0b97", "1dd10ca7-4435-47e5-92d0-a3cb5e8f2645", "b5c46a29-19b2-4d4d-b5ae-291abf43622e", "e9d4bae2-3c77-40aa-a155-9e43428c5319", "1b8a0dc9-ba1a-4c86-bb25-f825f0e9d4ac", "a17b464d-97ce-4f42-ad73-e34fdf948db5", "b4a2fd6f-5bd0-4145-81f5-e2ebc51b1cda", "ea446020-4443-44fe-8bd0-5b0710d7252c", "60c20b7e-1483-46b9-aa90-47f270deec99", "3e8f763c-364f-4111-847f-8c3711024858"]}');

-- ----------------------------
-- Indexes structure for table a_admin
-- ----------------------------
CREATE UNIQUE INDEX "a_admin_unique_idx" ON "public"."a_admin" USING btree (
  "user_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table a_admin
-- ----------------------------
ALTER TABLE "public"."a_admin" ADD CONSTRAINT "a_admin_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table a_menu
-- ----------------------------
ALTER TABLE "public"."a_menu" ADD CONSTRAINT "a_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table a_role
-- ----------------------------
ALTER TABLE "public"."a_role" ADD CONSTRAINT "a_role_pkey" PRIMARY KEY ("id");
