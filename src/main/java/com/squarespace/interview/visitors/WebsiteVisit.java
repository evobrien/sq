package com.squarespace.interview.visitors;

public class WebsiteVisit {

  private final String visitorId;
  private final long timestamp;

  WebsiteVisit(String visitorId, long timestamp) {
    this.visitorId = visitorId;
    this.timestamp = timestamp;
  }

  public String getVisitorId() {
    return visitorId;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
