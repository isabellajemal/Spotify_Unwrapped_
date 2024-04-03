package com.example.spotifyapp2340;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    private List<UserTopArtist> artists = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewArtist;
        public TextView textViewArtistName;
        public TextView textViewRanking;


        public ViewHolder(View itemView) {
            super(itemView);
            imageViewArtist = itemView.findViewById(R.id.imageViewArtist);
            textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
            textViewRanking = itemView.findViewById(R.id.textViewRanking); // Initialize textViewRanking
        }
    }

    @NonNull
    @Override
    public ArtistsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistsAdapter.ViewHolder holder, int position) {
        UserTopArtist artist = artists.get(position);
        holder.textViewArtistName.setText(artist.getName());
        holder.textViewRanking.setText(String.valueOf(position + 1)); // Rankings start at 1
        Picasso.get().load(artist.getImageUrl()).into(holder.imageViewArtist);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void loadUserTopArtists(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("userTopArtists").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UserTopArtist userTopArtists = document.toObject(UserTopArtist.class);
                            if (userTopArtists != null) {
                                this.artists = userTopArtists.getUserTopArtists(); // Assume this directly gives you List<Artist>
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
