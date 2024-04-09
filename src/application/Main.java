package application;
	
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintStream;

import education.EducationEditor;
import education.EducationEditors;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Main extends Application {
	public static Color dark_grey = new Color(0.3, 0.3, 0.3, 1);
	public static Color light_grey = new Color(0.7, 0.7, 0.7, 1);
	
	// ArrayList with the different Scenes and Runnables to change Scenes with run
	// for example Maximization
	Scene[] Scenes = new Scene[3];
	Runnable[] Runnables = new Runnable[3];

	// MainStage from start methode

	Stage MainStage;
	 
	
	@Override
	public void start(Stage primaryStage) {
		try {
			addStartScene();
			
			primaryStage.getIcons().add(new Image("Icon.png"));
			primaryStage.setTitle("bEIGHT-Manager");
			
			// set Scene and saves Stage
			MainStage = primaryStage;
			changeScene(0);
			MainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// Adding lines to output
				System.setOut(new PrintStream(System.out) {

					private StackTraceElement getCallSite() {
						for (StackTraceElement e : Thread.currentThread().getStackTrace())
							if (!e.getMethodName().equals("getStackTrace") && !e.getClassName().equals(getClass().getName()))
								return e;
						return null;
					}

					@Override
					public void println(String s) {
						println((Object) s);
					}
					
					@Override
					public void println(boolean b) {
						println((Object)b);
					}
					
					@Override
					public void println(double b) {
						println((Object)b);
					}
					@Override
					public void println(int b) {
						println((Object)b);
					}
					@Override
					public void println(long b) {
						println((Object)b);
					}
					@Override
					public void println(char b) {
						println((Object)b);
					}
					
					@Override
					public void println(Object o) {
						StackTraceElement e = getCallSite();
						String callSite = e == null ? "??" : String.format("%s.%s(%s:%d)", e.getClassName(), e.getMethodName(), e.getFileName(), e.getLineNumber());
						super.println(o + "\t\tat " + callSite);
					}
					
				});
		launch(args);
	}
	
	private void addStartScene() {
		// Size of screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		int width = screen.width;
		int height = screen.height;
		
		// Adding MenuBar
		MenuBar bar = new MenuBar();
		Menu theme = new Menu("Theme");
		MenuItem dark = new MenuItem("Dark");
		MenuItem light = new MenuItem("Light");
		theme.getItems().add(dark);
		theme.getItems().add(light);
		bar.getMenus().add(theme);
		VBox vbox = new VBox(bar);
		vbox.setMinHeight(height);
		vbox.setMinWidth(width);

		// Adding SubScene
		Group root = new Group();
		SubScene MainScene = new SubScene(root, 1000, 500);
		MainScene.heightProperty().bind(vbox.heightProperty());
		MainScene.widthProperty().bind(vbox.widthProperty());
		MainScene.setFill(dark_grey);
		
		dark.setOnAction(e->{
			System.out.println("dark");
			MainScene.setFill(dark_grey);
		});
		light.setOnAction(e->{
			System.out.println("light");
			MainScene.setFill(light_grey);
			EducationEditors.background.getFills().clear();
			EducationEditors.background.getFills().add(new BackgroundFill(light_grey, null, null));
		});
		
		Pane external_area = new Pane();
		Rectangle external_square = new Rectangle(width/5, width/5, Color.TURQUOISE);
		Text external_text = new Text("External-\nComponent-\nManager");
		external_text.setFont(new Font(50));
		
		external_text.setX((width/5-external_text.getBoundsInLocal().getWidth())/2);
		external_text.setY((width/5-external_text.getBoundsInLocal().getHeight())/2);
		
		external_area.getChildren().addAll(external_square, external_text);
		
		external_area.setLayoutX(width/5);
		external_area.setLayoutY((height-width/5)/2);
		
		EventHandler<MouseEvent> logic_click = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(Scenes[1] == null) {
					addExternalArea();
				}
				changeScene(1);
			}
		};
		external_area.addEventFilter(MouseEvent.MOUSE_CLICKED, logic_click);
		
		Pane education_area = new Pane();
		Rectangle education_square = new Rectangle(width/5, width/5, Color.TURQUOISE);
		Text education_text = new Text("Education-\nUnit-\nManager");
		education_text.setFont(new Font(50));
		
		education_text.setX((width/5-education_text.getBoundsInLocal().getWidth())/2);
		education_text.setY((width/5-education_text.getBoundsInLocal().getHeight())/2);
		
		education_area.getChildren().addAll(education_square, education_text);
		
		education_area.setLayoutX((width/5)*3);
		education_area.setLayoutY((height-width/5)/2);
		
		EventHandler<MouseEvent> education_click = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(Scenes[2]==null) {
					addEducationArea();
				}
				changeScene(2);
			}
		};
		education_area.addEventFilter(MouseEvent.MOUSE_CLICKED, education_click);
		
		root.getChildren().add(external_area);
		root.getChildren().add(education_area);
		vbox.getChildren().add(MainScene);

		Scene scene = new Scene(vbox);

		// Adding Runnable to maximize and resize
		Runnables[0] = new Runnable() {
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		};
		// Adding Scene
		Scenes[0] = scene;

	}
	
	public void addExternalArea() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screen.width;
		int height = screen.height;
		
		MenuBar bar = new MenuBar();
		
		Menu themes = new Menu("Theme");
		
		MenuItem dark = new MenuItem("Dark");
		MenuItem light = new MenuItem("Light");
		
		themes.getItems().add(dark);
		themes.getItems().add(light);
		bar.getMenus().add(themes);
		
		Menu returning = new Menu("Return");
		MenuItem returning_item = new MenuItem("Return to Start");
		returning_item.setOnAction(me ->{
			changeScene(0);
		});
		returning.getItems().add(returning_item);
		bar.getMenus().add(returning);	
		
		VBox vbox = new VBox(bar);
		vbox.setMinHeight(height);
		vbox.setMinWidth(width);
		Group root = new Group();
		SubScene MainScene = new SubScene(root, 1000, 500);
		MainScene.heightProperty().bind(vbox.heightProperty());
		MainScene.widthProperty().bind(vbox.widthProperty());
		MainScene.setFill(dark_grey);
		vbox.getChildren().add(MainScene);

		dark.setOnAction(e->{
			MainScene.setFill(dark_grey);
		});
		light.setOnAction(e->{
			MainScene.setFill(light_grey);
		});
		
		Scenes[1] = new Scene(vbox);
		Runnables[1] = new Runnable() {
			@Override
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		};
	}
	
	public void addEducationArea() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screen.width;
		int height = screen.height;
		
		MenuBar bar = new MenuBar();
		
		Menu themes = new Menu("Theme");
		
		MenuItem dark = new MenuItem("Dark");
		MenuItem light = new MenuItem("Light");
		
		themes.getItems().add(dark);
		themes.getItems().add(light);
		bar.getMenus().add(themes);
		
		Menu returning = new Menu("Return");
		MenuItem returning_item = new MenuItem("Return to Start");
		returning_item.setOnAction(me ->{
			changeScene(0);
		});
		returning.getItems().add(returning_item);
		bar.getMenus().add(returning);	
		
		VBox vbox = new VBox(bar);
		vbox.setMinHeight(height);
		vbox.setMinWidth(width);
		EducationEditor MainScene = EducationEditor.init(width, height-bar.getHeight());
		MainScene.heightProperty().bind(vbox.heightProperty());
		MainScene.widthProperty().bind(vbox.widthProperty());
		MainScene.setFill(dark_grey);
		vbox.getChildren().add(MainScene);
		
		Menu files = new Menu("File");
		MenuItem new_project = new MenuItem("new");
		new_project.setOnAction(e->{
			MainScene.createNewProject();
		});
		MenuItem open = new MenuItem("open");
		open.setOnAction(e->{
			MainScene.open();
		});
		MenuItem save = new MenuItem("save");
		save.setOnAction(e->{
			MainScene.save();
		});
		MenuItem saveas = new MenuItem("save as");
		saveas.setOnAction(e->{
			MainScene.saveas();
		});
		files.getItems().addAll(new_project, open, save, saveas);
		
		bar.getMenus().add(files);
		


		dark.setOnAction(e->{
			MainScene.setFill(dark_grey);
		});
		light.setOnAction(e->{
			MainScene.setFill(light_grey);
		});
		
		Scenes[2] = new Scene(vbox);
		Runnables[2] = new Runnable() {
			@Override
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		};
	}
	
	// Changing Scene method
	public void changeScene(int SceneNumber) {
		// Setting Scene to Stage
		MainStage.setScene(Scenes[SceneNumber]);

		// Runs Runnable
		Runnables[SceneNumber].run();
	}
}
