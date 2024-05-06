package playlists;

import program.StatusCode;

public class Song {
	    
		private String songName;
	    private String artistName;
	    private int length;
	    
	    public Song(String songName, String artistName, int length) {
			super();
			this.songName = songName;
			this.artistName = artistName;
			this.length = length;
		}

		public Song(Song song) {
			this.songName = song.songName;
			this.artistName = song.artistName;
			this.length = song.length;
		}

		public String getSongName() {
			return songName;
		}
		public StatusCode setSongName(String songName) {
			if (songName != null) {
		        this.songName = songName;
		        return StatusCode.SUCCESS;
		    } else {
		    	return StatusCode.INVALID_INPUT;
		    }
		}
		public String getArtistName() {
			return artistName;
		}
		public StatusCode setArtistName(String artistName) {
			if (artistName != null) {
		        this.artistName = artistName;
		        return StatusCode.SUCCESS;
		    } else {
		    	return StatusCode.INVALID_INPUT;
		    }
		}
		public int getLength() {
			return length;
		}
		public StatusCode setLength(int length) {
			if (length >= 0) {
		        this.length = length;
		        return StatusCode.SUCCESS;
		    } else {
		    	return StatusCode.INVALID_INPUT;
		    }
		}
		
		@Override
		public String toString() {
			Integer totalMinutes = length / 60;
			Integer seconds = length % 60;
			
			//if seconds is less than 10 seconds, append a 0 to the front
			String sec = "";
			if(seconds < 10) {
				sec = "0" + seconds.toString();
			}
			else {
				sec = seconds.toString();
			}
			
			String totalTime = totalMinutes.toString() + ":" + sec;
			return songName + " - " + artistName + " " + totalTime;
		}

}
