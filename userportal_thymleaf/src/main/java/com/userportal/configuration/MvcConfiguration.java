package com.userportal.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfiguration implements WebMvcConfigurer{

	private final static Logger LOG = org.slf4j.LoggerFactory.getLogger(MvcConfiguration.class);
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		//WebMvcConfigurer.super.addResourceHandlers(registry);
		exposeDirectory("profile-image", registry); 
	
	}
	
	public void exposeDirectory(String pathToExpose, ResourceHandlerRegistry registry) {
		
		Path path = Paths.get(pathToExpose);
		
		String absolutePath = path.toFile().getAbsolutePath();
		
		
		String logicalPath = pathToExpose.replace("/", "") + "/**";
		
		LOG.info("LOGICAL PATH - : " +  logicalPath + " ABSOLUTE PATH :- "+absolutePath);
		
		registry.addResourceHandler(logicalPath)
			.addResourceLocations("file:///" + absolutePath + "/");
	}
	
	
}
