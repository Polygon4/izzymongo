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
package us.polygon4.izzymongo.dto;


import java.util.List;
import com.mongodb.DBObject;

/**
 * Collection metadata container
 * 
 * @author Mikhail Izrailov
 *
 */
public class CollectionMetadata {
	private long count;
	List<DBObject> indexInfo;
	private String collection;
	private String db;
	public CollectionMetadata(String db, String collection) {
		this.db=db;
		this.collection=collection;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public List<DBObject> getIndexInfo() {
		return indexInfo;
	}
	public void setIndexInfo(List<DBObject> indexInfo) {
		this.indexInfo = indexInfo;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	
	
}
