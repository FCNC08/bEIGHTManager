package education;

import javafx.scene.Parent;
import javafx.scene.SubScene;
import net.lingala.zip4j.ZipFile;

public class EducationEditors extends SubScene{
	protected ZipFile file;
	public EducationEditors(Parent root, double width, double height) {
		super(root, width, height);
	}
	
	public ZipFile getFile() {
		return file;
	}

}
