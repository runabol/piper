# Introduction

Piper is a distributed workflow engine designed to be dead simple.

In Piper, work to be done is defined as a set of tasks called a Pipeline. Pipelines can be sourced from many locations but typically they live on a Git repository where they can be versioned and tracked.

# Tasks

Tasks are the basic building blocks of a pipeline. Each task has a type property which maps to a TaskHandler implementation which is responsible for carrying out the task.

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



# License

## Piper Community Edition
Piper Community is a fully featured product released under version 2.0 of the [Apache License][]. I chose Apache v2 because it means that Piper Community Edition can be used for free with your project: whether in the cloud or behind the firewall. 

## Piper Enterprise Edition
Piper Enterprise is designed for commercial deployments where scale and availability are important. 

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0