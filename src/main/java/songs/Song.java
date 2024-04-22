package songs;

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

}
