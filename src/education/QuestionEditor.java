package education;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class QuestionEditor extends EducationEditors{

	protected TextField question;
	protected VBox answer_box = new VBox();
	protected ArrayList<TextField> answers = new ArrayList<>();
	protected Label new_answer = new Label("new Answer +");
	protected Font standard_answer_font;
	protected Label add_image = new Label("Add/Change Image +");
	protected Image question_image;
	protected ImageView question_view;
	public QuestionEditor(Group root, double width, double height, String name, EducationEditor parent) {
		super(root, width, height, name, parent);
		standard_answer_font = new Font(height*0.02);
		question = new TextField("Enter your Question");
		question.setFont(new Font(height*0.03));
		question.setLayoutY(height*0.05);
		question.setLayoutX((width-question.getBoundsInParent().getWidth())*0.5);
		
		TextField answer_1 = new TextField("Answer 1");
		answer_1.setFont(standard_answer_font);
		TextField answer_2 = new TextField("Answer 2");
		answer_2.setFont(standard_answer_font);
		answers.add(answer_1);
		answers.add(answer_2);
		answer_box.getChildren().addAll(answer_1, answer_2);
		answer_box.setLayoutY(height*0.1+question.getBoundsInParent().getHeight());
		answer_box.setLayoutX((width*0.2));
		
		new_answer.setFont(standard_answer_font);
		new_answer.setLayoutX((width-new_answer.getBoundsInParent().getWidth())*0.5);
		new_answer.setLayoutY(editor.getHeight()*0.9);
		new_answer.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			TextField new_field = new TextField("Answer "+answers.size());
			new_field.setFont(standard_answer_font);
			answers.add(new_field);
			answer_box.getChildren().addAll(new_field);
		});
		
		add_image.setFont(standard_answer_font);
		add_image.setLayoutX((width-add_image.getBoundsInParent().getWidth())*0.5);
		add_image.setLayoutY(editor.getHeight()*0.85);
		add_image.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
			File file = fc.showOpenDialog(new Stage());
			try {
				question_image = new Image(new FileInputStream(file));
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
		
		editor_root.getChildren().addAll(question, answer_box, new_answer, add_image);
	}

	public static QuestionEditor init(double width, double height, String name, EducationEditor parent) {
		return new QuestionEditor(new Group(), width, height, name, parent);
	}

	@Override
	public void close() {
		snapshot(null, image);
		
	}
	
}
