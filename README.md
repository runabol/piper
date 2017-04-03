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
  "id": "bf248e568990435e8e62876b8dc47d81",
  "creationDate": "2017-04-03T05:40:18+0000",
  "startDate": "2017-04-03T05:40:18+0000",
  "pipelineId": "demo/hello",
  "currentStep": 0,
  "status": "STARTED"
}
```

## Check Job Status

Use the Job ID, to check for it's status:

```
curl -s http://localhost:8080/jobs/bf248e568990435e8e62876b8dc47d81 
```

```
{
  "id": "bf248e568990435e8e62876b8dc47d81",
  "name": "Hello Demo",
  "pipelineId": "demo/hello",
  "currentStep": 3,
  "status": "COMPLETED",
  "completionDate": "2017-04-03T05:40:25+0000",
  "creationDate": "2017-04-03T05:40:18+0000",
  "startDate": "2017-04-03T05:40:18+0000",
  "execution": [
    {
      "output": 6802,
      "jobId": "bf248e568990435e8e62876b8dc47d81",
      "startInclusive": 0,
      "name": "randomNumber",
      "endInclusive": 10000,
      "completionDate": "2017-04-03T05:40:18+0000",
      "label": "Generate a random number",
      "id": "22bbd37330e3401ba9b31d22828e7cfb",
      "type": "randomInt",
      "creationDate": "2017-04-03T05:40:18+0000",
      "status": "COMPLETED"
    },
    {
      "jobId": "bf248e568990435e8e62876b8dc47d81",
      "completionDate": "2017-04-03T05:40:18+0000",
      "label": "Print a greeting",
      "text": "Hello Arik",
      "id": "f7c9b92dd3e349928b1a5fde8e4a3628",
      "type": "print",
      "creationDate": "2017-04-03T05:40:18+0000",
      "status": "COMPLETED"
    },
    {
      "jobId": "bf248e568990435e8e62876b8dc47d81",
      "completionDate": "2017-04-03T05:40:25+0000",
      "label": "Sleep a little",
      "id": "3400e0de67d34ae6b9eab3081b1fdd8a",
      "type": "sleep",
      "millis": 6802,
      "creationDate": "2017-04-03T05:40:18+0000",
      "status": "COMPLETED"
    },
    {
      "jobId": "bf248e568990435e8e62876b8dc47d81",
      "completionDate": "2017-04-03T05:40:25+0000",
      "label": "Print a farewell",
      "text": "Goodbye Arik",
      "id": "1f504b0bb286428a933a0f0d8b0f8f27",
      "type": "print",
      "creationDate": "2017-04-03T05:40:25+0000",
      "status": "COMPLETED"
    }
  ]
}


```

# Architecture

Piper is comprised of two central components: 

The `Coordinator`, responsible for the job execution, for doling out tasks to workers and for handling any errors that occur on job executions. 

and, 

The `Worker`, responsible for executing a single task outside of the context of a Job. i.e. `Worker` instances are meant to be "dumb", stateless processes who simply receive a task from the coordinator, execute it and reply to the Coordinator with the results or errors if any.

Since `Coordinator` and `Worker` do not talk to each other directly but only through a `Messenger`, they can easily run on seperate machines and talk to each other through some sort of middleware.  

# Roadmap

- JdbcJobRepository
- Stop/Resume Jobs
- For-Each support
- AMQP Support
- Job tags
- Define job output
