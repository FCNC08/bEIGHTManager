package education;

import javafx.scene.Group;

public class LessonEditor extends EducationEditors{

	public LessonEditor(Group root, double width, double height, String name) {
		super(root, width, height, name);
	}
	
	public static LessonEditor init(double width, double height, String name) {
		return new LessonEditor(new Group(), width, height, name);
	}

}
