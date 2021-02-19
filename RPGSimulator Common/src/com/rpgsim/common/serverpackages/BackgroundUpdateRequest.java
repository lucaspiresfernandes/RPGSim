package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ServerActions;

public class BackgroundUpdateRequest extends ServerPackage
{
    private String relativePath;

    public BackgroundUpdateRequest()
    {
    }

    public BackgroundUpdateRequest(String relativePath)
    {
        this.relativePath = relativePath;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        server.onBackgroundUpdate(relativePath);
    }

}
