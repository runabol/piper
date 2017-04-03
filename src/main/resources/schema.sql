create table job (
  id varchar(256) not null primary key,
  creation_date timestamp not null,
  data text not null
);

create table job_task (
  id varchar(256) not null primary key,
  job_id varchar(256) not null,
  data text not null
);