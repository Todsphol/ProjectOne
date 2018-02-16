package th.co.todsphol.add.projectone.activity;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import th.co.todsphol.add.projectone.R;
import th.co.todsphol.add.projectone.UserModel;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private List<UserModel> list;

    public UserAdapter(List<UserModel> list) {
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        UserModel user = list.get(position);
        holder.tvLatitude.setText(user.LAT);
        holder.tvLongitude.setText(user.LON);

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(holder.getAdapterPosition(), 0, 0, "don't");
                contextMenu.add(holder.getAdapterPosition(), 1, 0, "know");

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvLatitude, tvLongitude;

        public UserViewHolder(View itemView) {
            super(itemView);

            tvLatitude = itemView.findViewById(R.id.tv_latitude);
            tvLongitude = itemView.findViewById(R.id.tv_longitude);
        }
    }
}
