

### Requisites

> **Important:** this document assumes you use a Unix-like OS. If you are running Windows, then you have to replace the commands provided here for Windows-equivalent ones.

- Java 17.
- **(Optional)** Gradle. The project is bundled with a Gradle wrapper ready to use (the commands described by this guide use the bundled Gradle wrapper).
- Internet connection (to dowload dependencies).

### Passing custom data to process

##### Data file

Put your custom file under the  `src/main/resources` directory. Then, modify the variable named `fileName` in the `Main`
class to point to your file. See below the code you need to modify:

```java
// TODO: Put your file under src/main/resources and then modify this
// variable to hold your file's name.
final var fileName="main/resources/coding-test-data.txt";
```

##### N value

Open the `Main` class and modify the `n` variable to the value you want. See below the code you need to modify:

```java
// TODO: Modify this variable to hold the amount of names to be generated.
final var n=25;
```

### Compiling the project

1. Run the following command from a terminal window (make sure the PWD is the project root directory):

   ```bash
   ./gradlew build
   ```

2. **(Optional)** Run the unit tests by running:

   ```bash
   ./gradlew test
   ```

### Running the project

1. Run the following command:

   ```bash
   java -cp build/libs/people-name-program*.jar Main
   ```

### Output got with provided data file (_coding-test-data.txt_)

```
1. The names cardinality for full, last, and first names:
Full names: 49252
Last names: 468
First names: 3006

2. The most common last names are:
Barton: 143
Lang: 136
Ortiz: 135
Hilll: 134
Hills: 130
Terry: 129
Becker: 128
Johns: 128
Romaguera: 128
Batz: 127

3. The most common first names are:
Tara: 32
Keon: 31
Andreanne: 31
Stephania: 31
Kaycee: 30
Baron: 29
Heath: 29
Kayley: 29
Ulices: 29
Charlotte: 29

4. List of 25 Modified Names:
Graham, Scottie
Cartwright, Emanuel
Dickinson, Amy
Rogahn, Anita
Bahringer, Angelita
Stanton, Norma
Runolfsdottir, Colby
Koch, Geovanni
Ortiz, Jacquelyn
Fisher, Amalia
Hills, Cortney
McGlynn, Onie
Kunze, Tommie
McLaughlin, Coty
Tromp, Nathaniel
Lynch, Jade
Adams, Gertrude
Lang, Haleigh
Marvin, Kyla
Bradtke, Jayce
Hoppe, Katrina
Stoltenberg, Genesis
Tillman, Bert
Lehner, Leonie
Shanahan, Cullen
```
