
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

import com.mongodb.DBObject;

/**
 * Container for query construction
 * 
 * @author Mikhail Izrailov
 *
 */
public class DBQuery {
	private String db;
	private String collection;
	private DBObject criteria;
	private DBObject fields;
	private DBObject sort;
	private int limit;
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public DBObject getCriteria() {
		return criteria;
	}
	public void setCriteria(DBObject criteria) {
		this.criteria = criteria;
	}
	public DBObject getFields() {
		return fields;
	}
	public void setFields(DBObject fields) {
		this.fields = fields;
	}
	public DBObject getSort() {
		return sort;
	}
	public void setSort(DBObject sort) {
		this.sort = sort;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	@Override
	public String toString() {
		return "DBQuery [db=" + db + ", collection=" + collection
				+ ", criteria=" + criteria + ", fields=" + fields + ", sort="
				+ sort + ", limit=" + limit + "]";
	}
	
	

}
