package com.vmware.talentboost.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class to be mapped to the response of repos{owner}/{repo}/issues/comments endpoint
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueComment {
    private String id;
    private String body;

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }
}
