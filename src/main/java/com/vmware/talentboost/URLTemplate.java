package com.vmware.talentboost;

/**
 * Class meant to collect templates for easier URL management.
 */
public class URLTemplate {
    public static String IssuesURLTemplate = "/repos/{0}/{1}/issues";
    public static String SingleIssueURLTemplate = IssuesURLTemplate + "/{2}";
    public static String IssueCommentsURLTemplate = SingleIssueURLTemplate + "/comments";
    public static String SingleIssueCommentURLTemplate = IssuesURLTemplate + "/comments/{2}";
}
