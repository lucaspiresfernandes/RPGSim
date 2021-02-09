package com.rpgsim.common;

import com.rpgsim.common.sheets.Account;
import com.rpgsim.common.sheets.SheetModel;

public interface ClientActions
{
    public void onConnectionRequestResponse(boolean accepted, String message, Account account, SheetModel model, ConnectionType type);
    
    public void onInstantiateNetworkGameObject(int id, int clientID, Vector2 position, PrefabID pID);
    public void updateNetworkGameObjectTransform(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY);
    public void onNetworkGameObjectDestroy(int id);
}
