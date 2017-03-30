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

`TaskHandler`s are resolved according to the `type` property of each task. Here is the `print` `TaskHandler` implementations seen on the pipeline above: 

```
@Component
public class Print implements TaskHandler<Object> {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public Object handle (JobTask aTask) {
    log.info(aTask.getRequiredString("text"));
    return null; 
  }

}
``` 

# Pipelines

Pipeline definitions are located under the `pipelines/` directory on the root of the project.

# First time use

Prerequisites: JDK 8 and Maven 3

`mvn clean spring-boot:run` 

This will start piper on your local box, running fully in-memory and without relying on any external dependencies like database or a messaging middleware. 

## Start a Job 

Jobs can be started from the REST API: 

```
curl -s -X POST -H "Content-Type:application/json" -d '{"pipeline":"demo/hello"}' http://localhost:8080/job/start
```

Which will give you back something like: 

```
{
  "id": "8b896158ab6943db9b8e149e3311f3ed",
  "pipeline": "demo/hello:3517698",
  "creationDate": "2017-03-30T14:56:47-0700",
  "status": "STARTED",
  "execution": [
    {
      "name": "Print a greeting",
      "text": "hello world",
      "id": "c6866164ff39448aacb56e846b6736c5",
      "type": "print",
      "creationDate": "2017-03-30T14:56:47-0700",
      "status": "CREATED"
    }
  ],
  "completionDate": null,
  "startDate": "2017-03-30T14:56:47-0700",
  "failedDate": null
}

```

## Check Job Status

Use the Job ID, to check for it's status:

```
curl -s http://localhost:8080/job/8b896158ab6943db9b8e149e3311f3ed 
```

```
{
  "id": "8b896158ab6943db9b8e149e3311f3ed",
  "pipeline": "demo/hello:3517698",
  "creationDate": "2017-03-30T14:56:47-0700",
  "status": "COMPLETED",
  "execution": [
    {
      "name": "Print a greeting",
      "completionDate": 1490911007950,
      "text": "hello world",
      "id": "c6866164ff39448aacb56e846b6736c5",
      "type": "print",
      "creationDate": 1490911007933,
      "status": "COMPLETED"
    },
    {
      "name": "Sleep a little",
      "completionDate": 1490911008961,
      "id": "1918a7f46c214cb2a04f13c0aa23d96f",
      "type": "sleep",
      "millis": 1000,
      "creationDate": 1490911007956,
      "status": "COMPLETED"
    },
    {
      "name": "Print a farewell",
      "completionDate": 1490911008968,
      "text": "goodbye world",
      "id": "16c1ba49bd5f4851b81150884da79e3e",
      "type": "print",
      "creationDate": 1490911008965,
      "status": "COMPLETED"
    }
  ],
  "completionDate": "2017-03-30T14:56:48-0700",
  "startDate": "2017-03-30T14:56:47-0700",
  "failedDate": null
}

```

# Roadmap

- Job Input
- Expression Language
- JdbcJobRepository
- Stop/Resume Jobs
- For-Each support
- `TaskHandler` implemenations should be able to report back warnings and other metadata on their operation. not just the payload.
- Error handling

# Architecture

Piper is comprised of two central components: 

The `Coordinator`, responsible for the job execution, for doling out tasks to workers and for handling any errors that occur on job executions. 

and, 

The `Worker`, responsible for executing a single task outside of the context of a Job. i.e. `Worker` instances are meant to be "dumb", stateless processes who simply receive a task from the coordinator, execute it and reply to the Coordinator with the results or errors if any.

Since `Coordinator` and `Worker` do not talk to each other directly but only through a `Messenger`, they can easily run on seperate machines and talk to each other through some sort of middleware.  

# Licensing

Piper is licensed under the Apache License, Version 2.0. See [LICENSE](https://github.com/creactiviti/piper/blob/master/LICENSE) for the full license text.

