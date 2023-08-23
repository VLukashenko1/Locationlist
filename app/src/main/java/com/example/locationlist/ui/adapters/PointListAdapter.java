package com.example.locationlist.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationlist.Constants;
import com.example.locationlist.R;
import com.example.locationlist.data.room.Point;
import com.example.locationlist.util.PointWithDistance;
import com.example.locationlist.util.PointWithDistanceSorter;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class PointListAdapter extends RecyclerView.Adapter<PointListAdapter.ViewHolder> {
    private boolean[] isExpanded;
    private OnItemClickListener listener;
    List<PointWithDistance> pointsWithDistance;
    List<Point> points;
    LatLng currentLocation;
    private final LayoutInflater inflater;

    public PointListAdapter(Context context, List<Point> points, LatLng currentLocation, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.pointsWithDistance = PointWithDistance.getPointListWithDistance(points, currentLocation);
        this.points = points;
        this.listener = listener;
        this.currentLocation = currentLocation;

        isExpanded = new boolean[pointsWithDistance.size()];
        Arrays.fill(isExpanded, false);
    }

    @NonNull
    @Override
    public PointListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.point_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointListAdapter.ViewHolder holder, int position) {
        if (!isExpanded[position]) {
            holder.cardView.setVisibility(View.GONE);
            holder.distance.setText(pointsWithDistance.get(position).distanceString);
            holder.name.setText(pointsWithDistance.get(position).point.name);
            holder.note.setText(pointsWithDistance.get(position).point.note);
            Picasso.get().load(pointsWithDistance.get(position).point.photoLink).into(holder.imageView);
        } else {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.bigDistance.setText(pointsWithDistance.get(position).distanceString);
            holder.bigName.setText(pointsWithDistance.get(position).point.name);
            holder.bigNote.setText(pointsWithDistance.get(position).point.note);
            Picasso.get().load(pointsWithDistance.get(position).point.photoLink).into(holder.bigImageView);
        }
    }

    @Override
    public int getItemCount() {
        return pointsWithDistance.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView distance, name, note;
        ImageView imageView;

        CardView cardView;
        ImageView bigImageView;
        TextView bigDistance, bigName, bigNote;
        ImageButton route, noteEdit;

        ViewHolder(View view) {
            super(view);

            distance = view.findViewById(R.id.rvDestinationTextView);
            name = view.findViewById(R.id.rvPointName);
            note = view.findViewById(R.id.rvPointNote);
            imageView = view.findViewById(R.id.rvPointPhoto);

            cardView = view.findViewById(R.id.rvCardView);
            bigImageView = view.findViewById(R.id.rvBigImageView);
            bigDistance = view.findViewById(R.id.rvBigDistanceTV);
            bigNote = view.findViewById(R.id.rvBigNoteTV);
            bigName = view.findViewById(R.id.rvBigNameTV);

            route = view.findViewById(R.id.rvLayRoute);
            noteEdit = view.findViewById(R.id.rvNoteEdit);

            route.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(pointsWithDistance.get(getAdapterPosition()), 1);
                }
            });
            noteEdit.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(pointsWithDistance.get(getAdapterPosition()), 2);
                }
            });

            view.setOnClickListener(v -> onItemViewClick(getAdapterPosition()));
        }

        private void onItemViewClick(int position) {
            if (position == RecyclerView.NO_POSITION) return;

            isExpanded[position] = !isExpanded[position];

            notifyItemChanged(position);
        }
    }
    public void changePoints(List<Point> pointList){
        this.points = pointList;
        this.pointsWithDistance = PointWithDistance.getPointListWithDistance(pointList, currentLocation);
        notifyDataSetChanged();
    }
    public void changeCurrentLocation(LatLng currentLocation){
        this.currentLocation = currentLocation;
        this.pointsWithDistance = PointWithDistance.getPointListWithDistance(points, currentLocation);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(PointWithDistance point, int code);
    }

    public void sort(Constants.SortTypes type) {
        pointsWithDistance = PointWithDistanceSorter.sort(pointsWithDistance, type);
        notifyDataSetChanged();
    }
}
