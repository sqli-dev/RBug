## Old unfinished public project, finished privately

# RBug

An advanced tool which uses ByteBuddy library to manipulate methods at runtime

# RLang

Rlang is very simple scripting language made for RBug exclusively<br>
RLang variables are not initialized as fields but as ParsableObjects stored in Parser's cache.

```java
public abstract class ParsableObject {

    public Object object;
    public String varName;
    public String value;

    public ParsableObject(String varName, String value) {
        this.varName = varName;
        this.value = value;
    }

    public Object parsedValue() {
        return object;
    }
}

```
## Manipulation example. RBug VS Decompilation
### Live number return manipulation:

```
//You must place ; on end of every code line or : on end of value checking
//Optional: on script start type *VALUE to determine how many times script will be fired
//Value must be above 0
//For infinite calls dont use fire;

*1;

eq mname getMoney:

//Explained:
//mark - use on start to set variable
//value - variable name
//asnum - set variable as number
//999999 - value of number variable called 'value'
//push - equavilent to java's return
//^ - getter for variables

mark value asnum 999999;
push ^value;
```

### Original:

```java
public static double getMoney(UUID uuid) {
    Player player = Game.byUUID(uuid);
    return player.assets("money");
}
```

### Edited:

```java
public static double getMoney(UUID uuid) {
    Player player = Game.byUUID(uuid);
    return 999999;
}
```

# Limitations

### Descripting:

*Safe descripting must contain ; on end of codeline*

Bad descript:
```
mark value asnum 9999999 //set 'value' to 9999999
```

Correct descript:
```
mark value asnum 9999999; //set 'value' to 9999999
```

### Unsafe notations:

*Safe notation should contain defined method or classname otherwise use safecast*
```
eq mname * in *:

mark value asnum 9999999;
push ^value safecast;
```
