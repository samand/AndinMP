import java.io.*;

import javafx.collections.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.media.*;
import javafx.geometry.*;
import java.util.*;

import javafx.scene.input.*;

public class Andin extends Application{
	public Stage primaryStage; //huvudfÃ¶nster
	public Label nowPlaying;  
	public PlaylistHandler playlistHandler;//playlisthanterare som snackar med databasen
	public MediaView mediaView;		//Global vy fÃ¶r all media
	public LinkedList<Media> queue;	//Lista med media som skall spelas upp.
	public ListView<String> playlistView;//vyn for spellistor
	public ListView<String> playlistContentsView; //Vyn för spellistors innehåll
	public Slider volumeSlider;
	
	public static void main(String[] args) {
		launch(args); // Ladda applikationen
	}
	
	
	@Override
	public void start(Stage stage){
		primaryStage = stage;
		
		queue = new LinkedList<Media>();
		playlistView = new ListView<String>();
		mediaView = new MediaView();
		playlistHandler = new PlaylistHandler();
		playlistHandler.setXMLSource("C:/testljud/playlists.xml");
		playlistContentsView = new ListView<String>();

		BorderPane borderPane = new BorderPane();

		nowPlaying = new Label();
		nowPlaying.setText("No media playing.");

		HBox buttonbox = makeButtons();
		MenuBar menuBar = makeMenuBar();

		StackPane mediaPane = makeMediaPane();
		VBox playlistBox = makePlaylistBox(mediaPane);
		volumeSlider = makeSlider();
		BorderPane bottomPane = new BorderPane();
		bottomPane.setLeft(buttonbox);
		bottomPane.setRight(nowPlaying);
		bottomPane.setCenter(volumeSlider);
		
		
		borderPane.setCenter(mediaPane);
		borderPane.setTop(menuBar);
		borderPane.setBottom(bottomPane);
		borderPane.setLeft(playlistBox);
		Scene scene = new Scene(borderPane, 1920,1080);
		
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Andin Media Player");
		
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
			String mediaName =m.getSource().replaceAll("(.*/)+", "Now playing: ");	
			mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
			nowPlaying.setText(mediaName);
			mediaView.setMediaPlayer(mediaPlayer);

		}else{
			nowPlaying.setText("No active media playing");
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
		playButton.setOnAction(e -> setPlay());
		Button pauseButton = new Button("Pause");
		pauseButton.setOnAction(e -> setPause());
		Button stopButton = new Button("Stop");
		stopButton.setOnAction(l -> setStop());
		Button nextButton = new Button("Next");
		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				setStop();
				initMediaPlayer();

			}

		});
		buttonbox.getChildren().add(playButton);buttonbox.getChildren().add(pauseButton);
		buttonbox.getChildren().add(stopButton);buttonbox.getChildren().add(nextButton);
		return buttonbox;
	}
	private MenuBar makeMenuBar(){
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");

		MenuItem playMedia = new MenuItem("Play media");
		playMedia.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("mp3 files (*.mp3)", "*.mp3");
				FileChooser.ExtensionFilter mp4Filter = new FileChooser.ExtensionFilter("mp4 files (*.mp4)", "*.mp4");
				
				fileChooser.getExtensionFilters().addAll(mp3Filter,mp4Filter);
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
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("mp3 files (*.mp3)", "*.mp3");
				FileChooser.ExtensionFilter mp4Filter = new FileChooser.ExtensionFilter("mp4 files (*.mp4)", "*.mp4");
				
				fileChooser.getExtensionFilters().addAll(mp3Filter,mp4Filter);
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {

					Media m = new Media(file.toURI().toString());
					queueMedia(m);
				}
			}
		});
		menuFile.getItems().addAll(playMedia, queueMedia);

		menuBar.getMenus().add(menuFile);
		return menuBar;
	}
	private void showPlaylistContents(String playlist, VBox playlistContentsBox){
		ObservableList<String> playlistContents =FXCollections.observableArrayList();
		String[] mediaPaths = playlistHandler.getMediaPaths(playlist);
		for (String s: mediaPaths){
			s =s.replaceAll("(.*/)+", "");
			s = s.replaceAll("\\..*", "");
			playlistContents.add(s);
		}
		playlistContentsView.setItems(playlistContents);
		
	}
	private VBox makeShowPlaylistContentsBox(){

		VBox playlistContentsBox = new VBox();
		playlistContentsBox.setVisible(false);
		Button hideplaylistContentsBoxButton = new Button("<<< Hide contents");		
		playlistContentsBox.setVgrow(playlistContentsView, Priority.ALWAYS);
		hideplaylistContentsBoxButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				playlistContentsBox.setVisible(false);
			}
		});
		playlistContentsBox.getChildren().add(hideplaylistContentsBoxButton);
		playlistContentsBox.getChildren().add(playlistContentsView);
		
		return playlistContentsBox;
	}
	private VBox makePlaylistBox(StackPane mediaPaneRef){
		VBox playlistBox = new VBox();
		AnchorPane aPane = new AnchorPane();
		ContextMenu contextMenu = new ContextMenu();
		ObservableList<String> playlists =FXCollections.observableArrayList();
		
		VBox playlistContentsBox = makeShowPlaylistContentsBox();
		mediaPaneRef.getChildren().add(playlistContentsBox);
		playlistContentsBox.setMaxWidth(300);
		mediaPaneRef.setAlignment(playlistContentsBox,Pos.TOP_LEFT);


		String[] playlistStrings=playlistHandler.getPlaylists();
		for(String s: playlistStrings){
			playlists.add(s);
		}
		playlistView.setItems(playlists);
		Button newPlaylistButton = new Button("New Playlist");
		MenuItem playPlaylistOption = new MenuItem("Play playlist");
		MenuItem queuePlaylistOption = new MenuItem("Queue playlist");
		MenuItem addToPlaylistOption = new MenuItem("Add media to playlist");
		
		playPlaylistOption.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String playlistname = playlistView.getSelectionModel().getSelectedItem();
				
				playPlaylist(playlistname);
				
			
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
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("mp3 files (*.mp3)", "*.mp3");
				FileChooser.ExtensionFilter mp4Filter = new FileChooser.ExtensionFilter("mp4 files (*.mp4)", "*.mp4");
				
				fileChooser.getExtensionFilters().addAll(mp3Filter,mp4Filter);
				List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
				int filesAmount = files.size();
				String[] paths = new String[filesAmount];
				for(int i=0;i<filesAmount;i++){
					paths[i]=files.get(i).toURI().toString();
				}
				String playlistname=playlistView.getSelectionModel().getSelectedItem();
				playlistHandler.addMedia(playlistname, paths);
	
			}
		});
		playlistView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
					contextMenu.show(aPane, mouseEvent.getScreenX(), mouseEvent.getSceneY());
				}
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
					
					String playlistName = playlistView.getSelectionModel().getSelectedItem();
					if(playlistName != null){
						showPlaylistContents(playlistName,playlistContentsBox);
						playlistContentsBox.setVisible(true);
						
						if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount()==2){
							String playlistname = playlistView.getSelectionModel().getSelectedItem();
							playPlaylist(playlistname);
						}
					}
					
				}
				
			}
		});
		newPlaylistButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				Stage stage = new Stage();
				VBox vbox = new VBox();
				Label text = new Label("Enter the name of the new playlist");
				TextArea area = new TextArea();
				area.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent keyEvent) {
						if (keyEvent.getCode() == KeyCode.ENTER)  {
							String text = area.getText();
	
							playlistHandler.newPlaylist(text);
							playlistView.getItems().add(text);
							
							area.setText("");
							stage.hide();
						}
					}
				}); 
				Button makePlButton = new Button("Done");
				makePlButton.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event){
						String text = area.getText();
	
						playlistHandler.newPlaylist(text);
						playlistView.getItems().add(text);
						
						area.setText("");
						stage.hide();
					}
				});
				vbox.getChildren().add(text);vbox.getChildren().add(area);vbox.getChildren().add(makePlButton);
				Scene scene = new Scene(vbox, 300,100);
				stage.setScene(scene);
				stage.show();
			}
		}); 

		contextMenu.getItems().addAll(playPlaylistOption, queuePlaylistOption, addToPlaylistOption);
		playlistView.setContextMenu(contextMenu);
		
		playlistBox.getChildren().add(newPlaylistButton);

		playlistBox.getChildren().add(playlistView);
		playlistBox.setVgrow(playlistView, Priority.ALWAYS);
		return playlistBox;
	}

		StackPane makeMediaPane(){
		StackPane stackPane = new StackPane();

		Pane backgroundPane = new Pane();
		backgroundPane.setStyle("-fx-background-color: linear-gradient(to bottom right, derive(goldenrod, 20%), derive(goldenrod, -40%));");
		//backgroundPane.setStyle("-fx-background-color: #abcdef");
		stackPane.getChildren().add(backgroundPane);
		stackPane.getChildren().add(mediaView);
		return stackPane;
	}
	private Slider makeSlider(){
		
		Slider volumeSlider = new Slider(0,1,0);
		
		volumeSlider.setOrientation(Orientation.HORIZONTAL);
		
		volumeSlider.setMaxWidth(300);
		
		volumeSlider.setValue(0.5);
		
		        
		return volumeSlider;

	}
	private void playPlaylist(String playlistname){
		String[] mediaPaths = playlistHandler.getMediaPaths(playlistname);
		Media[] mediaArray = buildPlaylist(mediaPaths);
		if(mediaArray.length !=0){
			playMedia(mediaArray[0]);
			for(int i=1;i<mediaArray.length;i++){
				queueMedia(mediaArray[i]);
			}
		}
	}
	private void queueMedia(Media m){
		queue.add(m);
		if(mediaView.getMediaPlayer() == null) {
			initMediaPlayer();
		}
	}
	private void playMedia(Media m){
		queue.clear();
		queue.add(m);
		if(mediaView.getMediaPlayer() !=null){
			setStop();
			mediaView.setMediaPlayer(null);
		}

		initMediaPlayer();

	}
	private void setPlay(){
		try{
			mediaView.getMediaPlayer().play();
		}catch(Exception e){
			
		}
	}
	private void setPause(){
		try{
			mediaView.getMediaPlayer().pause();
		}catch(Exception e){
			
		}
	}
	private void setStop(){
		try{
			mediaView.getMediaPlayer().stop();
		}catch(Exception e){
			
		}
	}
	
}
