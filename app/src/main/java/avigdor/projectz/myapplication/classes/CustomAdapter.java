package avigdor.projectz.myapplication.classes;
import static avigdor.projectz.myapplication.classes.FBRef.refTasks;
import static avigdor.projectz.myapplication.classes.FBRef.userID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import avigdor.projectz.myapplication.R;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Tasks> tasks;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<Tasks> tasks) {
        this.context = context;
        this.tasks = tasks;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.custom_lv, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        Tasks task = tasks.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.taskClass.setText(task.getTaskClass());
        if (task.getTaskStatus().equals("active")) {
            holder.checked.setChecked(false);
        }else {
            holder.checked.setChecked(true);
        }
        //holder.checked.setChecked(false);

        holder.checked.setOnCheckedChangeListener((buttonView, isChecked) -> {

            holder.checked.setChecked(isChecked);
            if (holder.checked.isChecked()) {
                task.setTaskStatus("done");

            }else {
                task.setTaskStatus("active");
            }
            refTasks.child(userID).child(task.getTaskYear()).child(task.getTaskClass()+task.getTaskNumClass()).child(task.getTaskEndDate()).setValue(task);
        });
        return view;
    }
    private class ViewHolder {
        TextView taskName, taskClass;
        CheckBox checked;

        public ViewHolder(View view) {
            taskName = view.findViewById(R.id.name);
            taskClass = view.findViewById(R.id.Class);
            checked = view.findViewById(R.id.check);
        }
    }
}
