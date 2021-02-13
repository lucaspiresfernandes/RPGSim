package com.rpgsim.common;

import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.UpdateField;

public interface ServerActions
{
    public void onClientConnection(int connectionID, String username, String password, ConnectionType type);
    
    public void onNetworkGameObjectTransformUpdate(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY);
    
    public void onNetworkGameObjectRequest(Vector2 position, PrefabID pID, int clientID, String imageRelativePath);
    
    public void onNetworkGameObjectImageUpdate(int id, String relativePath);
    
    public void onNetworkGameObjectDestroy(int id);
    
    public void onCharacterSheetFieldUpdate(int connectionID, UpdateField field, Object newValue, int propertyIndex, UpdateType type);
    
}
