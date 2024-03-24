package education;

import javafx.scene.Group;

public class TestEditor extends EducationEditors{

	public TestEditor(Group root, double width, double height, String name, EducationEditor parent) {
		super(root, width, height, name, parent);
	}

	public static TestEditor init(double width, double height, String name, EducationEditor parent) {
		return new TestEditor(new Group(), width, height, name, parent);
	}
	
}
