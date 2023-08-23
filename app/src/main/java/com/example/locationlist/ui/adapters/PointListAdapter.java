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

import com.example.locationlist.R;
import com.example.locationlist.data.room.Point;
import com.example.locationlist.util.DistanceCalculator;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PointListAdapter extends RecyclerView.Adapter<PointListAdapter.ViewHolder> {
    private boolean[] isExpanded;
    private OnItemClickListener listener;
    List<Point> points;
    List<String> distance;
    LatLng currentLocation;
    private final LayoutInflater inflater;

    public PointListAdapter(Context context, List<Point> points, LatLng currentLocation, OnItemClickListener listener){
        this.inflater = LayoutInflater.from(context);
        this.points = points;
        this.listener = listener;
        this.currentLocation = currentLocation;

        isExpanded = new boolean[points.size()];
        Arrays.fill(isExpanded, false);

        calculateDistance();
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
            holder.distance.setText(distance.get(position));
            holder.name.setText(points.get(position).name);
            holder.note.setText(points.get(position).note);
            Picasso.get().load(points.get(position).photoLink).into(holder.imageView);
        } else {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.bigDistance.setText(distance.get(position));
            holder.bigName.setText(points.get(position).name);
            holder.bigNote.setText(points.get(position).note);
            Picasso.get().load(points.get(position).photoLink).into(holder.bigImageView);
        }
    }

    @Override
    public int getItemCount() {
        return points.size();
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

            route.setOnClickListener(v->{
                if (getAdapterPosition() != RecyclerView.NO_POSITION){
                    listener.onItemClick(points.get(getAdapterPosition()), 1);
                }
            });
            noteEdit.setOnClickListener(v->{
                if (getAdapterPosition() != RecyclerView.NO_POSITION){
                    listener.onItemClick(points.get(getAdapterPosition()), 2);
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
    private void calculateDistance(){
        if (currentLocation == null || currentLocation.longitude == 0 && currentLocation.latitude == 0) {
            String[] s = new String[points.size()];
            Arrays.fill(s, "- 0 -");
            distance = Arrays.asList(s);
            return;
        }
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        try {
            List<String> distanceStringList = distanceCalculator.getSupplier(points, currentLocation).get();
            if (distanceStringList != null){
                distance = distanceStringList;
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePoints(List<Point> pointList){
        points = pointList;
        notifyDataSetChanged();
    }
    public void changeCurrentLocation(LatLng latLng){
        currentLocation = latLng;
        calculateDistance();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Point point, int code);
    }
}
