package education;

import javafx.scene.Group;

public class QuestionEditor extends EducationEditors{

	public QuestionEditor(Group root, double width, double height, String name, EducationEditor parent) {
		super(root, width, height, name, parent);
	}

	public static QuestionEditor init(double width, double height, String name, EducationEditor parent) {
		return new QuestionEditor(new Group(), width, height, name, parent);
	}
	
}
