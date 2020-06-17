package com.cbat.cbat.adapter.original;

/**
 * Created by miguel on 13/02/2016.
 */
public class Nexus {
    public final String type;
    public final String[] data;
    public final String indicatorColor;

    public Nexus(String type) {
        this.type = type;
        data = null;
        this.indicatorColor=null;
    }

    public Nexus(String customer, String total, String april, String may, String june, String july, String august,String september,String octuber,String november,String december,String january,String febuary,String march) {
        this.type = null;
        this.indicatorColor=null;
        data = new String[] {
                customer,
                total,
                april,
                may,
                june,
                july,
                august,september,octuber,november,december,january,febuary ,march};

    }

    public Nexus(String type,String indicatorColor,String customer, String total, String april, String may, String june, String july, String august,String september,String octuber,String november,String december,String january,String febuary,String march) {
        this.type = type;
        data = new String[] {
                customer,
                total,
                april,
                may,
                june,
                july,
                august,september,octuber,november,december,january,febuary ,march};
        this.indicatorColor=indicatorColor;
    }
    public Nexus(String type, String[] data) {
        this.type = type;
        this.data = data;
        this.indicatorColor = null;
    }

    public Nexus(String type, String[] data,String indicatorColor) {
        this.type = type;
        this.data = data;
        this.indicatorColor = indicatorColor;
    }
    public boolean isSection() {
        return data == null;
    }
}