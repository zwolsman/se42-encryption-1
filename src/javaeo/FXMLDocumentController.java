package javaeo;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Marvin
 */
public class FXMLDocumentController implements Initializable, ChangeListener<String> {

    @FXML
    private Label lblFolder;

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

    //Tab 3 variable
    private String verifyFilePath;
    private String verifyKeyPath;

    @FXML
    private TextField txtSigner;

    @FXML
    private void handleBrowseAction(ActionEvent event) {
        System.out.println("You clicked me!");

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

    }

    @FXML
    private void handleBrowseKeyAction(ActionEvent event) {

    }

    @FXML
    private void handleSignAction(ActionEvent event) throws IOException {
        if (signer == null) {
            System.out.println("Signer is null.");
            return;
        }

        if (signer.sign(keyPath, filePath, txtSigner.getText())) {
            System.out.println("Signed file!");
        } else {
            System.out.println("Error signing file");
        }

    }

    @FXML
    private void handleVerifyAction(ActionEvent event) {
        if (signer.restore(verifyKeyPath, verifyFilePath)) {
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

        keyPath = folder + "\\private.key";
        filePath = "test.txt";

        verifyKeyPath = folder + "\\public.key";
        verifyFilePath = folder + "\\test(Signed by M W Zwolsman).txt";
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
