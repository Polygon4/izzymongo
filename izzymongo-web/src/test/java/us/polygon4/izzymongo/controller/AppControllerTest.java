package us.polygon4.izzymongo.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:test-context-servlet.xml"})
public class AppControllerTest {
	@Autowired
    private WebApplicationContext wac;
	
	private MockMvc mockMvc;

    

    @Before
    public void setup() {
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void getDbSchema() throws Exception{
    	mockMvc.perform(get("/mongo/dbstruct").accept(MediaType.APPLICATION_JSON))
    	.andExpect((status().isOk()))
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    	.andExpect(jsonPath("$.db").value("localhost"))
    	.andExpect(jsonPath("$.dbMap").exists());
    }
    
    @Test
    public void loadCollectionInfo() throws Exception{
    	mockMvc.perform(get("/mongo/collection/{db}/{col}","testdb","zips").accept(MediaType.APPLICATION_JSON))
    	.andExpect((status().isOk()))
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    	.andExpect(jsonPath("$.collectionInfo.count").value(29470));
    }
    
    @Test
    public void findNoParamsAsc() throws Exception{
    	mockMvc.perform(get("/mongo/browse")
    			.param("db", "training")
    			.param("collection", "scores")
    			.param("sort", "_id")
    			.param("sort_dir", "1")
    			.param("page_num", "0")
    			.param("limit", "10")
    			.param("browse_dir", "")
    			.accept(MediaType.APPLICATION_JSON))
		    	.andExpect((status().isOk()))
		    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		    	.andExpect(jsonPath("$.firstPage").value(true))
		    	.andExpect(jsonPath("$.lastPage").value(false))
		    	.andExpect(jsonPath("$.maxSize").value(3000))
		    	.andExpect(jsonPath("$.firstId").value("4c90f2543d937c033f424701"))
		    	.andExpect(jsonPath("$.lastId").value("4c90f2543d937c033f42470a"));
    }
    
    @Test
    public void findNoParamsDesc() throws Exception{
    	mockMvc.perform(get("/mongo/browse")
    			.param("db", "training")
    			.param("collection", "scores")
    			.param("sort", "_id")
    			.param("sort_dir", "-1")
    			.param("page_num", "0")
    			.param("limit", "10")
    			.param("browse_dir", "")
    			.accept(MediaType.APPLICATION_JSON))
		    	.andExpect((status().isOk()))
		    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		    	.andExpect(jsonPath("$.firstPage").value(true))
		    	.andExpect(jsonPath("$.lastPage").value(false))
		    	.andExpect(jsonPath("$.maxSize").value(3000))
		    	.andExpect(jsonPath("$.firstId").value("4c90f2543d937c033f4252b8"))
		    	.andExpect(jsonPath("$.lastId").value("4c90f2543d937c033f4252af"));
    }
    
    @Test
    public void findWithCriteria() throws Exception{
    	mockMvc.perform(get("/mongo/browse")
    			.param("db", "training")
    			.param("collection", "scores")
    			.param("sort", "_id")
    			.param("sort_dir", "1")
    			.param("page_num", "0")
    			.param("limit", "10")
    			.param("browse_dir", "")
    			.param("criteria", "score")
    			.param("criteria_val", "50")
    			.param("criteria_type", "Number")
    			.accept(MediaType.APPLICATION_JSON))
		    	.andExpect((status().isOk()))
		    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		    	.andExpect(jsonPath("$.firstPage").value(true))
		    	.andExpect(jsonPath("$.lastPage").value(false))
		    	.andExpect(jsonPath("$.maxSize").value(59))
		    	.andExpect(jsonPath("$.firstId").value("4c90f2543d937c033f424701"))
		    	.andExpect(jsonPath("$.lastId").value("4c90f2543d937c033f42495c"));
    }
    
    @Test
    public void createExport() throws Exception{
    	mockMvc.perform(get("/mongo/export/{fileName}","testdb").accept(MediaType.APPLICATION_XML))
    	.andExpect((status().isOk()))
    	.andExpect(content().contentType(MediaType.APPLICATION_XML));
    }

}
