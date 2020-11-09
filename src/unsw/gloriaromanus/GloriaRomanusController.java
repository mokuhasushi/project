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
import javafx.geometry.Point2D;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONObject;
import unsw.gloriaromanus.game.BattleReporter;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.units.SoldierType;
import unsw.gloriaromanus.world.Province;

public class GloriaRomanusController{

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
  private TableView<Soldier> recruitment_table;

  private ArcGISMap map;

//  private Map<String, String> provinceToOwningFactionMap;

//  private Map<String, Integer> provinceToNumberTroopsMap;

//  private String humanFaction;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;

  private FeatureLayer featureLayer_provinces;

  private Game game;

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException {
    // TODO = you should rely on an object oriented design to determine ownership
/*
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    Random r = new Random();
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, r.nextInt(500));
*/
    Game.newGame("Rome");
    game = Game.getInstance();
    game.setBattleReporter(new TextFXReporter());

    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

    showPlayerInfo();
    current_turn.setText(game.getTurn()+"");
    initializeProvinceLayers();
    soldiersSelected = province_army.getSelectionModel();
    soldiersSelected.setSelectionMode(SelectionMode.MULTIPLE);


  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      if (confirmIfProvincesConnected(humanProvince, enemyProvince)){
        game.invade(game.getProvince(humanProvince), game.getProvince(enemyProvince), new Army(soldiersSelected.getSelectedItems()));
//        game.moveOrInvade(game.getProvince(humanProvince), game.getProvince(enemyProvince));
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
    Province province = game.getProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
    if (province.numberOfTrainingSlotsAvailable() == 0)
      printMessageToTerminal("No training slots available!");
    else if (currentlySelectedHumanProvince != null) {
      if (game.recruit(province, SoldierType.MELEE_INFANTRY))
        printMessageToTerminal("Training begun!");
      else
        printMessageToTerminal("Not enough gold!");
    }
    else
      printMessageToTerminal("No province selected!");
  }
  @FXML
  public void recruitRangedInfantry (ActionEvent actionEvent) {
    Province province = game.getProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
    if (province.numberOfTrainingSlotsAvailable() == 0)
      printMessageToTerminal("No training slots available!");
    else if (currentlySelectedHumanProvince != null) {
      if (game.recruit(province, SoldierType.RANGED_INFANTRY))
        printMessageToTerminal("Training begun!");
      else
        printMessageToTerminal("Not enough gold!");
    }
    else
      printMessageToTerminal("No province selected!");
  }
  @FXML
  public void recruitMeleeChivalry (ActionEvent actionEvent) {
    Province province = game.getProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
    if (province.numberOfTrainingSlotsAvailable() == 0)
      printMessageToTerminal("No training slots available!");
    else if (currentlySelectedHumanProvince != null) {
      if (game.recruit(province, SoldierType.MELEE_CHIVALRY))
        printMessageToTerminal("Training begun!");
      else
        printMessageToTerminal("Not enough gold!");
    }
    else
      printMessageToTerminal("No province selected!");
  }
  @FXML
  public void recruitRangedChivalry (ActionEvent actionEvent) {
    Province province = game.getProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
    if (province.numberOfTrainingSlotsAvailable() == 0)
      printMessageToTerminal("No training slots available!");
    else if (currentlySelectedHumanProvince != null) {
      if (game.recruit(province, SoldierType.RANGED_CHIVALRY))
        printMessageToTerminal("Training begun!");
      else
        printMessageToTerminal("Not enough gold!");
    }
    else
      printMessageToTerminal("No province selected!");
  }
  @FXML
  public void recruitMeleeArtillery (ActionEvent actionEvent) {
    Province province = game.getProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
    if (province.numberOfTrainingSlotsAvailable() == 0)
      printMessageToTerminal("No training slots available!");
    else if (currentlySelectedHumanProvince != null) {
      if (game.recruit(province, SoldierType.MELEE_ARTILLERY))
        printMessageToTerminal("Training begun!");
      else
        printMessageToTerminal("Not enough gold!");
    }
    else
      printMessageToTerminal("No province selected!");
  }
  @FXML
  public void recruitRangedArtillery (ActionEvent actionEvent) {
    Province province = game.getProvince((String)currentlySelectedHumanProvince.getAttributes().get("name"));
    if (province.numberOfTrainingSlotsAvailable() == 0)
      printMessageToTerminal("No training slots available!");
    else if (currentlySelectedHumanProvince != null) {
      if (game.recruit(province, SoldierType.RANGED_ARTILLERY))
        printMessageToTerminal("Training begun!");
      else
        printMessageToTerminal("Not enough gold!");
    }
    else
      printMessageToTerminal("No province selected!");
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

                //TODO CHECK
                if (game.getFactionFromProvince(province).equals(game.getPlayer())){
                  // province owned by human
                  if (currentlySelectedHumanProvince != null){
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                    province_army.getItems().clear();
                  }
                  updateHumanSelection(f);
//                  currentlySelectedHumanProvince = f;
//                  invading_province.setText(province);
                }
                else{
                  if (currentlySelectedEnemyProvince != null){
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  opponent_province.setText(province);
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
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null)
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));
    else if (currentlySelectedHumanProvince != null)
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedHumanProvince));
    else if (currentlySelectedEnemyProvince != null)
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince));
    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    invading_province.setText("");
    opponent_province.setText("");
    province_tax_revenue.setText("");
    province_tax_level.setText("");
    province_wealth.setText("");
    province_army.getItems().clear();
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
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      printMessageToTerminal(evt.getNewValue().toString());
    }
  }
}