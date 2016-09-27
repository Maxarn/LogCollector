package logaggregatorclient;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        /*
        This method contains the UI that will initialize when the 
        application starts.
        
        Maybe a login window or something similar could be created here.
        */
        
        primaryStage.setScene(login(primaryStage));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
    
    private Scene login(Stage primaryStage){
    GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text login_title = new Text("Hej och välkommen");
        login_title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(login_title, 0, 0, 2, 1);
        
        grid.add(new Label("Username:"), 0, 1);
        grid.add(new Label("Password:"), 0, 2);
        
        TextField username_textfield = new TextField();
        grid.add(username_textfield, 1, 1);
        
        PasswordField password_passwordfield = new PasswordField();
        grid.add(password_passwordfield, 1, 2);
        
        Button login_button = new Button("Login");
        HBox login_button_hbox = new HBox(10);
        login_button_hbox.setAlignment(Pos.BOTTOM_RIGHT);
        login_button_hbox.getChildren().add(login_button);
        grid.add(login_button_hbox, 1, 4);
        
        login_button.setOnAction(e-> primaryStage.setScene(app(primaryStage)));
        
        Scene login = new Scene(grid, 350, 270);
        
        return login;
}
   private Scene app(Stage primaryStage){
       primaryStage.setTitle("Register service");
       GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Label company = new Label("Company");
        grid.add(company, 0, 0);
        Label inter = new Label("Interval");
        grid.add(inter, 1, 0);      
        Label servname = new Label("Source");
        grid.add(servname, 0, 2);
        
        TextField texcom = new TextField();
        grid.add(texcom,0,1);
        TextField texint = new TextField();
        grid.add(texint, 1, 1);
        TextField texser = new TextField();
        grid.add(texser,0,3);
        
        Button browse = new Button("Browse");
        grid.add(browse, 1, 3);
       
        Scene app = new Scene(grid,350,270);
        
       return app;
   }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}