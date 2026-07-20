\# Lab Device Reservation API



\## 1. Project Overview



This project is a Spring Boot REST API for laboratory device reservation management.



It provides backend features for:



\- Managing lab devices

\- Querying devices with pagination

\- Creating reservations

\- Querying reservations with pagination

\- Preventing invalid reservation operations

\- Returning unified API responses



The project is built with Spring Boot, MyBatis, MySQL, Maven, and Lombok.



\## 2. Response Format



All APIs return a unified response structure.



Success response:



{

&#x20; "code": 200,

&#x20; "message": "success",

&#x20; "data": {}

}



Business error response:



{

&#x20; "code": 400,

&#x20; "message": "Device not found",

&#x20; "data": null

}



\## 3. Device APIs



\### 3.1 Search Devices



Request:



GET /devices



Query parameters:



| Name | Type | Required | Description |

|---|---|---|---|

| keyword | string | no | Search by device name or type |

| status | string | no | Filter by device status |

| page | integer | no | Page number, default 1 |

| size | integer | no | Page size, default 10 |



Example:



Invoke-RestMethod -Uri "http://localhost:8080/devices?page=1\&size=2"



Example response data:



{

&#x20; "items": \[

&#x20;   {

&#x20;     "id": 1,

&#x20;     "name": "Microscope",

&#x20;     "type": "Optical Equipment",

&#x20;     "status": "Available"

&#x20;   }

&#x20; ],

&#x20; "total": 4,

&#x20; "page": 1,

&#x20; "size": 2

}



\### 3.2 Get Device By ID



Request:



GET /devices/{id}



Example:



Invoke-RestMethod -Uri "http://localhost:8080/devices/1"



\### 3.3 Create Device



Request:



POST /devices



Request body:



{

&#x20; "name": "Camera",

&#x20; "type": "Imaging Equipment",

&#x20; "status": "Available"

}



Example:



Invoke-RestMethod -Uri "http://localhost:8080/devices" `

&#x20; -Method Post `

&#x20; -ContentType "application/json" `

&#x20; -Body '{"name":"Camera","type":"Imaging Equipment","status":"Available"}'



\### 3.4 Update Device



Request:



PUT /devices/{id}



Request body:



{

&#x20; "name": "Camera",

&#x20; "type": "Imaging Equipment",

&#x20; "status": "Maintenance"

}



Example:



Invoke-RestMethod -Uri "http://localhost:8080/devices/1" `

&#x20; -Method Put `

&#x20; -ContentType "application/json" `

&#x20; -Body '{"name":"Camera","type":"Imaging Equipment","status":"Maintenance"}'



\### 3.5 Delete Device



Request:



DELETE /devices/{id}



Example:



Invoke-RestMethod -Uri "http://localhost:8080/devices/1" -Method Delete



Rule:



A device cannot be deleted if it already has reservation records.



\## 4. Reservation APIs



\### 4.1 Search Reservations



Request:



GET /reservations



Query parameters:



| Name | Type | Required | Description |

|---|---|---|---|

| deviceId | integer | no | Filter by device id |

| date | string | no | Filter by reservation date, format yyyy-MM-dd |

| page | integer | no | Page number, default 1 |

| size | integer | no | Page size, default 10 |



Example:



Invoke-RestMethod -Uri "http://localhost:8080/reservations?page=1\&size=2"



Example with device id:



Invoke-RestMethod -Uri "http://localhost:8080/reservations?deviceId=1\&page=1\&size=2"



Example with date:



Invoke-RestMethod -Uri "http://localhost:8080/reservations?date=2026-07-20\&page=1\&size=2"



\### 4.2 Get Reservation By ID



Request:



GET /reservations/{id}



Example:



Invoke-RestMethod -Uri "http://localhost:8080/reservations/1"



\### 4.3 Create Reservation



Request:



POST /reservations



Request body:



{

&#x20; "deviceId": 1,

&#x20; "userName": "Tom",

&#x20; "reservationDate": "2026-07-20",

&#x20; "startTime": "09:00:00",

&#x20; "endTime": "11:00:00"

}



Example:



Invoke-RestMethod -Uri "http://localhost:8080/reservations" `

&#x20; -Method Post `

&#x20; -ContentType "application/json" `

&#x20; -Body '{"deviceId":1,"userName":"Tom","reservationDate":"2026-07-20","startTime":"09:00:00","endTime":"11:00:00"}'



Business rules:



\- Device must exist

\- Device status must be Available

\- Start time must be before end time

\- Reservation time cannot conflict with existing reservations



\### 4.4 Delete Reservation



Request:



DELETE /reservations/{id}



Example:



Invoke-RestMethod -Uri "http://localhost:8080/reservations/1" -Method Delete



\## 5. Main Business Rules



Device rules:



\- Device name cannot be empty

\- Device type cannot be empty

\- Device status must be one of: Available, Maintenance, Disabled

\- Device with existing reservations cannot be deleted



Reservation rules:



\- Device id is required

\- User name is required

\- Reservation date is required

\- Start time and end time are required

\- Start time must be before end time

\- Device must be available

\- Reservation time cannot overlap with existing reservation time



\## 6. Current Architecture



The project uses a layered backend architecture:



controller -> service -> mapper -> database



Layer responsibilities:



\- controller: receives HTTP requests and returns responses

\- service: handles business rules

\- mapper: executes database operations

\- entity: maps database tables

\- dto: defines request and response objects

\- common: defines unified response format

\- exception: handles business exceptions globally



\## 7. Tech Stack



\- Java

\- Spring Boot

\- Spring Web

\- MyBatis

\- MySQL

\- Maven

\- Lombok

\- Git

\- GitHub



\## 8. Database Tables



Device table:



| Field | Description |

|---|---|

| id | Device id |

| name | Device name |

| type | Device type |

| status | Device status |



Reservation table:



| Field | Description |

|---|---|

| id | Reservation id |

| device\_id | Related device id |

| user\_name | Reservation user |

| reservation\_date | Reservation date |

| start\_time | Start time |

| end\_time | End time |



\## 9. Current Project Stage



The current version has completed:



\- Basic Spring Boot project setup

\- Device CRUD APIs

\- Reservation APIs

\- MySQL database connection

\- MyBatis mapper layer

\- Service layer business validation

\- Unified response format

\- Global exception handling

\- Pagination query support

\- Basic API documentation



Next possible improvements:



\- Add Swagger / OpenAPI documentation

\- Add unit tests for service layer

\- Add integration tests for REST APIs

\- Add login and role-based permissions

\- Add frontend page for device reservation

