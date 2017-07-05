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


# License

## Piper Community Edition
Piper Community is a fully featured product released under version 2.0 of the [Apache License][]. I chose Apache v2 because it means that Piper Community Edition can be used for free with your project: whether in the cloud or behind the firewall. 

## Piper Enterprise Edition
Piper Enterprise is designed for commercial deployments where scale and availability are important. 

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0