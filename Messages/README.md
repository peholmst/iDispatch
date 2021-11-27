# iDispatch Protocol Buffers

This module contains the Protocol Buffers for all the messages sent and received by the iDispatch applications. It does not contain any client libraries or service interfaces.

## Generating Java Sources

On Windows, run the `build_java.ps1` script in the root directory of this module. This will download the Protocol Buffer compiler, generate the Java sources and generate a JAR-file that is installed into the local Maven repository. You can import it into your projects like this:

```xml
<dependency>
    <groupId>net.pkhapps.idispatch.messages</groupId>
    <artifactId>idispatch-messages</artifactId>
    <version>3.0-SNAPSHOT</version>
</dependency>
```
