package com.example.spotifyapp2340;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class TopTracksAdapter extends RecyclerView.Adapter<TopTracksAdapter.ViewHolder> {

    private List<String> trackNames = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTrackName;
        public TextView textViewRanking;
        public ImageButton buttonPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTrackName = itemView.findViewById(R.id.textViewTrackName);
            textViewRanking = itemView.findViewById(R.id.textViewRanking);
            buttonPlay = itemView.findViewById(R.id.buttonPlay);
        }
    }

    @NonNull
    @Override
    public TopTracksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopTracksAdapter.ViewHolder holder, int position) {
        String trackName = trackNames.get(position);
        holder.textViewTrackName.setText(trackName);
        holder.textViewRanking.setText(String.valueOf(position + 1));
        // Set the onClickListener for buttonPlay if needed
    }

    @Override
    public int getItemCount() {
        return trackNames.size();
    }

    public void loadUserTopTracksThisWeek() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // The document ID is "userId" based on your description
        db.collection("userTopTracks").document("userId") // Use the correct document ID here
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Assuming 'tracks' is the array field you want to retrieve
                            List<String> names = (List<String>) document.get("tracks");
                            if (names != null) {
                                this.trackNames.clear();
                                this.trackNames.addAll(names);
                                notifyDataSetChanged(); // Notify the adapter that data has changed
                            }
                        } else {
                            Log.d("Firestore", "No such document");
                        }
                    } else {
                        Log.d("Firestore", "get failed with ", task.getException());
                    }
                });
    }
}
