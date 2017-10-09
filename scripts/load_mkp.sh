#!/bin/bash

# Loads the locally running Elide instance with some buyers and sellers

curl -H'Content-Type: application/vnd.api+json; ext=jsonpatch' \
     -H'Accept: application/vnd.api+json; ext=jsonpatch' \
      -d '
     [
        {
            "op": "add",
            "path": "/seller",
            "value": {
                "id": 1,
                "type": "seller",
                "attributes": {
                    "name": "seller 1",
                    "address": "seller 1 address",
                    "description": "seller 1 descr"
                },
                "relationships": {
                    "projects": {
                        "data": [
                            {
                                "type": "project",
                                "id": 1
                            },
                            {
                                "type": "project",
                                "id": 2
                            }
                        ]
                    }
                }
            }
        },
        {
            "op": "add",
            "path": "/project",
            "value": {
                "type": "project",
                "id": 1,
                "attributes": {
                    "name": "project 1",
                    "description": "project 1 descr",
                    "deadline" : 1507570707
                }
            }
        },
        {
             "op": "add",
             "path": "/project",
             "value": {
                 "type": "project",
                 "id": 2,
                 "attributes": {
                     "name": "project 2",
                     "description": "project 2 descr",
                     "deadline" : 1508331413
                 }
             }
        }
     ]
     ' -X PATCH http://localhost:4080/


curl -H'Content-Type: application/vnd.api+json; ext=jsonpatch' \
     -H'Accept: application/vnd.api+json; ext=jsonpatch' \
      -d '
     [
        {
            "op": "add",
            "path": "/seller",
            "value": {
                "id": 2,
                "type": "seller",
                "attributes": {
                    "name": "seller 2",
                    "address": "seller 2 address",
                    "description": "seller 2 descr"
                },
                "relationships": {
                    "projects": {
                        "data": [
                            {
                                "type": "project",
                                "id": 3
                            },
                            {
                                "type": "project",
                                "id": 4
                            }
                        ]
                    }
                }
            }
        },
        {
            "op": "add",
            "path": "/project",
            "value": {
                "type": "project",
                "id": 3,
                "attributes": {
                    "name": "project 3",
                    "description": "project 3 descr",
                    "deadline" : 1508360213
                }
            }
        },
        {
             "op": "add",
             "path": "/project",
             "value": {
                 "type": "project",
                 "id": 4,
                 "attributes": {
                     "name": "project 4",
                     "description": "project 4 descr",
                     "deadline" : 1508367413
                 }
             }
        }
     ]
     ' -X PATCH http://localhost:4080/


curl -H'Content-Type: application/vnd.api+json; ext=jsonpatch' \
     -H'Accept: application/vnd.api+json; ext=jsonpatch' \
      -d '
     [
        {
            "op": "add",
            "path": "/buyer",
            "value": {
                "id": 1,
                "type": "buyer",
                "attributes": {
                    "name": "buyer 1",
                    "address": "buyer 1 address",
                    "description": "buyer 1 descr"
                },
                "relationships": {
                    "bids": {
                        "data": [
                            {
                                "type": "bid",
                                "id": 1
                            },
                            {
                                "type": "bid",
                                "id": 2
                            }
                        ]
                    }
                }
            }
        },
        {
            "op": "add",
            "path": "/bid",
            "value": {
                "type": "bid",
                "id": 1,
                "attributes": {
                    "totalQuote" : 1000
                },
                "relationships": {
                    "project": {
                        "data": {
                            "type": "project",
                            "id": 1
                        }
                    }
                }
            }
        },
        {
             "op": "add",
             "path": "/bid",
             "value": {
                 "type": "bid",
                 "id": 2,
                 "attributes": {
                     "hourlyQuote" : 10,
                     "noOfHrs" : 10
                 },
                 "relationships": {
                     "project": {
                         "data": {
                             "type": "project",
                             "id": 2
                         }
                     }
                 }
             }
        }
     ]
     ' -X PATCH http://localhost:4080/

curl -H'Content-Type: application/vnd.api+json; ext=jsonpatch' \
     -H'Accept: application/vnd.api+json; ext=jsonpatch' \
      -d '
     [
        {
            "op": "add",
            "path": "/buyer",
            "value": {
                "id": 2,
                "type": "buyer",
                "attributes": {
                    "name": "buyer 2",
                    "address": "buyer 2 address",
                    "description": "buyer 2 descr"
                },
                "relationships": {
                    "bids": {
                        "data": [
                            {
                                "type": "bid",
                                "id": 3
                            },
                            {
                                "type": "bid",
                                "id": 4
                            }
                        ]
                    }
                }
            }
        },
        {
            "op": "add",
            "path": "/bid",
            "value": {
                "type": "bid",
                "id": 3,
                "attributes": {
                    "totalQuote" : 1000
                },
                "relationships": {
                    "project": {
                        "data": {
                            "type": "project",
                            "id": 1
                        }
                    }
                }
            }
        },
        {
             "op": "add",
             "path": "/bid",
             "value": {
                 "type": "bid",
                 "id": 4,
                 "attributes": {
                     "hourlyQuote" : 10,
                     "noOfHrs" : 10
                 },
                 "relationships": {
                     "project": {
                         "data": {
                             "type": "project",
                             "id": 2
                         }
                     }
                 }
             }
        }
     ]
     ' -X PATCH http://localhost:4080/


