package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import java.io.Serializable;

public abstract class ClientPackage implements Serializable
{
    public abstract void executeClientAction(ClientActions client);
}
