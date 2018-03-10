package th.co.todsphol.add.projectone.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import th.co.todsphol.add.projectone.R;
import th.co.todsphol.add.projectone.activity.maptimes.MapsFiftyMinuteActivity;
import th.co.todsphol.add.projectone.activity.maptimes.MapsFortyMinuteActivity;
import th.co.todsphol.add.projectone.activity.maptimes.MapsSixtyMinuteActivity;
import th.co.todsphol.add.projectone.activity.maptimes.MapsTenMinuteActivity;
import th.co.todsphol.add.projectone.activity.maptimes.MapsThirtyMinuteActivity;
import th.co.todsphol.add.projectone.activity.maptimes.MapsTwentyMinuteActivity;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private List<UserModel> list;
    private Context context;


    public UserAdapter(List<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        UserModel user = list.get(position);
        holder.tvLatitude.setText(user.LAT);
        holder.tvLongitude.setText(user.LON);
        Integer index = (list.indexOf(user) + 1) * 10;
        holder.tvTime.setText(index.toString());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (position == 0) {
                    Intent tenMinute = new Intent(context, MapsTenMinuteActivity.class);
                    context.startActivity(tenMinute);
                } else if (position == 1) {
                    Intent twentyMinute = new Intent(context, MapsTwentyMinuteActivity.class);
                    context.startActivities(new Intent[]{twentyMinute});
                } else if (position == 2) {
                    Intent thirtyMinute = new Intent(context, MapsThirtyMinuteActivity.class);
                    context.startActivities(new Intent[]{thirtyMinute});
                } else if (position == 3) {
                    Intent fortyMinute = new Intent(context, MapsFortyMinuteActivity.class);
                    context.startActivities(new Intent[]{fortyMinute});
                } else if (position == 4) {
                    Intent fiftyMinute = new Intent(context, MapsFiftyMinuteActivity.class);
                    context.startActivities(new Intent[]{fiftyMinute});
                } else if (position == 5) {
                    Intent sixtyMinute = new Intent(context, MapsSixtyMinuteActivity.class);
                    context.startActivities(new Intent[]{sixtyMinute});
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvLatitude, tvLongitude, tvTime;
        ItemClickListener itemClickListener;

        UserViewHolder(View itemView) {
            super(itemView);

            tvLatitude = itemView.findViewById(R.id.tv_latitude);
            tvLongitude = itemView.findViewById(R.id.tv_longitude);
            tvTime = itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
