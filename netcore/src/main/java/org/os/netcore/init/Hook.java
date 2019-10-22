package org.os.netcore.init;

public interface Hook<D> {
    boolean hook(D d);
}
