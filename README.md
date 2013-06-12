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

### Some more Background ###

The basic idea behind this project is to have a code base that can continue development to support future OData 4.0 standard. OData 4.0 is expected to be release by OAISI in 2013. For that the code is modular and has following components:

- URI parser: currently a OData 2.0 parser
- Dispatcher: is handling OData 2.0 URI types
- Processor interfaces: to support server side implementations
- EDM model API: OData 2.0 compliant, supports lazy and partial loading of metadata
- Entity provider: for JSON and Atom default formats and extendible to support future formats

The support of OData 2.0 is just mandatory because of there is already a wide adoption of this standard version. It is also an option to extend the OData 2.0 library to support features of OData 3.0 if required. Contributions to this are welcome.

For OData 4.0 development could happen in a new branch and we target that OData 2.0 (3.0) and OData 4.0 can exist in parallel. 

Maven
-----

The project build with Maven. To consume sources execute 

    mvn eclipse:eclipse

and import generated projects into Eclipse. 

To consume libraries with Maven add following dependencies to your pom.xml:

    $odata.lib.version=0.5.0

    <dependency>
      <groupId>com.sap.odata</groupId>
      <artifactId>com.sap.core.odata.api</artifactId>
      <version>${odata.lib.version}</version> 
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.sap.odata</groupId>
      <artifactId>com.sap.core.odata.core</artifactId>
      <version>${odata.lib.version}</version>
      <scope>runtime</scope>
    </dependency>

For advanced JPA processor usage these additional dependencies are required:

    <dependency>
      <groupId>com.sap.odata</groupId>
      <artifactId>com.sap.core.odata.processor.api</artifactId>
      <version>${odata.lib.version}</version> 
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.sap.odata</groupId>
      <artifactId>com.sap.core.odata.api.annotation</artifactId>
      <version>${odata.lib.version}</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>com.sap.odata</groupId>
      <artifactId>com.sap.core.odata.processor.core</artifactId>
      <version>${odata.lib.version}</version>
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

0.6.0-SNAPSHOT basic $batch as work in progress <br/>
0.5.0  Service Document parsing / performance optimization<br/>
0.4.1  JPA processor support  
0.4.0  Advanced Read / Write and JSON support  
0.3.0  Advanced Read / Basic Write  
0.2.0  Basic Read / Write  
0.1.0  Basic Read

Roadmap
-------

1. $batch
2. Conditional Handling  
3. Client Support

