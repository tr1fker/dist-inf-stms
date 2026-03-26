# Stage 1: build WAR
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline -q

COPY src ./src
RUN mvn -B package -DskipTests -q

# Stage 2: GlassFish 7 runtime
FROM eclipse-temurin:17-jdk
ENV GLASSFISH_VERSION=7.0.25
ENV GLASSFISH_HOME=/opt/glassfish

# Install GlassFish 7
RUN apt-get update -y && apt-get install -y --no-install-recommends \
    curl unzip \
    && curl -sL -o /tmp/glassfish.zip \
    "https://download.eclipse.org/ee4j/glassfish/glassfish-${GLASSFISH_VERSION}.zip" \
    && unzip -q /tmp/glassfish.zip -d /opt \
    && mv /opt/glassfish7 ${GLASSFISH_HOME} \
    && rm /tmp/glassfish.zip \
    && apt-get purge -y curl unzip \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/*

# domain1 is already included in the GlassFish distribution

# Deploy WAR to autodeploy
COPY --from=build /app/target/mini-poll-app-*.war \
    ${GLASSFISH_HOME}/glassfish/domains/domain1/autodeploy/mini-poll-app.war

EXPOSE 8080 4848

CMD ["/opt/glassfish/bin/asadmin", "start-domain", "-v"]
