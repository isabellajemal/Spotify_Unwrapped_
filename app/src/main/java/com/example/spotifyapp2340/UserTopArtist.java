package com.example.spotifyapp2340;
import java.util.List;

public class UserTopArtist {
     private String userId = "be0luah4gpo9cbfzc3zwellep";
        private String name;
        private String imageUrl;
        private List<UserTopArtist> artists;

        public UserTopArtist() {
            // Default constructor required for calls to DataSnapshot.getValue(UserTopArtists.class)
        }
        public UserTopArtist(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public class UserTopArtistsCollection {
        private String userId;
        private List<UserTopArtist> artists;
        public UserTopArtistsCollection() {
            // Default constructor for Firebase
        }
    }


   public UserTopArtist(String userId, List<UserTopArtist> artists) {
        this.userId = "be0luah4gpo9cbfzc3zwellep";
            this.imageUrl = getImageUrl();
            this.artists = artists;
        }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public List<UserTopArtist> getUserTopArtists() {
            return artists;
        }

        public void setArtists(List<UserTopArtist> artists) {
            this.artists = artists;
        }

        // Ensure you have a matching getter for every setter if you're using Firebase Realtime Database.
    }

