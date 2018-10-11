package com.tinkoff.androidcourse;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {

    private final List<Worker> oldList;
    private final List<Worker> newList;

    public DiffUtilCallback(List<Worker> oldList, List<Worker> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        Worker oldWorker = oldList.get(i);
        Worker newWorker = newList.get(i1);
        return oldWorker.getName().equals(newWorker.getName());
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        Worker oldWorker = oldList.get(i);
        Worker newWorker = newList.get(i1);
        return oldWorker.getPosition().equals(newWorker.getPosition()) && oldWorker.getAge().equals(newWorker.getAge());
    }
}
