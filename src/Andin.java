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
import javafx.scene.input.*;

public class Andin extends Application{
	public String nowPlaying;
	public PlaylistHandler playlistHandler;
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
		playlistHandler = new PlaylistHandler();
		playlistHandler.setXMLSource("C:/testljud/playlists.xml");
		//test
		ObservableList<String> playlists =FXCollections.observableArrayList();
		
		String[] playlistStrings=playlistHandler.getPlaylists();
		for(String s: playlistStrings){
			playlists.add(s);
		}

		
		Button newPlaylistButton = new Button("New Playlist");
		//newPlaylistButton.setOnAction(); TOOOODOOOOOO
		playlistView.setItems(playlists);
		
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");

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
					queueMedia(m);
				}
			}
		});
		menuFile.getItems().addAll(playMedia, queueMedia);
		
		menuBar.getMenus().add(menuFile);

		StackPane s = new StackPane();

		Pane p = new Pane();
		p.setStyle("-fx-background-color: #abcdef");
		s.getChildren().add(p);
		s.getChildren().add(mediaView);

		
		
		borderPane.setCenter(s);

		nowPlaying="No media playing.";
		Label l = new Label();
		l.setText(nowPlaying);
		
		HBox buttonbox = makeButtons();
		AnchorPane aPane = new AnchorPane();
		ContextMenu contextMenu = new ContextMenu();
		MenuItem playPlaylistOption = new MenuItem("Play playlist");
		MenuItem queuePlaylistOption = new MenuItem("Queue playlist");
		MenuItem addToPlaylistOption = new MenuItem("Add media to playlist");
		contextMenu.getItems().addAll(playPlaylistOption, queuePlaylistOption, addToPlaylistOption);
		playPlaylistOption.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String playlistname = playlistView.getSelectionModel().getSelectedItem();
				String[] mediaPaths = playlistHandler.getMediaPaths(playlistname);
				Media[] mediaArray = buildPlaylist(mediaPaths);
				playMedia(mediaArray[0]);
				for(int i=1;i<mediaArray.length;i++){
					queueMedia(mediaArray[i]);
				}
			}
		});
		queuePlaylistOption.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String playlistname = playlistView.getSelectionModel().getSelectedItem();
				String[] mediaPaths = playlistHandler.getMediaPaths(playlistname);
				Media[] mediaArray = buildPlaylist(mediaPaths);
				
				for(int i=0;i<mediaArray.length;i++){
					queueMedia(mediaArray[i]);
				}
			}
		});
		addToPlaylistOption.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
				int filesAmount = files.size();
				String[] paths = new String[filesAmount];
				for(int i=0;i<filesAmount;i++){
					paths[i]=files.get(i).toURI().toString();
				}
				playlistHandler.addMedia(playlistView.getSelectionModel().getSelectedItem(), paths);
				
			}
		});
		playlistView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
					contextMenu.show(aPane, mouseEvent.getScreenX(), mouseEvent.getSceneY());
				}
			}
		});
		playlistView.setContextMenu(contextMenu);


		
		BorderPane test = new BorderPane();
		test.setLeft(buttonbox);
		test.setRight(l);
		borderPane.setTop(menuBar);
		borderPane.setBottom(test);

		
		VBox tut = new VBox();
		tut.getChildren().add(newPlaylistButton);
		tut.getChildren().add(playlistView);
		tut.setVgrow(playlistView, Priority.ALWAYS);
		borderPane.setLeft(tut);
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
		buttonbox.getChildren().add(playButton);buttonbox.getChildren().add(pauseButton);
		buttonbox.getChildren().add(stopButton);buttonbox.getChildren().add(nextButton);
		return buttonbox;
	}
	public void queueMedia(Media m){
		queue.add(m);
		if(mediaView.getMediaPlayer() == null) {
			initMediaPlayer();
		}
	}
	public void playMedia(Media m){
		queue.clear();
		queue.add(m);
		if(mediaView.getMediaPlayer() !=null){
			mediaView.getMediaPlayer().stop();
			mediaView.setMediaPlayer(null);
		}

		initMediaPlayer();

	}
}
