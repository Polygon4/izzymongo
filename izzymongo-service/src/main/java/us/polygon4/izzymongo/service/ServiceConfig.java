
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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import us.polygon4.izzymongo.util.MongoTemplateContainer;

import com.mongodb.Mongo;

/**
 * Dynamically creates a map of MongoTemplates objects for each database
 * 
 * @author Mikhail Izrailov
 *
 */
@Configuration
public class ServiceConfig {
	private @Autowired MongoFactoryBean mongoFactoryBean;
	Logger log = LoggerFactory.getLogger(ServiceConfig.class);
	
	public @Bean MongoTemplateContainer initialize() throws Exception{
		Map<String,MongoOperations> templateMap=new HashMap<>();
		Mongo m=mongoFactoryBean.getObject();
		for(String dbname:m.getDatabaseNames()){
			MongoOperations mongoOps = new MongoTemplate(m, dbname);
			templateMap.put(dbname, mongoOps);
		}
		log.info(templateMap.toString());
		MongoTemplateContainer mtc=new MongoTemplateContainer();
		mtc.setTemplateMap(templateMap);
		return mtc;
	}
	
	
}
