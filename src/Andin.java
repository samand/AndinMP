import java.io.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.media.*;
import java.util.*;

import java.lang.*;

public class Andin extends Application{
	public FileChooser fileChooser;
	public MediaView mediaView;
	public LinkedList<Media> queue;	//Lista med media som skall spelas upp.
	public Media currentMedia; 		//Nuvarande media
	public static void main(String[] args) {
		launch(args); // Ladda applikationen

	}
	// TODO: Play/pause/stop etc görs antagligen via lambdafunktioner bundna till knappar
	@Override
	public void start(Stage primaryStage){
		fileChooser = new FileChooser();
		queue = new LinkedList<Media>();
		
		
		// INITIAL TESTNING
		VBox box = new VBox();
		
		mediaView = new MediaView();
		box.getChildren().add(mediaView);
		
		// Meny
		MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuPlaylist = new Menu("Playlists");
        
        MenuItem playMedia = new MenuItem("Play media");
        playMedia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                	
                	Media m = new Media(file.toURI().toString());
                	queue.add(m);
                	initMediaPlayer();
                }
            }
        });
        
        MenuItem queueMedia = new MenuItem("Queue media");

        MenuItem newPlaylist = new MenuItem("New playlist");
        MenuItem playPlaylist = new MenuItem("Queue media");
        MenuItem queuePlaylist = new MenuItem("Queue playlist");
        
        
        
        menuFile.getItems().addAll(playMedia, queueMedia);
        menuPlaylist.getItems().addAll(newPlaylist, playPlaylist, queuePlaylist);
       
      
        
        
        menuBar.getMenus().addAll(menuFile, menuPlaylist);
        
        
        
        box.getChildren().add(menuBar);
		
        
        
        //Huvudknappar
        Button playButton = new Button("Play");
		playButton.setOnAction(e -> mediaView.getMediaPlayer().play());
		box.getChildren().add(playButton);
		Button pauseButton = new Button("Pause");
		pauseButton.setOnAction(e -> mediaView.getMediaPlayer().pause());
		box.getChildren().add(pauseButton);
		Button stopButton = new Button("Stop");
		stopButton.setOnAction(l -> mediaView.getMediaPlayer().stop());
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
	private void initMediaPlayer(){
	    if (!queue.isEmpty()){
	    	Media m = queue.pop();
	        MediaPlayer mediaPlayer = new MediaPlayer(m);
	        mediaPlayer.setOnEndOfMedia(new Runnable() {
	            @Override public void run() {
	                initMediaPlayer();
	            }
	        });
	        mediaView.setMediaPlayer(mediaPlayer);
	    }
	    
	    
	}
}
