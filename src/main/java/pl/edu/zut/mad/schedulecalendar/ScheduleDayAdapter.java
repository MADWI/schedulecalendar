package pl.edu.zut.mad.schedulecalendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScheduleDayAdapter extends RecyclerView.Adapter<ScheduleDayAdapter.ClassViewHolder> {

    private final Context context;
    private List<Schedule.Hour> hoursInDay;

    public ScheduleDayAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_task_item, parent, false);
        return new ClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        if (position % 2 != 0) {
            int itemViewColor = ContextCompat.getColor(context, R.color.scheduleLightGray);
            int timeViewColor = ContextCompat.getColor(context, R.color.scheduleColorPrimaryDark);
            holder.itemView.setBackgroundColor(itemViewColor);
            holder.hoursView.setBackgroundColor(timeViewColor);
        }
        Schedule.Hour hour = hoursInDay.get(position);
        holder.timeStartView.setText(hour.getStartTime());
        holder.timeEndView.setText(hour.getEndTime());
        holder.nameTypeTextView.setText(hour.getSubjectNameWithType());
        holder.roomLecturerTextView.setText(hour.getLecturerWithRoom());
    }

    @Override
    public int getItemCount() {
        return hoursInDay == null ? 0 : hoursInDay.size();
    }

    public void setHoursInDay(List<Schedule.Hour> hoursInDay) {
        this.hoursInDay = hoursInDay;
        notifyDataSetChanged();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private View hoursView;
        private TextView timeStartView;
        private TextView timeEndView;
        private TextView nameTypeTextView;
        private TextView roomLecturerTextView;

        ClassViewHolder(View view) {
            super(view);
            itemView = view.findViewById(R.id.scheduleTaskItemView);
            hoursView = view.findViewById(R.id.timeGroupView);
            timeStartView = view.findViewById(R.id.timeStartView);
            timeEndView = view.findViewById(R.id.timeEndView);
            nameTypeTextView = view.findViewById(R.id.subjectView);
            roomLecturerTextView = view.findViewById(R.id.lecturerAndRoomView);
        }
    }
}
