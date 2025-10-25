# 2025-CCE-E-DEV-022
It's a repository showcasing kata about a bookstore


## About Project

* __Java version: 21__
* __Spring Boot Version: 3.5.7__

This application has been written using TDD method.

# How to run this project

### Using IDE

1. open Java IDE of your choice
2. set JDK version 21
3. run the application (from BookstoreApplication class)

### Using Maven

1. Ensure that maven is installed on your pc 
```cmd
mvn -version 
```
if you get error, you can visit [this](https://www.baeldung.com/install-maven-on-windows-linux-mac) link

2. in your command line tool, navigate to /DevelopmentBooks
```cmd
cd DevelopmentBooks
```
3. build the maven project
```cmd
mvn clean install
```
4. run the app
```cmd
java -jar target/bookstore-0.0.1-SNAPSHOT.jar
```

### Further thoughts

Application is solving requirements described [here](https://stephane-genicot.github.io/DevelopmentBooks.html).

It could have been further developed to have exposed API, allowing users to fetch available products and 
post baskets in order to get their price.

