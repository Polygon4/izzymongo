
package us.polygon4.izzymongo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.io.*;

import org.w3c.dom.*;

import us.polygon4.izzymongo.dto.CollectionMetadata;
import us.polygon4.izzymongo.dto.Page;
import us.polygon4.izzymongo.service.AbstractService;
import us.polygon4.izzymongo.service.DataService;
import us.polygon4.izzymongo.util.DBQuery;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;


//@Service
public class DataServiceImpl extends AbstractService implements DataService {
	
	ObjectMapper mapper = new ObjectMapper();
	

	/* (non-Javadoc)
	 * @see com.izzymongo.service.DataService#getDbStructure()
	 */
	@Override
	public Map<String, Set<String>> getDbStructure() {
		Map<String,Set<String>> dbStructure=new HashMap<String,Set<String>>();
		for(String key:mtContainer.getTemplateMap().keySet()){
			MongoOperations mo=mtContainer.getTemplateMap().get(key);
			if(LOG.isDebugEnabled()) LOG.debug("DB====================="+key);
			Set<String> collections=new HashSet<String>();
			for(String colName:mo.getCollectionNames()){
				if(LOG.isDebugEnabled()) LOG.debug("COLLECTION====================="+colName);
				//skip system collections
				if(!colName.equalsIgnoreCase("system.indexes"))collections.add(colName);
			}
			dbStructure.put(key, collections);
		}
		return dbStructure;
	}


	@Override
	public long getDocumentCount(DBQuery query) {
		MongoOperations mo=mtContainer.getTemplateMap().get(query.getDb());
		 long count=mo.execute(query.getCollection(),new CollectionCallback<Long>(){
			public Long doInCollection(DBCollection collection) throws MongoException, DataAccessException{
				return collection.count();
			}
		});
		return count;
	}

	@Override
	public List<DBObject> getIndexInfo(DBQuery query) {
		MongoOperations mo=mtContainer.getTemplateMap().get(query.getDb());
		List<DBObject> indexes=mo.execute(query.getCollection(),new CollectionCallback<List<DBObject>>(){
			public List<DBObject> doInCollection(DBCollection collection) throws MongoException, DataAccessException{
				return collection.getIndexInfo();
			}
		});
		return indexes;
	}

	@Override
	public DBObject findSingleDocument(final DBQuery query) {
		MongoOperations mo=mtContainer.getTemplateMap().get(query.getDb());
		
		DBObject dbObject=mo.execute(query.getCollection(),new CollectionCallback<DBObject>(){
			public DBObject doInCollection(DBCollection collection) throws MongoException, DataAccessException{
				return collection.findOne(query.getCriteria(),query.getFields());
			}
		});
		return dbObject;
	}

	@Override
	public Page findDocuments(final DBQuery query) {
		Page page=new Page();
		List<DBObject> list=new ArrayList<DBObject>();
		MongoOperations mo=mtContainer.getTemplateMap().get(query.getDb());
		
		DBCursor cursor=mo.execute(query.getCollection(),new CollectionCallback<DBCursor>(){
			public DBCursor doInCollection(DBCollection collection) throws MongoException, DataAccessException{
				if(query.getLimit()>0) {
					return collection.find(query.getCriteria(), query.getFields()).sort(query.getSort()).limit(query.getLimit());
				}else return collection.find(query.getCriteria(), query.getFields()).sort(query.getSort());
			}
		});
		while(cursor.hasNext()){			
			DBObject dbobj=cursor.next();
			if(page.getFirstId()==null)page.setFirstId(dbobj.get("_id").toString());
			if(!cursor.hasNext())page.setLastId(dbobj.get("_id").toString());
			list.add(dbobj);
		}
		page.setItems(list);
		page.setMaxSize(cursor.count());
		page.setSize(list.size());
		return page;
	}

	@Override
	public Page getCollectionInfo(DBQuery query) {
		CollectionMetadata colMeta=new CollectionMetadata(query.getDb(),query.getCollection());
		colMeta.setCount(getDocumentCount(query));
		colMeta.setIndexInfo(this.getIndexInfo(query));
		Page page=new Page(colMeta);
		page.setDbObject(this.findSingleDocument(query));
		return page;
	}


