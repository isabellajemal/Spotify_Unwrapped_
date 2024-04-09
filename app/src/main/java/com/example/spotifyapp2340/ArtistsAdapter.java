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

    private List<String> artistNames = new ArrayList<>();
    private List<UserTopArtist> artists;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewArtistName, textViewRanking;
        // Remove imageViewArtist and textViewRanking if not used

        public ViewHolder(View itemView) {
            super(itemView);
            textViewArtistName = itemView.findViewById(R.id.textViewArtistName);
            textViewRanking = itemView.findViewById(R.id.textViewRanking);
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
        String artistName = artistNames.get(position);
        holder.textViewArtistName.setText(artistName);
        holder.textViewRanking.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return artistNames.size();
    }
    public void loadUserTopArtists() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // The document ID is "userId" based on your photo description
        db.collection("userTopArtists").document("userId") // Use the correct document ID here
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // The user ID field within the document is "be0luah4gpo9cbfzc3zwellep"
                            // Assuming 'artists' is the array field you want to retrieve
                            List<String> names = (List<String>) document.get("artists");
                            if (names != null) {
                                this.artistNames.clear();
                                this.artistNames.addAll(names);
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



