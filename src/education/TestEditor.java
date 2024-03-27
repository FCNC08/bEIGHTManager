package education;

import javafx.scene.Group;

public class TestEditor extends EducationEditors{

	public TestEditor(Group root, double width, double height, String name, String path, EducationEditor parent) {
		super(root, width, height, name, path, parent);
	}

	public static TestEditor init(double width, double height, String name, String path, EducationEditor parent) {
		return new TestEditor(new Group(), width, height, name, path, parent);
	}

	@Override
	public void close() {
		snapshot(null, image);
	}
	
}
