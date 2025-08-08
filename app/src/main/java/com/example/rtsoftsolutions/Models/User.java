package com.example.rtsoftsolutions.Models;

public class User {

    private String user_name ;
    private String user_role ;
    private String user_id ;
    private String user_course ;
    private String user_batch ;
    private long user_phoneno ;


public User() {

    }

public User(String name , String role , String id , String course , String batch , long phoneno) {
         user_name = name ;
         user_role = role ;
         user_id = id ;
         user_course = course ;
         user_batch = batch;
         user_phoneno = phoneno ;
    }
   // all getters
    public String getUser_name() {
         return this.user_name ;
    }
    public String getUser_role() {
         return this.user_role ;
    }
    public String getUser_id() {
         return this.user_id ;
    }
    public String getUser_course() {
         return this.user_course ;
    }
    public String getUser_batch() {
         return this.user_batch ;
    }


    // all setters
    public void setUserName(String username) {
    user_name = username ;
    }
    public void setUserRole(String userrole) {
    user_role = userrole ;
    }
    public void setUser_id(String userid) {
    user_id = userid ;
    }
    public void setUser_course(String usercourse){
    user_course = usercourse ;
    }
    public void setUser_batch(String userbatch){
    user_batch = userbatch ;
    }

}
