package com.tinkoff.androidcourse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.DiffUtil.DiffResult;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Worker> mWorkerList;
    private List<Worker> mOldWorkerList;
    private WorkerAdapter mWorkerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWorkerList = WorkerGenerator.generateWorkers(2);
        mOldWorkerList = new ArrayList<>(mWorkerList);

        mWorkerAdapter = new WorkerAdapter(mWorkerList);
        recyclerView.setAdapter(mWorkerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(itemDecoration);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCustom(mWorkerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Worker worker = WorkerGenerator.generateWorker();
                mWorkerList.add(worker);
                DiffUtilCallback diffUtilCallback = new DiffUtilCallback(mOldWorkerList, mWorkerList);
                CalculationDiffUtil calculationDiffUtil = new CalculationDiffUtil(worker);
                calculationDiffUtil.execute(diffUtilCallback);
            }
        });
    }

    private class WorkerHolder extends RecyclerView.ViewHolder {

        private Worker mWorker;

        private TextView mNameTextView;
        private TextView mAgeTextView;
        private ImageView mImageView;

        public WorkerHolder(@NonNull View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.textViewName);
            mAgeTextView = itemView.findViewById(R.id.textViewAge);
            mImageView = itemView.findViewById(R.id.imageViewPhoto);
        }

        public void bind(com.tinkoff.androidcourse.Worker worker){
            mWorker = worker;

            mNameTextView.setText(mWorker.getName());
            mAgeTextView.setText(mWorker.getAge());
            mImageView.setImageResource(mWorker.getPhoto());
        }

    }

    private class WorkerAdapter extends RecyclerView.Adapter<WorkerHolder> implements ItemTouchHelperAdapter{

        List<Worker> mWorkers;

        public WorkerAdapter(List<Worker> workers) {
            mWorkers = workers;
        }

        @Override
        public WorkerHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_worker, viewGroup, false);
            return new WorkerHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkerHolder workerHolder, int i) {
            Log.d("myLogs", "onBindViewHolder");
            workerHolder.bind(mWorkers.get(i));
        }


        @Override
        public int getItemCount() {
            return mWorkers.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mWorkerList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mWorkerList, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            mWorkerList.remove(position);
            DiffUtilCallback diffUtilCallback = new DiffUtilCallback(mOldWorkerList, mWorkerList);
            CalculationDiffUtil calculationDiffUtil = new CalculationDiffUtil(position);
            calculationDiffUtil.execute(diffUtilCallback);
        }

    }

    private class CalculationDiffUtil extends AsyncTask<DiffUtilCallback, Void, DiffResult> {

        Worker worker;
        int position;

        public CalculationDiffUtil(Worker worker) {
            this.worker = worker;
        }

        public CalculationDiffUtil(int position) {
            this.position = position;
        }

        @Override
        protected DiffResult doInBackground(DiffUtilCallback...diffUtilCallback) {
            DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback[0], false);
            return diffResult;
        }

        @Override
        protected void onPostExecute(DiffResult diffResult) {
            diffResult.dispatchUpdatesTo(mWorkerAdapter);
            if (worker != null) {
                mOldWorkerList.add(worker);
            } else {
                mOldWorkerList.remove(position);
            }
        }

    }


}
