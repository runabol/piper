CREATE TABLE job (
  job_id varchar(256) not null primary key,
  status varchar(256) not null,
  creation_date timestamp not null,
  start_date timestamp null,
  completion_date null
);

CREATE TABLE job_task (
  job_task_id varchar(256) not null primary key,
  job_id varchar(256) not null,
  status varchar(256) not null,
  creation_date timestamp not null,
  start_date timestamp null,
  completion_date null
);