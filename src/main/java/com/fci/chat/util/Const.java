package com.fci.chat.util;

public class Const {
    public enum MessageType{
        ERROR((short)0),INFO((short)1);
        MessageType(short key) {
            this.key = key;
        }
        short key;
        public short getKey() {
            return key;
        }
    }
}
