package com.cbat.cbat.adapter.original_sortable;


import com.cbat.cbat.adapter.original.Nexus;

import java.util.List;

/**
 * Created by miguel on 06/03/2016.
 */
public class NexusWithImage extends Nexus {
    public List<Integer> resImages;

    public NexusWithImage(String type, List<Integer> resImages) {
        super(type);
        this.resImages = resImages;
    }

    public NexusWithImage(String customer, String total, String april, String may, String june, String july, String august,String september,String octuber,String november,String december,String january,String febuary,String march) {
        super(customer,
                total,
                april,
                may,
                june,
                july,
                august,september,octuber,november,december,january,febuary ,march);
    }

    public NexusWithImage(String type, String customer, String total, String april, String may, String june, String july, String august,String september,String octuber,String november,String december,String january,String febuary,String march) {
        super(customer,
                total,
                april,
                may,
                june,
                july,
                august,september,octuber,november,december,january,febuary ,march);
    }
    public NexusWithImage(String type, String[] data) {
        super(type,data);
    }

    public NexusWithImage(String type, String[] data,String indicatorColor) {
        super(type,data, indicatorColor);
       // this.indicatorColor=indicatorColor;
    }

//

}
