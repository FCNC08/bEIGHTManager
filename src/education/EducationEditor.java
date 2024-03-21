package education;

import java.io.File;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
	protected ArrayList<EducationEditors> sections = new ArrayList();
	
	protected ZipFile file;
	protected static ZipParameters parameter;
	
	public EducationEditor(Group root, double width, double height) {
		super(root, width, height);
		if(parameter != null) {
			parameter.setCompressionLevel(CompressionLevel.NORMAL);
			parameter.setCompressionMethod(CompressionMethod.DEFLATE);
		}
		this.Mainroot = root;
		
		Pane add_new = new Pane();
		
		Rectangle add_new_rectangle = new Rectangle(width*0.1, width*0.1);
		add_new_rectangle.setFill(Color.ALICEBLUE);
		Text add_new_text = new Text("Add new Component+");
		add_new_text.setFont(new Font(width*0.007));
		add_new_text.setLayoutX((width*0.1-add_new_text.getBoundsInLocal().getWidth())*0.5);
		add_new_text.setLayoutY((width*0.1-add_new_text.getBoundsInLocal().getHeight())*0.5);
		
		add_new.getChildren().addAll(add_new_rectangle, add_new_text);
		
		add_new.setLayoutX(15);
		add_new.setLayoutY(15);
		
		EventHandler<MouseEvent> create_new_module_handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				FileChooser fc = new FileChooser();
				ExtensionFilter test_filter = new ExtensionFilter("Test-files (*.tst)", "*.tst");
				ExtensionFilter lesson_filter = new ExtensionFilter("Lesson-Files (*.lsn)", "*.lsn");
				ExtensionFilter question_filter = new ExtensionFilter("Question-file (*.qst)", "*.qst");
				fc.getExtensionFilters().addAll(test_filter, lesson_filter, question_filter);
				String name = fc.showSaveDialog(new Stage()).getName();
				EducationEditors editor;
				String ending = name.substring(name.length()-5);
				if(ending.contains(".qst")) {
					editor = QuestionEditor.init(width, height, name);
				}else if(ending.contains(".lsn")) {
					editor = LessonEditor.init(width, height, name);
				}else if(ending.contains(".tst")){
					editor = TestEditor.init(width, height, name);
				}else {
					editor = null;
				}
				sections.add(editor);
				Mainroot.getChildren().add(editor.getIcon());
			}
		};
		add_new.addEventFilter(MouseEvent.MOUSE_CLICKED, create_new_module_handler);
		Mainroot.getChildren().add(add_new);
		
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
