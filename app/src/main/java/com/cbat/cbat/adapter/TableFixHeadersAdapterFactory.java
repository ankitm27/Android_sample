package com.cbat.cbat.adapter;

import android.content.Context;

import com.cbat.cbat.adapter.basic.BasicTableFixHeader;
import com.cbat.cbat.adapter.original.OriginalTableFixHeader;
import com.cbat.cbat.adapter.original_sortable.OriginalSortableTableFixHeader;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;

/**
 * Created by miguel on 12/02/2016.
 */
public class TableFixHeadersAdapterFactory {
    public static final int ORIGINAL = 0, BASIC = 1, ORIGINAL_SORTABLE = 2;
    private Context context;

    public TableFixHeadersAdapterFactory(Context context) {
        this.context = context;
    }

    public BaseTableAdapter getAdapter(int type) {
        switch (type) {
            case ORIGINAL: return new OriginalTableFixHeader(context).getInstance();
            case BASIC: return new BasicTableFixHeader(context).getInstance();
            case ORIGINAL_SORTABLE: return new OriginalSortableTableFixHeader(context).getInstance();
            default: return new OriginalTableFixHeader(context).getInstance();
        }
    }
}
