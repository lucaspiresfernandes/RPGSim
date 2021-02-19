package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;

public class BackgroundUpdateResponse extends ClientPackage
{
    private String relativePath;

    public BackgroundUpdateResponse()
    {
    }

    public BackgroundUpdateResponse(String relativePath)
    {
        this.relativePath = relativePath;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
       client.onBackgroundUpdate(relativePath);
    }

}
