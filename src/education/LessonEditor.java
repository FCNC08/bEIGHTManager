package education;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;
import javafx.stage.FileChooser.ExtensionFilter;

public class LessonEditor extends EducationEditors{
	public static final String hti = "headline-text-image";
	public static final String hit = "headline-image-text";
	public static final String iht = "image-headline-text";
	public static final String ith = "image-text-headline";
	
	protected VBox vbox = new VBox();
	protected ScrollPane pane = new ScrollPane(vbox);
	
	protected ComboBox<String> type = new ComboBox<>();
	
	protected TextField lesson_headline = new TextField("Headline");
	protected TextArea lesson_text = new TextArea("Text: Lorem ipsum");
	protected Label add_image = new Label("Add/Change Image +");
	protected Font text_font;
	
	protected File image_file;
	protected Image lesson_image;
	protected ImageView lesson_view = new ImageView(noimage);
	
	public LessonEditor(Group root, double width, double height, String name, String path, EducationEditor parent) {
		super(root, width, height, name, path, parent);
		vbox.setAlignment(Pos.CENTER);
		
		type.getItems().addAll(hti, hit, iht, ith);
		type.setValue(hti);
		type.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("ComboBox Changed: old value = " + oldValue + ", new value = " + newValue);
				if(oldValue!=newValue) {
					vbox.getChildren().clear();
					switch(newValue) {
					case hti:
						vbox.getChildren().addAll(type, lesson_headline, lesson_text, lesson_view, add_image);
						break;
					case hit:
						vbox.getChildren().addAll(type, lesson_headline, lesson_view, add_image, lesson_text);
						break;
					case iht:
						vbox.getChildren().addAll(type, lesson_view, add_image, lesson_headline, lesson_text);
						break;
					case ith:
						vbox.getChildren().addAll(type, lesson_view, add_image, lesson_text, lesson_headline);
						break;
					default:
						vbox.getChildren().addAll(type, lesson_headline, lesson_text, lesson_view, add_image);
						break;
					}
					root.requestLayout();
				}
			}
		});
		
		lesson_headline.setFont(new Font(height*0.04));
		lesson_headline.setBackground(background);
		
		text_font = new Font(height*0.02);
		
		lesson_text.setFont(text_font);
		lesson_text.setPrefHeight(height*0.2);
		String cssColor = String.format("rgba(%d, %d, %d, %f)", (int) (Main.dark_grey.getRed() * 255), (int) (Main.dark_grey.getGreen() * 255), (int) (Main.dark_grey.getBlue() * 255), Main.dark_grey.getOpacity());
		lesson_text.setStyle("-fx-control-inner-background: "+cssColor+";");
		
		lesson_view.setFitHeight(height*0.5);
		lesson_view.setFitWidth(lesson_view.getFitHeight()*(noimage.getWidth()/noimage.getHeight()));
		
		add_image.setFont(text_font);
		add_image.setTextFill(Color.BLACK);
		add_image.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
			image_file = fc.showOpenDialog(new Stage());
			try {
				lesson_image = new Image(new FileInputStream(image_file));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			lesson_view.setImage(lesson_image);;
			lesson_view.setFitWidth(lesson_view.getFitHeight()*(lesson_image.getWidth()/lesson_image.getHeight()));
			vbox.requestLayout();
		});
		pane.setPrefHeight(editor.getHeight());
		vbox.setSpacing(20);
		vbox.getChildren().addAll(type, lesson_headline, lesson_text, lesson_view, add_image);
		vbox.requestLayout();
		vbox.setBackground(background);
		pane.setBackground(background);
		pane.setLayoutX((width-vbox.getBoundsInParent().getWidth())*0.5);
		editor_root.getChildren().addAll(pane);
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
			object.append("type", type.getValue());
			object.append("image", image_file.getName());
			try {
				file.addFile(image_file, EducationEditor.parameter);
			} catch (ZipException e) {
				e.printStackTrace();
			}
		}else {
			object.append("type", "ht");
		}
		File temp_file = new File("temporary/content.json");
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
