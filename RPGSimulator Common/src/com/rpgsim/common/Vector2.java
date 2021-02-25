package com.rpgsim.common;

public class Vector2
{
    public float x, y;

    public Vector2()
    {
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Vector2(double x, double y)
    {
        this.x = (float) x;
        this.y = (float) y;
    }
    
    public Vector2(Vector2 other)
    {
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public String toString()
    {
        return "Vector2{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Float.floatToIntBits(this.x);
        hash = 17 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector2 other = (Vector2) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        return true;
    }
    
    private static float lerp(float firstFloat, float secondFloat, float by)
    {
         return firstFloat * (1 - by) + secondFloat * by;
    }
    
    public static Vector2 lerp(Vector2 a, Vector2 b, float t)
    {
        return new Vector2(lerp(a.x, b.x, t), lerp(a.y, b.y, t));
    }
    
    public static Vector2 subtract(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x - b.x, a.y - b.y);
    }
    
    public static Vector2 add(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x + b.x, a.y + b.y);
    }
    
    public static Vector2 multiply(Vector2 a, float b)
    {
        return new Vector2(a.x * b, a.y * b);
    }
    
    public static Vector2 divide(Vector2 a, float b)
    {
        return new Vector2(a.x / b, a.y / b);
    }
    
}
