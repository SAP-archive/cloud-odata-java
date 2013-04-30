OData Library (Java)
====================

Introduction
------------

### What is it about? ###

The [Open Data Protocol (OData)](http://odata.org) is a Web protocol for querying and updating data that provides a way to unlock your data and free it from silos that exist in applications today. OData does this by applying and building upon Web technologies such as HTTP, Atom Publishing Protocol (AtomPub) and JSON to provide access to information from a variety of applications, services, and stores.

The OData Library (Java) is a library which enables developers to implement OData producers and OData consumers and adheres to the following principles:

- Provide an OData 2.0 specification compliant OData Library (Java) 
- Enhancements shall be possible in a compatible manner 
- Clear separation between Core and API 
- Option to build extensions on top

Maven
-----

The project build with Maven. To consume sources execute 

    mvn eclipse:eclipse

and import generated projects into Eclipse. 

To consume libraries with Maven add following dependencies to your pom.xml:

    <dependency>
      <groupId>com.sap.odata</groupId>
      <artifactId>com.sap.core.odata.api</artifactId>
      <version>0.4.0</version> 
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.sap.odata</groupId>
      <artifactId>com.sap.core.odata.core</artifactId>
      <version>0.4.0</version>
      <scope>runtime</scope>
    </dependency>

Build artifacts are already available at Maven Central.

Additional Information
----------------------

### License ###

This project is copyrighted by [SAP AG](http://www.sap.com/) and made available under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). Please also confer to the text files "LICENSE" and "NOTICE" included with the project sources.


### Contributions ###

Contributions to this project are very welcome, but can only be accepted if the contributions themselves are given to the project under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html). Contributions other than those given under Apache License 2.0 will be rejected.


Version History
---------------

0.5.0-SNAPSHOT basic $batch as work in progress <br/>
0.4.0  Advanced Read / Write and JSON support  
0.3.0  Advanced Read / Basic Write  
0.2.0  Basic Read / Write  
0.1.0  Basic Read

Roadmap
-------

1. $batch
2. Conditional Handling  
3. Client Support

