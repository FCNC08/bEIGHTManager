package education;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class TestEditor extends EducationEditors{
	protected TextField headline = new TextField("Headline");
	protected VBox elements = new VBox();
	CheckBox and_box;
	CheckBox nand_box;
	CheckBox nor_box;
	CheckBox not_box;
	CheckBox or_box;
	CheckBox xnor_box;
	CheckBox xor_box;
	protected Label add_external = new Label("Add External-Component");
	protected VBox externals = new VBox();
	protected ArrayList<File> external_files = new ArrayList<>();
	
	public TestEditor(Group root, double width, double height, String name, String path, EducationEditor parent) {
		super(root, width, height, name, path, parent);
		headline.setLayoutY(height*0.1);
		headline.setLayoutX(width*0.4);
		headline.setFont(new Font(height*0.04));
		
		Font text_font = new Font(height*0.02);
		
		and_box = new CheckBox("AND-Gate");
		and_box.setFont(text_font);
		and_box.setTextFill(Color.WHEAT);
		
		nand_box = new CheckBox("NAND-Gate");
		nand_box.setFont(text_font);
		nand_box.setTextFill(Color.WHEAT);
		
		nor_box = new CheckBox("NOR-Gate");
		nor_box.setFont(text_font);
		nor_box.setTextFill(Color.WHEAT);
		
		not_box = new CheckBox("NOT-Gate");
		not_box.setFont(text_font);
		not_box.setTextFill(Color.WHEAT);
		
		or_box = new CheckBox("OR-Gate");
		or_box.setFont(text_font);
		or_box.setTextFill(Color.WHEAT);
		
		xnor_box = new CheckBox("XNOR-Gate");
		xnor_box.setFont(text_font);
		xnor_box.setTextFill(Color.WHEAT);
		
		xor_box = new CheckBox("XOR-Gate");
		xor_box.setFont(text_font);
		xor_box.setTextFill(Color.WHEAT);
		
		elements.getChildren().addAll(and_box, nand_box, nor_box, not_box, or_box, xnor_box, xor_box);
		elements.setLayoutY(height*0.3);
		elements.setLayoutX(width*0.1);
		
		add_external.setFont(text_font);
		add_external.setUnderline(true);
		add_external.setTextFill(Color.WHEAT);
		add_external.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
			FileChooser fc = new FileChooser();
			ExtensionFilter extfilt = new ExtensionFilter("External-Component", "*.cmp");
			fc.getExtensionFilters().add(extfilt);
			File comp = fc.showOpenDialog(new Stage());
			if(comp != null) {
				Label comp_label = new Label(comp.getName());
				comp_label.setFont(text_font);
				comp_label.setTextFill(Color.WHEAT);
				externals.getChildren().add(comp_label);
				external_files.add(comp);
				root.requestLayout();
			}
		});
		
		externals.getChildren().add(add_external);
		
		externals.setLayoutY(height*0.3);
		externals.setLayoutX(width*0.6);
		
		editor_root.getChildren().addAll(headline, elements, externals);
	}

	public static TestEditor init(double width, double height, String name, String path, EducationEditor parent) {
		return new TestEditor(new Group(), width, height, name, path, parent);
	}

	@Override
	public void close() {
		snapshot(null, image);
		ZipFile space_file = new ZipFile("temporary/space.spce");
		JSONObject space = new JSONObject();
		JSONArray defaults = new JSONArray();
		if(and_box.isSelected()) {
			defaults.put("AND");
		}
		if(nand_box.isSelected()) {
			defaults.put("NAND");
		}
		if(nor_box.isSelected()) {
			defaults.put("NOR");
		}
		if(not_box.isSelected()) {
			defaults.put("NOT");
		}
		if(or_box.isSelected()) {
			defaults.put("OR");
		}
		if(xnor_box.isSelected()) {
			defaults.put("XNOR");
		}
		if(xor_box.isSelected()) {
			defaults.put("XOR");
		}
		space.append("defaultcomponents", defaults);
		
		JSONArray externals = new JSONArray();
		for(File ext_file : external_files) {
			externals.put(ext_file.getName());
			try {
				space_file.addFile(ext_file, EducationEditor.parameter);
			} catch (ZipException e) {
				e.printStackTrace();
			}
		}
		
		space.append("externalcomponents", externals);
		
		File temp_file = new File("temporary/space.json");
		try(FileWriter fwriter = new FileWriter(temp_file)){
			fwriter.write(space.toString());
			fwriter.flush();
			space_file.addFile(temp_file, EducationEditor.parameter);
			temp_file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONObject object = new JSONObject();
		object.append("headline", headline.getText());
		object.append("space", "space.spce");
		try {
			file.addFile(space_file.getFile(), EducationEditor.parameter);
		} catch (ZipException e) {
			e.printStackTrace();
		}
		temp_file = new File("temporary/test.json");
		try(FileWriter fwriter = new FileWriter(temp_file)){
			fwriter.write(object.toString());
			fwriter.flush();
			file.addFile(temp_file, EducationEditor.parameter);
			temp_file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		parent.save();
	}
	
}
