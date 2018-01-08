package com.hongon.statuspagedemo.DataModel;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by Admin on 2018/1/6.
 */
public class ResponseRunningDataTest {
    private short a = -5;
    private byte[] x;
    @Before
    public void setUp() throws Exception {
        byte[] x = new byte[]{(byte)0xff,(byte)0xfa};
    }


    @Test
    public void _2byteToFloat_sign() throws Exception {

        assertEquals(-5f,new ResponseRunningData()._2byteToFloat_sign(x,0,1.0f));
    }

}