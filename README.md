### Follow me


```
# Add following mapping in hosts
127.0.0.1 eureka1
127.0.0.1 eureka2
127.0.0.1 eureka3

# build eureka-server and start the 1st instance
cd ./eureka-server
mvn clean package -Dmaven.test.skip=true
cd ./target
java -jar eureka-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=eureka1

# open a new Terminal session and start the 2nd instance
java -jar eureka-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=eureka2

# open a new Terminal session and start the 3rd instance
java -jar eureka-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=eureka3

# check Eureka status on http://eureka1:8001


# build user-service and start the 1st instance
cd ./user-service
mvn clean package -Dmaven.test.skip=true
cd ./target
java -jar user-service-0.0.1-SNAPSHOT.jar --server.port=8011

# open a new Terminal session and start the 2nd instance
java -jar user-service-0.0.1-SNAPSHOT.jar --server.port=8012


# build member-service and start it
cd ./member-service
mvn clean package -Dmaven.test.skip=true
cd ./target
java -jar member-service-0.0.1-SNAPSHOT.jar --server.port=8021

# check Eureka status on http://eureka1:8001

```

Till now, the Eureka cluster (3 replicas) has been set up, with service provider (2 instances) and service comsumer (1 instsance) registered on it.

Try to call this several times: http://localhost:8021/members, you will see Ribbon dispatches the client requests to different instances of service provider by turns.

