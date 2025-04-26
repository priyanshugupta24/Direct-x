package com.URLShorten.DTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OutputJsonDTO {
    private List<String> ip; 
    private List<String> browserName; 
    private List<String> osName;
    public OutputJsonDTO(){
        this.ip = new ArrayList<>();
        this.browserName = new ArrayList<>();
        this.osName = new ArrayList<>();
    }
    public void addToIp(String inputIp){
        // System.out.println(inputIp);
        this.ip.add(inputIp);
    }
    public void addToBrowserName(String inputBrowserName){
        System.out.println(inputBrowserName);
        this.browserName.add(inputBrowserName);
    }
    public void addToOsName(String inputOsName){
        System.out.println(inputOsName);
        this.osName.add(inputOsName);
    }
    public List<String> getListIp(){
        return Collections.unmodifiableList(this.ip);
    }
    public List<String> getListBrowserName(){
        return Collections.unmodifiableList(this.browserName);
    }
    public List<String> getListOsName(){
        return Collections.unmodifiableList(this.osName);
    }
}
