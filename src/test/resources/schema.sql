create table job (
  id varchar(256) not null primary key,
  status varchar(256) not null,
  creation_date timestamp not null,
  completion_date timestamp,
  data text not null
);

create table job_task (
  id varchar(256) not null primary key,
  parent_id varchar(256),
  status varchar(256) not null,
  job_id varchar(256) not null,
  data text not null,
  creation_date timestamp not null
);

create table job_context (
  id varchar(256) not null primary key,
  job_id varchar(256) not null,
  creation_date timestamp not null,
  data text not null
);