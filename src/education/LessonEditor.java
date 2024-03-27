package education;

import javafx.scene.Group;

public class LessonEditor extends EducationEditors{

	public LessonEditor(Group root, double width, double height, String name, String path, EducationEditor parent) {
		super(root, width, height, name, path, parent);
	}
	
	public static LessonEditor init(double width, double height, String name, String path, EducationEditor parent) {
		return new LessonEditor(new Group(), width, height, name, path, parent);
	}

	@Override
	public void close() {
		snapshot(null, image);
	}

}
