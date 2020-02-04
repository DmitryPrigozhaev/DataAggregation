# DataAggregation

Service for receiving and aggregating data from an external system.

## Description

The service [downloads](http://www.mocky.io/v2/5c51b9dd3400003252129fb5) a list of available cameras from 
an external service. The list is represented by objects, each of which contains a link for receiving data on a 
specific camera. Example:

```json
[
  {
    "id": 1,
    "sourceDataUrl": "http://www.mocky.io/v2/5c51b230340000094f129f5d",
    "tokenDataUrl": "http://www.mocky.io/v2/5c51b5b6340000554e129f7b?mocky-delay=1s"
  },
  {
    "id": 20,
    "sourceDataUrl": "http://www.mocky.io/v2/5c51b2e6340000a24a129f5f?mocky-delay=100ms",
    "tokenDataUrl": "http://www.mocky.io/v2/5c51b5ed340000554e129f7e"
  },
  {
    "id": 3,
    "sourceDataUrl": "http://www.mocky.io/v2/5c51b4b1340000074f129f6c",
    "tokenDataUrl": "http://www.mocky.io/v2/5c51b600340000514f129f7f?mocky-delay=2s"
  },
  {
    "id": 2,
    "sourceDataUrl": "http://www.mocky.io/v2/5c51b5023400002f4f129f70",
    "tokenDataUrl": "http://www.mocky.io/v2/5c51b623340000404f129f82"
  }
]
```

Data for each camera is collected and aggregated in a separate object, after which the list 
of aggregated data is available through API ***/getAggregatedData*** (using the GET method).
Multithreading is used to aggregate data, so the program does not idle while waiting for a 
response from the network.

Response example:

```json
[
  {
    "id": 2,
    "urlType": "LIVE",
    "videoUrl": "rtsp://127.0.0.1/20",
    "value": "fa4b5f64-249b-11e9-ab14-d663bd873d93",
    "ttl": 180
  },
  {
    "id": 20,
    "urlType": "ARCHIVE",
    "videoUrl": "rtsp://127.0.0.1/2",
    "value": "fa4b5b22-249b-11e9-ab14-d663bd873d93",
    "ttl": 60
  },
  {
    "id": 1,
    "urlType": "LIVE",
    "videoUrl": "rtsp://127.0.0.1/1",
    "value": "fa4b588e-249b-11e9-ab14-d663bd873d93",
    "ttl": 120
  },
  {
    "id": 3,
    "urlType": "ARCHIVE",
    "videoUrl": "rtsp://127.0.0.1/3",
    "value": "fa4b5d52-249b-11e9-ab14-d663bd873d93",
    "ttl": 120
  }
]
```

The program carries out a full cycle (loading, processing and returning aggregated data) 
through a call of the presented API.

If the data request for an individual camera is performed for more than 10 seconds (customizable), 
the data are considered invalid and are not taken into account as a result.

## Used technologies

* Java 8
* Spring Boot 2.2.4.RELEASE
* Lombok 1.18.10
* JUnit 4.13
* Mockito 3.2.4

