package com.rpgsim.common;

public class AsyncTask
{
    
    public static void executeAsyncTask(String asyncDescription, Task task)
    {
        Thread asyncThread = new Thread(() ->
        {
            task.invoke();
        }, asyncDescription);
        asyncThread.start();
    }
    
    public static void executeAsyncTask(String asyncDescription, ParameterTask task, Object value)
    {
        Thread asyncThread = new Thread(() ->
        {
            task.invoke(value);
        }, asyncDescription);
        asyncThread.start();
    }
    
}
