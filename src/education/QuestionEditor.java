package education;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class QuestionEditor extends EducationEditors{
	public static final String hqoi = "headline-question-option-image";
	public static final String hqio = "headline-question-image-option";
	public static final String hiqo = "headline-image-question-option";
	public static final String ihqo = "image-headline-question-option";
	
	protected Group root;
	protected int number;
	
	protected VBox vbox = new VBox(20);
	protected ScrollPane pane = new ScrollPane(vbox);
	
	protected ComboBox<String> type = new ComboBox<>();
	
	protected TextField question_headline = new TextField("Headline");
	protected TextField question = new TextField("Enter your Question");
	
	protected VBox answer_box = new VBox();
	protected ArrayList<TextField> answers = new ArrayList<>();
	protected Label new_answer = new Label("new Answer +");
	protected Label add_image = new Label("Add/Change Image +");
	protected Font standard_answer_font;
	
	protected File image_file;
	protected Image question_image;
	protected ImageView question_view = new ImageView(noimage);
	protected Label correct_answer_label = new Label("Correct Answer");
	protected ComboBox<Integer> correct_answer = new ComboBox<>();
	public QuestionEditor(Group root, double width, double height, String name, int number,  EducationEditor parent) {
		super(root, width, height, name, parent);
		this.root = root;
		this.number = number;
		vbox.setAlignment(Pos.CENTER);
		
		type.getItems().addAll(hqoi, hqio, hiqo, ihqo);
		type.setValue(hqoi);
		type.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(oldValue!=newValue) {
					vbox.getChildren().removeAll(question_headline, question, answer_box, correct_answer_label, correct_answer, new_answer, question_view, add_image);
					switch(newValue) {
					case hqoi:
						vbox.getChildren().addAll(question_headline, question, answer_box, correct_answer_label, correct_answer, new_answer, question_view, add_image);
						break;
					case hqio:
						vbox.getChildren().addAll(question_headline, question, question_view, add_image, answer_box, correct_answer_label, correct_answer, new_answer);
						break;
					case hiqo:
						vbox.getChildren().addAll(question_headline, question_view, add_image, question, answer_box, correct_answer_label, correct_answer, new_answer);
						break;
					case ihqo:
						vbox.getChildren().addAll(question_view, add_image, question_headline, question, answer_box, correct_answer_label, correct_answer, new_answer);
						break;
					}
					root.requestLayout();
				}
			}
		});
		
		question_headline.setFont(new Font(height*0.04));
		question_headline.setBackground(background);
		
		standard_answer_font = new Font(height*0.02);
		
		question.setFont(new Font(height*0.03));
		question.setBackground(background);
		
		question_view.setFitHeight(height*0.5);
		question_view.setFitWidth(question_view.getFitHeight()*(noimage.getWidth()/noimage.getHeight()));
		
		correct_answer.getItems().addAll(1,2);
		correct_answer.setValue(1);
		correct_answer_label.setFont(standard_answer_font);
		correct_answer_label.setTextFill(Color.WHEAT);
		
		TextField answer_1 = new TextField("Answer 1");
		answer_1.setFont(standard_answer_font);
		TextField answer_2 = new TextField("Answer 2");
		answer_2.setFont(standard_answer_font);
		answers.add(answer_1);
		answers.add(answer_2);
		answer_box.getChildren().addAll(answer_1, answer_2);
		
		new_answer.setFont(standard_answer_font);
		new_answer.setTextFill(Color.ALICEBLUE);
		new_answer.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			addAnswer("Answer "+(answers.size()+1));
		});
		
		add_image.setFont(standard_answer_font);
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
			question_view.setImage(question_image);;
			question_view.setFitWidth(question_view.getFitHeight()*(question_image.getWidth()/question_image.getHeight()));
			vbox.requestLayout();
		});
		pane.setPrefHeight(editor.getHeight());
		vbox.getChildren().addAll(type, question_headline, question, answer_box, correct_answer_label, correct_answer, new_answer, question_view, add_image);
		vbox.setBackground(background);
		vbox.requestLayout();
		pane.setBackground(background);
		pane.setLayoutX((width-vbox.getBoundsInParent().getWidth())*0.5);
		editor_root.getChildren().add(pane);
	}
	
	public QuestionEditor(Group root, double width, double height, String name, int number, EducationEditor parent, String headline_text, String question_text, String type_text, int correct_answer_int, JSONArray options, boolean image_used, File image_file) {
		this(root, width, height, name, number, parent);
		question_headline.setText(headline_text);
		question.setText(question_text);
		
		correct_answer.setValue(correct_answer_int);
		
		if(type_text.contains("i")) {
			type_text = type_text.replace("h", "headline-").replace("q", "question-").replace("o", "options-").replace("i", "image-");
			type_text = type_text.substring(0, type_text.length()-1);
		}else {
			type_text = type_text.replace("h", "headline-").replace("q", "question-").replace("o", "options-")+"image";
		}
		type.setValue(type_text);
		
		for(Object o : options) {
			if(o instanceof String) {
				addAnswer((String) o);
			}
		}
		
		if(image_used) {
			this.image_file = image_file;
			try {
				question_image = new Image(new FileInputStream(image_file));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			question_view.setImage(question_image);;
			question_view.setFitWidth(question_view.getFitHeight()*(question_image.getWidth()/question_image.getHeight()));
			vbox.requestLayout();
		}
	}

	public static QuestionEditor init(double width, double height, String name, int number, EducationEditor parent) {
		return new QuestionEditor(new Group(), width, height, name, number, parent);
	}
	
	public static QuestionEditor init(double width, double height, int number, EducationEditor parent, ZipFile file) {
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
			InputStream inputStream = file.getInputStream(file.getFileHeader("question.json"));
			
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
		QuestionEditor editor;
		if(jsonobjekt.getString("type").contains("i")) {
			editor = new QuestionEditor(new Group(), width, height, file.getFile().getName(), number, parent, jsonobjekt.getString("headline"), jsonobjekt.getString("question"), jsonobjekt.getString("type"), jsonobjekt.getInt("correctanswer"), 
					jsonobjekt.getJSONArray("options"), true,  new File("temporary/"+file.getFile().getName().replace(".", "-")+"/"+jsonobjekt.getString("image")));
		}else {
			editor = new QuestionEditor(new Group(), width, height, file.getFile().getName(), number, parent, jsonobjekt.getString("headline"), jsonobjekt.getString("question"), jsonobjekt.getString("type"), jsonobjekt.getInt("correctanswer"),
					jsonobjekt.getJSONArray("options"), false, null);
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
		object.put("headline", question_headline.getText());
		object.put("question", question.getText());
		String final_type = type.getValue();
		final_type = final_type.replace("headline", "h");
		final_type = final_type.replace("image", "i");
		final_type = final_type.replace("question", "q");
		final_type = final_type.replace("option", "o");
		final_type = final_type.replaceAll("-", "");
		if(question_image != null) {
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
		object.put("optioncount", answers.size());
		object.put("jumpto", number-1);
		object.put("correctanswer", correct_answer.getValue()-1);
		JSONArray options = new JSONArray();
		for(TextField tf : answers) {
			options.put(tf.getText());
		}
		object.put("options", options);
		File temp_file = new File("temporary/"+name.substring(0).replace(".", "-")+"/question.json");
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
	
	private void addAnswer(String answer) {
		TextField new_field = new TextField(answer);
		new_field.setFont(standard_answer_font);
		correct_answer.getItems().add(answers.size()+1);
		answers.add(new_field);
		answer_box.getChildren().add(new_field);
		root.requestLayout();
	}
	
	
}