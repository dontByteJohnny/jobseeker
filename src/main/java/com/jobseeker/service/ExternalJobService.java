package com.jobseeker.service;

import com.jobseeker.model.Job;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ExternalJobService {

    private final RestTemplate restTemplate;
    private static final String EXTERNAL_URL = "http://localhost:8081/jobs";

    public ExternalJobService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<Job> fetchExternalJobs() {

        ResponseEntity<Map<String, List<List<Object>>>> response = restTemplate.exchange(
                EXTERNAL_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, List<List<Object>>> data = response.getBody();
        if (data == null) return Collections.emptyList();

        List<Job> jobs = new ArrayList<>();

        for (Map.Entry<String, List<List<Object>>> entry : data.entrySet()) {
            String country = entry.getKey();
            for (List<Object> jobData : entry.getValue()) {
                String title = (String) jobData.get(0);
                Integer salary = (Integer) jobData.get(1);
                String skillsXml = (String) jobData.get(2);

                List<String> skills = extractSkillsFromXml(skillsXml);

                Job job = Job.builder()
                        .title(title)
                        .salary(salary)
                        .skills(skills)
                        .country(country)
                        .build();

                jobs.add(job);
            }
        }

        return jobs;
    }

    private List<String> extractSkillsFromXml(String xml) {
        try {
            List<String> skills = new ArrayList<>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);
            NodeList nodes = doc.getElementsByTagName("skill");
            for (int i = 0; i < nodes.getLength(); i++) {
                skills.add(nodes.item(i).getTextContent());
            }
            return skills;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("Unknown");
        }
    }
}
