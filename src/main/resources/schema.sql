drop table if exists job cascade;

create table job (
  id                       varchar(256) not null primary key,
  status                   varchar(256) not null,
  current_task             int          not null,
  pipeline_id              varchar(256) not null,
  label                    varchar(256)     null,
  create_time              timestamp    not null,
  start_time               timestamp        null,
  end_time                 timestamp        null,
  priority                 int          not null,
  inputs                   JSON        not null,
  webhooks                 JSON        not null,
  outputs                  JSON        not null,
  parent_task_execution_id varchar(256)
);

create index job_create_time on job (create_time);
create index job_status on job (status);

drop table if exists task_execution cascade;

create table task_execution (
  id                   varchar(256) not null primary key,
  parent_id            varchar(256)     null,
  status               varchar(256) not null,
  progress             int not          null,
  job_id               varchar(256) not null,
  create_time          timestamp    not null,
  start_time           timestamp        null,
  end_time             timestamp        null,
  serialized_execution text        not null,
  priority             int          not null,
  task_number          int          not null
);

create index task_execution_job_id on task_execution (job_id);

drop table if exists context cascade;

create table context (
  id                 varchar(256) not null primary key,
  stack_id           varchar(256) not null,
  create_time        timestamp    not null,
  serialized_context text        not null
);

create index context_stack_id on context (stack_id);

drop table if exists counter cascade;

create table counter (
  id          varchar(256) not null primary key,
  create_time timestamp    not null,
  value       bigint       not null
);

drop table if exists pipeline cascade;

create table pipeline
(
    id      varchar(256) not null primary key,
    label   varchar(50)  not null,
    tasks   json         not null,
    inputs  json         null,
    outputs json         null,
    retry   int          null
);