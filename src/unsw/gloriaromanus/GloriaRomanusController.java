package unsw.gloriaromanus;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONObject;
import unsw.gloriaromanus.game.BattleReporter;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.units.SoldierType;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

public class GloriaRomanusController{

  @FXML
  private StackPane root;
  @FXML
  private MapView mapView;
  @FXML
  private TextField invading_province;
  @FXML
  private TextField opponent_province;
  @FXML
  private TextArea output_terminal;
  @FXML
  private TextField player_faction;
  @FXML
  private TextField player_treasure;
  @FXML
  private TextField player_wealth;
  @FXML
  private TextField current_turn;
  @FXML
  private TextField province_wealth;
  @FXML
  private TextField province_tax_level;
  @FXML
  private TextField province_tax_revenue;
  @FXML
  private TableView<Soldier> province_army;
  private TableView.TableViewSelectionModel <Soldier> soldiersSelected;

  @FXML
  private Button invadeButton;

  private ArcGISMap map;

//  private Map<String, String> provinceToOwningFactionMap;

//  private Map<String, Integer> provinceToNumberTroopsMap;

//  private String humanFaction;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedOtherProvince;

  private FeatureLayer featureLayer_provinces;

  private Game game;

  String [] factions;
  String [] players;

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException {
    if (players == null) {
      Game.newGame("Rome");
      game = Game.getInstance();
      game.addReporter(new TextFXReporter());
    }
    else {
      Game.newGame(players);
      game = Game.getInstance();
      game.addReporter(new TextFXReporter());
    }
    currentlySelectedHumanProvince = null;
    currentlySelectedOtherProvince = null;

    showPlayerInfo();
    current_turn.setText(game.getTurn()+"");
    initializeProvinceLayers();
    soldiersSelected = province_army.getSelectionModel();
    soldiersSelected.setSelectionMode(SelectionMode.MULTIPLE);

    factions = game.getFactions();
    players = null;
  }

  @FXML
  public void gamePaused (ActionEvent e) {
    VBox pauseRoot = new VBox(10);
    pauseRoot.getChildren().add(new Label("Main Menu"));
    pauseRoot.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
    pauseRoot.setAlignment(Pos.CENTER);
    pauseRoot.setPadding(new Insets(20));

    Button resume = new Button("Continue");
    pauseRoot.getChildren().add(resume);

    Button newGame = new Button("New Game");
    pauseRoot.getChildren().add(newGame);

    Button saveGame = new Button("Save Game");
    pauseRoot.getChildren().add(saveGame);

    Button loadGame = new Button("Load Game");
    pauseRoot.getChildren().add(loadGame);

    Button exit = new Button("Quit");
    pauseRoot.getChildren().add(exit);

    Stage popupStage = new Stage(StageStyle.TRANSPARENT);
    popupStage.initOwner(root.getScene().getWindow());
    popupStage.initModality(Modality.APPLICATION_MODAL);
    popupStage.setScene(new Scene(pauseRoot, Color.TRANSPARENT));
    popupStage.setMinWidth(400);
    popupStage.setMinHeight(400);


    resume.setOnAction(event -> {
      root.setEffect(null);
      popupStage.hide();
    });

    newGame.setOnAction(event -> {
//      ArrayList<String> players = new ArrayList<>();
      VBox newGameRoot = new VBox(10);
      newGameRoot.minWidth(500);
      newGameRoot.minHeight(500);
      newGameRoot.getChildren().add(new Label("Main Menu"));
      newGameRoot.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
      newGameRoot.setAlignment(Pos.CENTER);
      newGameRoot.setPadding(new Insets(20));

      Button onePlayer = new Button("Single Player");
      newGameRoot.getChildren().add(onePlayer);

      Button twoPlayer = new Button("2 Players");
      newGameRoot.getChildren().add(twoPlayer);


      Button startButton = new Button("Start");

      onePlayer.setOnAction(event1 -> {

        players = new String[1];
        newGameRoot.getChildren().clear();
        newGameRoot.getChildren().add(createSelectionBox(1));
        newGameRoot.getChildren().add(startButton);
      });
      twoPlayer.setOnAction(event1 -> {
        players = new String[2];
        newGameRoot.getChildren().clear();
        newGameRoot.getChildren().add(createSelectionBox(2));
        newGameRoot.getChildren().add(startButton);
    });

      Stage newGameStage = new Stage(StageStyle.TRANSPARENT);
      newGameStage.initOwner(root.getScene().getWindow());
      newGameStage.initModality(Modality.APPLICATION_MODAL);
      newGameStage.setScene(new Scene(newGameRoot, Color.TRANSPARENT));
      newGameStage.setMinHeight(400);
      newGameStage.setMinWidth(400);

      newGameStage.show();
      startButton.setOnAction(event1 -> {
        try {
          initialize();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        newGameStage.hide();
      });
      root.setEffect(null);
      popupStage.hide();
    });
    saveGame.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();

      //Set extension filter for text files
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Save files (*.save)", "*.save");
      fileChooser.getExtensionFilters().add(extFilter);

      //Show save file dialog
      File file = fileChooser.showSaveDialog(popupStage);

      if (file != null) {
        game.saveGame(file);
      }
    });
    loadGame.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();

