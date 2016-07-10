# PIPER

A miniature workflow engine. 

# How it works? 

Piper works by executing a set of tasks defined as a YAML document. 

An hello world example might look like this: 

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