--create table ACT_ID_GROUP (
--    ID_ varchar(64),
--    REV_ integer,
--    NAME_ varchar(255),
--    TYPE_ varchar(255),
--    primary key (ID_)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;
--
--create table ACT_ID_MEMBERSHIP (
--    USER_ID_ varchar(64),
--    GROUP_ID_ varchar(64),
--    primary key (USER_ID_, GROUP_ID_)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;
--
--create table ACT_ID_USER (
--    ID_ varchar(64),
--    REV_ integer,
--    FIRST_ varchar(255),
--    LAST_ varchar(255),
--    EMAIL_ varchar(255),
--    PWD_ varchar(255),
--    PICTURE_ID_ varchar(64),
--    primary key (ID_)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;
--
--create table ACT_ID_INFO (
--    ID_ varchar(64),
--    REV_ integer,
--    USER_ID_ varchar(64),
--    TYPE_ varchar(64),
--    KEY_ varchar(255),
--    VALUE_ varchar(255),
--    PASSWORD_ LONGBLOB,
--    PARENT_ID_ varchar(255),
--    primary key (ID_)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;
--
--alter table ACT_ID_MEMBERSHIP
--    add constraint ACT_FK_MEMB_GROUP
--    foreign key (GROUP_ID_)
--    references ACT_ID_GROUP (ID_);
--
--alter table ACT_ID_MEMBERSHIP
--    add constraint ACT_FK_MEMB_USER
--    foreign key (USER_ID_)
--    references ACT_ID_USER (ID_);

DROP VIEW  IF EXISTS act_id_membership;
DROP VIEW  IF EXISTS act_id_user;
DROP VIEW  IF EXISTS act_id_group;

CREATE or replace VIEW ACT_ID_USER(ID_,REV_,FIRST_,LAST_,EMAIL_,PWD_,PICTURE_ID_ ) AS
  select username AS ID_,
      0 AS REV_,
      left(realname,1) AS FIRST_,
      right(realname, CHAR_LENGTH(realname)-1) AS LAST_,
      email AS EMAIL_,
      password AS PWD_,
      avatar AS PICTURE_ID_
  from upms_user;


CREATE VIEW ACT_ID_GROUP
AS
  select name AS ID_,
  0 AS REV_,
  description AS NAME_,
  'assignment' AS TYPE_
  from upms_organization;

CREATE VIEW ACT_ID_MEMBERSHIP
AS
   select
       u.username AS USER_ID_,
       o.`name`  AS GROUP_ID_
   from upms_user_organization u_o join upms_user u
   on u_o.user_id = u.user_id
   join upms_organization o
   on o.organization_id = u_o.organization_id;

create table ACT_ID_INFO (
    ID_ varchar(64),
    REV_ integer,
    USER_ID_ varchar(64),
    TYPE_ varchar(64),
    KEY_ varchar(255),
    VALUE_ varchar(255),
    PASSWORD_ LONGBLOB,
    PARENT_ID_ varchar(255),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

