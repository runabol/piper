create table job (
  id varchar(256) not null primary key,
  status varchar(256) not null,
  current_task int not null,
  pipeline_id varchar(256) not null,
  name varchar(256),
  start_time timestamp,
  create_time timestamp not null,
  end_time timestamp,
  data text not null
);

create table job_task (
  id varchar(256) not null primary key,
  parent_id varchar(256) null,
  status varchar(256) not null,
  job_id varchar(256) not null,
  data text not null,
  create_time timestamp not null
);

create table job_context (
  id varchar(256) not null primary key,
  job_id varchar(256) not null,
  create_time timestamp not null,
  data text not null
);
