package com.server;

import java.util.List;

public interface IObserver<T> {
    void notifyClients(List<T> list);
}
