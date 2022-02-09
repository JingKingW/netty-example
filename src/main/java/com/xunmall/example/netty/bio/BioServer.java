package com.xunmall.example.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wangyanjing
 * @date 2020/7/23 22:19
 */
public class BioServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9999);

        Socket socket = serverSocket.accept();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        String request, response;

        while ((request = bufferedReader.readLine()) != null) {
            if ("Done".equals(request)) {
                break;
            }
            response = processRequest(request);
            printWriter.println(response);

        }
    }

    private static String processRequest(String request) {
        System.out.println(request);
        return request;
    }

}
