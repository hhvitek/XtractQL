# spring-xml-file-downloader

Spring boot app to download remote file from URL. This file is archived xml (zip format). Parse and store some data into DB.

- spring boot, hibernate, jackson, tomcat 10, apache commons, sqlite, modelmapper

- To run tomcat 10 required + fill application.properties:

```properties
spring.datasource.url=jdbc:sqlite:<PATH TO SQLITE FILE>
parser.tmp.folder=/tmp/parserTemporaryFolder
```