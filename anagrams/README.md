### Problem

Given a list of Strings, return a Set of Sets, each of which contains all strings which are anagrams of each other.

### Example

#### Input

```java
final var input = ["cats", "redraw", "tap", "dog", "pat", "acts", "drawer", "remote", "reward", "god"];
```

#### Output

```pseudocode
Output = Set {
    Set {"cats", "acts"},
    Set {"redraw", "drawer", "reward"},
    Set {"tap","pat"},
    Set {"dog","god"},
    Set {"remote"}
}
```

