package education;

import javafx.scene.Group;

public class TestEditor extends EducationEditors{

	public TestEditor(Group root, double width, double height, String name) {
		super(root, width, height, name);
	}

	public static TestEditor init(double width, double height, String name) {
		return new TestEditor(new Group(), width, height, name);
	}
	
}
