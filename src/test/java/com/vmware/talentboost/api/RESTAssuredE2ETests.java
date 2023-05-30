package com.vmware.talentboost.api;
import com.vmware.talentboost.api.actors.IssueE2EActor;
import com.vmware.talentboost.objects.IssueComment;
import com.vmware.talentboost.objects.Issue;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

public class RESTAssuredE2ETests extends BaseTest{

    private final Dotenv dotenv = Dotenv.load();

    private final String owner = dotenv.get("OWNER");
    private final String repo = dotenv.get("REPO");

    @Test
    /**
     * Test steps:
     * 1. Get all issues DONE
     * 2. Select one issue from the existing DONE
     * 3. Add comment to the issue DONE
     * 4. Assert comment is visible and total comments + 1 DONE
     * 5. Delete comment, assert no longer visible.
     */
    public void testIssueCommentE2E(){
        IssueE2EActor actor = new IssueE2EActor(reqSpec);
        List<Issue> queriedIssues = actor.getIssuesForRepo(owner, repo);
        List<IssueComment> commentsBefore = actor.getCommentsForAnIssue(owner, repo, queriedIssues.get(0).getNumber());
        String comment = "This repo really sucks!!!";
        IssueComment newComment = actor.postCommentToAnIssue(owner, repo, queriedIssues.get(0).getNumber(), comment);
        List<IssueComment> commentsAfter = actor.getCommentsForAnIssue(owner, repo, queriedIssues.get(0).getNumber());
        Assertions.assertEquals(commentsBefore.size() + 1, commentsAfter.size());
        Assertions.assertEquals(comment, newComment.getBody());
        actor.deleteCommentForAnIssue(owner, repo, newComment.getId());
        List<IssueComment> commentsFinal = actor.getCommentsForAnIssue(owner, repo, queriedIssues.get(0).getNumber());
        Assertions.assertEquals(commentsBefore.size(), commentsFinal.size());
    }
}
