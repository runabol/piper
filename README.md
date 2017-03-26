# What is this?

Piper is a miniature workflow engine written in Java and built atop Spring Boot.

# How it works? 

Piper works by executing a set of tasks defined as a YAML document. 

Example:

`pipelines/demo/hello.yaml`

```
name: Hello World

tasks: 
  - name: Print a greeting
    type: print
    text: hello world
   
  - name: Sleep a little
    type: sleep
    millis: 1000
    
  - name: Print a farewell
    type: print
    text: goodbye world
    
```

The central interface that is used to execute tasks is the `TaskHandler`:

```
public interface TaskHandler<O> {

  O handle (JobTask aTask) throws Exception;
  
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
curl -s -X POST -H "Content-Type:application/json" -d '{"pipeline":"demo/hello"}' http://localhost:8080/job/start
```

Which will give you back something like: 

```
{
  "id": "8043ed19a7cf4a7d9956aa6e0cf7633f",
  "pipeline": {
    "id": "demo/hello",
    "name": "Hello World",
    "tasks": [
      {
        "name": "Print a greeting",
        "text": "hello world",
        "type": "print"
      },
      {
        "name": "Sleep a little",
        "type": "sleep",
        "millis": 1000
      },
      {
        "name": "Print a farewell",
        "text": "goodbye world",
        "type": "print"
      }
    ]
  },
  "creationDate": "2017-03-26T15:28:38-0700",
  "status": "STARTED",
  "execution": [
    {
      "name": "Print a greeting",
      "text": "hello world",
      "id": "dc9fd2c8fd854673818dd7bfa62a46eb",
      "type": "print",
      "creationDate": "2017-03-26T15:28:38-0700",
      "status": "CREATED"
    }
  ],
  "completionDate": null,
  "startDate": "2017-03-26T15:28:38-0700"
}

```

## Check Job Status

Use the Job ID, to check for it's status:

```
curl -s http://localhost:8080/job/240bda633eb6405f8d21e1651285dd2b 
```

```
{
  "id": "240bda633eb6405f8d21e1651285dd2b",
  "pipeline": {
    "id": "demo/hello",
    "name": "Hello World",
    "tasks": [
      {
        "name": "Print a greeting",
        "text": "hello world",
        "type": "print"
      },
      {
        "name": "Sleep a little",
        "type": "sleep",
        "millis": 1000
      },
      {
        "name": "Print a farewell",
        "text": "goodbye world",
        "type": "print"
      }
    ]
  },
  "creationDate": "2017-03-26T15:29:06-0700",
  "status": "COMPLETED",
  "execution": [
    {
      "name": "Print a greeting",
      "completionDate": "2017-03-26T15:29:06-0700",
      "text": "hello world",
      "id": "753ea17539af4a7f9a99ea625a57190d",
      "creationDate": "2017-03-26T15:29:06-0700",
      "type": "print",
      "status": "COMPLETED"
    },
    {
      "name": "Sleep a little",
      "completionDate": "2017-03-26T15:29:07-0700",
      "id": "96e4a4c32ee34f39aca9d6a372d437a2",
      "millis": 1000,
      "creationDate": "2017-03-26T15:29:06-0700",
      "type": "sleep",
      "status": "COMPLETED"
    },
    {
      "name": "Print a farewell",
      "completionDate": "2017-03-26T15:29:07-0700",
      "text": "goodbye world",
      "id": "5349f65aba9940a2b6044544865a5431",
      "creationDate": "2017-03-26T15:29:07-0700",
      "type": "print",
      "status": "COMPLETED"
    }
  ],
  "completionDate": "2017-03-26T15:29:07-0700",
  "startDate": "2017-03-26T15:29:06-0700"
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

