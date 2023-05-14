package com.vmware.talentboost.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class to be mapped to the response of issues endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    private String title;
    private String body;

    private int number;

    private int comments;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getComments(){
        return comments;
    }

    public int getNumber(){
        return number;
    }
}
