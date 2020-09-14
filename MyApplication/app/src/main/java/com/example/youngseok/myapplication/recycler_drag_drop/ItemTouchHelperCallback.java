package com.example.youngseok.myapplication.recycler_drag_drop;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    ItemTouchHelperListener listener;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener){
        this.listener=listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){

        int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START|ItemTouchHelper.END;
        return makeMovementFlags(dragFlags,0);

    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder source,
                          RecyclerView.ViewHolder target){
        return listener.onItemMove(source.getAdapterPosition(),
                target.getAdapterPosition());
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                         int direction){
        listener.onItemRemove(viewHolder.getAdapterPosition());
    }

}
