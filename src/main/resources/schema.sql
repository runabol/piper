CREATE TABLE job (
  id varchar(256) not null primary key,
  status varchar(256) not null,
  pipeline_id varchar(256) not null,
  name varchar(256) not null,
  creation_date timestamp not null,
  start_date timestamp null,
  completion_date null,
  failed_date null
);

CREATE TABLE job_task (
  id varchar(256) not null primary key,
  job_id varchar(256) not null,
  type varchar(256) not null,
  name varchar(256),
  label varchar(256),
  node varchar(256),
  status varchar(256) not null,
  output text,
  exception text,
  creation_date timestamp not null,
  start_date timestamp null,
  completion_date null,
  failed_date null,
  execution_time int
);