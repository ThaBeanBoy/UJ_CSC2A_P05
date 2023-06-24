package acsse.csc2a.gui;

import acsse.csc2a.model.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;

import acsse.csc2a.file.DataReader;

/**
 * Root node for GUI that views MWSBC Ship & Message data
 * @author TG Chipoyera
 * @version P05
 * @see Insets,GridPane,VBox,FileChooser,StackPane
 */
public class ShipPane extends StackPane{
    private final MenuBar menuBar;
    private Ship ship;

    /**
     * Initialises ShipPane Class
     */
    public ShipPane(){
        final Menu FileMenu = new Menu("File");
        final MenuItem OpenFileMenuItem = new MenuItem("Open");

        // Adding file menu to the menu bar
        menuBar = new MenuBar(FileMenu);

        // Adding the open menu item to the file menu
        FileMenu.getItems().add(OpenFileMenuItem);

        // Adding event listener to menu item
        OpenFileMenuItem.setOnAction(e->{
            final FileChooser fileChooser = new FileChooser();

            // Setting file chooser window
            fileChooser.setTitle("Open file");
            fileChooser.setInitialDirectory(new File("data"));

            // Open file picker window
            File shipFile = fileChooser.showOpenDialog(null);

            if(shipFile != null){
                // reading the file
                this.ship  = DataReader.readShipFile(shipFile);

                //Display the ships
                this.displayMessages();
            }
        });

        //Displaying the ShipPane
        VBox Box = new VBox();
        Box.getChildren().addAll(menuBar);
        this.getChildren().add(Box);
        this.setWidth(400);
    }

    /**
     * Used when the 'open' menu item is clicked. it will display all the data in the file that is selected
     */
    private void displayMessages(){
        // Ship Information
        TitledPane shipTitle = new TitledPane();
        shipTitle.setText(String.format("%s's information", this.ship.getName()));
        shipTitle.setContent(this.ShipProperties());

        //Making list view
        ListView<TitledPane> listView = new ListView<>();


        //Adding elements to the list view
        listView.getItems().add(shipTitle);
        listView.getItems().add(this.MessagesSet("Normal Messages", MESSAGE_TYPE.NormalMessage));
        listView.getItems().add(this.MessagesSet("Encrypted Message", MESSAGE_TYPE.EncryptedMessage));
        listView.getItems().add(this.MessagesSet("SOS Messages", MESSAGE_TYPE.SOSMessage));

        //Clearing the screen
        this.getChildren().clear();
        this.getChildren().add(listView);
    }

    /**
     * Makes a grid pane that contains information about the Ship's information.
     * @return GridPane
     */
    private GridPane ShipProperties(){
        GridPane grid  = new GridPane();

        //Adding the Ship ID
        grid.add(new Label("Ship ID"), 0, 0);
        grid.add(this.ReadOnlyTextField(this.ship.getID()), 0, 1);

        // Adding the Ship name
        grid.add(new Label("Ship Name"), 1, 0);
        grid.add(this.ReadOnlyTextField(this.ship.getName()), 1, 1);

        //Setting gaps
        grid.setVgap(8);
        grid.setHgap(8);

        return grid;
    }

    /**
     * Creates a TitlePane that has the Title, &  contains all the relevant messages
     * @param SetName - Set's the title of the TitlePane
     * @param type - Is a MESSAGE_TYPE. This filters the messages & displays the appropriate messages.
     * @return TitlePan
     */
    private TitledPane MessagesSet(String SetName, MESSAGE_TYPE type){
        TitledPane MessageSetTitle = new TitledPane();
        // Setting the title
        MessageSetTitle.setText(SetName);

        //Making the ScrollPan
        ScrollPane Scroll = new ScrollPane();
        VBox Box = new VBox();
        Box.setSpacing(32);
        Box.setPadding(new Insets(16));

        // filtering the right messages
        Message[] messages = Arrays.stream(this.ship.getMessages()).filter(m-> m.getMessage_type() == type).toArray(Message[]::new);
        for(Message m: messages){
            Box.getChildren().add(MessageProperties(m));
        }

        Scroll.setContent(Box);
        MessageSetTitle.setContent(Scroll);

        return MessageSetTitle;
    }

    /**
     * Returns a Grid of information pertaining to the Message, such as the contents, source planet etc...
     * @param m The message object
     * @return GridPane
     */
    private GridPane MessageProperties(Message m){
        GridPane grid = new GridPane();

        // Adding Message properties

        // Adding the ID
        grid.add(new Label("ID: "), 0, 0);
        grid.add(this.ReadOnlyTextField(m.getID()), 1, 0);

        //Adding the source planet
        grid.add(new Label("Source Planet: "), 2, 0);
        grid.add(this.ReadOnlyTextField(m.getPlanet_source().toString()), 3, 0);

        //Adding the Message Type
        grid.add(new Label("Type"), 0, 1);
        grid.add(this.ReadOnlyTextField(m.getMessage_type().toString()), 1, 1);

        // Adding the Destination planet
        grid.add(new Label("Destination Planet "), 2, 1);
        grid.add(this.ReadOnlyTextField(m.getPlanet_destination().toString()),3,1);

        // Adding the contents of the message
        grid.add(new Label("Contents:"),0,2);
        TextField MessageContent = this.ReadOnlyTextField(m.getContents());
        //Spanning the content over the whole entire row
        GridPane.setColumnSpan(MessageContent, 3);
        grid.add(MessageContent,1,2);

        // Adding gaps
        grid.setHgap(8);
        grid.setVgap(8);

        return grid;
    }

    /**
     * This returns a read only TextField, so that the user is not able to make changes to the contents of the text field.
     * @param Value The value that the TextField should contain
     * @return TextField
     */
    private TextField ReadOnlyTextField(String Value){
        TextField field = new TextField(Value);
        field.setEditable(false);
        return field;
    }
}
