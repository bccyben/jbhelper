create table user_info (
  user_id character varying(32) not null,
  family_name character varying(32) not null,
  mail_address character varying(128) not null,
  created_at timestamp(6) without time zone not null,
  update_time timestamp(6) without time zone not null,
  role_id smallint not null,
  given_name character varying(32) not null,
  user_name bytea not null,
  primary key (user_id)
);