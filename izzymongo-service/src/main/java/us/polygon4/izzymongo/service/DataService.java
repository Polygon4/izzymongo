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
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import us.polygon4.izzymongo.dto.Page;
import us.polygon4.izzymongo.util.DBQuery;

import com.mongodb.DBObject;

/**
 * @author Mikhail Izrailov
 *
 */
@Service
public interface DataService {
	
	Map<String,List<String>> getDbStructure();

	long getDocumentCount(DBQuery query);

	List<DBObject> getIndexInfo(DBQuery query);

	DBObject findSingleDocument(DBQuery query);
	
	Page findDocuments(DBQuery query);
	
	Page getCollectionInfo(DBQuery query);
	
	byte[] getDbSchema(String db) throws Exception;
	
}
