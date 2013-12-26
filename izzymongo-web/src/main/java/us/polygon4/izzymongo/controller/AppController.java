package us.polygon4.izzymongo.controller;

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


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import us.polygon4.izzymongo.dto.DbStructure;
import us.polygon4.izzymongo.dto.Page;
import us.polygon4.izzymongo.service.DataService;
import us.polygon4.izzymongo.service.DataServiceScala;
import us.polygon4.izzymongo.util.DBQuery;

import com.mongodb.BasicDBObject;
/**
 * @author Mikhail Izrailov
 */


@Controller
@RequestMapping("/mongo*")
public class AppController extends AbstractController {
	//@Autowired private DataService service;
	@Autowired private DataServiceScala service;
		
	/**
	 * Returns a list of database and collections for navigation menu
	 * 
	 * @return DbStructure
	 */
	@RequestMapping(value = "/dbstruct", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody DbStructure loadDbStructure(){
		DbStructure dbs=new DbStructure();
		dbs.setDb("localhost");
		dbs.setDbMap(service.getDbStructure());
		return dbs;
	}
	
	/**
	 * Returns Page container comprised of collection metadata and index information
	 * 
	 * @param db  database name
	 * @param col  collection name
	 * @return Page
	 */
	@RequestMapping(value = "/collection/{db}/{col}", method = RequestMethod.GET, produces = "application/json")
	public  @ResponseBody Page loadCollectionInfo(@PathVariable String db, @PathVariable String col){
		DBQuery query=new DBQuery();
		query.setDb(db);
		query.setCollection(col);
		return service.getCollectionInfo(query);
	}
	
	 /**
	 * Returns Page container with subset of collection data based on criteria passed
	 * 
	 * @param db  database name
	 * @param col collection name
	 * @param criteria criteria field names
	 * @param criteria_op operator values
	 * @param criteria_val criteria values
	 * @param criteria_type criteria types
	 * @param fields fields to return
	 * @param prevId id of offset data object for backward pagination support
	 * @param nextId id of offset data object for forward pagination support
	 * @param browse_dir pagination direction ("previous" or "next")
	 * @param page_num page number
	 * @param sort sorting criteria
	 * @param sort_dir sorting order ("ASC" or "DESC")
	 * @param limit page records size limit
	 * @return Page
	 */
	@RequestMapping(value = "/browse", method = RequestMethod.GET, produces = "application/json")
	 public @ResponseBody Page find(
			 @RequestParam("db") String db,
			 @RequestParam("collection") String col,
			 @RequestParam(value="criteria",required=false) String[]criteria,
			 @RequestParam(value="criteria_op",required=false) String[]criteria_op,
			 @RequestParam(value="criteria_val",required=false) String[]criteria_val,
			 @RequestParam(value="criteria_type",required=false) String[]criteria_type,
			 @RequestParam(value="field",required=false) String[]fields,			 
			 @RequestParam(value="prev_id",required=false) String prevId,
			 @RequestParam(value="next_id",required=false) String nextId,
			 @RequestParam(value="browse_dir",required=false) String browse_dir,
			 @RequestParam(value="page_num") int page_num,
			 @RequestParam("sort") String sort,
			 @RequestParam("sort_dir") int sort_dir,
			 @RequestParam("limit") int limit
			 ){
		 
		DBQuery query = buildQuery(new BuildQueryParameter(db, col, criteria, criteria_val, criteria_type,
				fields, prevId, nextId, browse_dir, sort, sort_dir, limit));
		
		Page page = service.findDocuments(query);

		if (browse_dir.equals("next")) {
			page.setPageId(++page_num);
			if (page.getMaxSize() <= limit * page_num)
				page.setLastPage(true);
		} else if (browse_dir.equals("prev")) {
			if (page_num > 0)
				page.setPageId(--page_num);
			if (page_num < 1) {
				page.setFirstPage(true);
			}
		} else {
			page.setFirstPage(true);
			if (page.getMaxSize() <= limit)
				page.setLastPage(true);
		}

		if(log.isDebugEnabled())log.debug(page.toString());

		return page;
	 }
	 
	 /**
	  * Exports database schema as FreeMind map
	  * 
	 * @param fileName database name
	 * @return fileName + ".mm"
	 * @throws Exception
	 */
	@RequestMapping(value = "/export/{fileName}", method = RequestMethod.GET)
	 public HttpEntity<byte[]> createExport(
	                  @PathVariable("fileName") String fileName) throws Exception {
	     byte[] documentBody = null;
		 documentBody = service.getDbSchema(fileName);		
		 fileName=fileName+".mm";
	     HttpHeaders header = new HttpHeaders();
	     header.setContentType(new MediaType("application", "xml"));
	     header.set("Content-Disposition",
	                    "attachment; filename=" + fileName.replace(" ", "_"));
	     header.setContentLength(documentBody.length);

	     return new HttpEntity<byte[]>(documentBody, header);
	 }
	 
	 

	DBQuery buildQuery(final BuildQueryParameter parameterObject) {
		DBQuery query=new DBQuery();
			query.setDb(parameterObject.db);
			query.setCollection(parameterObject.col);
			//criteria
			//TODO: Should be able to accept multiple criteria objects and operators
			//For now the assumption is there is just one criterion and operator is fixed to "="
			BasicDBObject criteriaObj=new BasicDBObject();
			if(parameterObject.criteria_vals!=null&&parameterObject.criteria_vals.length>0 
					&&parameterObject.criterias!=null&&parameterObject.criterias.length>0
					&&parameterObject.criteria_vals.length==parameterObject.criterias.length){
					for(int i=0;i<parameterObject.criterias.length;i++){
						criteriaObj.put(parameterObject.criterias[i], convert(parameterObject.criteria_vals[i], parameterObject.criteria_types[i]));
					}
			}
			//pagination
			if(parameterObject.browse_dir!=null&&(parameterObject.prevId!=null||parameterObject.nextId!=null)){
				if(parameterObject.browse_dir.equals("next")&&parameterObject.nextId!=null){
					Object idObject=ObjectId.isValid(parameterObject.nextId)?ObjectId.massageToObjectId(parameterObject.nextId):parameterObject.nextId;
					criteriaObj.append("_id", new BasicDBObject(parameterObject.sort_dir==1?"$gt":"$lt",idObject));
				}else if(parameterObject.browse_dir.equals("prev")&&parameterObject.prevId!=null){
					Object idObject=ObjectId.isValid(parameterObject.prevId)?ObjectId.massageToObjectId(parameterObject.prevId):parameterObject.prevId;
					criteriaObj.append("_id", new BasicDBObject(parameterObject.sort_dir==1?"$gte":"$lte",idObject));
				}
			}
					
			query.setCriteria(criteriaObj);
			
			//fields to return
			if(parameterObject.fields!=null&&parameterObject.fields.length>0){
				BasicDBObject fieldsObj=new BasicDBObject();
				for(String field:parameterObject.fields){
					fieldsObj.put(field, 1);
				}
				
				query.setFields(fieldsObj);
			}
			
			//sort
			//TODO: Enable sorting by multiple elements
			BasicDBObject sortObj=new BasicDBObject();			
			sortObj.put(parameterObject.sort,parameterObject.sort_dir);			
			query.setSort(sortObj);
			
			//limit
			query.setLimit(parameterObject.limit);
			if(log.isDebugEnabled())log.debug(query.toString());
		return query;
	}
	 
	 //TODO: Add support for more types. Move to Utility class
	 Object convert(String valueToConvert, String type){
		if(type.equals("ObjectId")){
			return ObjectId.massageToObjectId(valueToConvert);
		}else if(type.equals("Number")){
			return Long.parseLong(valueToConvert);
		}
			
		return valueToConvert;
	 }
	

}
