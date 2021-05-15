# spring-boot-logging-input-request-output-response
example for logging the input request coming into controller and output json response going from controller.

some times we need to print the input request and output json response and time into the logs to identify the issues.

sample input: 	{
	              "name": "guru",
	               "companyName": "happiestMinds",
	               "age": 28
	               }
                 
                 
sample output:
2021-05-15 12:49:58.976  INFO 14432 --- [nio-8080-exec-4] e.s.jpa.filter.HttpLoggingFilter         : Request and response: id=4 info={
  "url" : "http://localhost:8080/create?null",
  "requestHeaders" : {
    "content-length" : "76",
    "postman-token" : "88da5aa0-771b-4776-8e42-0420dc64aadc",
    "host" : "localhost:8080",
    "content-type" : "application/json",
    "connection" : "keep-alive",
    "accept-encoding" : "gzip, deflate, br",
    "user-agent" : "PostmanRuntime/7.28.0",
    "accept" : "*/*"
  },
  "requestBody" : "\t{\r\n\t\"name\": \"guru\",\r\n\t  \"companyName\": \"happiestMinds\",\r\n\t  \"age\": 28\r\n\t  }",
  "responseHeaders" : { },
  "responseBody" : "{\"name\":\"guru\",\"companyName\":\"happiestMinds\",\"age\":\"28\"}"
}
