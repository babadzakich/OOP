javadoc -d build/docs/javadoc -sourcepath src/main/java -subpackages ru.nsu.chuvashov

javac src/main/java/ru/nsu/chuvashov/Main.java -d ./build

java -cp ./build ru.nsu.chuvashov.Main

