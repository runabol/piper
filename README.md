# What is this?

Piper is a miniature workflow engine written in Java/Spring.

# For god's sake, why another workflow engine? 

Many of the workflow engines that i've looked at, claim to be "light" and "simple" but expect you to master BPMN and their 500+ pages documentation just to get going. In this project I'm striving to deliver on these promises and allow developer to cut to the chase.
    
# How it works? 

Piper works by executing a set of tasks defined as a YAML document. 

Example:

`pipelines/demo/hello.yaml`

```
name: Hello World
    
tasks: 
  - name: Print a greeting
    handler: log
    text: hello world
    
  - name: Print a greeting
    handler: log
    text: what's up world?
    
  - name: Print a greeting
    handler: log
    text: goodbye world
```

The central interface that is used to execute tasks is the `TaskHandler`:

```
public interface TaskHandler<O> {

  O handle (JobTask aTask);
  
}
```

`TaskHandler`s are resolved according to the `handler` property of each task. Here is the `log` `TaskHandler` implementations seen on the pipeline above: 

```
@Component // register the implementation with the application
public class Log implements TaskHandler<Object> {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public Object handle (JobTask aTask) { // receive the task instance to execute
    log.info(aTask.getString("text")); // get the text property from the task and output it
    return null; // don't return anything
  }

}
``` 

# Pipelines

Pipeline definitions are located under the `pipelines/` directory on the root of the project.

# First time use

Prerequisites: JDK 8 and Maven 3

`mvn clean spring-boot:run` 

This will start piper on your local box, running fully in-memory and without relying on any external dependencies like database or a messaging middleware. 

# API

## Start a Job 

Jobs can be started from the REST API: 

```
curl -s -X POST -H "Content-Type:application/json" -d '{"pipelineId":"demo/hello"}' http://localhost:8080/job/start
```

Which will give you back something like: 

```
{
  "id": "881e6a78a23a42f5985bcc9e6d2bf444",
  "status": "STARTED",
  "tasks": [
    {
      "handler": "log",
      "_completionDate": "2016-07-10T14:11:49-0700",
      "name": "Print a greeting",
      "text": "hello world",
      "_id": "6fe42b1bf2a142e9a3487a4e903f5a28",
      "_creationDate": "2016-07-10T14:11:49-0700",
      "_status": "COMPLETED"
    },
    {
      "name": "Print a greeting",
      "handler": "log",
      "text": "what's up world?",
      "_id": "1afcbb9ed4694e689f56bdf1836e76dd",
      "_status": "CREATED",
      "_creationDate": "2016-07-10T14:11:49-0700"
    }
  ],
  "creationDate": "2016-07-10T14:11:49-0700",
  "completionDate": null,
  "startDate": "2016-07-10T14:11:49-0700"
}
```

## Check Job Status

Use the Job ID, to check for it's status:

```
curl -s http://localhost:8080/job/881e6a78a23a42f5985bcc9e6d2bf444 | jq . 
```

```
{
  "id": "7aa46bd0bd41495889e9fc392c78aff9",
  "status": "COMPLETED",
  "tasks": [
    ... 
  ],
  "creationDate": "2016-07-10T14:35:26-0700",
  "completionDate": "2016-07-10T14:35:26-0700",
  "startDate": "2016-07-10T14:35:26-0700"
}
```


# Architecture

Piper is comprised of two central components: 

The `Coordinator`, responsible for the job execution, for doling out tasks to workers and for handling any errors that occur on job executions. 

and, 

The `Worker`, responsible for executing a single task outside of the context of a Job. i.e. `Worker` instances are meant to be "dumb", stateless processes who simply receive a task from the coordinator, execute it and reply to the Coordinator with the results or errors if any.

Since `Coordinator` and `Worker` do not talk to each other directly but only through a `Messenger`, they can easily run on seperate machines and talk to each other through some sort of middleware.  
 
# Licensing

Piper is licensed under the Apache License, Version 2.0. See [LICENSE](https://github.com/creactiviti/piper/blob/master/LICENSE) for the full license text.

