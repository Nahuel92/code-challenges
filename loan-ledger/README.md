### Requisites

> **Important:** this document assumes you use a Unix-like OS. If you are running Windows, then you have to replace the commands provided here for Windows-equivalent ones.

#### Docker approach
- Docker up and running.
- Internet connection (to download dependencies).

#### Manual approach
- Java 17.
- **(Optional)** Gradle. The project is bundled with a Gradle wrapper ready to use (the commands described by this guide
  use the bundled Gradle wrapper).
- Internet connection (to download dependencies).

### Available CLI Commands

**Built-In Commands**

| Command        | Description                                            |
| -------------- | ------------------------------------------------------ |
| **clear**      | Clear the shell screen                                 |
| **exit, quit** | Exit the shell                                         |
| **help**       | Display help about available commands                  |
| **history**    | Display or save the history of previously run commands |
| **script**     | Read and execute commands from a file                  |

**Shell CLI**

| Command       | Description                                            |
| ------------- | ------------------------------------------------------ |
| **balances**  | Get loan balances                                      |
| **create-db** | Initialize the SQLite database                         |
| **drop-db**   | Delete the SQLite database                             |
| **load**      | Load a CSV file that contains advance and payment events |

### Compiling and Running the Project

#### Docker approach

1. Run the following command from a terminal window (make sure the PWD is the project root directory):

   ```bash
   docker build -t org/nahuelrodriguez-loanledger .
   ```

2. Run the following command:

```bash
docker run --rm -it org/nahuelrodriguez-loanledger
```

> **Important:** If you want to add new test data files for later testing, put them under the `src/test/resources/tests` directory before building the Docker image.

#### Manual approach #1

1. Run the following command from a terminal window (make sure the PWD is the project root directory):

   ```bash
   ./gradlew clean build
   ```

2. **(Optional)** Run the unit tests by running:

   ```bash
   ./gradlew test
   ```

3. Run the following command:

   ```bash
   java -jar build/libs/JAR_NAME.jar
   ```

   Where `JAR_NAME` is the name of the Jar file (it should be `loan-ledger-0.0.1-SNAPSHOT.jar` but do check the Jar
   name because it may be different in your case).

#### Manual approach #2

1. Run the following command from a terminal window (make sure the PWD is the project root directory):

   ```bash
   ./gradlew clean bootJar
   ```

2. **(Optional)** Run the unit tests by running:

   ```bash
   ./gradlew test
   ```

3. Run the following command:

```bash
java -jar build/libs/*.jar
```

### Test data files

#### Docker approach

You will find the test files under the `test-files` directory inside the container (including the ones you may have added when building the Docker image). For example, to load the `test1.csv` file run:

```bash
load test-files/test1.csv
```

### Manual approach

You can find the test data files under the `src/test/resources/tests` directory.