      //Set extension filter for text files
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Save files (*.save)", "*.save");
      fileChooser.getExtensionFilters().add(extFilter);

      //Show save file dialog
      File file = fileChooser.showOpenDialog(popupStage);

      if (file != null) {
        game.loadGame(file);
        game = Game.getInstance();
        root.setEffect(null);
        popupStage.hide();
      }

    });
    exit.setOnAction(event -> {System.exit(0);}); //System.exit()

    popupStage.show();
  }

  private VBox createSelectionBox (int playerNumber) {
    VBox selectionBox = new VBox(10);

    for (int i = 0; i < playerNumber; i++) {
      selectionBox.getChildren().add(new Label("Player " + i + ":"));

      HBox hb = new HBox(10);
      hb.minWidth(400);
      for (String s : factions) {
        ToggleButton b = new ToggleButton(s);
        b.setUserData(i);
        hb.getChildren().add(b);
        b.setOnAction(event1 -> {
          players[(Integer) b.getUserData()] = s;
        });
      }
      selectionBox.getChildren().add(hb);
    }
    return selectionBox;
  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedOtherProvince != null){
      if (soldiersSelected.isEmpty()) {
        printMessageToTerminal("No army selected!");
        return;
      }
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String) currentlySelectedOtherProvince.getAttributes().get("name");
      if (confirmIfProvincesConnected(humanProvince, enemyProvince)) {
        Army army = new Army(soldiersSelected.getSelectedItems());
        game.moveOrInvade(game.getProvince(humanProvince), game.getProvince(enemyProvince), army);
        resetSelections();  // reset selections in UI
        addAllPointGraphics(); // reset graphics
      }
      else{
        printMessageToTerminal("Provinces not adjacent, cannot invade!");
      }

    }
  }

  @FXML
  public void clickedEndTurn(ActionEvent actionEvent) throws IOException {
    game.pass();
    showPlayerInfo();
    current_turn.setText(game.getTurn()+"");
    resetSelections();  // reset selections in UI
    addAllPointGraphics(); // reset graphics
  }

  @FXML
  public void recruitMeleeInfantry (ActionEvent actionEvent) {
    recruitUnit(SoldierType.MELEE_INFANTRY);
  }
  @FXML
  public void recruitRangedInfantry (ActionEvent actionEvent) {
    recruitUnit(SoldierType.RANGED_INFANTRY);
  }
  @FXML
  public void recruitMeleeChivalry (ActionEvent actionEvent) {
    recruitUnit(SoldierType.MELEE_CHIVALRY);
  }
  @FXML
  public void recruitRangedChivalry (ActionEvent actionEvent) {
    recruitUnit(SoldierType.RANGED_CHIVALRY);
  }
  @FXML
  public void recruitMeleeArtillery (ActionEvent actionEvent) {
    recruitUnit(SoldierType.MELEE_ARTILLERY);
  }
  @FXML
  public void recruitRangedArtillery (ActionEvent actionEvent) {
    recruitUnit(SoldierType.RANGED_ARTILLERY);
  }
  private void recruitUnit (SoldierType soldierType) {
    if (currentlySelectedHumanProvince != null) {
      Province province = game.getProvince((String) currentlySelectedHumanProvince.getAttributes().get("name"));
      if (province.numberOfTrainingSlotsAvailable() == 0)
        printMessageToTerminal("No training slots available!");
      else if (game.recruit(province, soldierType)){
        printMessageToTerminal("Training begun!");
        player_treasure.setText(game.getPlayerGold() + " gold");
      }
      else
        printMessageToTerminal("Not enough gold!");
    }
    else
      printMessageToTerminal("No province selected!");
  }
  @FXML
  public void lowTaxesClicked (ActionEvent actionEvent) {
    setTaxes(TaxLevel.LOW_TAX);
  }
  @FXML
  public void normalTaxesClicked (ActionEvent actionEvent) {
    setTaxes(TaxLevel.NORMAL_TAX);
  }
  @FXML
  public void highTaxesClicked (ActionEvent actionEvent) {
    setTaxes(TaxLevel.HIGH_TAX);
  }
  @FXML
  public void veryHighTaxesClicked (ActionEvent actionEvent) {
    setTaxes(TaxLevel.VERY_HIGH_TAX);
  }
  private void setTaxes(TaxLevel taxLevel) {
    if (currentlySelectedHumanProvince != null){
      Province province = game.getProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
      province.setTaxLevel(taxLevel);
      province_tax_level.setText(province.getTaxLevel() + "");
      province_tax_revenue.setText(province.getTaxRevenue() + "");
    }
  }
  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }
  public void showPlayerInfo () {
    player_faction.setText(game.getPlayer().getName());
    player_treasure.setText(game.getPlayerGold()+ " gold");
    player_wealth.setText(game.getPlayerWealth()+ " total");
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String faction = game.getFactionFromProvince(province).getName();


        //This is to Show troops under the province name
        TextSymbol t = new TextSymbol(10,
            faction + "\n" + province, 0xFFFF0000, //  + "\n" + provinceToNumberTroopsMap.get(province),
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);


        switch (faction) {
          case "Gaul":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
            break;
          case "Rome":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol (different to BufferedImage)
            s = new PictureMarkerSymbol("images/legionary.png");
            break;
          // TODO = handle all faction names, and find a better structure...
        }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");

                if (game.getFactionFromProvince(province).equals(game.getPlayer())){
                  // province owned by human
                  if (soldiersSelected.isEmpty()) {
                    if (currentlySelectedHumanProvince != null){
                      resetSelections();
                    }
                    updateHumanSelection(f);
                  }
                  else {
                    if (currentlySelectedOtherProvince != null)
                      featureLayer.unselectFeature(currentlySelectedOtherProvince);
                    currentlySelectedOtherProvince = f;
                    opponent_province.setText(province);
                    invadeButton.setText("Move");
                  }

                }
                else{
                  if (currentlySelectedOtherProvince != null){
                    featureLayer.unselectFeature(currentlySelectedOtherProvince);
                  }
                  currentlySelectedOtherProvince = f;
                  opponent_province.setText(province);
                  invadeButton.setText("Invade");
                }

                featureLayer.selectFeature(f);                
              }

              
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  private void updateHumanSelection(Feature f) {
    Province province = game.getProvince((String)f.getAttributes().get("name"));
    currentlySelectedHumanProvince = f;
    invading_province.setText(province.getName());

    province_wealth.setText(province.getWealth()+"");
    province_tax_level.setText(province.getTaxLevel() +"");
    province_tax_revenue.setText(province.getTaxRevenue() + "");

    for (Soldier s: province.getArmy().getArmy())
      province_army.getItems().add(s);
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
/*
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
*/
    for (Province p : game.getPlayerProvinces()) {
      l.add("name='" + p.getName() + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  }

  private void resetSelections(){
    if (currentlySelectedHumanProvince != null && currentlySelectedOtherProvince != null)
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedOtherProvince, currentlySelectedHumanProvince));
    else if (currentlySelectedHumanProvince != null)
      featureLayer_provinces.unselectFeature(currentlySelectedHumanProvince);
    else if (currentlySelectedOtherProvince != null)
      featureLayer_provinces.unselectFeature(currentlySelectedOtherProvince);
    soldiersSelected.clearSelection();
    currentlySelectedOtherProvince = null;
    currentlySelectedHumanProvince = null;
    invading_province.setText("");
    opponent_province.setText("");
    province_tax_revenue.setText("");
    province_tax_level.setText("");
    province_wealth.setText("");
    province_army.getItems().clear();
    invadeButton.setText("");
  }

  private void printMessageToTerminal(String message){
    output_terminal.appendText(message+"\n");
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  class TextFXReporter extends BattleReporter {
    private String faction1;
    private String faction2;
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      switch (evt.getPropertyName()) {
        case "factions":
          String [] factions = (String [])evt.getNewValue();
          faction1 = factions[0];
          faction2 = factions[1];
          break;
        case "battlef1f2":
          printMessageToTerminal(String.format(evt.getNewValue().toString(), faction1, faction2));
          break;
        case "battlef1":
          printMessageToTerminal(String.format(evt.getNewValue().toString(), faction1));
          break;
        case "battlef2":
          printMessageToTerminal(String.format(evt.getNewValue().toString(), faction2));
          break;
        case "message":
          printMessageToTerminal(evt.getNewValue().toString());
          break;
        default:
          printMessageToTerminal(evt.getNewValue().toString());
      }
    }
  }
}