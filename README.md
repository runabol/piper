# PIPER

A miniature workflow engine. 

# How it works? 

Piper works by executing a set of tasks defined as a YAML document. 

Example:

```
hello.yaml

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