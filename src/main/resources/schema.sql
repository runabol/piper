CREATE TABLE job (
  id varchar(256) not null primary key,
  data text not null
);

CREATE TABLE job_task (
  id varchar(256) not null primary key,
  job_id varchar(256) not null,
  data text not null
);