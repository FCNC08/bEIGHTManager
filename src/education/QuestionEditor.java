package education;

import javafx.scene.Group;

public class QuestionEditor extends EducationEditors{

	public QuestionEditor(Group root, double width, double height, String name) {
		super(root, width, height, name);
	}

	public static QuestionEditor init(double width, double height, String name) {
		return new QuestionEditor(new Group(), width, height, name);
	}
	
}
