# Dropwizard Elide Example
This example demonstrates how to use the dropwizard-elide bundle.

## Usage

1. Install and start a MySQL server

2. Create ```mkp``` database

        mysql> create database mkp;

3. Create ```mkp``` user with password ```mkp123```

        mysql> grant all on mkp.* to 'mkp'@'localhost' identified by 'mkp123';

4. Launch the mkp webservice

        mvn install
        
        run the main method with 2 arguments: ```server example.yml``` 
        

   The output of the last command will have a line at the very bottom like this

        INFO  [2016-05-07 21:25:09,453] org.eclipse.jetty.server.ServerConnector: Started application@***{HTTP/1.1}{0.0.0.0:{port}}

   The application starts listening at port {port}. This port number is specified in [configuration file](https://github.com/yahoo/elide/blob/master/elide-example/dropwizard-elide-example/example.yml). Let's take {port}=4080 for an example.

5. Create an seller

        $ curl -H'Content-Type: application/vnd.api+json' -H'Accept: application/vnd.api+json' --data '{
          "data": {
            "id": "-",
            "type": "seller",
            "attributes": {
                "name": "seller 1",
                "address": "seller 1 address",
                "description": "seller 1 descr"
            }
          }
        }' -X POST http://localhost:4080/seller

        
6. Create a project

        $ curl -H'Content-Type: application/vnd.api+json' -H'Accept: application/vnd.api+json' --data '{
          "data": {
            "id": "-",
            "type": "project",
            "attributes": {
                "name": "project 1",
                "description": "project 1 descr",
                "deadline" : 1507420800000
            }
          }
        }' -X POST http://localhost:4080/project
        
7. Associate the seller and project

        $ curl -H'Content-Type: application/vnd.api+json' -H'Accept: application/vnd.api+json' --data '{
          "data": {
            "id": "1",
            "type": "project"
          }
        }' -X PATCH http://localhost:4080/seller/1/relationships/projects

 
8. Create a buyer

        $ curl -H'Content-Type: application/vnd.api+json' -H'Accept: application/vnd.api+json' --data '{
          "data": {
            "id": "-",
            "type": "buyer",
            "attributes": {
                "name": "buyer 1",
                "address": "buyer 1 address",
                "description": "buyer 1 descr"
            }
          }
        }' -X POST http://localhost:4080/buyer

9. Create a bid

        $ curl -H'Content-Type: application/vnd.api+json' -H'Accept: application/vnd.api+json' --data '{
          "data": {
            "id": "-",
            "type": "bid",
            "attributes": {
                "totalQuote" : 1000
            }
          }
        }' -X POST http://localhost:4080/bid

10. Associate the buyer and bid

        $ curl -H'Content-Type: application/vnd.api+json' -H'Accept: application/vnd.api+json' --data '{
          "data": {
            "id": "1",
            "type": "bid"
          }
        }' -X PATCH http://localhost:4080/buyer/1/relationships/bids

11. Associate the project and bid

        $ curl -H'Content-Type: application/vnd.api+json' -H'Accept: application/vnd.api+json' --data '{
          "data": {
            "id": "1",
            "type": "bid"
          }
        }' -X PATCH http://localhost:4080/project/1/relationships/bids

8. Get projects

        $ curl -H'Content-Type: application/vnd.api+json' -H'Accept: application/vnd.api+json' http://localhost:4080/project/

You can also load some pre-configured authors and books using `load_mkp.sh` in `/scripts`

