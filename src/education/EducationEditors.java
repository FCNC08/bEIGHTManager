package education;

import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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
	protected String name;
	protected Pane icon = new Pane();
	protected ImageView view = new ImageView();
	protected WritableImage image;
	protected Text headline;
	protected EducationEditor parent;
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
		next.setLayoutY(height*0.85-next.getBoundsInLocal().getHeight());
		Button back = new Button("back");
		back.setOnAction(e->{
			close();
			parent.back();
		});
		back.setLayoutX(width*0.1);
		back.setLayoutY(height*0.85-next.getBoundsInLocal().getHeight());
		root.getChildren().addAll(next, back);
		file = new ZipFile("temporary/"+name);
		image = new WritableImage((int)(width*0.1), (int)(width*0.1));
		snapshot(null, image);
		view.setImage(image);
		icon.getChildren().add(view);
		view.setLayoutX(6);
		view.setLayoutY(6);
		icon.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
	}
	
	public void close() {
		snapshot(null, image);
	}
	
	public Pane getIcon() {
		return icon;
	}
	public ZipFile getFile() {
		return file;
	}

}
