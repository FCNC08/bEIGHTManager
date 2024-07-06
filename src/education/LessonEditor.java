package education;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
import net.lingala.zip4j.ZipFile;
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
	
	public LessonEditor(Group root, double width, double height, String name, EducationEditor parent) {
		super(root, width, height, name, parent);
		vbox.setAlignment(Pos.CENTER);
		
		type.getItems().addAll(hti, hit, iht, ith);
		type.setValue(hti);
		type.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
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
	
	public LessonEditor(Group root, double width, double height, String name, EducationEditor parent, String lesson_headline_text, String lesson_text_text, String type_text, boolean image_used, File image_file) {
		this(root, width, height, name, parent);
		if(type_text.contains("i")) {
			type_text = type_text.replace("h", "headline-").replace("t", "text-").replace("i", "image-");
			type_text = type_text.substring(0, type_text.length()-1);
		}else {
			type_text = type_text.replace("h", "headline-").replace("t", "text-")+"image";
		}
		type.setValue(type_text);
		
		lesson_headline.setText(lesson_headline_text);
		lesson_text.setText(lesson_text_text);
		
		if(image_used) {
			this.image_file = image_file;
			try {
				lesson_image = new Image(new FileInputStream(image_file));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			lesson_view.setImage(lesson_image);;
			lesson_view.setFitWidth(lesson_view.getFitHeight()*(lesson_image.getWidth()/lesson_image.getHeight()));
			vbox.requestLayout();
		}

	}
	
	public static LessonEditor init(double width, double height, String name, EducationEditor parent) {
		return new LessonEditor(new Group(), width, height, name, parent);
	}
	
	public static LessonEditor init(double width, double height, EducationEditor parent, ZipFile file) {
		
		try {
			file.extractAll("temporary/"+file.getFile().getName().replace(".", "-"));
		} catch (ZipException e) {
			e.printStackTrace();
		}
		
		JSONObject jsonobjekt = null;
		try {
			if(file.isEncrypted()) {
				throw new IllegalArgumentException();
			}
			InputStream inputStream = file.getInputStream(file.getFileHeader("content.json"));
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead;
			while((bytesRead=inputStream.read(buffer))!= -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			String jsonString = outputStream.toString(StandardCharsets.UTF_8);
			jsonobjekt = new JSONObject(jsonString);
		}catch(IOException e) {
			e.printStackTrace();
		}
		LessonEditor editor;
		if(jsonobjekt.getString("type").contains("i")) {
			editor = new LessonEditor(new Group(), width, height, file.getFile().getName(), parent, jsonobjekt.getString("headline"), jsonobjekt.getString("text"), jsonobjekt.getString("type"), 
					true, new File("temporary/"+file.getFile().getName().replace(".", "-")+"/"+jsonobjekt.getString("image")));
		}else {
			editor = new LessonEditor(new Group(), width, height, file.getFile().getName(), parent, jsonobjekt.getString("headline"), jsonobjekt.getString("text"), jsonobjekt.getString("type"), false, null);
		}
		
		return editor;
		
	}

	@Override
	public void close() {
		snapshot(null, image);
		File temp_directory = new File("temporary/"+name.substring(0).replace(".", "-")+"/");
		temp_directory.mkdir();
		/*try {
			if(!file.getFileHeaders().isEmpty()) {
				for(FileHeader fh : file.getFileHeaders()) {
					file.removeFile(fh);
				}
			}
			
		} catch (ZipException e) {
			e.printStackTrace();
		}*/
		JSONObject object = new JSONObject();
		object.put("headline", lesson_headline.getText());
		object.put("text", lesson_text.getText());
		String final_type = type.getValue();
		final_type = final_type.replace("headline", "h");
		final_type = final_type.replace("image", "i");
		final_type = final_type.replace("text", "t");
		final_type = final_type.replaceAll("-", "-");
		if(lesson_image != null) {
			object.put("image", image_file.getName());
			try {
				file.addFile(image_file, EducationEditor.parameter);
			} catch (ZipException e) {
				e.printStackTrace();
			}
			object.put("type", final_type);
		}else {
			final_type = final_type.replace("i", "");
			object.put("type",final_type);
		}
		File temp_file = new File("temporary/"+name.substring(0).replace(".", "-")+"/content.json");
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
