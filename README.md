# MuleSoft Logging Project

## Summary
There are 3 flows in this project. 
The first flow is an example of logging bad practices. 
The second flow is an example of logging best practices, and also shows how log4j2 can be used to log instead of the standard MuleSoft Logger.
The third flow is also an example of logging best practices, and shows how a custom connector can be used to log instead of a the standard MuleSoft Logger.

## API Definition
Send a POST HTTP request to one of the following URLs and replace {pokemonName} with the name of your favorite pokemon such as pikachu or bulbasaur.
 - http://meetup-demo-project.us-e1.cloudhub.io/log/best-practice?name={pokemonName}
 - http://meetup-demo-project.us-e1.cloudhub.io/log/bad-practice?name={pokemonName}
 - http://meetup-demo-project.us-e1.cloudhub.io/log/custom?name={pokemonName}

## Logging to ELK
All of the flows in this project use log4j2 to send messages in JSON format to SQS which are consumed by Logstash, persisted to Elasticsearch, and brought to life in Kibana. Feel free to reach out to Big Compass or create an issue on this repository if you would like to see or get a demo of the ELK stack configuration.
Documentation on the ELK stack can be found here https://www.elastic.co/elk-stack

## log4j2 Appenders
### Log4J2CloudhubLogAppender
This Appender is used to log to MuleSoft CloudHub logs when logging on the application in CloudHub is disabled and is useful when logging to an external target in parallel. Check the following link for details. When deploying the application to CloudHub, you have to put in a ticket with MuleSoft to be able to disable an application's logs.
https://docs.mulesoft.com/runtime-manager/custom-log-appender

### SQS Appender
This Appender sends messages out to an AWS SQS queue. The way it is set up in this project, it sends JSON in the SQS message body to be consumed by Logstash in the ELK stack for logging and reporting purposes.
This Appender was originally built here: https://github.com/avioconsulting/log4j2-sqs-appender
The Java classes were taken from this repository and included in this application so another dependency did not have to be included for easier startup of the application.

### RollingFile
This Appender logs to a file as a backup.

# Mule runtime version
Mule 3.9.0

# Installation
1. Clone this GitHub repository:  https://github.com/BigCompass/mulesoft-meetup-logging-demo
2. Install the custom logger connector by cloning this GitHub repository: https://github.com/BigCompass/custom-logger-connector. You can either 
 - Download the source code and run `mvn clean install`, then insert this dependency in your pom.xml 
```
<dependency>
    <groupId>37513a5c-61c8-4566-8c30-c9d2a08150d8</groupId>
    <artifactId>logger-connector</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
``` 
 - Download the source code and build it with AnyPoint Studio devkit to find it available on your local repository. Then you can add it to Studio. Details here: https://docs.mulesoft.com/connector-devkit/3.9/creating-an-anypoint-connector-project
3. Once the custom logger connector is installed, open the log4j2.xml file in the src/main/resources folder of this project. If you want log4j2 to send messages to an AWS SQS queue, alter the below section of the configuration with your AWS access and secret key, and specify the queue's name. The other option to run this project if you do not have an AWS account or are not familiar with AWS SQS, is to comment out or delete this section of the configuration.
```		 
<SQS name="SQS" 
	 awsAccessKey="accessKeyHere"
	 awsRegion="us-east-1" 
	 awsSecretKey="secretKeyHere"
	 maxBatchOpenMs="10000" 
	 maxBatchSize="5" 
	 maxInflightOutboundBatches="5" 
	 queueName="yourQueueNameHere">
	<PatternLayout pattern="%m%n"/>
</SQS>
```
4. Start the project! When running the project locally, you will get this error `ERROR Error processing element Log4J2CloudhubLogAppender ([Appenders: null]): CLASS_NOT_FOUND`, but the application will still start up. This is because the application tries to start the log4j2 configuration before initializing all dependencies. You can comment out the following code in src/main/resources.log4j2.xml if running locally. The explanation of this Appender is detailed in the Depedencies section above.
```
<Log4J2CloudhubLogAppender name="CLOUDHUB"
	addressProvider="com.mulesoft.ch.logging.DefaultAggregatorAddressProvider"
	applicationContext="com.mulesoft.ch.logging.DefaultApplicationContext"
	appendRetryIntervalMs="${sys:logging.appendRetryInterval}"
	appendMaxAttempts="${sys:logging.appendMaxAttempts}"
	batchSendIntervalMs="${sys:logging.batchSendInterval}"
	batchMaxRecords="${sys:logging.batchMaxRecords}"
	memBufferMaxSize="${sys:logging.memBufferMaxSize}"
	journalMaxWriteBatchSize="${sys:logging.journalMaxBatchSize}"
	journalMaxFileSize="${sys:logging.journalMaxFileSize}"
	clientMaxPacketSize="${sys:logging.clientMaxPacketSize}"
	clientConnectTimeoutMs="${sys:logging.clientConnectTimeout}"
	clientSocketTimeoutMs="${sys:logging.clientSocketTimeout}"
	serverAddressPollIntervalMs="${sys:logging.serverAddressPollInterval}"
	serverHeartbeatSendIntervalMs="${sys:logging.serverHeartbeatSendIntervalMs}"
	statisticsPrintIntervalMs="${sys:logging.statisticsPrintIntervalMs}">
	<PatternLayout pattern="%d [%X{resourceName}=%X{resourceId}] [%t] %-5p %c - %m%n"/>
</Log4J2CloudhubLogAppender> 
```

# Reporting Issues
Feel free to report any issues or troubles here: https://github.com/BigCompass/mulesoft-meetup-logging-demo/issues
