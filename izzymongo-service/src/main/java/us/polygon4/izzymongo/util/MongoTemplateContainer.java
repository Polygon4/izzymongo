package us.polygon4.izzymongo.util;

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

import java.util.Map;

import org.springframework.data.mongodb.core.MongoOperations;
/*
 * Container for MongoTemplate instances for each database
 * 
 * @author Mikhail Izrailov
 */
public class MongoTemplateContainer {
	private String db;
	Map<String,MongoOperations> templateMap;

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public Map<String, MongoOperations> getTemplateMap() {
		return templateMap;
	}

	public void setTemplateMap(Map<String, MongoOperations> templateMap) {
		this.templateMap = templateMap;
	}
	
	
	
	
}
