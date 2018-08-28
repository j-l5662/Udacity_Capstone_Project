package com.example.johann.awsdocs.data;

public class AWSDocumentation {

    //TODO If the url does not have userguide in it, open up a webpage
    private String documentationHeader;
    private String documentationName;
    private String url;
    private String text;

    private boolean columnHeader = false;

    public AWSDocumentation(String documentationHeader,String documentationName,
                            String url) {

        this.documentationHeader = documentationHeader;
        this.documentationName = documentationName;
        this.url = url;
    }

    public String getDocumentationHeader() {
        return documentationHeader;
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
