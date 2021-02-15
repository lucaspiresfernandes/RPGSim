package com.rpgsim.common;

public class AsynTask
{
    
    public static void executeAsyncTask(String asyncDescription, Task task)
    {
        Thread asyncThread = new Thread(() ->
        {
            task.invokeTask();
        }, asyncDescription);
        asyncThread.start();
    }
    
}
