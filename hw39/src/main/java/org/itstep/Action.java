package org.itstep;

import java.io.IOException;

@FunctionalInterface
public interface Action {
    void doIt() throws IOException;
}
