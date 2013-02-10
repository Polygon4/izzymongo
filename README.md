IzzyMongo experimental MongoDB data viewer
==========================================

Practical alternative to the PetClinic app and also a useful tool for viewing MondoDb databases.

Browse, visualise data and export database schema in FreeMind format. For more information visit [IzzyMongo](http://www.polygon4.us)

#Pre-requisites
* Java JDK 1.7+

Tested with Oracle JDK 1.7

To use with versions 1.5 or 1.6 change java-version in project main pom.xml:

~~~~~ xml
<java-version>1.x.x</java-version>
~~~~~

And make minor java code modifications:
~~~~~ java
Map<String,List<String>> dbStructure=new HashMap<>();
//change to
Map<String,List<String>> dbStructure=new HashMap<String,List<String>>();
~~~~~

* Maven 3.0.4+

* MongoDB v.2.0+

Tested with local and remote instances of MongoDB v.2.2

#Quick-start
1. Download or git clone from Github.
2. Configure database connections.
    By default the app is configured to point to 127.0.0.1:27017.
    In order to change these settings modify Jetty JNDI registry at jetty-env.xml located under izzymongo-web/src/main/webapp/WEB-INF directory.
    Windows users enter full paths to your files in descriptor, jettyEnvXml and webAppSourceDirectory in izzymongo-web/pom.xml
3. Run ./izzymongo.sh or mvn -Pjetty and open your browser at [http://localhost:8080](http://localhost:8080)

#Front-end developers
Just drop folder with your files under izzymongo-web/src/main/webapp/ui and change index.jsp to redirect to your folder.
There is no need for external web server as Jetty serves both as web and application server.
Also there are no issues with cross-site scripting exceptions because both client and service are listening on the same server and port.


