package com.xunmall.example.netty.rpc.simple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: wangyj03
 * @Description:
 * @Date: 2022/2/9 16:27
 */
public class SimpleRpcFramework {

    public static void export(Object service, int port) throws Exception {

        ServerSocket server = new ServerSocket(port);
        while (true) {
            Socket socket = server.accept();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        String methodName = (String) input.readObject();
                        Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                        Object[] arguments = (Object[]) input.readObject();

                        Method method = service.getClass().getMethod(methodName, parameterTypes);

                        Object result = method.invoke(service, arguments);

                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }


    }

    public static <T> T refer(Class<T> interfaceClass, String host, int port) throws Exception {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket(host, port);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(method.getName());
                output.writeObject(method.getParameterTypes());
                output.writeObject(args);
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                Object result = input.readObject();
                return result;
            }
        });
    }


}
