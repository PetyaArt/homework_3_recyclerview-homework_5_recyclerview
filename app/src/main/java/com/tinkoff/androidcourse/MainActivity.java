package com.tinkoff.androidcourse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil.DiffResult;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.v7.util.DiffUtil.calculateDiff;

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

        mWorkerList = WorkerGenerator.generateWorkers(4);
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
                update();
            }
        });
    }

    private void update() {
        DiffUtilCallback diffUtilCallback = new DiffUtilCallback(mOldWorkerList, mWorkerList);
        CalculationDiffUtil calculationDiffUtil = new CalculationDiffUtil();
        calculationDiffUtil.execute(diffUtilCallback);
    }

    private class WorkerHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;
        private TextView mAgeTextView;
        private TextView mPositionsTextView;
        private ImageView mImageView;

        WorkerHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.textViewName);
            mAgeTextView = itemView.findViewById(R.id.textViewAge);
            mImageView = itemView.findViewById(R.id.imageViewPhoto);
            mPositionsTextView = itemView.findViewById(R.id.textViewPositions);
        }

        void bind(Worker worker) {
            mNameTextView.setText(worker.getName());
            mAgeTextView.setText(worker.getAge());
            mImageView.setImageResource(worker.getPhoto());
            mPositionsTextView.setText(worker.getPosition());
        }

    }

    private class WorkerAdapter extends RecyclerView.Adapter<WorkerHolder> implements ItemTouchHelperAdapter {

        private List<Worker> mWorkers;

        WorkerAdapter(List<Worker> workers) {
            mWorkers = workers;
        }

        @NonNull
        @Override
        public WorkerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_worker, viewGroup, false);
            return new WorkerHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkerHolder workerHolder, int i) {
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
                    Collections.swap(mWorkers, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mWorkers, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            mWorkers.remove(position);
            update();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_name:
                Collections.sort(mWorkerList, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                break;
            case R.id.sort_by_positions:
                Collections.sort(mWorkerList, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
                break;
            case R.id.sort_by_age:
                Collections.sort(mWorkerList, (o1, o2) -> o1.getAge().compareTo(o2.getAge()));
                break;
        }
        update();
        return super.onOptionsItemSelected(item);
    }

    private class CalculationDiffUtil extends AsyncTask<DiffUtilCallback, Void, DiffResult> {

        @Override
        protected DiffResult doInBackground(DiffUtilCallback...diffUtilCallback) {
            return calculateDiff(diffUtilCallback[0], true);
        }

        @Override
        protected void onPostExecute(DiffResult diffResult) {
            diffResult.dispatchUpdatesTo(mWorkerAdapter);
            mOldWorkerList.clear();
            mOldWorkerList.addAll(mWorkerList);
        }
    }
}

