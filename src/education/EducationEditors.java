package education;

import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
		file = new ZipFile("temporary/"+name);
		view.setImage(image);
		icon.getChildren().add(view);
		icon.getChildren().add(text);
		setFill(Color.PURPLE);
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
