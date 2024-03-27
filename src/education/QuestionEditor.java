package education;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class QuestionEditor extends EducationEditors{

	protected TextField question_headline = new TextField("Headline");
	protected TextField question;
	protected VBox answer_box = new VBox();
	protected ArrayList<TextField> answers = new ArrayList<>();
	protected Label new_answer = new Label("new Answer +");
	protected Font standard_answer_font;
	protected Label add_image = new Label("Add/Change Image +");
	protected File image_file;
	protected Image question_image;
	protected ImageView question_view;
	public QuestionEditor(Group root, double width, double height, String name, String path,  EducationEditor parent) {
		super(root, width, height, name, path, parent);
		
		question_headline.setFont(new Font(height*0.04));
		question_headline.setLayoutX(width*0.4);
		question_headline.setLayoutY(height*0.01);
		standard_answer_font = new Font(height*0.02);
		question = new TextField("Enter your Question");
		question.setFont(new Font(height*0.03));
		question.setLayoutY(height*0.1);
		question.setLayoutX((width-question.getBoundsInParent().getWidth())*0.5);
		
		TextField answer_1 = new TextField("Answer 1");
		answer_1.setFont(standard_answer_font);
		TextField answer_2 = new TextField("Answer 2");
		answer_2.setFont(standard_answer_font);
		answers.add(answer_1);
		answers.add(answer_2);
		answer_box.getChildren().addAll(answer_1, answer_2);
		answer_box.setLayoutY(height*0.15+question.getBoundsInParent().getHeight());
		answer_box.setLayoutX((width*0.2));
		
		new_answer.setFont(standard_answer_font);
		new_answer.setLayoutX((width-new_answer.getBoundsInParent().getWidth())*0.5);
		new_answer.setLayoutY(editor.getHeight()*0.9);
		new_answer.setTextFill(Color.ALICEBLUE);
		new_answer.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			TextField new_field = new TextField("Answer "+(answers.size()+1));
			new_field.setFont(standard_answer_font);
			answers.add(new_field);
			answer_box.getChildren().add(new_field);
			root.requestLayout();
		});
		
		add_image.setFont(standard_answer_font);
		add_image.setLayoutX((width-add_image.getBoundsInParent().getWidth())*0.5);
		add_image.setLayoutY(editor.getHeight()*0.85);
		add_image.setTextFill(Color.ALICEBLUE);
		add_image.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
			image_file = fc.showOpenDialog(new Stage());
			try {
				question_image = new Image(new FileInputStream(image_file));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			boolean is_null = question_view==null;
			question_view = new ImageView(question_image);
			question_view.setLayoutY(height*0.45);
			question_view.setFitHeight(height*0.45);
			question_view.setFitWidth(question_view.getFitHeight()*(question_image.getWidth()/question_image.getHeight()));
			if(is_null) {
				root.getChildren().add(question_view);
			}
		});
		
		editor_root.getChildren().add(question_headline);
		editor_root.getChildren().add(question);
		editor_root.getChildren().add(answer_box);
		editor_root.getChildren().add(new_answer);
		editor_root.getChildren().add(add_image);
		//editor_root.getChildren().addAll(question_headline, question, answer_box, new_answer, add_image);
	}

	public static QuestionEditor init(double width, double height, String name, String path, EducationEditor parent) {
		return new QuestionEditor(new Group(), width, height, name, path, parent);
	}

	@Override
	public void close() {
		snapshot(null, image);
		
		try {
			if(!file.getFileHeaders().isEmpty()) {
				for(FileHeader fh : file.getFileHeaders()) {
					file.removeFile(fh);
				}
			}
			
		} catch (ZipException e) {
			e.printStackTrace();
		}
		JSONObject object = new JSONObject();
		object.append("headline", question_headline.getText());
		object.append("question", question.getText());
		if(question_image != null) {
			object.append("image", image_file.getName());
			object.append("type", "hqoi");
			try {
				file.addFile(image_file, EducationEditor.parameter);
			} catch (ZipException e) {
				e.printStackTrace();
			}
		}else {
			object.append("type", "hqo");
		}
		object.append("optioncount", answers.size());
		JSONArray options = new JSONArray();
		for(TextField tf : answers) {
			options.put(tf.getText());
		}
		object.append("options", options);
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
