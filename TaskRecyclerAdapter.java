package useful.com.todolist.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import useful.com.todolist.MainActivity;
import useful.com.todolist.R;
import useful.com.todolist.model.Task;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static useful.com.todolist.MainActivity.adapter;


/**
 * Created by KarateKid on 12/11/2017.
 */

public class TaskRecyclerAdapter extends RecyclerView.Adapter {

    private static final int REQUEST_SPEECH = 100;
    private List<Task> mTasks;
    private Context mContext;


    public TaskRecyclerAdapter(Context context) {
        mTasks = MainActivity.TASKS;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
        taskViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private View mLayout;
        private CheckBox mCheckBox;
        private Task mTask;
        private ImageView mSpeakButton;
        private int mPosition;

        public TaskViewHolder(View itemView) {
            super(itemView);

            mCheckBox = (CheckBox) itemView.findViewById(R.id.taskCheckBox);
            mLayout = itemView.findViewById(R.id.taskItemLayout);


        }

        public void bind (final int position) {
            mPosition = position;
            mTask = mTasks.get(position);
            mCheckBox.setText(mTask.getDescription());
            mLayout.setBackgroundColor(mTask.getTaskColor());
            if(mTask.isDone()) {
                mCheckBox.setChecked(true);
                mCheckBox.setPaintFlags(mCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mCheckBox.setChecked(false);
            }

            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mTask.isDone()) {
                        mTask.setDone(true);
                        mCheckBox.setPaintFlags(mCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        mTask.setDone(false);
                        mCheckBox.setPaintFlags(mCheckBox.getPaintFlags()  & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    }

                }
            });



            mCheckBox.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String [] options = new String[]{"Delete", "Edit"};
                    AlertDialog.Builder optionBuilder = new AlertDialog.Builder(mContext);
                    optionBuilder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    mTasks.remove(mTask);
                                    notifyDataSetChanged();
                                    break;
                                case 1:
//                                    mTasks.remove(mTask);
                                    MainActivity.promptTask(mTask, mContext);
//                                    notifyItemRemoved(position);
                                    break;
                        }
                    }});
                    optionBuilder.setTitle("Please Select");
                    AlertDialog dialog = optionBuilder.create();
                    dialog.show();
                    notifyDataSetChanged();
                    return true;
                }
            });
        }



    }


}

//C/Users/KarateKid/AndroidStudioProjects/TestGitApp

