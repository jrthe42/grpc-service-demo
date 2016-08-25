gRPC Example
=======================

This is a example for using [gRPC](http://www.grpc.io/), which is a high performance, open-source universal RPC framework from Google.

This project demonstrates how to write gRPC server and client application using Java. A python client also been proposed to examine the across languages feature of gRPC.

## how to Build

### Java server & client

To build this demo, run in this directory:

```
$ ./gradlew installDist
```

This creates the scripts `JRService-server`, `JRService-client` in the
`build/install/examples/bin/` directory. Remember to run the server first before runing the client.

```
$ ./build/install/examples/bin/JRService-server
```

Then in another terminal window,

```
$ ./build/install/examples/bin/JRService-client
```

That's it.

### Python client

Install gRPC first:

```
$ sudo python -m pip install grpcio
$ sudo python -m pip install grpcio-tools
```

Generate the Python code using folling commond:

```
$ python -m grpc.tools.protoc -I./proto --python_out=. --grpc_python_out=. ./proto/jr.proto
```

Then run the client:

```
$ python jr_service_client.py
```
