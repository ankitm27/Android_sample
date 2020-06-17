package com.cbat.cbat.adapter.original_sortable;

import android.content.Context;

import com.cbat.cbat.R;
import com.cbat.cbat.adapter.TableFixHeaderAdapter;


import java.util.Arrays;
import java.util.List;


/**
 * Created by miguel on 11/02/2016.
 */
public class OriginalSortableTableFixHeaderAdapter extends TableFixHeaderAdapter<
        ItemSortableCheckBox, OriginalFirstHeaderCellViewGroup,
        ItemSortable, OriginalHeaderCellViewGroup,
        NexusWithImage,
        OriginalFirstBodyCellViewGroup,
        OriginalBodyCellViewGroup,
        OriginalSectionCellViewGroup> {
    private Context context;

    public OriginalSortableTableFixHeaderAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected OriginalFirstHeaderCellViewGroup inflateFirstHeader() {
        return new OriginalFirstHeaderCellViewGroup(context);
    }

    @Override
    protected OriginalHeaderCellViewGroup inflateHeader() {
        return new OriginalHeaderCellViewGroup(context);
    }

    @Override
    protected OriginalFirstBodyCellViewGroup inflateFirstBody() {
        return new OriginalFirstBodyCellViewGroup(context);
    }

    @Override
    protected OriginalBodyCellViewGroup inflateBody() {
        return new OriginalBodyCellViewGroup(context);
    }

    @Override
    protected OriginalSectionCellViewGroup inflateSection() {
        return new OriginalSectionCellViewGroup(context);
    }

    @Override
    protected List<Integer> getHeaderWidths() {
        Integer[] witdhs = {
                (int) context.getResources().getDimension(R.dimen._150dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),
                (int) context.getResources().getDimension(R.dimen._90dp),

        };

        return Arrays.asList(witdhs);
    }

    @Override
    protected int getHeaderHeight() {
        return (int) context.getResources().getDimension(R.dimen._35dp);
    }

    @Override
    protected int getSectionHeight() {
        return (int) context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected int getBodyHeight() {
        return (int) context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected boolean isSection(List<NexusWithImage> items, int row) {
        return items.get(row).data == null;
    }
}
