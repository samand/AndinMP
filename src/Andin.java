import java.io.*;
import javafx.collections.*;
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
	public FileChooser fileChooser;
	public MediaView mediaView;		//Global vy för all media
	public LinkedList<Media> queue;	//Lista med media som skall spelas upp.
	public ListView<String> playlistView;
	public static void main(String[] args) {
		launch(args); // Ladda applikationen

	}
	@Override
	public void start(Stage primaryStage){
		//ASDASDASD
		//QWEQWEQWE
		// Actual stuff
		fileChooser = new FileChooser();
		queue = new LinkedList<Media>();
		playlistView = new ListView<String>();
		
		
		mediaView = new MediaView();
		
		BorderPane borderPane = new BorderPane();
		
		//test
		ObservableList<String> playlists =FXCollections.observableArrayList();
		/*
		String[] playlistStrings=Playlist.getPlaylists();
		for(String s: playlistStrings){
			playlists.add(s);
		}
		playlistView.setItems(playlists);
		*/
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
					queue.clear();
					queue.add(m);
					if(mediaView.getMediaPlayer() !=null){
						mediaView.getMediaPlayer().stop();
						mediaView.setMediaPlayer(null);
					}

					initMediaPlayer();
				}
			}
		});
		MenuItem queueMedia = new MenuItem("Queue media");
		queueMedia.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {

					Media m = new Media(file.toURI().toString());
					queue.add(m);
					if(mediaView.getMediaPlayer() == null) {initMediaPlayer();}
				}
			}
		});
		MenuItem newPlaylist = new MenuItem("New playlist");
		MenuItem playPlaylist = new MenuItem("Play playlist");
		MenuItem queuePlaylist = new MenuItem("Queue playlist");
		menuFile.getItems().addAll(playMedia, queueMedia);
		menuPlaylist.getItems().addAll(newPlaylist, playPlaylist, queuePlaylist);
		menuBar.getMenus().addAll(menuFile, menuPlaylist);
		
		StackPane s = new StackPane();
		
		Pane p = new Pane();
		p.setStyle("-fx-background-color: #003020");
		s.getChildren().add(p);
		s.getChildren().add(mediaView);
		
		borderPane.setCenter(s);
		
		//Huvudknappar
		Button playButton = new Button("Play");
		playButton.setOnAction(e -> mediaView.getMediaPlayer().play());
		Button pauseButton = new Button("Pause");
		pauseButton.setOnAction(e -> mediaView.getMediaPlayer().pause());
		Button stopButton = new Button("Stop");
		stopButton.setOnAction(l -> mediaView.getMediaPlayer().stop());
		Button nextButton = new Button("Next");
		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
					mediaView.getMediaPlayer().stop();
					initMediaPlayer();
					
			}
			
		});
		
		Label l = new Label("Now playing:");
		
		HBox buttonbox = new HBox();
		buttonbox.getChildren().add(playButton);buttonbox.getChildren().add(pauseButton);
		buttonbox.getChildren().add(stopButton);buttonbox.getChildren().add(nextButton);buttonbox.getChildren().add(l);
		BorderPane test = new BorderPane();
		test.setRight(l);
		test.setLeft(buttonbox);
		
		borderPane.setTop(menuBar);
		borderPane.setBottom(test);

		borderPane.setLeft(playlistView);
		Scene scene = new Scene(borderPane, 1920,1080, Color.CORNFLOWERBLUE);
		primaryStage.setScene(scene);
		primaryStage.show();




	}
	private void initMediaPlayer(){
		if (!queue.isEmpty()){
			Media m = queue.pop();
			MediaPlayer mediaPlayer = new MediaPlayer(m);
			mediaPlayer.setAutoPlay(true);
			mediaPlayer.setOnEndOfMedia(new Runnable() {
				@Override public void run() {
					initMediaPlayer();
				}
			});
			mediaView.setMediaPlayer(mediaPlayer);
			System.out.println(mediaView.getMediaPlayer().getMedia().getMetadata().get("artist").toString());
			
		}else{
			mediaView.setMediaPlayer(null);
		}


	}
	private Media[] buildPlaylist(String[] paths){
		Media[] mediaArray = new Media[paths.length];
		for(int i=0;i<paths.length;i++){
			mediaArray[i] = new Media(paths[i]);
		}
		return mediaArray; 
	}
	private HBox makeButtons(){
		HBox buttonbox = new HBox();
		Button playButton = new Button("Play");
		playButton.setOnAction(e -> mediaView.getMediaPlayer().play());
		Button pauseButton = new Button("Pause");
		pauseButton.setOnAction(e -> mediaView.getMediaPlayer().pause());
		Button stopButton = new Button("Stop");
		stopButton.setOnAction(l -> mediaView.getMediaPlayer().stop());
		Button nextButton = new Button("Next");
		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
					mediaView.getMediaPlayer().stop();
					initMediaPlayer();
					
			}
			
		});
		return buttonbox;
	}
	
	

}
