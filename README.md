# Logback ElasticSearch Appender

The Logback ElasticSearch appender send log to ElasticSearch cluster directly without LogStash or FileBeat.

[中文说明](https://www.toutiao.com/i6592518127915893256/)

## User Guide

### Download

Download logback-elasticsearch-appender project and run mvn install to install the jar to local repository

### Dependency

Add the following dependency to your spring boot project:

```
<dependency>
    <groupId>net.xdevelop</groupId>
    <artifactId>logback-elasticsearch-appender</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Configure logback-spring.xml

The following is a sample. Please setup the ElasticSearch cluster name and nodes address within the ElasticAppender settings. Because the log appender will be initialized before spring boot configuration, so it can't get the ElasticSearch setting from Spring Boot context.

```
<?xml version="1.0" encoding="UTF-8"?>

<configuration>
    <include
        resource="org/springframework/boot/logging/logback/base.xml" />
    <appender name="STDOUT"
        class="ch.qos.logback.core.ConsoleAppender">
        <encoder
            class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="ELASTIC" class="net.xdevelop.logback.appender.ElasticAppender">
        <clusterName>docker-cluster</clusterName>
        <clusterNodes>192.168.1.21:9300</clusterNodes>
        <applicationName>elastic-log-sample</applicationName>
    </appender>

    <root level="INFO">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="ELASTIC" />
    </root>
    <logger name="net.xdevelop.elasticlogdemo" level="INFO" />
</configuration>
```

### Run Application

Run the Sprint Boot Application, then the log will send to ElasticSearch cluster.

### Configure Index in Kibana

Open Kibana &gt; Management &gt; Index Patterns &gt; Create index pattern with log-index, then you can view logs in Discover.



