Qieam project

Prereq:
- create a progresql database named qieam
- create a table named users with the following columns:
	id : integer + primary key
	username : character varying 255
	email : character varying 255
	games : interger[]
	friends : integer[]
- create a table named games with the following columns:
	id : integer + primary key
	title : character varying 255
	cover : character varying 255

Create the qieam server docker image from the folder containing the dockerfile:
- docker build -t qieam .

Start the qieam server with docker:
- docker run -p 4567:4567 -e DB_PASS=<PASSWORD> -e DB_HOST=<HOST_IP> qieam

Test:
- curl -X GET http://192.168.99.100:4567/api/users