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

import java.util.*;

import javafx.scene.input.*;

public class Andin extends Application{
	public Stage primaryStage; //huvudfÃ¶nster
	public Label nowPlaying;  
	public PlaylistHandler playlistHandler;//playlisthanterare som snackar med databasen
	public FileChooser fileChooser;	//For att valja filer
	public MediaView mediaView;		//Global vy fÃ¶r all media
	public LinkedList<Media> queue;	//Lista med media som skall spelas upp.
	public ListView<String> playlistView;//vyn for spellistor
	public ListView<String> playlistContentsView; //Vyn för spellistors innehåll

	
	public static void main(String[] args) {
		launch(args); // Ladda applikationen
	}
	
	
	@Override
	public void start(Stage stage){
		primaryStage = stage;
		fileChooser = new FileChooser();
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

		BorderPane bottomPane = new BorderPane();
		bottomPane.setLeft(buttonbox);
		bottomPane.setRight(nowPlaying);

		borderPane.setCenter(mediaPane);
		borderPane.setTop(menuBar);
		borderPane.setBottom(bottomPane);
		borderPane.setLeft(playlistBox);
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
			String mediaName =m.getSource().replaceAll("(.*/)+", "Now playing: ");
//			mediaName = mediaName.replaceAll("\\..*", "");
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
	private MenuBar makeMenuBar(){
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
		Button hideplaylistContentsBoxButton = new Button("<<< Hide tracks");		
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
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount()==2){
					String playlistname = playlistView.getSelectionModel().getSelectedItem();
					playPlaylist(playlistname);
				}
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount()==1){
					String playlistName = playlistView.getSelectionModel().getSelectedItem();
					showPlaylistContents(playlistName,playlistContentsBox);
					playlistContentsBox.setVisible(true);
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
		backgroundPane.setStyle("-fx-background-color: #abcdef");
		stackPane.getChildren().add(backgroundPane);
		stackPane.getChildren().add(mediaView);
		return stackPane;
	}
	public void playPlaylist(String playlistname){
		String[] mediaPaths = playlistHandler.getMediaPaths(playlistname);
		Media[] mediaArray = buildPlaylist(mediaPaths);
		playMedia(mediaArray[0]);
		for(int i=1;i<mediaArray.length;i++){
			queueMedia(mediaArray[i]);
		}
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
