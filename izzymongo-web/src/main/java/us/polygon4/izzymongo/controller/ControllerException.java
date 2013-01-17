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


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.google.common.collect.ImmutableMap;


/**
 * Global controller exception handler 
 * See 
 * <a href="http://www.asyncdev.net/2011/12/spring-restful-controllers-and-error-handling/">Spring: RESTful controllers and error handling</a>
 *
 */
@SuppressWarnings("serial")
public class ControllerException extends Exception {

	
	private final String message;

	public ControllerException(String message){		
		super(message);
		this.message = message;
	}
	
	public ModelAndView asModelAndView() {
	        MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
	        return new ModelAndView(jsonView, ImmutableMap.of("error", message));
	}
}
