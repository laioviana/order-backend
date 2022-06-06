# Peecho Backend

# Running the project

Before running the project make sure the MySQL database is running.
To start the DB go to folder `order-backend/db-start` and then execute the `docker compose up` command.

Also you need to run our local AWS/SQS. To do this use the flowing commands in order on the `order-backend` folder:

`docker-compose up -d`
`docker exec -it localstack sh`
`ws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name order-queue.fifo --attributes FifoQueue=true`

To run the project execute the `mvn spring-boot:run` command.
To run the tests execute the  `mvn test` command.


You can also access the swagger ui interface in [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

# API Documentation

## GET /order

*List all orders*

### Parameters

|Name|Type|Description|Default|
|---|---|---|---|
|page|integer|1-based index of the results page|1|
|size|integer|Number of search results per page|10|

### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|location not found|

## POST /order

*Creates a new order*

### Body Parameters

|Name|Type|Description|
|---|---|---|
|productType|string|product type of the order|
|description|string|description of the order|
|customer|Integer|customer that created the order|

### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|wrong input parameters|

## PUT /order/pay/{orderId}

*Process payment for the order*

### Path Parameters

|Name|Type|Description|
|---|---|---|
|orderId|long|id for the order that will be paid|


### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|wrong input parameters|

## PUT /order/cancel/{orderId}

* Cancel order*

### Path Parameters

|Name|Type|Description|
|---|---|---|
|orderId|long|id for the order that will be canceled|


### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|wrong input parameters|

## PUT /order/sendToQueue/{orderId}

*Send order to print queue*

### Path Parameters

|Name|Type|Description|
|---|---|---|
|orderId|long|id for the order that will be sent to queue|


### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|wrong input parameters|

## GET /customer

*List all customers*

### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|location not found|

## POST /customer

*Creates a new customer*

### Body Parameters

|Name|Type|Description|
|---|---|---|
|firstName|string|first name of the new customer |
|lastName|string|last name of the new customer|
|email|string|e-mail of the new customer|
|country|string|country of the new customer|
|zipCode|string|zip code of the new customer|
|city|string|city of the new customer|
|addressLine|string|address line name of the new customer|


### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|wrong input parameters|
