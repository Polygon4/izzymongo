package us.polygon4.izzymongo.service

import com.mongodb.casbah.Imports._
import collection.JavaConverters._
import collection.JavaConversions._
import us.polygon4.izzymongo.dto._
import us.polygon4.izzymongo.util.DBQuery
import us.polygon4.izzymongo.dto.Page
import org.springframework.stereotype.Service
import com.mongodb.DBCursor
import collection.mutable.ListBuffer
import com.mongodb.DBCollection
import org.codehaus.jackson.map.ObjectMapper
import com.mongodb.util.JSON
import collection.mutable.{Map=>MtbMap}
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import com.mongodb.Mongo
import org.springframework.data.mongodb.core.MongoFactoryBean
import scala.xml._


@Service
class DataServiceScala extends AbstractService with DataService with ApplicationContextAware {
  
  var mongo:Mongo=null
  
  def setApplicationContext(applicationContext:ApplicationContext):Unit=  {
        System.out.println("setting context");
        mongo=applicationContext.getBean("mongoFactoryBean").asInstanceOf[Mongo]
    }  
  
  lazy val mongoClient = MongoClient(mongo.getAddress())

  val mapper = new ObjectMapper

  override def getDbStructure: java.util.Map[String, java.util.Set[String]] =
    mongoClient.dbNames.map(
      db => (db, mongoClient(db).collectionNames.-("system.indexes").asJava)).toMap.asJava

  override def getDocumentCount(dbquery: DBQuery): Long = dbCollection(dbquery).count

  override def getIndexInfo(dbquery: DBQuery): java.util.List[DBObject] = dbCollection(dbquery).getIndexInfo

  override def findSingleDocument(dbquery: DBQuery): DBObject = dbCollection(dbquery).findOne(dbquery.getCriteria, dbquery.getFields)

  override def findDocuments(dbquery: DBQuery): Page = {
    val page = new Page
    val cursor: DBCursor = {
      if (dbquery.getLimit > 0)
        dbCollection(dbquery).find(dbquery.getCriteria, dbquery.getFields).sort(dbquery.getSort).limit(dbquery.getLimit)
      else
        dbCollection(dbquery).find(dbquery.getCriteria, dbquery.getFields).sort(dbquery.getSort)

    }
    val list: java.util.List[DBObject] = {
      val listBuffer: ListBuffer[DBObject] = new ListBuffer[DBObject]
      while (cursor.hasNext) {
        val dbobj = cursor.next
        if (page.getFirstId == null) page.setFirstId(dbobj.get("_id").toString)
        if (!cursor.hasNext) page.setLastId(dbobj.get("_id").toString);
        listBuffer += dbobj
      }
      listBuffer.asJava
    }
    page.setItems(list)
    page.setMaxSize(cursor.count);
    page.setSize(list.size)
    page
  }

  override def getCollectionInfo(query: DBQuery): Page = {
        if(logger.isDebugEnabled()) logger.debug("DB=====================SCALA");		

    val colMeta = new CollectionMetadata(query.getDb, query.getCollection)
    colMeta.setCount(getDocumentCount(query));
    colMeta.setIndexInfo(this.getIndexInfo(query))
    val page = new Page(colMeta)
    page.setDbObject(findSingleDocument(query))
    page
  }

  override def getDbSchema(db: String): Array[Byte] = {
    val database = mongoClient.getDB(db).getCollectionNames.-("system.indexes")
    //if(logger.isDebugEnabled()) logger.debug("DB=====================SCALA");		
    
    val nodes:NodeBuffer={
      val nb=new NodeBuffer()
      for (col <- database) {
              /*val documentData: MtbMap[String, Object] = mapAsScalaMap(
                mapper.readValue(
                  JSON.serialize(
                    mongoClient.getDB(db).getCollection(col).findOne).getBytes,
                  classOf[java.util.Map[String, Object]]))*/  
        val documentData: java.util.Map[String, Object] = mapper.readValue(
                  JSON.serialize(
                    mongoClient.getDB(db).getCollection(col).findOne).getBytes,
                  classOf[java.util.Map[String, Object]])
              nb +=addNode(db, col, documentData)
                  
            } 
      nb
    }
    
    val xml =
      <map version="0.9.0">
        <!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
        <node TEXT={ db } BACKGROUND_COLOR="#ffffff">
          {nodes}
        </node>
      </map>

    xml.toString.getBytes
  }
  


  private def addNode(dbName: String, name: String, map: java.util.Map[String, Object]): Elem = {
	val nodes:NodeBuffer={
	  val nb=new NodeBuffer()
	  for ((k, v) <- map) {
            nb +=addAttribute(k, v)
            val attrValue = if (k == "_id") "ObjectId" else getValue(v)
            if (attrValue == "object") nb +=addNode(dbName, k, v.asInstanceOf[java.util.Map[String, Object]])
          }
	  nb
	}
    val node =
      <node TEXT={ name } POSITION="right">
        <font BOLD="true" NAME="SansSerif" SIZE="12"/>
        <attribute-layout NAME_WIDTH="100" VALUE_WIDTH="100"/>
        {nodes}
      </node>
    logger.debug(node.toString)
    node
  }
  
  

  private def addAttribute(key: String, value: Any): Elem = {
    val attrValue = if (key == "_id") "ObjectId" else getValue(value)
    //if(logger.isDebugEnabled)logger.debug("OBJECT TYPE: " +value.getClass.toString)
    val attribute =
      <attribute NAME={ key } VALUE={ attrValue }></attribute>
    logger.debug(attribute.toString)  
    attribute
  }

  private def getValue(value: Any): String = value.getClass.toString match {
    case "class java.util.LinkedHashMap" => "object"
    case "class java.util.ArrayList"  => "array"
    case "class java.lang.String" => "text"
    case "class java.lang.Integer"|"class java.lang.Long"|"class java.lang.Double" => "number"
    case "class java.lang.Boolean" => "boolean"
    case _ => ""
  }

  private def dbCollection(dbquery: DBQuery): DBCollection =
    mongoClient.getDB(dbquery.getDb()).getCollection(dbquery.getCollection())

}