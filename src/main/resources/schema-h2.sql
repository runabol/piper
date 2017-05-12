create table job (
  id varchar(256) not null primary key,
  status varchar(256) not null,
  current_task int not null,
  pipeline_id varchar(256) not null,
  name varchar(256),
  start_time timestamp,
  create_time timestamp not null,
  end_time timestamp,
  tags ARRAY
);

create table task_execution (
  id varchar(256) not null primary key,
  parent_id varchar(256),
  status varchar(256) not null,
  job_id varchar(256) not null,
  create_time timestamp not null,
  start_time timestamp,
  end_time timestamp,
  serialized_execution text not null
);

create table context (
  id varchar(256) not null primary key,
  stack_id varchar(256) not null,
  create_time timestamp not null,
  serialized_context text not null
);

create table counter (
  id varchar(256) not null primary key,
  create_time timestamp not null,
  value bigint not null
);
