package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Model.Account;
import sample.Model.Datasource;

import java.io.IOException;

public class WithdrawDialogController {

    @FXML
    private Button backButton;

    @FXML
    private Button okButton;

    @FXML
    private TextField IDField;

    @FXML
    private TextField amountField;

    public void initialize() {
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switchScene(event);
            }
        });

        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String id = IDField.getText().trim();
                if(id.isEmpty()) Controller.showAlert("ID field can't be empty");
                else {
                    int intID = Account.stringToInt(id);
                    if(!(Account.checkIfIsPositiveInt(intID))) Controller.showAlert("ID must be positive integer");
                    else {
                        String amount = amountField.getText().trim();
                        if(amount.isEmpty()) Controller.showAlert("Amount field can't be empty");
                        else {
                            double doubleAmount = Account.stringToDouble(amount);
                            if(!(Account.checkIfIsPositiveDouble(doubleAmount))) Controller.showAlert("Amount must be greater than 0. Decimal point is . not , ");
                            else{
                                if(Controller.showConfirmation("Withdrawing "+doubleAmount+ "$ from client with ID = "+intID)){
                                    int result = Datasource.getInstance().withdraw(intID, doubleAmount);
                                    if(result==-1) Controller.showAlert("There is no ID = "+intID+ " in database");
                                    else if(result==-2) Controller.showAlert("User with ID ="+intID+ " doesn't have enough money to withdraw");
                                    else if(result==0) Controller.showAlert("Couldn't withdraw");
                                    switchScene(event);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void switchScene(MouseEvent e){
        try{
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent home_page_parent =loader.load();

            Scene home_page_scene = new Scene(home_page_parent);

            Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            app_stage.setScene(home_page_scene);
            app_stage.show();
            Controller controller = loader.getController();
            controller.listAccounts();
        } catch(IOException io){
            System.out.println("Error while swiching scene");
        }

    }
}
