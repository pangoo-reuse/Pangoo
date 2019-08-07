package org.os.netcore.init;

public interface Sign {
    String encrypt(String text);
    String decrypt(String text);
}
