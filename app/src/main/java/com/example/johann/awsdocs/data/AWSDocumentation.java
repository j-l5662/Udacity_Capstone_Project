package com.example.johann.awsdocs.data;

public class AWSDocumentation {

    //TODO If the url does not have userguide /or guide in it, open up a webpage
    private String documentationName;
    private String url;
    private String text;

    private boolean columnHeader = false;

    public AWSDocumentation(String documentationName,
                            String url) {

        this.documentationName = documentationName;
        this.url = url;
    }

    public String getDocumentationName() {
        return documentationName;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public boolean isColumnHeader() {
        return columnHeader;
    }

    public void setasColumnHeader() {
        this.columnHeader = true;
    }
}
