package education;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
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

	protected double width;
	protected double height;
	
	protected Group Mainroot;
	protected ArrayList<SubScene> sections = new ArrayList<SubScene>();
	protected FlowPane fpane;
	protected int actual_section;
	
	protected Group main_scene_root;
	protected SubScene main_scene;
	
	protected TextField headline = new TextField("Headline");
	ComboBox<String> difficulty = new ComboBox<>();
	
	protected ZipFile file;
	public static ZipParameters parameter;
	
	public EducationEditor(Group root, double width, double height) {
		super(root, width, height);
		this.width = width;
		this.height = height;
		if(parameter == null) {
			parameter = new ZipParameters();
			parameter.setCompressionLevel(CompressionLevel.NORMAL);
			parameter.setCompressionMethod(CompressionMethod.DEFLATE);
		}
		this.Mainroot = root;
		
		main_scene_root = new Group();
		main_scene = new SubScene(main_scene_root, width, height);
		
		headline.setFont(new Font(height*0.04));
		headline.setLayoutY(height*0.01);
		
		difficulty.getItems().addAll("easy", "medium", "hard");
		difficulty.setValue("easy");
		difficulty.setLayoutY(height*0.01);
		difficulty.setLayoutX(width*0.5);
		
		fpane = new FlowPane();
		fpane.setPrefWrapLength(width*0.9);
		
		Group add_new_root = new Group();
		SubScene add_new_scene = new SubScene(add_new_root, width*0.1, width*0.1);
		
		Rectangle add_new_rectangle = new Rectangle(width*0.1, width*0.1);
		add_new_rectangle.setFill(Color.ALICEBLUE);
		Text add_new_text = new Text("Add new Component+");
		add_new_text.setFont(new Font(width*0.007));
		add_new_text.setLayoutX((width*0.1-add_new_text.getBoundsInLocal().getWidth())*0.5);
		add_new_text.setLayoutY((width*0.1-add_new_text.getBoundsInLocal().getHeight())*0.5);
		
		add_new_root.getChildren().addAll(add_new_rectangle, add_new_text);
		
		//add_new_scene.setLayoutX(15);
		//add_new_scene.setLayoutY(0);
		sections.add(main_scene);
		actual_section = 0;
		fpane.setLayoutY(height*0.1);

		final EducationEditor that = this;
		fpane.getChildren().add(add_new_scene);
		EventHandler<MouseEvent> create_new_module_handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				FileChooser fc = new FileChooser();
				ExtensionFilter test_filter = new ExtensionFilter("Test-files (*.tst)", "*.tst");
				ExtensionFilter lesson_filter = new ExtensionFilter("Lesson-Files (*.lsn)", "*.lsn");
				ExtensionFilter question_filter = new ExtensionFilter("Question-file (*.qst)", "*.qst");
				fc.getExtensionFilters().addAll(test_filter, lesson_filter, question_filter);
				fc.setInitialFileName("test.tst");
				File file = fc.showSaveDialog(new Stage());
				if(file != null) {
					String name = file.getName();
					EducationEditors editor;
					String ending = name.substring(name.length()-5);
					final int number = (sections.size());
					if(ending.contains(".qst")) {
						editor = QuestionEditor.init(width, height, name, number-2, that);
					}else if(ending.contains(".lsn")) {
						editor = LessonEditor.init(width, height, name, that);
					}else if(ending.contains(".tst")){
						editor = TestEditor.init(width, height, name, that);
					}else {
						editor = null;
					}
					sections.add(editor);
					EventHandler<MouseEvent> open_event_handler = new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							Mainroot.getChildren().remove(sections.get(actual_section));
							actual_section = number;
							Mainroot.getChildren().add(sections.get(actual_section));
						}
					};
					editor.getIcon().addEventFilter(MouseEvent.MOUSE_CLICKED, open_event_handler);
					//editor.getIcon().setLayoutX(hbox.getBoundsInLocal().getWidth());;
					fpane.getChildren().add(editor.getIcon());
				}
				
				}
			
		};
		add_new_scene.addEventFilter(MouseEvent.MOUSE_CLICKED, create_new_module_handler);
		main_scene_root.getChildren().addAll(fpane, headline, difficulty);
		Mainroot.getChildren().add(main_scene);
	}
	
	public void next() {
		Mainroot.getChildren().remove(sections.get(actual_section));
		actual_section++;
		actual_section%=sections.size();
		Mainroot.getChildren().add(sections.get(actual_section));
	}
	public void back() {
		Mainroot.getChildren().remove(sections.get(actual_section));
		actual_section--;
		actual_section%=sections.size();
		Mainroot.getChildren().add(sections.get(actual_section));
	}
	
	public void createNewProject() {
		save();
		remove();
	}
	public void save() {
		if(file == null) {
			saveas();
		}else {
			/*try {
				if(!file.getFileHeaders().isEmpty()) {
					for(FileHeader fh : file.getFileHeaders()) {
						try{file.removeFile(fh);}catch(ZipException e) {e.printStackTrace();}
					}
				}
				
			} catch (ZipException e) {
				e.printStackTrace();
			}*/
			JSONObject object = new JSONObject();
			object.put("name", headline.getText());
			object.put("difficulty", difficulty.getValue());
			JSONArray order = new JSONArray();
			try {
				List<FileHeader> fileheaders = file.getFileHeaders();
				for(FileHeader fh : fileheaders) {
					file.removeFile(fh);
				}
			} catch (ZipException e1) {
				e1.printStackTrace();
			}
			for(SubScene sub: sections) {
				try {
					if(sub instanceof EducationEditors) {
						EducationEditors ee = (EducationEditors) sub;
						File ee_file = ee.getFile().getFile();
						file.addFile(ee_file, parameter);
						JSONObject ee_object = new JSONObject();
						if(ee instanceof QuestionEditor) {
							ee_object.put("type", "question");
						}else if(ee instanceof LessonEditor) {
							ee_object.put("type", "lesson");
						}else if(ee instanceof TestEditor) {
							ee_object.put("type", "test");
						}
						ee_object.put("filename", ee_file.getName());
						order.put(ee_object);
					}					
				} catch (ZipException e) {
					e.printStackTrace();
				}
			}
			object.put("order", order);
			File temp_file = new File("temporary/settings.json");
			try(FileWriter fwriter = new FileWriter(temp_file)){
				fwriter.write(object.toString());
				fwriter.flush(); 
				file.addFile(temp_file, parameter);
				temp_file.delete();
			} catch (IOException e) {
				e.printStackTrace();
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
			save();
		}
	}
	public void remove() {
		
		actual_section = 0;
		sections.clear();
		main_scene_root.getChildren().clear();
		
		headline.setFont(new Font(height*0.04));
		headline.setLayoutY(height*0.01);
		
		difficulty.getItems().addAll("easy", "medium", "hard");
		difficulty.setValue("easy");
		difficulty.setLayoutY(height*0.01);
		difficulty.setLayoutX(width*0.5);
		
		fpane = new FlowPane();
		fpane.setPrefWrapLength(width*0.9);
		
		Group add_new_root = new Group();
		SubScene add_new_scene = new SubScene(add_new_root, width*0.1, width*0.1);
		
		Rectangle add_new_rectangle = new Rectangle(width*0.1, width*0.1);
		add_new_rectangle.setFill(Color.ALICEBLUE);
		Text add_new_text = new Text("Add new Component+");
		add_new_text.setFont(new Font(width*0.007));
		add_new_text.setLayoutX((width*0.1-add_new_text.getBoundsInLocal().getWidth())*0.5);
		add_new_text.setLayoutY((width*0.1-add_new_text.getBoundsInLocal().getHeight())*0.5);
		
		add_new_root.getChildren().addAll(add_new_rectangle, add_new_text);
		
		//add_new_scene.setLayoutX(15);
		//add_new_scene.setLayoutY(0);
		sections.add(main_scene);
		actual_section = 0;
		fpane.setLayoutY(height*0.1);

		final EducationEditor that = this;
		fpane.getChildren().add(add_new_scene);
		EventHandler<MouseEvent> create_new_module_handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				FileChooser fc = new FileChooser();
				ExtensionFilter test_filter = new ExtensionFilter("Test-files (*.tst)", "*.tst");
				ExtensionFilter lesson_filter = new ExtensionFilter("Lesson-Files (*.lsn)", "*.lsn");
				ExtensionFilter question_filter = new ExtensionFilter("Question-file (*.qst)", "*.qst");
				fc.getExtensionFilters().addAll(test_filter, lesson_filter, question_filter);
				File file = fc.showSaveDialog(new Stage());
				if(file != null) {
					String name = file.getName();
					EducationEditors editor;
					String ending = name.substring(name.length()-5);
					final int number = (sections.size());
					if(ending.contains(".qst")) {
						editor = QuestionEditor.init(width, height, name, number-2, that);
					}else if(ending.contains(".lsn")) {
						editor = LessonEditor.init(width, height, name, that);
					}else if(ending.contains(".tst")){
						editor = TestEditor.init(width, height, name, that);
					}else {
						editor = null;
					}
						sections.add(editor);
						EventHandler<MouseEvent> open_event_handler = new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent event) {
								Mainroot.getChildren().remove(sections.get(actual_section));
								actual_section = number;
								Mainroot.getChildren().add(sections.get(actual_section));
							}
						};
						editor.getIcon().addEventFilter(MouseEvent.MOUSE_CLICKED, open_event_handler);
						//editor.getIcon().setLayoutX(hbox.getBoundsInLocal().getWidth());;
						fpane.getChildren().add(editor.getIcon());
				}
				
				}
			
		};
		add_new_scene.addEventFilter(MouseEvent.MOUSE_CLICKED, create_new_module_handler);
		main_scene_root.getChildren().addAll(fpane, headline, difficulty);
	}
	public void createSections(ZipFile file) {
		JSONObject jsonobject = null;
		try {
			if(file.isEncrypted()) {
				throw new IllegalArgumentException();
			}
			InputStream inputStream = file.getInputStream(file.getFileHeader("settings.json"));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead;
			while((bytesRead = inputStream.read(buffer))!=-1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			String jsonString = outputStream.toString(StandardCharsets.UTF_8);
			jsonobject = new JSONObject(jsonString);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		headline.setText(jsonobject.getString("name"));
		difficulty.setValue(jsonobject.getString("difficulty"));
		JSONArray modules = jsonobject.getJSONArray("order");
		try {
			file.extractAll("temporary/");
		} catch (ZipException e) {
			e.printStackTrace();
		}
		for(Object object : modules) {
			if(object instanceof JSONObject) {
				JSONObject modul = (JSONObject) object;
				EducationEditors editor = null;
				final int number = (sections.size());
				switch(modul.getString("type")) {
				case("lesson"):{
					ZipFile temporary_file = new ZipFile("temporary/"+modul.getString("filename"));
					editor = LessonEditor.init(width, height, this, temporary_file);
					temporary_file = null;
					break;
				}
				case("question"):{
					ZipFile temporary_file = new ZipFile("temporary/"+modul.getString("filename"));
					editor = QuestionEditor.init(width, height, number, this, temporary_file);
					temporary_file = null;
					break;
				}
				case("test"):{
					ZipFile temporary_file = new ZipFile("temporary/"+modul.getString("filename"));
					editor = TestEditor.init(width, height, this, temporary_file);
					temporary_file = null;
					break;
				}
				}
				sections.add(editor);
				EventHandler<MouseEvent> open_event_handler = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						Mainroot.getChildren().remove(sections.get(actual_section));
						actual_section = number;
						Mainroot.getChildren().add(sections.get(actual_section));
					}
				};
				editor.getIcon().addEventFilter(MouseEvent.MOUSE_CLICKED, open_event_handler);
				//editor.getIcon().setLayoutX(hbox.getBoundsInLocal().getWidth());;
				fpane.getChildren().add(editor.getIcon());
			}
		}
		
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
			createSections(file);
		}
	}
	
	public static EducationEditor init(double width, double height) {
		return new EducationEditor(new Group(), width, height);
	}

}
