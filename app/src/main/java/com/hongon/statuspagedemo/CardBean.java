package com.hongon.statuspagedemo;

import android.renderscript.Sampler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Admin on 2017/12/23.
 */

public class CardBean {
    private String title;
    public List<CardItemBean> content;
    public CardBean(String title)
    {
        this.title = title;
        content = new ArrayList<>();

    }

    public String getTitle() {
        return title;
    }

    public List<CardItemBean> getContent() {
        return content;
    }

    // inner class
    public class CardItemBean
    {
        String Name;
        String Value;
        public CardItemBean(String name,String value)
        {
            this.setName(name);
            this.setValue(value);
        }
        public String getName() {
            return Name;
        }

        public String getValue() {
            return Value;
        }

        public void setName(String name) {
            Name = name;
        }

        public void setValue(String value) {
            Value = value;
        }
    }
}
