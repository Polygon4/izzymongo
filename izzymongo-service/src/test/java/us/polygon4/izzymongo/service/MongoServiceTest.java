package us.polygon4.izzymongo.service;

/*
 * IzzyMongo Database Viewer 
 * 
 * Copyright (C) 2013 Polygon4, and individual contributors
 * by the @authors tag.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import us.polygon4.izzymongo.dto.Page;
import us.polygon4.izzymongo.service.DataService;
import us.polygon4.izzymongo.util.DBQuery;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoServiceTest extends AbstractDataTest{
	@Autowired DataService dataService;
	@Test
	public void dbStructureTest() {
		Map<String,List<String>> dbStructure=dataService.getDbStructure();
		assertThat(dbStructure, notNullValue());
		assertTrue(dbStructure.containsKey("local"));
	}
	
	@Test
	public void findOneTest(){
		DBQuery query = createDBQuery();
		DBObject doc=dataService.findSingleDocument(query);
		log.debug(doc.toString());
		assertNotNull(doc.get("_id"));
	}

	private DBQuery createDBQuery() {
		DBQuery query=new DBQuery();
		query.setDb("training");
		query.setCollection("scores");
		return query;
	}
	
	
	@Test
	public void findDocsByCriteriaTest(){
		DBQuery query = createDBQuery();
		BasicDBObject criteria=new BasicDBObject();
		criteria.put("_id", ObjectId.massageToObjectId("4c90f2543d937c033f424701"));
		query.setCriteria(criteria);
		BasicDBObject fields=new BasicDBObject();
		fields.put("score", 1);
		fields.put("student", 1);
		query.setFields(fields);
		BasicDBObject sort=new BasicDBObject();
		sort.put("_id", 0);
		query.setSort(sort);
		query.setLimit(5);
		Page page=dataService.findDocuments(query);
		log.debug(page.toString());
		List<DBObject>list=page.getItems();
		assertNotNull(list);
		assertNotNull(list.get(0).get("_id"));
		assertThat(list.get(0).get("_id").toString(),equalTo("4c90f2543d937c033f424701"));
	}
	
	
	
	@Test
	public void findOneByIdTest(){
		DBQuery query = createDBQuery();
		BasicDBObject criteria=new BasicDBObject();
		criteria.put("_id", ObjectId.massageToObjectId("4c90f2543d937c033f424701"));
		query.setCriteria(criteria);
		DBObject doc=dataService.findSingleDocument(query);
		log.debug(doc.toString());
		assertNotNull(doc.get("_id"));
		assertThat(doc.get("_id").toString(),equalTo("4c90f2543d937c033f424701"));
	}
	
	@Test
	public void findOneByIdCustomFieldsTest(){
		DBQuery query = createDBQuery();
		BasicDBObject criteria=new BasicDBObject();
		criteria.put("_id", ObjectId.massageToObjectId("4c90f2543d937c033f424701"));
		query.setCriteria(criteria);
		BasicDBObject fields=new BasicDBObject();
		fields.put("score", 1);
		fields.put("student", 1);
		query.setFields(fields);
		DBObject doc=dataService.findSingleDocument(query);
		log.debug(doc.toString());
		assertNotNull(doc.get("_id"));
		assertThat(doc.get("_id").toString(),equalTo("4c90f2543d937c033f424701"));
	}
	
	@Test
	public void getCountTest(){
		DBQuery query = createDBQuery();
		long count=dataService.getDocumentCount(query);
		log.debug(""+count);
		assertThat(new Long(count),greaterThan(new Long(0)));
		
	}
	
	@Test
	public void getIndexInfo(){
		DBQuery query = createDBQuery();
		List<DBObject> list=dataService.getIndexInfo(query);
		log.debug(list.toString());
		assertThat(new Integer(list.size()),greaterThan((0)));	
	}
	
	@Test
	public void getXml() throws Exception{
		byte[] xml=dataService.getDbSchema("testdb");	
		assertNotNull(xml);		
		String converted=new String(xml).trim();		
		assertTrue(converted.startsWith("<map version=\"0.9.0\">"));
		assertTrue(converted.contains("<attribute NAME=\"_id\" VALUE=\"ObjectId\"/>"));
		assertTrue(converted.endsWith("</map>"));
	}

}
