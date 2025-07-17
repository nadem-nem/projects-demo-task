package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Projects;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CSVService {

    List<Projects> projects;    

    public void save(MultipartFile file) throws NumberFormatException, ParseException {
        try {
           projects = com.example.helper.CSVHelper.csvToOProjects(file.getInputStream());           
           
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Projects> getAllProjects() {
        return projects;
    }
    
    public  Map<String, List<String>> getEmpPairWithLongestPeriodOnCommonProject()
    {    	
        List<String> duplicates = new ArrayList<String>();
        Set<String> seen = new HashSet<String>();        
        
        for(Projects val : projects)
        {
        	if(!seen.add(val.getProjId()))
        	{
        		duplicates.add(val.getProjId());
        	}
        }     
        
        Map<String, List<String>> pairsOfLongestPerionOnCommonProject = projects.stream()
        		.filter(e -> duplicates.contains(e.getProjId()))
        		.collect(Collectors.groupingBy(Projects::getProjId)) 
        				.entrySet().stream()        				
        				.collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().stream()        						
        						.sorted(Comparator.comparing(Projects::daysInPewriod).reversed())
        						.limit(2)
        						.map(Projects::getEmpId)        						
        						.collect(Collectors.toList())));    	
               
    	return pairsOfLongestPerionOnCommonProject;
    }
}

