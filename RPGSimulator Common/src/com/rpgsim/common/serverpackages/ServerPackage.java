package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ServerActions;
import java.io.Serializable;

public abstract class ServerPackage implements Serializable
{
    public abstract void executeServerAction(ServerActions server);
}
