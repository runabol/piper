# What is this?


Piper is a miniature workflow engine written in Java and built atop Spring Boot.

# How it works? 

Piper works by executing a set of tasks defined as a YAML document. 

Example:

```
name: Hello Demo

input:
  - name: yourName
    label: Your Name
    type: string
    required: true
    
tasks:
  - name: randomNumber
    label: Generate a random number
    type: randomInt
    startInclusive: 0
    endInclusive: 10000
      
  - label: Print a greeting
    type: print
    text: Hello ${yourName}
   
  - label: Sleep a little
    type: sleep
    millis: ${randomNumber}
    
  - label: Print a farewell
    type: print
    text: Goodbye ${yourName}
    
```

# First time use

Prerequisites: JDK 8 and Maven 3

`mvn clean spring-boot:run` 

This will start piper on your local box, running fully in-memory and without relying on any external dependencies like database or a messaging middleware. 

## Start a Job 

Jobs can be started from the REST API: 

```
curl -s -X POST -H "Content-Type:application/json" -d '{"pipeline":"demo/hello","yourName":"Arik"}' http://localhost:8080/jobs/start
```

Which will give you back something like: 

```
{
  "name": "Hello Demo",
  "currentTask": 0,
  "id": "396ab4b863244dd5bf376813d94f9932",
  "creationDate": "2017-04-03T06:01:51+0000",
  "startDate": "2017-04-03T06:01:51+0000",
  "pipelineId": "demo/hello:7b911fe",
  "status": "STARTED"
}
```

## Check Job Status

Use the Job ID, to check for it's status:

```
curl -s http://localhost:8080/jobs/396ab4b863244dd5bf376813d94f9932 
```

```
{
  "execution": [
    {
      "startInclusive": 0,
      "label": "Generate a random number",
      "type": "randomInt",
      "creationDate": "2017-04-03T06:01:51+0000",
      "output": 8495,
      "jobId": "396ab4b863244dd5bf376813d94f9932",
      "name": "randomNumber",
      "endInclusive": 10000,
      "completionDate": "2017-04-03T06:01:51+0000",
      "taskNumber": 0,
      "id": "fbd8bdaa63b54ffeb4c928ceb04de48a",
      "status": "COMPLETED"
    },
    {
      "jobId": "396ab4b863244dd5bf376813d94f9932",
      "completionDate": "2017-04-03T06:01:52+0000",
      "label": "Print a greeting",
      "text": "Hello Arik",
      "taskNumber": 1,
      "id": "fa8342b300a04a09ad77e73066886628",
      "type": "print",
      "creationDate": "2017-04-03T06:01:52+0000",
      "status": "COMPLETED"
    },
    {
      "jobId": "396ab4b863244dd5bf376813d94f9932",
      "completionDate": "2017-04-03T06:02:01+0000",
      "label": "Sleep a little",
      "taskNumber": 2,
      "id": "bade523960d142f492c78465eba1ff02",
      "type": "sleep",
      "millis": 8495,
      "creationDate": "2017-04-03T06:01:52+0000",
      "status": "COMPLETED"
    },
    {
      "jobId": "396ab4b863244dd5bf376813d94f9932",
      "completionDate": "2017-04-03T06:02:01+0000",
      "label": "Print a farewell",
      "text": "Goodbye Arik",
      "taskNumber": 3,
      "id": "9d2d3b68b6074c34babea0c3077a78cc",
      "type": "print",
      "creationDate": "2017-04-03T06:02:01+0000",
      "status": "COMPLETED"
    }
  ],
  "name": "Hello Demo",
  "currentTask": 3,
  "completionDate": "2017-04-03T06:02:01+0000",
  "id": "396ab4b863244dd5bf376813d94f9932",
  "creationDate": "2017-04-03T06:01:51+0000",
  "startDate": "2017-04-03T06:01:51+0000",
  "pipelineId": "demo/hello:7b911fe",
  "status": "COMPLETED"
}

```

# Architecture

Piper is comprised of two central components: 

The `Coordinator`, responsible for the job execution, for doling out tasks to workers and for handling any errors that occur on job executions. 

and, 

The `Worker`, responsible for executing a single task outside of the context of a Job. i.e. `Worker` instances are meant to be "dumb", stateless processes who simply receive a task from the coordinator, execute it and reply to the Coordinator with the results or errors if any.

Since `Coordinator` and `Worker` do not talk to each other directly but only through a `Messenger`, they can easily run on seperate machines and talk to each other through some sort of middleware.  

# Roadmap

- Job Restarts
- Postgres Support
- Standalone worker
- Standalone Coordinator
- Stop/Resume Jobs
- For-Each support
- AMQP Support
- Job tags
- Define job output (e.g. `output: ${int(output)}`)
