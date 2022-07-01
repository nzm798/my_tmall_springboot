package com.how2java.tmall.util;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

public class PortUtil {
    public static boolean testPort(int port){
        try {
            ServerSocket ss=new ServerSocket(port);
            ss.close();
            return false;
        }catch (java.net.BindException e){
            return true;
        }catch (IOException e){
            return true;
        }
    }
    public static void checkPort(int port,String server,boolean shutdown){
        if (!testPort(port)){
            if (shutdown){
                String message=String.format("在端口 %d 未检查得到 %s 启动%n",port,server);
                JOptionPane.showMessageDialog(null,message);//该组件确定显示对话框的框架； 如果为null，或者parentComponent没有框架，则使用默认框架。message对话框中存放的信息
                System.exit(1);
            }else {
                String message =String.format("在端口 %d 未检查得到 %s 启动%n,是否继续?",port,server);
                if (JOptionPane.OK_OPTION!=JOptionPane.showConfirmDialog(null,message))
                    System.exit(1);//作用是终止当前正在运行的Java虚拟机，这个status表示退出的状态码，非零表示异常终止。
            }
        }
    }
}
