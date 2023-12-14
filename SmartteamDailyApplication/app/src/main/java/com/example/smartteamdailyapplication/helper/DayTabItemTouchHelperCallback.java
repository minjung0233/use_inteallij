package com.example.smartteamdailyapplication.helper;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartteamdailyapplication.R;

//import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class DayTabItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter;

    public DayTabItemTouchHelperCallback(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags =  ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getBindingAdapterPosition();
        int toPosition = target.getBindingAdapterPosition();
        mAdapter.onItemMove(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getBindingAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            mAdapter.onItemDelete(position);
        } else if (direction == ItemTouchHelper.RIGHT) {
            mAdapter.onItemCancel(position);
        }
    }

//    @Override
//    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                .addSwipeLeftBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(),R.color.red))
//                .addSwipeLeftActionIcon(R.drawable.ic_delete)
//                .addSwipeRightBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(),R.color.grey))
//                .addSwipeRightActionIcon(R.drawable.ic_cancel)
//                .create()
//                .decorate();
//
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(
                ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white)
        );
        mAdapter.onUpdate();
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            if (viewHolder != null) {
                viewHolder.itemView.setBackgroundColor(
                        ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.primaryDarkColorTheme1)
                );
            }
        }
    }
}
