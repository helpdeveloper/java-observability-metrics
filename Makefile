runargs = -d --build 

build:
	./mvnw clean install -f java-quarkus-metrics/pom.xml
	./mvnw clean install -f java-spring-metrics/pom.xml

install:
	docker build -f java-quarkus-metrics/src/main/docker/Dockerfile.jvm --build-arg TARGET=java-quarkus-metrics/target/ -t helpdev/java-quarkus-metrics .
	docker build -f java-spring-metrics/src/main/docker/Dockerfile.jvm --build-arg JAR_FILE=java-spring-metrics/target/*.jar -t helpdev/java-spring-metrics .

run-stack:
	docker-compose -f docker/docker-compose-stack.yml up $(runargs)

run-apps:
	docker-compose -f docker/docker-compose-apps.yml up $(runargs)

run-all: build install run-stack run-apps

stop-stack:
	docker-compose -f docker/docker-compose-stack.yml down

stop-apps:
	docker-compose -f docker/docker-compose-apps.yml down

stop-all: stop-stack stop-apps