package com.example.demo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.CSVService;
import com.example.helper.CSVHelper;
import com.example.model.Projects;

@RestController
public class ProjectsController {
	@GetMapping("/hello")
	public String sayHello() 
	{         return "Hello, World!";     
	} 
	
	 @Autowired
     CSVService fileService;

     @PostMapping("/upload")
     public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
         String message = "";

         if (CSVHelper.hasCSVFormat(file)) {
             try {
				fileService.save(file);
			 } catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
             
             Map<String, List<String>> projectsMap = fileService.getEmpPairWithLongestPeriodOnCommonProject();
             StringBuilder sb = new StringBuilder("Emp 1; Emp 2; Project ").append(System.getProperty("line.separator"));
             
             for(String prjId : projectsMap.keySet())
             {
            	 sb.append(projectsMap.get(prjId).get(0)).append("      ")
            	 .append(projectsMap.get(prjId).get(1)).append("      ")
              	 .append(prjId).append(System.getProperty("line.separator"));            	 
             }

             try {
                 fileService.save(file);
                 message = "Uploaded the file successfully: \n" + sb.toString();
                 
                 return ResponseEntity.status(HttpStatus.OK).body( "\" message \": \" "+ message);
             } catch (Exception e) {
                 message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                 return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("\" message \": \" "+ message +" \"");
             }
         }
         message = "Please upload a csv file!";
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\" message \": \" "+ message +" \"");
     }
     
     //1- SELECT * FROM Order;
     @GetMapping("/projects")
     public ResponseEntity<List<Projects>> getAllProjects () {
         try {
             List<Projects> projects = fileService.getAllProjects();

             if (projects.isEmpty()) {
                 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
             }

             return new ResponseEntity<List<Projects>>(HttpStatus.OK);
         } catch (Exception e) {
             return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
}

