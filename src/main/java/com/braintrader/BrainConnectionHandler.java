package com.braintrader;

import com.braintrader.exceptions.BrainConnectionHandlerException;
import com.ib.controller.ApiController;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BrainConnectionHandler implements ApiController.IConnectionHandler {

    @Getter
    private boolean connected = false;

    @Getter
    private List<String> accountList=new ArrayList<>();

    private Consumer<String> consumerError;
    private Consumer<String> consumerMessage;
    private Consumer<String> consumerShow;

    public BrainConnectionHandler(Consumer<String> consumerError, Consumer<String> consumerMessage, Consumer<String> consumerShow) throws BrainConnectionHandlerException {

        if (consumerError==null) {
            throw new BrainConnectionHandlerException("consumerError is null");
        }

        if (consumerMessage==null) {
            throw new BrainConnectionHandlerException("consumerMessage is null");
        }

        if (consumerShow==null) {
            throw new BrainConnectionHandlerException("consumerShow is null");
        }

        this.consumerError=consumerError;
        this.consumerMessage=consumerMessage;
        this.consumerShow=consumerShow;

    }

    @Override
    public void connected() {

        this.connected=true;

    }

    @Override
    public void disconnected() {
        this.connected=false;
    }

    @Override
    public void accountList(List<String> list) {
        this.accountList=list;
    }

    @Override
    public void error(Exception e) {
        consumerError.accept("exception: "+e.getMessage()+"\n"+e.getCause());
    }

    @Override
    public void message(int i, int i1, String s, String s1) {
        consumerMessage.accept("message: "+i+"/"+i1+"/"+s+"/"+s1);
    }

    @Override
    public void show(String s) {
        consumerShow.accept("show: "+s);
    }

}
