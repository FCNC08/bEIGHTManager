package education;

import javafx.scene.Group;

public class LessonEditor extends EducationEditors{

	public LessonEditor(Group root, double width, double height, String name, EducationEditor parent) {
		super(root, width, height, name, parent);
	}
	
	public static LessonEditor init(double width, double height, String name, EducationEditor parent) {
		return new LessonEditor(new Group(), width, height, name, parent);
	}

	@Override
	public void close() {
		snapshot(null, image);
	}

}
