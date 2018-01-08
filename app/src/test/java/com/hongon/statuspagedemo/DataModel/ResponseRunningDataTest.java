package com.hongon.statuspagedemo.DataModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Admin on 2018/1/6.
 */
public class ResponseRunningDataTest {
    @Test
    public void _2byteToFloat_sign() throws Exception {
        short a = -5;
        byte[] x =  new byte[]{(byte)((a&0xff00)>>8),(byte)(a&0xff)};
        assertEquals(Float.toString((float)a),new ResponseRunningData()._2byteToFloat_sign(x,0,1.0f));
    }

    @Test
    public void _2byteToFloat() throws Exception {
        short a = -5;
        byte[] x =  new byte[]{(byte)((a&0xff00)>>8),(byte)(a&0xff)};
        assertEquals(Float.toString((float)a),new ResponseRunningData()._2byteToFloat(x,0,1.0f));
    }
}