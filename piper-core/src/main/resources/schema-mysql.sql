drop table if exists job;

create table job (
  id varchar(256) not null primary key,
  status varchar(256) not null,
  current_task int not null,
  pipeline_id varchar(256) not null,
  label varchar(256),
  create_time datetime not null,
  start_time datetime,
  end_time datetime,
  tags text not null,
  priority int not null,
  inputs text not null,
  webhooks text not null,
  outputs text not null,
  INDEX idx_job_create_time (create_time),
  INDEX idx_job_status (status)
);

drop table if exists task_execution;

create table task_execution (
  id varchar(256) not null primary key,
  parent_id varchar(256) null,
  status varchar(256) not null,
  job_id varchar(256) not null,
  create_time datetime not null,
  start_time datetime,
  end_time datetime,
  serialized_execution text not null,
  priority int not null,
  task_number int not null,
  INDEX idx_task_execution_jobid (job_id)
);

drop table if exists context;

create table context (
  id varchar(256) not null primary key,
  stack_id varchar(256) not null,
  create_time datetime not null,
  serialized_context text not null,
  INDEX idx_context_stackid (stack_id)
);

drop table if exists counter;

create table counter (
  id varchar(256) not null primary key,
  create_time datetime not null,
  value bigint not null
);
