package com.example.youngseok.myapplication.recycler_drag_drop;

import android.util.Log;

public interface ItemTouchHelperListener {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemRemove(int position);

}
