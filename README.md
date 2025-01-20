## Slot Game

### Prerequisites
- JDK 23


### Run Unit Tests from root folder

```
./gradlew clean test
```

### Build fat jar from root folder

```
./gradlew clean fatJar
```

The jar will be generated to folder

```
./build/libs/SlotGame-1.0.jar
```

Now you can execute the command:
```
java -jar SlotGame-1.0.jar --config {PATH_TO_CONFIG_JSON_FILE} --betting-amount 1000
```