
package us.polygon4.izzymongo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.stereotype.Service;
import us.polygon4.izzymongo.util.MongoTemplateContainer;
import com.mongodb.Mongo;


/**
 * Base service class
 * 
 *
 */
@Service
public abstract class AbstractService {
	protected static final Logger LOG = LoggerFactory.getLogger(AbstractService.class);
	@Autowired protected MongoTemplateContainer mtContainer;
	@Autowired protected MongoFactoryBean mongoFactoryBean;
	
	public MongoTemplateContainer getMtContainer() {
		return mtContainer;
	}
	public void setMtContainer(MongoTemplateContainer mtContainer) {
		this.mtContainer = mtContainer;
	}
	
	public Logger logger(){
		return LOG;
	}
	
	/*public Mongo mongo() throws Exception{
		return mongoFactoryBean.getObject();		
	}*/
	
}
