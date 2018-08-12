package com.bride.rest;

public class Output {
    private String subject;
    private String predicate;
    private String object;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Output{" +
                "subject='" + subject + '\'' +
                ", predicate='" + predicate + '\'' +
                ", object='" + object + '\'' +
                '}';
    }
}
