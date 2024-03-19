package education;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

public class EducationEditor extends SubScene{

	protected Group Mainroot;
	protected ArrayList<EducationEditors> sections;
	
	protected ZipFile file;
	protected static ZipParameters parameter;
	
	public EducationEditor(Group root, double width, double height) {
		super(root, width, height);
		if(parameter != null) {
			parameter.setCompressionLevel(CompressionLevel.NORMAL);
			parameter.setCompressionMethod(CompressionMethod.DEFLATE);
		}
		this.Mainroot = root;
	}
	
	public void createNewProject() {
		save();
		remove();
	}
	public void save() {
		if(file != null) {
			saveas();
		}else {
			try {
				for(FileHeader fh : file.getFileHeaders()) {
					try{file.removeFile(fh);}catch(ZipException e) {e.printStackTrace();}
				}
			} catch (ZipException e) {
				e.printStackTrace();
			}
			for(EducationEditors ee: sections) {
				try {
					file.addFile(ee.getFile().getFile());
				} catch (ZipException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void saveas() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Save as");
		ExtensionFilter ext_filter = new ExtensionFilter("Lection-File (*.lct)", "*.lct");
		fc.getExtensionFilters().add(ext_filter);
		File selected_file = fc.showSaveDialog(new Stage());
		if(selected_file != null) {
			file = new ZipFile(selected_file);
		}
	}
	public void remove() {
		
	}
	public void open() {
		save();
		FileChooser fc = new FileChooser();
		fc.setTitle("open");
		ExtensionFilter ext_filter = new ExtensionFilter("Lection-File (*.lct)", "*.lct");
		fc.getExtensionFilters().add(ext_filter);
		File selected_file = fc.showOpenDialog(new Stage());
		if(selected_file != null) {
			remove();
			file = new ZipFile(selected_file);
		}
	}
	
	public static EducationEditor init(double width, double height) {
		return new EducationEditor(new Group(), width, height);
	}

}
