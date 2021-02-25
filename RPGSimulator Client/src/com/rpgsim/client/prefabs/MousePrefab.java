package com.rpgsim.client.prefabs;

import com.rpgsim.client.ClientGame;
import com.rpgsim.common.PrefabID;
import com.rpgsim.common.Vector2;
import com.rpgsim.common.game.Input;
import com.rpgsim.common.game.KeyCode;
import com.rpgsim.common.game.NetworkGameObject;
import com.rpgsim.client.Physics;

public class MousePrefab extends NetworkGameObject
{
    private NetworkGameObject heldObject;
    private Vector2 anchorPoint;
    
    public MousePrefab(int id, int clientID, PrefabID prefabID)
    {
        super(id, clientID, prefabID);
    }

    @Override
    public void update(int clientID, float deltaTime)
    {
        super.update(clientID, deltaTime);
        
        if (clientID != this.getClientID())
            return;
        
        if (Input.mouseMoved())
            setDirty(true);
        
        Vector2 mousePosition = Input.mousePosition();
        transform().position(mousePosition);
        
        Vector2 s = ClientGame.getScreen().getTransform().scale();
        s.x = 1 / s.x;
        s.y = 1 / s.y;
        
        transform().scale(s);
        
        if (heldObject == null)
        {
            if (Input.getMouseButtonDown(1))
            {
                NetworkGameObject obj = Physics.getCollision(this);
                if (obj != null && obj instanceof DraggableObjectPrefab)
                {
                    heldObject = obj;
                    anchorPoint = Vector2.subtract(transform().position(), obj.transform().position());
                }
            }
        }
        else
        {
            if (Input.getMouseButtonUp(1))
            {
                heldObject = null;
                return;
            }
            
            if (Input.getKeyDown(KeyCode.DELETE))
                heldObject.setDestroyed(true);
            
            if (Input.getKey(KeyCode.Z))
                heldObject.transform().scale(new Vector2(heldObject.transform().scale().x + 0.025f, heldObject.transform().scale().y + 0.025f));
            else if (Input.getKey(KeyCode.C))
                heldObject.transform().scale(new Vector2(heldObject.transform().scale().x - 0.025f, heldObject.transform().scale().y - 0.025f));
            
            if (Input.getKey(KeyCode.Q))
                heldObject.transform().rotation(heldObject.transform().rotation() + 2);
            else if (Input.getKey(KeyCode.E))
                heldObject.transform().rotation(heldObject.transform().rotation() - 2);
            
            if (Input.getKeyDown(KeyCode.PERIOD))
                heldObject.transform().flipX(!heldObject.transform().flipX());
            if (Input.getKeyDown(KeyCode.COMMA))
                heldObject.transform().flipY(!heldObject.transform().flipY());
            
            heldObject.transform().position(Vector2.subtract(transform().position(), anchorPoint));
            
            heldObject.setDirty(true);
        }
        
    }
    
}
