# Smart Config Server

Smart config server aim as exposing any application configratio as key/value part. This server is built using:
1. Spring Cloud
2. MongoDB

This server can be great asset:
1. storing configuration of an application
2. storing data quality rules (in python,scala,groovy ...) for data pipline (spark)

## Pre-Requisite
Run MondoDB docker container

```bash
docker run -d -p 27017:27017 --name mongodb mongo

docker exec -it mongodb bash

mongo

show dbs



```

Add dependency in the project & Create a standard Spring Boot application, like this:
```java
@SpringBootApplication
@AutoConfigureSmartConfigServer
public class SomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

Configure the application's `spring.data.mongodb.*` properties in `application.yml`, as shown below:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://0.0.0.0/config-db
```

Add documents to the `config-db` mongo database, like this:
```javascript
use gonnect-dataengineering-configuration-db;

db.someapplication.insert({
  "label": "release-1.0",
  "profile": "prod",
  "source": {
    "gonnect": {
      "dbname": "somedb-name",
      "password": "somepassword",
      "dqrule": "python code"
    }
  }
});
```
The above code snippet configurs properties for an application named `someapplication` having profile `prod` and label `release-1.0`.

*NOTE*

label > release version
profile > prod or test

  
 
Finally, access these properties by invoking `http://0.0.0.0:8080/release-1.0/someapplication-prod.properties`. The response would be like this:
```json
gonnect.dbname: somedb-name
gonnect.password: somepassword
gonnect.dqrule: "python code"
```

# References
[spring-cloud-config](https://github.com/spring-cloud/spring-cloud-config)
