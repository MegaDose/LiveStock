package com.bongi.emobilepastoralism.loginreg;

public class Member {
	
	 //private variables
    String _name;
	String _knownas;
	String _location;
	String _about;
	String _insp;
	String _username;
	String _genre;
	String _sex;
	String _follow;

	  // Empty constructor
    public Member(){
         
    }
    // constructor
    public Member(String name, String knownas, String location, String about,String insp, String username, String genre,String sex,String follow ){
        this._name = name;
        this._knownas = knownas;
        this._location = location;
        this._about =about;
        this._insp =insp;
    	this._username =username;
    	this._genre =genre;
    	this._sex =sex;
    	this._follow =follow;
        
    }
    
    // getting values
    public String getName(){
        return this._name;
    }
    public String getKnownas(){
        return this._knownas;
    }
    public String getLocation(){
        return this._location;
    }
    public String getAbout(){
        return this._about;
    }
    public String getIsnp(){
        return this._insp;
    }
    public String getUsername(){
        return this._username;
    }
    public String getGenre(){
        return this._genre;
    }
    public String getSex(){
        return this._sex;
    }
    public String getFollow(){
        return this._follow;
    }
     
    // setting values
    public void setName(String name){
        this._name = name;
    }
    public void setKnownas(String knownas){
        this._knownas = knownas;
    }
    public void setLocation(String location){
        this._location = location;
    }
    public void setAbout(String about){
        this._about = about;
    }
    public void setInsp(String insp){
        this._insp = insp;
    }
    public void setUserName(String username){
        this._username = username;
    }
    public void setGenre(String genre){
        this._genre = genre;
    }
    public void setSex(String sex){
        this._sex = sex;
    }
    public void setFollow(String follow){
        this._follow = follow;
    }
    
}
