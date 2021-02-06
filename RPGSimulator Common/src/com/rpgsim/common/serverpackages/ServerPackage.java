package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ServerActions;

public abstract class ServerPackage
{
    public abstract void executeClientAction(ServerActions client);
}
