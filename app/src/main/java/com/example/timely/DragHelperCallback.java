package com.example.timely;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragHelperCallback extends ItemTouchHelper.Callback {
    private final DragHelperAdapter adapter;
    private boolean isVertial;
    public DragHelperCallback(DragHelperAdapter adapter) {
        this.adapter = adapter;
        this.isVertial = true;
    }
    public DragHelperCallback(DragHelperAdapter adapter, boolean isVertial) {
        this.adapter = adapter;
        this.isVertial = isVertial;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (isVertial) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        } else {
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // no swipe implemented
    }
}
