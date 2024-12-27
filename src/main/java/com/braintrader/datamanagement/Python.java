package com.braintrader.datamanagement;

import jep.MainInterpreter;

public class Python {

    private Python() {}

    private static final String PYTHON_JEP_PATH="C:\\Program Files\\Python313\\Lib\\site-packages\\jep\\jep.dll";

    public static void setInterpreterPath() {
        MainInterpreter.setJepLibraryPath(PYTHON_JEP_PATH);
    }

}
