package us.polygon4.izzymongo.util;

import java.util.Map;

import org.springframework.data.mongodb.core.MongoOperations;
/*
 * Container for MongoTemplate instances for each database
 * 
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
