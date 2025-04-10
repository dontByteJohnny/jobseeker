package com.jobseeker.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job implements Serializable {
    private static final long serialVersionUID = 1L;

    @Setter
    @Getter
    private String id;
    @Setter
    @Getter
    private String title;
    @Setter
    @Getter
    private String company;
    @Setter
    @Getter
    private String country;
    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private List<String> skills;
    @Setter
    @Getter
    private Integer salary;
}
