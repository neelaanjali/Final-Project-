package playlists;

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
			this.artistName = song.songName;
			this.length = song.length;
		}

		public String getSongName() {
			return songName;
		}
		public void setSongName(String songName) {
			this.songName = songName;
		}
		public String getArtistName() {
			return artistName;
		}
		public void setArtistName(String artistName) {
			this.artistName = artistName;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
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
