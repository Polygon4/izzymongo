
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;


/**
 * Base controller
 * 
 * @author Mikhail Izrailov
 *
 */
public abstract class AbstractController {
	Logger log = LoggerFactory.getLogger(AbstractController.class);
	
	@ExceptionHandler (Exception.class)   
	@ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception ex) {        
		log.error(ex.getMessage());
		return new ControllerException(ex.getMessage()).asModelAndView();
    }
	
}
