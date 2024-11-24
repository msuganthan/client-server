### Java version used

JDK 21

### Checkout this project

`git clone git@github.com:msuganthan/client-server.git`


### Run the server project

```
javac clientserver/server/Server.java
java clientserver.server.Server server-config.properties
```

### Run the client project

```
javac clientserver/client/DirectoryMonitorClient.java 
java clientserver.client.DirectoryMonitorClient client-config.properties
```

### Testing

1. Move the `sample-file.properties` to `directory-to-monitor` folder
2. Observer an output new file being created inside `directory-to-write` folder