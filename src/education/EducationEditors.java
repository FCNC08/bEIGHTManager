package education;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
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
import javafx.stage.Stage;
import net.lingala.zip4j.ZipFile;

public abstract class EducationEditors extends SubScene{
		
	protected ZipFile file;
	protected String name;
	protected Pane icon = new Pane();
	protected ImageView view = new ImageView();
	protected WritableImage image;
	protected Text text;
	public EducationEditors(Group root, double width, double height, String name) {
		super(root, width, height);
		snapshot(null, image);
		this.name = name;
		text = new Text(name);
		text.setFont(new Font(15));
		file = new ZipFile("temporary/"+name);
		image = new WritableImage((int)(width*0.1), (int)(width*0.1));
		setFill(Color.PURPLE);
		view.setImage(image);
		Stage stage = new Stage();
		Group testroot = new Group();
		Scene scene = new Scene(testroot);
		testroot.getChildren().add(text);
		stage.setScene(scene);
		stage.show();
		icon.getChildren().add(view);
		icon.getChildren().add(text);
		//icon.setBackground(new Background(new BackgroundFill(Color.PURPLE, null, null)));
		icon.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
	}
	
	public void close() {
		//TODO Write closing method
		snapshot(null, image);
	}
	
	public Pane getIcon() {
		return icon;
	}
	public ZipFile getFile() {
		return file;
	}

}
