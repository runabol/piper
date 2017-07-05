# Introduction

Piper is a distributed workflow engine designed to be dead simple.

In Piper, work to be done is defined as a set of tasks called a Pipeline. Pipelines can be sourced from many locations but typically they live on a Git repository where they can be versioned and tracked.

# Tasks

Tasks are the basic building blocks of a pipeline. Each task has a `type` property which maps to a `TaskHandler` implementation, responsible for carrying out the task.

For example here's the `RandomInt` `TaskHandler` implementation:

```
  public class RandomInt implements TaskHandler<Object> {

    @Override
    public Object handle(Task aTask) throws Exception {
      int startInclusive = aTask.getInteger("startInclusive", 0);
      int endInclusive = aTask.getInteger("endInclusive", 100);
      return RandomUtils.nextInt(startInclusive, endInclusive);
    }
    
  }
```

While it doesn't do much beyond generating a random integer, it does  demonstrate how a `TaskHandler` works. a `Task` instance is passed as  an argument to 
the `TaskHandler` which contains all the Key-Value pairs of that task.

The `TaskHandler` is then responsible for executing the task using this input and optionally returning an output which can be used by other pipeline tasks downstream.


# Pipelines

Piper pipelines are authored in YAML, a JSON superset. 

Here is an example of a basic pipeline definition.

```
name: Hello Demo

inputs:                --+
  - name: yourName       |
    label: Your Name     | - This defines the inputs
    type: string         |   expected by the pipeline
    required: true     --+

tasks: 
  - name: randomNumber               --+
    label: Generate a random number    |
    type: randomInt                    | - This is a task
    startInclusive: 0                  |
    endInclusive: 10000              --+
                            
  - label: Print a greeting 
    type: print             
    text: Hello ${yourName} 
                           
  - label: Sleep a little
    type: sleep             --+
    millis: ${randomNumber}   | - tasks may refer to the result of a previous task
                            --+
  - label: Print a farewell
    type: print
    text: Goodbye ${yourName}
```


So tasks are nothing but a collection of key-value pairs. At a minimum each task contains a `type` property which maps to an appropriate `TaskHandler` that needs to execute it.

Tasks may also specify a `name` property which can be used to name the output of the task so it can be used later in the pipeline.

The `label` property is used to give a human-readble description for the task.

The `node` property can be used to route tasks to work queues other than the default `tasks` queue. This allows one to design a cluster of worker nodes of different types, of different capacity, different 3rd party software dependencies and so on.

The `retry` property can be used to specify the number of times that a task is allowed to automatically retry in case of a failure.

The `timeout` property can be used to specify the number of seconds/minutes/hours that a task may execute before it is cancelled.

The `output` property can be used to modify the output of the task in some fashion. e.g. convert it to an integer.

All other key-value pairs are task-specific and may or may not be required depending on the specific task.


# Architecture

Piper is composed of the following components: 

**Coordinator**: The Coordinator is the like the central nervous system of Piper. It keeps tracks of jobs, dishes out work to be done by Worker machines, keeps track of failures, retries and other job-level details. Unlike Worker nodes, it does not execute actual work but delegate all task activities to Worker instances. 

**Worker**: Workers are the work horses of Piper. These are the Piper nodes that actually execute tasks requested to be done by the Coordinator machine. Unlike the Coordinator, the workers are stateless, which by that is meant that they do not interact with a database or keep any state in memory about the job or anything else. This makes it very easy to scale up and down the number of workers in the system without fear of losing application state. 

**Message Broker**:  All communication between the Coordinator and the Worker nodes is done through a messaging broker. This has many advantages: a) if all workers are busy the message broker will simply queue the message until they can handle it. b) when workers boot up they subscribe to the appropriate queues for the type of work they are intended to handle c) if a worker crashes the task will automatically get re-queued to be handle by another worker. 

**Database**: This piece holds all the jobs state in the system, what tasks completed, failed etc. It is used by the Coordinator as its "mind". 

**Pipeline Repository**: The component where pipelines (workflows) are created, edited etc. by pipeline engineers.

# Control Flow

Piper support the following constructs to control the flow of execution:

## Each

Applies the function `iteratee` to each item in `list`, in parallel. Note, that since this function applies iteratee to each item in parallel, there is no guarantee that the `iteratee` functions will complete in order.


```
- type: each
  list: [1000,2000,3000]
  iteratee:
    type: sleep         
    millis: ${item} 
```

This will generate three parallel tasks, one for each items in the list, which will `sleep` for 1, 2 and 3 seconds respectively.

## Parallel

Run the `tasks` collection of functions in parallel, without waiting until the previous function has completed.

```
- type: parallel
  tasks: 
    - type: print
      millis: hello
        
    - type: print
      text: goodbye
```

## Fork/Join

Executes each branch in the `branches` as a seperate and isolated sub-flow. Branches are executed internally in sequence.

```
- type: fork
  branches: 
     - - name: randomNumber                 <-- branch 1 start here
         label: Generate a random number
         type: randomInt
         startInclusive: 0
         endInclusive: 5000
           
       - type: sleep
         millis: ${randomNumber}
           
     - - name: randomNumber                 <-- branch 2 start here
         label: Generate a random number
         type: randomInt
         startInclusive: 0
         endInclusive: 5000
           
       - type: sleep
         millis: ${randomNumber}      
```

## Switch

Executes one and only one branch of execution based on the `expression` value.

```
- type: switch
  expression: ${selector} <-- determines which case will be executed
  cases: 
     - key: hello                 <-- case 1 start here
       tasks: 
         - type: print
           text: hello world
     - key: bye                   <-- case 2 start here
       tasks: 
         - type: print
           text: goodbye world
```

## Map

Produces a new collection of values by mapping each value in `list` through the `iteratee` function. The `iteratee` is called with an item from `list` in parallel. When the `iteratee` is finished executing on all items the `map` task will return a list of execution results in an order which corresponds to the order of the source `list`.

```
- name: fileSizes 
  type: map
  list: ["/path/to/file1.txt","/path/to/file2.txt","/path/to/file3.txt"]
  iteratee:
    type: filesize         
    file: ${item}
``` 

# Getting started

Build the sources:

```
./scripts/build.sh
```

Start Piper in memory without any dependencies:

```
./scripts/all.sh
```

Go to the browser at [http://localhost:8080/jobs][]


# License

## Piper Community Edition
Piper Community is a fully featured product released under version 2.0 of the [Apache License][]. I chose Apache v2 because it means that Piper Community Edition can be used for free with your project: whether in the cloud or behind the firewall. 

## Piper Enterprise Edition
Piper Enterprise is designed for commercial deployments where scale and availability are important. 

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0