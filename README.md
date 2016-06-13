# PIPER

A distributed, fault-tolerant pipeline execution engine. 

# Why another one?

I've been looking around for some time now for a workflow engine for distributed processing. While there are several candidates, none seemed to be designed from the ground up for distributed processing on a per-task basis. 

# How it works? 

Piper works by executing a set of tasks defined as a YAML document. 

An hello world example might look like this: 

```
name: Hello World
tasks: 
  ffmpeg:
    text: hello world
  add:
    right: 1
    left: 2
```