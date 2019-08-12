package com.app.eduapp.pojo;

/**
 * Created by User1 on 18-07-2017.
 */

public class ModelInbox {
    private String inboxId;
    private String inbox_title;
    private String inbox_current_time;
    private String inbox_msg;
    private String inbox_subtitle;
    private String inbox_flag;
    private String inbox_date;
    private String inbox_details;
    private String inbox_imageurl;
    private String inbox_read_status;
    private String classtype ;
    private String messagetype ;
    private String cDate ;

    public ModelInbox(String inboxId, String inbox_title, String inbox_current_time,
                      String inbox_msg, String inbox_subtitle, String inbox_flag,
                      String inbox_date, String inbox_details, String inbox_imageurl,
                      String inbox_read_status) {

        this.inbox_flag = inbox_flag;
        this.inboxId = inboxId;
        this.inbox_title = inbox_title;
        this.inbox_current_time = inbox_current_time;
        this.inbox_msg = inbox_msg;
        this.inbox_subtitle = inbox_subtitle;
        this.inbox_date = inbox_date;
        this.inbox_details = inbox_details;
        this.inbox_imageurl = inbox_imageurl;
        this.inbox_read_status = inbox_read_status;

    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public String getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }


    public ModelInbox() {
    }

    public String getInbox_subtitle() {
        return inbox_subtitle;
    }

    public void setInbox_subtitle(String inbox_subtitle) {
        this.inbox_subtitle = inbox_subtitle;
    }

    public String getInbox_flag() {
        return inbox_flag;
    }

    public void setInbox_flag(String inbox_flag) {
        this.inbox_flag = inbox_flag;
    }

    public String getInboxId() {
        return inboxId;
    }

    public void setInboxId(String inboxId) {
        this.inboxId = inboxId;
    }

    public String getInbox_title() {
        return inbox_title;
    }

    public void setInbox_title(String inbox_title) {
        this.inbox_title = inbox_title;
    }

    public String getInbox_current_time() {
        return inbox_current_time;
    }

    public void setInbox_current_time(String inbox_current_time) {
        this.inbox_current_time = inbox_current_time;
    }

    public String getInbox_msg() {
        return inbox_msg;
    }

    public void setInbox_msg(String inbox_msg) {
        this.inbox_msg = inbox_msg;
    }

    public String getInbox_date() {
        return inbox_date;
    }

    public void setInbox_date(String inbox_date) {
        this.inbox_date = inbox_date;
    }

    public String getInbox_details() {
        return inbox_details;
    }

    public void setInbox_details(String inbox_details) {
        this.inbox_details = inbox_details;
    }

    public String getInbox_imageurl() {
        return inbox_imageurl;
    }

    public void setInbox_imageurl(String inbox_imageurl) {
        this.inbox_imageurl = inbox_imageurl;
    }

    public String getInbox_read_status() {
        return inbox_read_status;
    }

    public void setInbox_read_status(String inbox_read_status) {
        this.inbox_read_status = inbox_read_status;
    }
}
