import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.media.*;
import java.util.*;



public class Andin extends Application{
	
	public MediaPlayer mediaplayer; //Huvudspelaren, uppdateras vartefter.
	public ArrayList<Media> queue;	//Lista med media som skall spelas upp.
	public Media currentMedia; 		//Nuvarande media
	public static void main(String[] args) {
		launch(args); // Ladda applikationen

	}
	// TODO: Play/pause/stop etc görs antagligen via lambdafunktioner bundna till knappar
	@Override
	public void start(Stage primaryStage){
		
		// INITIAL TESTNING
		Group group = new Group();
		
		Button playButton = new Button("SHIEEET");
		playButton.setOnAction(e -> System.out.println("IT WORKS XDD TOP LEL OMG LE TROLEFACE"));
		
		group.getChildren().add(playButton);
		
		Scene scene = new Scene(group, 400,400, Color.CORNFLOWERBLUE);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		String[] slask = Playlist.getPlaylists();
		for (String s:slask){
			System.out.println(s);
		}
	}
	public void addPlaylist(){
		//TODO
	}
	public void removePlaylist(String name){
		//TODO
	}
	public void queueMedia(){
		//TODO
	}
	
	

}
