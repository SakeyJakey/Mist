package dev.sakey.mist.utils.client;

import dev.sakey.mist.Mist;

public class Logger {

    public void log(String s){
        System.out.println(Mist.instance.name + " - [Log] " + s);
    }

    public void warn(String s){
        System.out.println(Mist.instance.name + " - [Warning] " + s);
    }

    public void error(String s){
        System.err.println(Mist.instance.name + " - [Error] " + s);
    }
}