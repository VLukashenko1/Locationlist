package com.example.locationlist.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.locationlist.Constants;
import com.example.locationlist.data.room.Point;
import com.example.locationlist.ui.adapters.PointListAdapter;
import com.example.locationlist.util.PointWithDistance;
import com.example.locationlist.vm.PointListViewModel;
import com.example.locationlist.vm.PointsListLocationViewModel;
import com.example.locationlist.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class PointsListFragment extends Fragment implements PointListAdapter.OnItemClickListener {
    private PointsListLocationViewModel locationViewModel;
    private PointListViewModel pointsVM;
    private RecyclerView recyclerView;
    private PointListAdapter pointListAdapter;
    private LatLng currentLocation;
    private List<Point> points;
    private LifecycleOwner lifecycleOwner;
    private boolean isRecyclerViewCreate = false;
    private View view;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lifecycleOwner = this;

        locationViewModel = new ViewModelProvider(this).get(PointsListLocationViewModel.class);
        pointsVM = new ViewModelProvider(this).get(PointListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_points_list, container, false);

        locationViewModel.checkLocationPermission(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);

        pointsVM.getAllPoints().observe(lifecycleOwner, pointsFromDb -> {
            if (pointsFromDb != null) {
                this.points = pointsFromDb;
                if (!isRecyclerViewCreate) {
                    createRecyclerView();
                } else {
                    updateRecyclerView(points);
                }

            }
        });
        locationViewModel.currentLocation.observe(lifecycleOwner, location -> {
            if (location == null) return;

            currentLocation = location;

            if (isRecyclerViewCreate) {
                updateRecyclerView(location);
            } else {
                createRecyclerView();
            }
        });
        locationViewModel.isLocationPermissionAllow.observe(lifecycleOwner, isEnabled -> {
            if (!isEnabled) {
                Toast.makeText(getContext(), "Location not available", Toast.LENGTH_SHORT).show();
            }
        });
        this.view = view;
        onSortButtonClick();
        return view;
    }

    private void createRecyclerView() {
        if (points == null) return;

        if (currentLocation != null) {
            pointListAdapter = new PointListAdapter(getContext(),
                    points,
                    currentLocation,
                    this::onItemClick);
        } else {
            pointListAdapter = new PointListAdapter(getContext(),
                    points,
                    new LatLng(0, 0),
                    this::onItemClick);
        }

        recyclerView.setAdapter(pointListAdapter);

        isRecyclerViewCreate = true;
    }

    private void updateRecyclerView(List<Point> points) {
        pointListAdapter.changePoints(points);
    }

    private void updateRecyclerView(LatLng latLng) {
        pointListAdapter.changeCurrentLocation(latLng);
    }

    @Override
    public void onItemClick(PointWithDistance point, int code) {
        switch (code) {
            case 1:
                layRoute(point);
                break;
            case 2:
                editNote(point);
                break;
        }
    }

    void layRoute(PointWithDistance point) {
        System.out.println("Route laying");
        String destination = point.point.lat + "," + point.point.lng;
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destination + "&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(mapIntent);
        } else {
            Toast.makeText(getContext(), "it is not possible to build a route", Toast.LENGTH_SHORT).show();
        }
    }

    void editNote(PointWithDistance point) {
        System.out.println("Note Edit");
        EditNoteDialog dialogFragment = new EditNoteDialog(point);
        dialogFragment.show(getFragmentManager(), "CustomDialogFragment");
    }

    public void onSortButtonClick() {
        Button sortByName, sortByNote, sortByDistance;
        ImageButton reverse;

        sortByName = view.findViewById(R.id.SortByNameBtn);
        sortByNote = view.findViewById(R.id.SortByNoteBtn);
        sortByDistance = view.findViewById(R.id.SortByDistanceBtn);
        reverse = view.findViewById(R.id.sortReverceButton);
        sortByName.setOnClickListener(v -> pointListAdapter.sort(Constants.SortTypes.BY_NAME));
        sortByNote.setOnClickListener(v -> pointListAdapter.sort(Constants.SortTypes.BY_NOTE));
        sortByDistance.setOnClickListener(v -> pointListAdapter.sort(Constants.SortTypes.BY_DISTANCE));
        reverse.setOnClickListener(v -> pointListAdapter.sort(Constants.SortTypes.REVERSE));

    }
}