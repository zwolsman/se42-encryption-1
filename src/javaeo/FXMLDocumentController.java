package javaeo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javaeo.generators.Generator;
import javaeo.generators.GeneratorFactory;
import javaeo.signers.Signer;
import javaeo.signers.SignerFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 *
 * @author Marvin
 */
public class FXMLDocumentController implements Initializable, ChangeListener<String> {

    @FXML
    private Label lblFolder;

    @FXML
    private Label lblKeyPath;

    @FXML
    private Label lblFilePath;

    @FXML
    private Label lblValidFilePath;

    @FXML
    private Label lblValidKeyPath;

    @FXML
    private TextField txtKeySize;

    @FXML
    private ComboBox cbEncType;

    @FXML
    private ComboBox cbSignType;

    @FXML
    private ComboBox cbVerifyType;

    //Tab 1 variable
    private String folder;

    //Tab 2 variable
    private String keyPath;
    private String filePath;

    @FXML
    private TextField txtSigner;

    private static final String PRIVATE_KEY_FILENAME = "\\private.key";
    private static final String PUBLIC_KEY_FILENAME = "\\public.key";

    @FXML
    private void handleBrowseAction(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a File to Encrypt");
        File defaultDirectory = new File(System.getProperty("user.dir"));
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(lblFolder.getScene().getWindow());
        if (selectedDirectory != null) {
            folder = selectedDirectory.getAbsolutePath();
            lblFolder.setText(folder);
        }
    }

    @FXML
    private void handleCreateAction(ActionEvent event) {
        if (generator == null) {
            System.out.println("Generator is null");
            return;
        }

        if (generator.save(Integer.parseInt(txtKeySize.getText()), folder)) {
            System.out.println("Saved to " + folder);
        } else {
            System.out.println("Something went wrong while saving information");
        }
    }

    @FXML
    private void handleBrowseFileAction(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        File defaultDirectory = new File(System.getProperty("user.dir"));
        chooser.setInitialDirectory(defaultDirectory);
        File selectedFile = chooser.showOpenDialog(lblFolder.getScene().getWindow());

        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            lblFilePath.setText(filePath);
            lblValidFilePath.setText(filePath);
        }
    }

    @FXML
    private void handleBrowseKeyAction(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        File defaultDirectory = new File(System.getProperty("user.dir"));
        chooser.setInitialDirectory(defaultDirectory);
        File selectedFile = chooser.showOpenDialog(lblFolder.getScene().getWindow());

        if (selectedFile != null) {
            keyPath = selectedFile.getAbsolutePath();
            lblKeyPath.setText(keyPath);
            lblValidKeyPath.setText(keyPath);
        }
    }

    @FXML
    private void handleSignAction(ActionEvent event) throws IOException {
        if (signer == null) {
            System.out.println("Signer is null.");
            return;
        }

        if (keyPath.equals(folder + PRIVATE_KEY_FILENAME)) {
            if (signer.sign(keyPath, filePath, txtSigner.getText())) {
                System.out.println("Signed file!");
            } else {
                System.out.println("Error signing file");
            }
        }
    }

    @FXML
    private void handleVerifyAction(ActionEvent event) {
        if (signer.restore(keyPath, filePath)) {
            System.out.println("Restored!");
        } else {
            System.out.println("Invalid");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbEncType.getItems().setAll(GeneratorFactory.generators.keySet());
        cbSignType.getItems().setAll(SignerFactory.signers.keySet());
        cbVerifyType.getItems().setAll(SignerFactory.signers.keySet());

        cbEncType.getSelectionModel().selectedItemProperty().addListener(this);
        cbSignType.getSelectionModel().selectedItemProperty().addListener(this);
        cbVerifyType.getSelectionModel().selectedItemProperty().addListener(this);

        //Initialize folder thingy
        folder = System.getProperty("user.dir");
        lblFolder.setText(System.getProperty("user.dir"));
        System.out.println("DEFAULT FOLDER: " + folder);

        keyPath = folder + PRIVATE_KEY_FILENAME;
        filePath = "test.txt";
        lblFilePath.setText(filePath);

        keyPath = folder + PUBLIC_KEY_FILENAME;
    }

    private Generator generator = null;
    private Signer signer = null;

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

        if (GeneratorFactory.generators.containsKey(newValue)) {
            this.generator = (Generator) GeneratorFactory.generators.get(newValue);
        }
        if (SignerFactory.signers.containsKey(newValue)) {
            this.signer = (Signer) SignerFactory.signers.get(newValue);
        }
    }

}
