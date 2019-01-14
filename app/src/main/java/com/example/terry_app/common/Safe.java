package com.example.terry_app.common;

/**
 * Created by terry on 2018-06-06.
 */

public class Safe {
    public static String encipher(String s){
        int start = s.indexOf("192.168.");//正常返回0
        if((start!=-1)&&(start+1<s.length())){
            String sPort = s.substring(8);//获取剩余字符串
            start = sPort.indexOf(".");
            if((start!=-1)&&(start+1<sPort.length())) {
                String sIP = sPort.substring(0, start);
                int port1 = Integer.parseInt(sIP);
                port1+=213;
                sIP = sPort.substring(start + 1);
                int port2 = Integer.parseInt(sIP);
                port1+=port2;
                port2+=132;
                s = ""+port1+port2;
            }
        }
        return s;
    }
    public static String decipher(String s){
        String sIP = s.substring(0, 3);
        int port1 = Integer.parseInt(sIP);
        port1-=213;
        sIP = s.substring(3);
        int port2 = Integer.parseInt(sIP);
        port2-=132;
        port1-=port2;
        s="192.168."+port1+"."+port2;
        return s;
    }
    public static String getfront(String s){
        String sss="no";
        int start = s.indexOf(".,.");//正常返回4
        if((start!=-1)&&(start+1<s.length())){
            sss = s.substring(0,start);//获取前面字符串
        }
        return sss;
    }
    public static String getrear(String s){
        String port="";
        int start = s.indexOf(".,.");//正常返回0
        if((start!=-1)&&(start+1<s.length())){
            port = s.substring(start+3);//获取后面字符串
        }
        return port;
    }
    public static int getfrontnum(String s){
        int port=0;
        int start = s.indexOf(".,.");//正常返回4
        if((start!=-1)&&(start+1<s.length())){
            String sss = s.substring(0,start);//获取前面字符串
            port=Integer.parseInt(sss);
        }
        return port;
    }
}
