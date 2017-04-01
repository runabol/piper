# What is this?


Piper is a miniature workflow engine written in Java and built atop Spring Boot.

# How it works? 

Piper works by executing a set of tasks defined as a YAML document. 

Example:

`pipelines/demo/hello.yaml`

```
name: Hello World

input:
  - name: name
    label: Your Name
    type: string
    required: true
    
tasks:
  - name: randomNumber
    label: Generate a random number
    type: randomInt
    startInclusive: 0
    endInclusive: 1000
      
  - label: Print a greeting
    type: print
    text: Hello ${name}
   
  - label: Sleep a little
    type: sleep
    millis: ${randomNumber}
    
  - label: Print a farewell
    type: print
    text: Goodbye ${name}
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
curl -s -X POST -H "Content-Type:application/json" -d '{"pipeline":"demo/hello","name":"Arik"}' http://localhost:8080/jobs/start
```

Which will give you back something like: 

```
{
  "id": "3b019138d4474948bc3de23cdfb4f67c",
  "pipeline": "demo/hello:0e732fb",
  "creationDate": "2017-03-31T17:28:06-0700",
  "status": "STARTED",
  "execution": [
    {
      "startInclusive": 0,
      "name": "randomNumber",
      "endInclusive": 1000,
      "label": "Generate a random number",
      "id": "834d026a4a6d48e98774fcd3b2e5cf5f",
      "type": "randomInt",
      "creationDate": "2017-03-31T17:28:06-0700",
      "status": "CREATED"
    }
  ],
  "completionDate": null,
  "startDate": "2017-03-31T17:28:06-0700",
  "failedDate": null
}

```

## Check Job Status

Use the Job ID, to check for it's status:

```
curl -s http://localhost:8080/jobs/3b019138d4474948bc3de23cdfb4f67c 
```

```
{
  "id": "3b019138d4474948bc3de23cdfb4f67c",
  "pipeline": "demo/hello:0e732fb",
  "creationDate": "2017-03-31T17:28:06-0700",
  "status": "COMPLETED",
  "execution": [
    {
      "output": 507,
      "startInclusive": 0,
      "name": "randomNumber",
      "endInclusive": 1000,
      "completionDate": 1491006486618,
      "label": "Generate a random number",
      "id": "834d026a4a6d48e98774fcd3b2e5cf5f",
      "type": "randomInt",
      "creationDate": 1491006486610,
      "status": "COMPLETED"
    },
    {
      "completionDate": 1491006487551,
      "label": "Print a greeting",
      "text": "Hello Arik",
      "id": "843fda642f93401b8b8f763e26c50e3c",
      "type": "print",
      "creationDate": 1491006487547,
      "status": "COMPLETED"
    },
    {
      "completionDate": 1491006488066,
      "label": "Sleep a little",
      "id": "cc9ece2871e8488293f39d74512dd133",
      "type": "sleep",
      "millis": 507,
      "creationDate": 1491006487554,
      "status": "COMPLETED"
    },
    {
      "completionDate": 1491006488078,
      "label": "Print a farewell",
      "text": "Goodbye Arik",
      "id": "1d055d8eb1454f97b2f7e5be166bea1a",
      "type": "print",
      "creationDate": 1491006488074,
      "status": "COMPLETED"
    }
  ],
  "completionDate": "2017-03-31T17:28:08-0700",
  "startDate": "2017-03-31T17:28:06-0700",
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

