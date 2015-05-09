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
		
		
		mediaplayer = new MediaPlayer(new Media("file:///C:/Users/william/Music/shit.mp3"));
		
		
		// INITIAL TESTNING
		VBox box = new VBox();
		
		
		
		
		// Meny
		MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuPlaylist = new Menu("Playlists");
        
        MenuItem playMedia = new MenuItem("Play media");
        MenuItem queueMedia = new MenuItem("Queue media");

        MenuItem newPlaylist = new MenuItem("New playlist");
        MenuItem playPlaylist = new MenuItem("Queue media");
        MenuItem queuePlaylist = new MenuItem("Queue playlist");
        
        menuFile.getItems().addAll(playMedia, queueMedia);
        menuPlaylist.getItems().addAll(newPlaylist, playPlaylist, queuePlaylist);
        
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(primaryStage);
        
        
        menuBar.getMenus().addAll(menuFile, menuPlaylist);
        
        
        
        box.getChildren().add(menuBar);
		
        
        
        
        
        //Huvudknappar
        Button playButton = new Button("Play");
		playButton.setOnAction(e -> mediaplayer.play());
		box.getChildren().add(playButton);
		Button pauseButton = new Button("Pause");
		pauseButton.setOnAction(e -> mediaplayer.pause());
		box.getChildren().add(pauseButton);
		Button stopButton = new Button("Stop");
		stopButton.setOnAction(l -> mediaplayer.stop());
		box.getChildren().add(stopButton);
        
		
		
		
		
		Scene scene = new Scene(box, 400,400, Color.CORNFLOWERBLUE);
		primaryStage.setScene(scene);
		primaryStage.show();
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
