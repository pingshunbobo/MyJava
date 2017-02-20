package UrlMap;

/*
author by w3cschool.cc
Main.java
*/

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

public class UrlMap {
   public static void main(String[] args) throws IOException{
       URL url = new URL("http://www.cybermonkey.io");
       URLConnection conn = url.openConnection();
       
       Map headers = conn.getHeaderFields();
       Set<String> keys = headers.keySet();
       for( String key : keys ){
           String val = conn.getHeaderField(key);
           System.out.println(key+"\t"+val);
       }
       System.out.println( conn.getLastModified() );
   }
}
