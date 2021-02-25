package com.rpgsim.common.game;

public enum KeyCode
{
    A(0x41),
    B(0x42),
    C(0x43),
    D(0x44),
    E(0x45),
    F(0x46),
    G(0x47),
    H(0x48),
    I(0x49),
    J(0x4A),
    K(0x4B),
    L(0x4C),
    M(0x4D),
    N(0x4E),
    O(0x4F),
    P(0x50),
    Q(0x51),
    R(0x52),
    S(0x53),
    T(0x54),
    U(0x55),
    V(0x56),
    W(0x57),
    X(0x58),
    Y(0x59),
    Z(0x5A),
    NUM9(0x39),
    NUM0(0x30),
    NUM1(0x31),
    NUM2(0x32),
    NUM3(0x33),
    F1(0x70),
    EQUALS(0x3D),
    MINUS(0x2D),
    OPEN_BRACKET(0x5B),
    CLOSE_BRACKET(0x5D),
    COMMA(0x2C),
    PERIOD(0x2E),
    DELETE(0x7F)
    ;
    
    private final int code;

    private KeyCode(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
    
    /*
    public static final int VK_LEFT           = 0x25;
    public static final int VK_UP             = 0x26;
    public static final int VK_RIGHT          = 0x27;
    public static final int VK_DOWN           = 0x28;
    public static final int VK_COMMA          = 0x2C;
    public static final int VK_MINUS          = 0x2D;
    public static final int VK_PERIOD         = 0x2E;
    public static final int VK_SLASH          = 0x2F;
    public static final int VK_0              = 0x30;
    public static final int VK_1              = 0x31;
    public static final int VK_2              = 0x32;
    public static final int VK_3              = 0x33;
    public static final int VK_4              = 0x34;
    public static final int VK_5              = 0x35;
    public static final int VK_6              = 0x36;
    public static final int VK_7              = 0x37;
    public static final int VK_8              = 0x38;
    public static final int VK_9              = 0x39;
    */
    
}
