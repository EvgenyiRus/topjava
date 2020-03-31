curl http://localhost:8080/topjava/rest/meals

curl http://localhost:8080/topjava/rest/meals/100002

curl -X POST -d '{"dateTime": "2020-01-01T20:10:01","description": "test dinner","calories": 502}' -H 'Content-Type: application/json' http://localhost:8080/topjava/rest/meals/

curl -X DELETE http://localhost:8080/topjava/rest/meals/100002

curl -X PUT -d '{"dateTime": "2020-01-01T20:10:11","description": "test dinner2","calories": 502}' -H 'Content-Type: application/json' -X PUT http://localhost:8080/topjava/rest/meals/100002

curl http://localhost:8080/topjava/rest/meals/filter?startDateTime=2020-01-30T10:00:00.000&endDateTime=2020-01-30T13:01:00.000

curl http://localhost:8080/topjava/rest/meals/filter2?startTime=10:00&endTime=13:01