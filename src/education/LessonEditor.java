package education;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;
import javafx.stage.FileChooser.ExtensionFilter;

public class LessonEditor extends EducationEditors{

	protected TextField headline = new TextField("Headline");
	protected TextArea text = new TextArea("Text: Lorem ipsum");
	protected Label add_image = new Label("Add/Change Image +");
	protected File image_file;
	protected Image lesson_image;
	protected ImageView lesson_view;
	protected Font text_font;
	
	public LessonEditor(Group root, double width, double height, String name, String path, EducationEditor parent) {
		super(root, width, height, name, path, parent);
		headline.setLayoutY(height*0.15);
		headline.setLayoutX(width*0.4);
		headline.setFont(new Font(height*0.04));
		
		text_font = new Font(height*0.02);
		
		text.setLayoutY(height*0.3);
		text.setLayoutX(width*0.1);
		text.setFont(text_font);
		
		add_image.setFont(text_font);
		add_image.setLayoutX((width-add_image.getBoundsInParent().getWidth())*0.5);
		add_image.setLayoutY(editor.getHeight()*0.9);
		add_image.setTextFill(Color.ALICEBLUE);
		add_image.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
			image_file = fc.showOpenDialog(new Stage());
			try {
				lesson_image = new Image(new FileInputStream(image_file));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			boolean is_null = lesson_image==null;
			lesson_view = new ImageView(lesson_image);
			lesson_view.setLayoutY(height*0.45);
			lesson_view.setFitHeight(height*0.45);
			lesson_view.setFitWidth(lesson_view.getFitHeight()*(lesson_image.getWidth()/lesson_image.getHeight()));
			if(is_null) {
				root.getChildren().add(lesson_view);
			}
		});
		
		editor_root.getChildren().addAll(headline, text, add_image);
	}
	
	public static LessonEditor init(double width, double height, String name, String path, EducationEditor parent) {
		return new LessonEditor(new Group(), width, height, name, path, parent);
	}

	@Override
	public void close() {
		snapshot(null, image);
		JSONObject object = new JSONObject();
		object.append("headline", headline.getText());
		object.append("text", text.getText());
		if(lesson_image != null) {
			object.append("type", "hti");
			object.append("image", image_file.getName());
			try {
				file.addFile(image_file, EducationEditor.parameter);
			} catch (ZipException e) {
				e.printStackTrace();
			}
		}else {
			object.append("type", "ht");
		}
		File temp_file = new File("temporary/question.json");
		try(FileWriter fwriter = new FileWriter(temp_file)){
			fwriter.write(object.toString());
			fwriter.flush();
			file.addFile(temp_file, EducationEditor.parameter);
			temp_file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		parent.save();
	}

}
