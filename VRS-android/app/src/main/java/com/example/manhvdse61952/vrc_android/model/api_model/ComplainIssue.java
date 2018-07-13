package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class ComplainIssue implements Serializable {
    private boolean isInsideIssue;
    private boolean isOutsideIssue;
    private boolean isOwnerIssue;
    private String issueContent;

    public ComplainIssue(boolean isInsideIssue, boolean isOutsideIssue, boolean isOwnerIssue, String issueContent) {
        this.isInsideIssue = isInsideIssue;
        this.isOutsideIssue = isOutsideIssue;
        this.isOwnerIssue = isOwnerIssue;
        this.issueContent = issueContent;
    }

    public ComplainIssue() {
    }


    public boolean isInsideIssue() {
        return isInsideIssue;
    }

    public void setInsideIssue(boolean insideIssue) {
        isInsideIssue = insideIssue;
    }

    public boolean isOutsideIssue() {
        return isOutsideIssue;
    }

    public void setOutsideIssue(boolean outsideIssue) {
        isOutsideIssue = outsideIssue;
    }

    public boolean isOwnerIssue() {
        return isOwnerIssue;
    }

    public void setOwnerIssue(boolean ownerIssue) {
        isOwnerIssue = ownerIssue;
    }

    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }
}
