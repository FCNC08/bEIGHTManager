package education;

import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;

public class EducationEditors extends SubScene{
	protected ZipFile file;
	protected Pane icon;
	protected ImageView view = new ImageView();
	protected WritableImage image;
	protected Text text;
	public EducationEditors(Parent root, double width, double height) {
		super(root, width, height);
		snapshot(null, image);
		view.setImage(image);
		icon.getChildren().add(view);
		icon.getChildren().add(text);
	}
	
	public void close() {
		//TODO Write closing method
		snapshot(null, image);
	}
	
	public Pane getPane() {
		return icon;
	}
	public ZipFile getFile() {
		return file;
	}

}
