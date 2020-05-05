package com.sx.tank.model;

import com.sx.tank.utils.MsgType;

public abstract class Msg {
    public abstract byte[] toBytes();

    public abstract void parse(byte[] bytes);

    public abstract void handle();

    public abstract MsgType getMsgType();
}
