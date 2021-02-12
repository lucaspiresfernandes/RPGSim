package com.rpgsim.client.prefabs;

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
            
            if (Input.getKeyDown(KeyCode.EQUALS))
            {
                
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
            
            if (Input.getKeyDown(KeyCode.EQUALS))
                heldObject.transform().scale(new Vector2(heldObject.transform().scale().x + 0.1f, heldObject.transform().scale().y + 0.1f));
            else if (Input.getKeyDown(KeyCode.MINUS))
                heldObject.transform().scale(new Vector2(heldObject.transform().scale().x - 0.1f, heldObject.transform().scale().y - 0.1f));
            
            if (Input.getKeyDown(KeyCode.OPEN_BRACKET))
                heldObject.transform().rotation(heldObject.transform().rotation() - 15);
            else if (Input.getKeyDown(KeyCode.CLOSE_BRACKET))
                heldObject.transform().rotation(heldObject.transform().rotation() + 15);
            
            if (Input.getKeyDown(KeyCode.PERIOD))
                heldObject.transform().flipX(!heldObject.transform().flipX());
            if (Input.getKeyDown(KeyCode.COMMA))
                heldObject.transform().flipY(!heldObject.transform().flipY());
            
            heldObject.transform().position(Vector2.subtract(transform().position(), anchorPoint));
            
            heldObject.setDirty(true);
        }
        
    }
    
}
