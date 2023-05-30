package com.vmware.talentboost.api.actors;

import com.vmware.talentboost.URLTemplate;
import com.vmware.talentboost.objects.IssueComment;
import com.vmware.talentboost.objects.Issue;

import java.text.MessageFormat;
import java.util.List;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

/**
 * Actor class meant to store helper methods for e2e tests.
 */
public class IssueE2EActor {

    private RequestSpecification spec;

    public IssueE2EActor(RequestSpecification reqSpec) {
        this.spec = reqSpec;
    }

    public List<Issue> getIssuesForRepo(String owner, String repo){
        List<Issue> repoIssues = RestAssured.given()
                .spec(this.spec)
            .when()
                .get(MessageFormat.format(URLTemplate.IssuesURLTemplate, owner, repo))
                .jsonPath().getList("", Issue.class);

        return repoIssues;
    }

    public List<IssueComment> getCommentsForAnIssue(String owner , String repo, int issueNumber){
        List<IssueComment> comments = RestAssured.given()
                .spec(this.spec)
            .when()
                .get(MessageFormat.format(URLTemplate.IssueCommentsURLTemplate, owner, repo, issueNumber))
            .jsonPath().getList("", IssueComment.class);

        return comments;
    }

    public IssueComment postCommentToAnIssue(String owner , String repo, int issueNumber, String comment){
        JSONObject payload = new JSONObject();
        payload.put("body", comment);
        IssueComment postedComment = RestAssured.given()
                .spec(this.spec)
                .body(payload.toJSONString())
            .when()
                .post(MessageFormat.format(URLTemplate.IssueCommentsURLTemplate, owner, repo, issueNumber))
                .as(IssueComment.class);

        return postedComment;
    }

    public void deleteCommentForAnIssue(String owner , String repo, String commentId){
        RestAssured.given()
                .spec(this.spec)
            .when()
                .delete(MessageFormat.format(URLTemplate.SingleIssueCommentURLTemplate, owner, repo, commentId))
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
