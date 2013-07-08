package com.conventnunnery.libraries.config;

import java.io.InputStream;

public interface ConventConfiguration {

    boolean load();

    boolean save();

    void setDefaults(InputStream inputStream);

}
