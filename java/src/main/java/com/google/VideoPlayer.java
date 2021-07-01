package com.google;

import java.util.*;
import java.util.stream.Collectors;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;

  private VideoPlaylist videoPlaylist;

  private List <Video> videos = new VideoLibrary().getVideos() .stream()
          .sorted(Comparator.comparing(Video :: getTitle))
          .collect(Collectors.toList());

  private List<VideoPlaylist> allPlaylists = new ArrayList<>();

  private Video currentVideo = null;

  private Boolean pause = false;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.printf("Here's a list of all available videos:%n");
    for(Video video : videos){
      System.out.printf(video.getTitle() + " (" + video.getVideoId() + ") [" + video.getTags().stream().collect(Collectors.joining(" ")) + "]%n");
    }
  }

  public void playVideo(String videoId) {
    pause = false;
    if(currentVideo == null && videos.stream().anyMatch(video -> video.getVideoId().equals(videoId))){
      currentVideo = videos.stream().filter(video -> video.getVideoId().equals(videoId)).findAny().orElse(null);
      System.out.println("Playing video: " + currentVideo.getTitle());
    } else if(currentVideo != null && !videos.stream().noneMatch(video -> video.getVideoId().equals(videoId))){
      System.out.println("Stopping video: " + currentVideo.getTitle());
      currentVideo = videos.stream().filter(video -> video.getVideoId().equals(videoId)).findAny().orElse(null);
      System.out.println("Playing video: " + currentVideo.getTitle());
    } else{
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if(currentVideo != null){
      System.out.println("Stopping video: " + currentVideo.getTitle());
      currentVideo = null;
    } else{
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
    Random r = new Random();
    int randomVideoIndex = r.nextInt(videos.size());
    pause = false;
    if(currentVideo != null){
      stopVideo();
      playVideo(videos.get(randomVideoIndex).getVideoId());
    } else if(currentVideo == null){
      playVideo(videos.get(randomVideoIndex).getVideoId());
    }
  }

  public void pauseVideo() {
    if(currentVideo != null){
      if(!pause){
        pause = true;
        System.out.println("Pausing video: " + currentVideo.getTitle());
      } else if (pause){
        System.out.println("Video already paused: " + currentVideo.getTitle());
      }
    }else{
      System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  public void continueVideo() {
    if(currentVideo != null){
      if(pause){
        System.out.println("Continuing video: " + currentVideo.getTitle());
        pause = false;
      } else if(!pause){
        System.out.println("Cannot continue video: Video is not paused");
      }

    } else{
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    if(currentVideo != null){
      String pauseStatus;
      if(pause){
        pauseStatus = " - PAUSED";
      } else{
        pauseStatus = "";
      }
      System.out.println("Currently playing: " + currentVideo.getTitle() + " (" + currentVideo.getVideoId() + ") [" + currentVideo.getTags().stream().collect(Collectors.joining(" ")) + "]"+ pauseStatus);
    } else{
      System.out.print("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {
    if (allPlaylists.stream().anyMatch(videoPlaylist1 -> videoPlaylist1.getPlaylistName().equalsIgnoreCase(playlistName))){
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    } else{
      VideoPlaylist newPlaylist = new VideoPlaylist(playlistName);
      allPlaylists.add(newPlaylist);
      System.out.println("Successfully created new playlist: " + playlistName);
    }

  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    List<Video> allVideos = videoLibrary.getVideos();
    Video video = null;
    for (Video v : allVideos){
      if (v.getVideoId().equals(videoId)){
        video = v;
      }
    }
    for (VideoPlaylist pl : allPlaylists){
      if (pl.getPlaylistName().equalsIgnoreCase(playlistName)){
        if (video == null){
          System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
          return;
        }
        if (pl.videoAlreadyAdded(videoId)){
          System.out.println("Cannot add video to " + playlistName + ": Video already added");
          return;
        }
        pl.addVideo(video);
        System.out.println("Added video to " + playlistName + ": " + video.getTitle());
        return;
      }
    }
    System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
  }

  public void showAllPlaylists() {
    if (allPlaylists.isEmpty()){
      System.out.println("No playlists exist yet");
      return;
    }
    ArrayList<String> playlistNames = new ArrayList<>();
    System.out.println("Showing all playlists: ");
    for (VideoPlaylist pl : allPlaylists) {
      playlistNames.add(pl.getPlaylistName());
    }
    Collections.sort(playlistNames);
    for (String name : playlistNames){
      System.out.println("  " + name);
    }
  }

  public void showPlaylist(String playlistName) {
    for (VideoPlaylist pl : allPlaylists){
      if (pl.getPlaylistName().equalsIgnoreCase(playlistName)){
        System.out.println("Showing playlist: " + playlistName);
        if (pl.getSize() == 0){
          System.out.println("  No videos here yet");
          return;
        }
        for (Video vid: pl.getPlaylist()){
          List<Video> allVideos = videoLibrary.getVideos();
          for (Video v: allVideos){
            if (v == vid){
              System.out.println("  " + v.getVideo());
            }
          }
        }
        return;
      }
    }
    System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    List<Video> allVideos = videoLibrary.getVideos();
    Video video = null;
    for (Video v: allVideos){
      if (v.getVideoId().equals(videoId)){
        video = v;
      }
    }
    for (VideoPlaylist pl: allPlaylists){
      if (pl.getPlaylistName().equalsIgnoreCase(playlistName)){
        if (video == null){
          System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
          return;
        }
        for (Video vid : pl.getPlaylist()){
          if (video == vid){
            pl.removeVideo(video);
            System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
            return;
          }
        }
        System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        return;
      }
    }
    System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
  }

  public void clearPlaylist(String playlistName) {
    for (VideoPlaylist playlist : allPlaylists){
      if(playlist.getPlaylistName().equalsIgnoreCase(playlistName)){
        if(playlist.getSize() == 0){
          System.out.println("Playlist already empty");
          return;
        }
        playlist.clearPlaylist();
        System.out.println("Successfully removed all videos from " + playlistName);
        return;
      }
    }
    System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
  }

  public void deletePlaylist(String playlistName) {
    for (int i = 0; i< allPlaylists.size(); i++){
      if (allPlaylists.get(i).getPlaylistName().equalsIgnoreCase(playlistName)){
        allPlaylists.remove(i);
        System.out.println("Deleted playlist: " + playlistName);
        return;
      }
    }
    System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
  }

  public void searchVideos(String searchTerm) {
    System.out.println("searchVideos needs implementation");
  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}