	@Override
	public byte[] getDbSchema(String db) throws Exception {
		//TODO: Not the most elegant solution. Needs cleanup
		
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        //create the root element and add it to the document
        Element root = doc.createElement("map");
        root.setAttribute("version", "0.9.0");
        doc.appendChild(root);
        
        //create a comment and put it in the root element
        Comment comment = doc.createComment("To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net");
        root.appendChild(comment);
        //Database node
        Element node = doc.createElement("node");
        node.setAttribute("BACKGROUND_COLOR", "#ffffff");
        node.setAttribute("TEXT", db);
        root.appendChild(node);
		
		MongoOperations mo=mtContainer.getTemplateMap().get(db);
		for(String colName:mo.getCollectionNames()){
			if (!colName.equals("system.indexes")) {
				if (LOG.isDebugEnabled())LOG.debug("COLLECTION: " + colName);
				DBObject dbObject = mo.execute(colName,
						new CollectionCallback<DBObject>() {
							public DBObject doInCollection(
									DBCollection collection)
									throws MongoException, DataAccessException {
								return collection.findOne();
							}
						});
				String json = JSON.serialize(dbObject);
				if (LOG.isDebugEnabled())LOG.debug(json);
				@SuppressWarnings("unchecked")
				Map<String, Object> documentData = mapper.readValue(json.getBytes(), Map.class);
				Element colNode = createNode(doc, colName);
				for (String key : documentData.keySet()) {
					addAttribute(doc, colNode, key, documentData.get(key));
				}
						
				node.appendChild(colNode);
			}
		}
        
        //Output the XML
       
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        String xmlString = sw.toString();
        
        if (LOG.isDebugEnabled())LOG.debug("Here's the xml:\n\n" + xmlString);
        
		return xmlString.getBytes();
	}


	private Element createNode(Document doc, String name) {
		Element node = doc.createElement("node");
		node.setAttribute("POSITION", "right");
		node.setAttribute("TEXT", name);
		Element fontNode = doc.createElement("font");
		fontNode.setAttribute("BOLD", "true");
		fontNode.setAttribute("NAME", "SansSerif");
		fontNode.setAttribute("SIZE", "12");
		node.appendChild(fontNode);
		Element alNode = doc.createElement("attribute_layout");
		alNode.setAttribute("NAME_WIDTH", "100");
		alNode.setAttribute("VALUE_WIDTH", "100");
		node.appendChild(alNode);
		return node;
	}
	
	@SuppressWarnings("unchecked")
	private void addAttribute(Document doc,Element parentNode, String key,Object value) {
		String val=key.equals("_id")?"ObjectId":getValue(value);
		Element alNode = doc.createElement("attribute");
        alNode.setAttribute("NAME", key);
        alNode.setAttribute("VALUE", val);
        parentNode.appendChild(alNode);
        
        if(val.equals("object")){
        	Element node = createNode(doc, key);
        	LinkedHashMap<String,Object> map=(LinkedHashMap<String, Object>)value;
        	for(String k:map.keySet()){
				addAttribute(doc,node,k,map.get(k));
        	}   
        	parentNode.appendChild(node);
        }
        
       
	}


	private String getValue(Object value) {
		String val="null";
		if(value!=null){
			String s=value.getClass().toString();
			
			switch(s){
			case "class java.util.LinkedHashMap":{
				val="object";
				break;
			}
			case "class java.util.ArrayList":{
				val="array";
				break;
			}
			case "class java.lang.String":{
				val="text";
				break;
			}
			case "class java.lang.Integer":{
				val="number";
				break;
			}
			case "class java.lang.Long":{
				val="number";
				break;
			}
			case "class java.lang.Double":{
				val="number";
				break;
			}
			case "class java.lang.Boolean":{
				val="boolean";
				break;
			}
			}
		}
		return val;
	}
}
