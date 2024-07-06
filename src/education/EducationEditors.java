package education;

import application.Main;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;

public abstract class EducationEditors extends SubScene{
		
	protected ZipFile file;
	protected EducationEditor parent;
	protected String name;
	
	protected Pane icon = new Pane();
	protected Text text;
	protected ImageView view = new ImageView();
	protected WritableImage image;
	
	protected Text headline;
	protected Group editor_root = new Group();
	protected SubScene editor;
	protected static Image noimage = new Image("/NoImage.jpg");

	public static Background background = new Background(new BackgroundFill(Main.light_grey, null, null));
	
	public EducationEditors(Group root, double width, double height, String name, EducationEditor parent) {
		super(root, width, height);
		snapshot(null, image);
		this.name = name;
		this.parent = parent;
		headline = new Text();
		headline.setText(name);
		headline.setFont(new Font(width*0.05));
		headline.setLayoutY(height*0.1);
		headline.setLayoutX((width-headline.getBoundsInLocal().getWidth())*0.5);
		root.getChildren().add(headline);
		Button next = new Button("next");
		next.setOnAction(e->{
			close();
			parent.next();
		});
		next.setLayoutX(width*0.9-next.getBoundsInLocal().getWidth());
		next.setLayoutY(height*0.885-next.getBoundsInLocal().getHeight());
		Button back = new Button("back");
		back.setOnAction(e->{
			close();
			parent.back();
		});
		back.setLayoutX(width*0.1);
		back.setLayoutY(height*0.885-next.getBoundsInLocal().getHeight());
		root.getChildren().addAll(next, back);
		editor = new SubScene(editor_root, width, (height*0.785-headline.getBoundsInParent().getHeight()*0.75+back.getBoundsInParent().getHeight()));
		editor.setLayoutY(height*0.1+headline.getBoundsInLocal().getHeight()*0.5);
		root.getChildren().add(editor);
		file = new ZipFile("temporary/"+name);
		
		image = new WritableImage((int)(width*0.1), (int)(width*0.1));
		snapshot(null, image);
		view.setImage(image);
		view.setLayoutX(6);
		view.setLayoutY(6);
		text = new Text(name);
		text.setFont(new Font(width*0.01));
		text.setLayoutY(width*0.05);
		text.setLayoutX((width*0.1-text.getBoundsInParent().getWidth())*0.5);
		icon.getChildren().addAll(view, text);
		icon.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
	}
	
	public abstract void close();
	
	public Pane getIcon() {
		return icon;
	}
	public ZipFile getFile() {
		return file;
	}

}
