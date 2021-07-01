package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String playlistName;
    private final ArrayList<Video> videosInPlaylist = new ArrayList<>();

    public VideoPlaylist(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public int getSize(){
        return videosInPlaylist.size();
    }

    public void addVideo(Video video){
        videosInPlaylist.add(video);
    }

    public void removeVideo(Video video){
        videosInPlaylist.remove(video);
    }

    public void clearPlaylist(){
        videosInPlaylist.removeAll(videosInPlaylist);
    }

    public boolean videoAlreadyAdded(String id){
        for (Video v: videosInPlaylist){
            if (v.getVideoId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Video> getPlaylist(){
        return videosInPlaylist;
    }


    public List<Video> getVideosInPlaylist() {
        return videosInPlaylist;
    }


    @Override
    public String toString() {
        return "VideoPlaylist{" +
                "playlistName='" + playlistName + '\'' +
                ", videosInPlaylist=" + videosInPlaylist +
                '}';
    }
}
