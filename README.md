# PIPER

Piper is a miniature workflow engine.  

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

The central interface that is used to execute a task is the `TaskHandler`:

```
public interface TaskHandler<O> {

  O handle (JobTask aTask);
  
}
```

`TaskHandler`s are resolved according to the `handler` property of each task. Here is the `log` `TaskHandler` implementations seen on the pipeline above: 

```
@Component
public class Log implements TaskHandler<Object> {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public Object handle (JobTask aTask) {
    log.info(aTask.getString("text"));
    return null;
  }

}
``` 

 