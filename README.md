# Getting Started

## context
The application maintains list of actions for customers to be acted upon specified times. this is just for demonstration of 
how to leverage spring boot data capabilities to connect to postgres, kafka and redis in an application development context. 


## basic design
Following are the key components:

**Action Listener**: A Kafka topic listener; expects such actions to be available from a topic, and persists in a postgres db  

**Actions Controller**: a REST end-point which returns actions for specified customer and date. 
For demonstration of retrieval of actions from a kafka topic, the controller has an endpoint which
only publishes to a kafka topic. 

**Action Archiver Job**: a scheduled task that runs at specified time and cleans up any action more than 24 hours old

**Redis**: is used for user session-token lookup.

## codebase
- Typical spring boot application, that connects with Kafka and has a REST end-point.
- Authentication is done on basis of http headers - `x-auth-user` and `x-auth-token`. Check `SecurityConfig.java`. 
The headers are validated in `CustomAuthenticationProvider.java` - does an equals check against hard-coded value `super-secret` etc.
They code checks redis for key specified by `x-auth-user` and matches against that passed against `x-auth-token` http headers. 
If key does not exist, then it falls back to simple hardcoded string comparison.
- Persists data in a postgres table `actions_for_customer` - check `ActionForCustomer.java`
- All configurations are externalized and expects them to be initialized. check - `application.properties`. 
The variables are passed through the docker-compose environment, you may pass SPRING_CONFIG_FILE or other means.


## pre-requisites
JDK 17+, Docker

### build
Edit the build.sh file to set JAVA_HOME. (I used jenv, check build.sh file). Also check the docker-compose file for ports and variables. 
 
1. build - it will create a jar and the example app docker image. 
> ./build.sh

or 

> sh build.sh

3. verify. should list the just built local image

> docker images | grep customer-actions

## run

1. The following instructions are all docker based deployments. 
> docker compose down -v

> docker compose up -d

> docker compose logs --follow



2. If you would like to test/debug the app outside the docker environment, you may comment off the `app` service definition in the `docker-compose.yml`
to run db, kafka and redis within docker. Then run the app from the host system

> docker compose down -v

> docker compose up -d

> docker compose up -d
 
and now run the app from host command line with an externalized properties. for example, action_example.properties
> java -jar target/actions-0.0.1-SNAPSHOT.jar --spring.config.location=file:./action_example.properties


## test
1. if you prefer calling HTTP api to create actions, you can do so by making a POST request to `/api/actions`.
The above end point only posts to Kafka topic, where from the listener receives and saves onto db.

<pre>
curl --request POST \
  --url http://localhost:9080/api/actions \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/10.0.0' \
  --header 'x-auth-token: super-secret' \
  --header 'x-auth-user: super-user' \
  --data '{
	"customerId" : "101",
	"title": "pay for milk",
	"actOn": "29-09-2024 10:15"
}'
</pre>

2. GET `/api/actions` with `customerId` and `forDate` to fetch list of actions
<pre>
curl --request GET \
  --url 'http://localhost:9080/api/actions?customerId=101&forDate=29-09-2024%2008%3A35' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/10.0.0' \
  --header 'x-auth-token: super-secret' \
  --header 'x-auth-user: super-user'
</pre>

3. If you want to check the authentication against redis, then set keys for the `x-auth-user` with the value `x-auth-token`
> docker compose exec -it redis /bin/sh
> redis-cli SET super-user super-secret

Then change the curl headers accordingly and test