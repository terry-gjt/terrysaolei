package com.example.terry_app.common;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Enumeration;

import static com.example.terry_app.common.Safe.encipher;

/**
 * Created by terry on 2018-05-19.
 */

public class Connectserver extends Thread{
    public ServerSocket serverserverSocket;
    public boolean serverRunning=false;
    private Handler shandler;
    private boolean isConnecting = false;
    private Socket mSocketServer = null;
    private PrintWriter mPrintWriterServer = null;
    private String []mesage=new String[10];
    private int front=0, rear=0;
    private BufferedReader mBufferedReaderServer;
    private String MessageServer="";

    public void sethandler(Handler h){
        shandler=h;
    }
    public void stopserver() {
        if(serverRunning)
        {
            serverRunning = false;
            try
            {
                if(mSocketServer!=null)
                {
                    mSocketServer.close();
                    mSocketServer = null;
                    mPrintWriterServer.close();
                    mPrintWriterServer = null;

                    Log.i("线程关闭","正常关闭线程");
                }
            }catch (IOException e) {
                Log.i("线程结束异常","出现异常"+e.getMessage());
            }
            try {
                if(serverserverSocket!=null)
                    serverserverSocket.close();
            } catch (IOException e) {
                Log.i("线程结束异常","serverserverSocket关闭异常"+e.getMessage());
            }
            this.interrupt();
        }else
        {
            Log.i("线程关闭","线程不在运行");
        }
    }
    public void startserver(){
        if (!serverRunning){
            serverRunning=true;
            this.start();
        }
        else Log.i("线程启动","线程已经运行了");
    }
    public boolean sendstring(String ss) {
        if(isConnecting)
        {
            if(ss.length()<=0)
            {
                Log.i("线程connecserver","发送内容不能为空！");
            }else
            {
                try
                {
                    mPrintWriterServer.print(ss);
                    mPrintWriterServer.flush();
                    Log.i("线程connecserver","成功发送："+ss);
                    return true;
                }catch (Exception e) {
                    Log.i("线程connecserver","发送异常："+e.getMessage());
                }
            }
        }else
        {
            Log.i("线程connecserver","没有连接");
        }
        return false;
    }
    private void receivemess(String ss){//收到消息
        if(shandler!=null){
            mesage[rear]=ss;//放入消息
            if(rear<9)rear++;
            else {
                rear=0;
            }
            Message msg = new Message();
            msg.what = 1;
            shandler.sendMessage(msg);
        }
    }
    public String getmess(){
        String s=mesage[front];
        mesage[front]=null;
        if(front<9)front++;
        else {
            front=0;
        }
        return s;
    }
    public boolean isConnecting(){
        return isConnecting;
    }
    private String getInfoBuff(char[] buff, int count)	{
        char[] temp = new char[count];
        for(int i=0; i<count; i++)
        {
            temp[i] = buff[i];
        }
        return new String(temp);
    }
    @Override
    public void run() {
        try
        {
            try {
                serverserverSocket = new ServerSocket(55544);
                getLocalAddress();
            } catch (IOException e) {
                Log.i("线程connecserver","本机55544端口被占用，请退出冲突程序");
                e.printStackTrace();
            }
            SocketAddress address = null;
            if(!serverserverSocket.isBound())
            {
                serverserverSocket.bind(address,0);
            }
            mSocketServer=serverserverSocket.accept();
            mBufferedReaderServer=new BufferedReader(new InputStreamReader(mSocketServer.getInputStream()));
            mPrintWriterServer=new PrintWriter(mSocketServer.getOutputStream(),true);
            isConnecting=true;
            receivemess("0001.,.666");
            Log.i("线程connecserver","对方已连接！");
        }catch (IOException e) {
            Log.i("线程connecserver","对方连接异常:" + e.getMessage()/* + e.toString()*/);
            return;
        }
        char[] buffer = new char[256];
        int count = 0;
        while(serverRunning)
        {
            try
            {
                if((count=mBufferedReaderServer.read(buffer))>0);
                {
                    MessageServer = getInfoBuff(buffer, count);//消息换行
                    receivemess(MessageServer);
                }
            }
            catch (Exception e){
                Log.i("线程connecserver","其他异常:" + e.getMessage());
                isConnecting=false;
                return;
            }
        }
    }
    private void getLocalAddress( ){
        String MessageServer="";
        try
        {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    String ipstr=inetAddress.getHostAddress();
                    int start = ipstr.indexOf("192.168");
                    if((start!=-1)&&(start+1<ipstr.length())){
                        Log.i("密码测试","ip为"+ipstr);
                        MessageServer="房间密钥为："+encipher(ipstr);
                        Log.i("密码测试",MessageServer);
                    }
//                    MessageServer += "请连接IP："+inetAddress.getHostAddress()+"\n";
                }
            }
        }catch (SocketException ex) {
            Log.i("线程server","获取IP地址异常:" + ex.getMessage());
        }
        if("".equals(MessageServer))
            receivemess("获取IP地址异常");
        else
            receivemess(MessageServer);
    }

}
